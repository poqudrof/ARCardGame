# post_test.rb 

require 'net/http'
require 'json'
require 'yaml'

@host = "http://joue-maths-gie-manager.herokuapp.com"
@host = "http://localhost:1337"

def build_req(route)
  uri = URI("#{@host}#{route}")
  req = Net::HTTP::Post.new(uri, 'Content-Type' => 'application/json')
  [uri, req]
end

def send_req(uri, req, content)
  req.body = content.to_json
  res = Net::HTTP.start(uri.hostname, uri.port) do |http|
    p "SENDING: #{uri} #{content}"
    http.request(req)
  end
end

## Find ID of the deck 
deck_name = "joue-maths-gie"
uri = URI("#{@host}/decks")
params = { :uuid => deck_name }
uri.query = URI.encode_www_form(params)
res = Net::HTTP.get_response(uri)
 
if res.code.to_i == 200 
  deck_id = JSON.parse(res.body)[0]["id"]
end 

## Send the 
p "Sending card roles..."

uri, req = build_req("/card-roles")
roles = YAML.load_file('roles.yaml')
roles.each do |role| 
  role["deck"] = deck_id
  p send_req(uri, req, role)
end 


#### Send the cards.. 

##  Get the card roles for reference
uri = URI("#{@host}/card-roles")
res = Net::HTTP.get(uri)

roles_hash = {} 
JSON.parse(res).each do |role|
  roles_hash[role["title"]] = role
end


p "Sending the cards"

uri, req = build_req("/cards")
cards = YAML.load_file('cards.yml')
cards.each do |card| 
  role = card[:role]
  if role && roles_hash[role]
    card["card_role"] = roles_hash[role]["id"]
    card.delete :role
  end
  card["deck"] = deck_id
  
  ## Get role id 
  p send_req(uri, req, card)
end 


##  Get the cards for reference
uri = URI("#{@host}/cards?_limit=3000")
res = Net::HTTP.get(uri)

cards_hash = {} 
JSON.parse(res).each do |card|
  cards_hash[card["card_id"]] = card
end

## Sending the lines

uri, req = build_req("/lines")
lines = YAML.load_file('lines.yml')
lines.each do |line| 
  card = line[:card_id]
  if card && cards_hash[card]
    line["card"] = cards_hash[card]["id"]
    line.delete :card_id
  else  
    p "cannot link card to line " 
    p line
    next 
  end 
  line[:line] = line[:content]
  line.delete :content 
  ## Get role id 
  p send_req(uri, req, line)
end 

