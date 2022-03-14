# post_test.rb 

require 'net/http'
require 'json'
require 'yaml'

@host = "http://joue-maths-gie-manager.herokuapp.com"
@host = "http://localhost:1337"
deck_name = "joue-maths-gie"

def build_req(route)
  uri = URI("#{@host}#{route}")
  req = Net::HTTP::Post.new(uri, 'Content-Type' => 'application/json')
  [uri, req]
end

def build_put_req(route)
  uri = URI("#{@host}#{route}")
  req = Net::HTTP::Put.new(uri, 'Content-Type' => 'application/json')
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

##  Update the roles of all cards
uri = URI("#{@host}/cards?_limit=3000")
res = Net::HTTP.get(uri)
all_cards = JSON.parse(res) 
all_cards.each do |card| 
  #p "Role #{card['card_role']}"
  card_id = card["card_id"]
  if card["card_role"]
    p "skip #{card_id}"
    next
  end
  role = 1 if card_id.start_with? "e3d"
  role = 2 if card_id.start_with? "em"
  role = 3 if card_id.start_with? "mdp"
  role = 4 if card_id.start_with? "p2d"
  role = 5 if card_id.start_with? "vdn"
  # role = "Valle des nombres" if card_id.start_with? "vdn"
  
  card_update = {}
  if role 
    #if roles_hash[role]
      card_update[:card_role] = role # roles_hash[role]["id"]
      ## Create a request to set the id
      uri, req = build_put_req("/cards/#{card["id"]}")  
      ## Send the update
      send_req(uri, req, card_update)
    #else
    #  p "role not found: #{role}"
    #end 
  else  
    p "Role not found for #{card_id}"
  end
end; nil ;nil; 


### TODO: Line issues !
## Sending the lines

uri, req = build_req("/lines")
lines = YAML.load_file('lines.yml')
lines.each do |line| 

  ## Problem of line matching the wrong cards
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

### Sounds are mp3 files that have the card name as file name.
## Read all files in ../../assets/sounds

files = Dir["../../assets/sounds/*.mp3"]

files.each do |file|  
  ## Find the card name
  name = file.split("/")[-1]
  card_id = name.split(".mp3")[0]

  ## Find the card id
  uri = URI("#{@host}/cards?card_id=#{card_id}")
  res = Net::HTTP.get(uri)
  if res == "[]"
    p "Card not found: #{card_id}."
    next
  end
  card = JSON.parse(res)[0]
  card_id = card["id"]

  if card["voiceover"]
    p "voiceover alread set for card #{card_id}."
    next
  end
  ## Check if the voiceover is already set.
  ## Upload the image
  res = `curl -X POST -F 'files=@./#{file}' #{@host}/upload`
  answer = JSON.parse(res)

  ## Get the id of the uploaded image
  upload_id = answer[0]["id"]

  ## Create a request to set the id
  uri, req = build_put_req("/cards/#{card_id}")
  card_update = {}

  ## Setting ID is not enough ?
  card_update["voiceover"] = upload_id
  
  ## Send the update
  send_req(uri, req, card_update)
end