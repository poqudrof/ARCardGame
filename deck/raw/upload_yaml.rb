# post_test.rb 

require 'net/http'
require 'json'
require 'openssl'
require 'yaml'

@host = "http://joue-maths-gie-manager.herokuapp.com"
@host = "http://localhost:1337"
@host = "https://cards.natar.fr/api"
deck_name = "joue-maths-gie"

@http_headers = {
  'authorization'=>'bearer c81412fe0cc6d66f69b136b804e3405855b0e5cc8237d68074d0e4cab475874ae934e4bc7cbdda30bc18501b15c9711f8deb3e38630c88c33bf0a5fd528293b59971a1fcb7ea9561ee4c21d4b29e37cb942321b6becddf4ea13ac2a941932552e39d2671279ea26cc50336edd76aa77e168fbbb1a9fa5ecbb02b3ff467c5300b',
  'content-type' =>'application/json',
  'accept'=>'application/json'
}
# def build_req(route)
#   uri = URI("#{@host}#{route}")
#   req = Net::HTTP::Post.new(uri, "", @http_headers)
#   [uri, req]
# end

# def build_put_req(route)
#   uri = URI("#{@host}#{route}")
#   req = Net::HTTP::Put.new(uri)
#   request["Content-Type"] = 'application/json'
#   request["Authorization"] = 'Bearer c81412fe0cc6d66f69b136b804e3405855b0e5cc8237d68074d0e4cab475874ae934e4bc7cbdda30bc18501b15c9711f8deb3e38630c88c33bf0a5fd528293b59971a1fcb7ea9561ee4c21d4b29e37cb942321b6becddf4ea13ac2a941932552e39d2671279ea26cc50336edd76aa77e168fbbb1a9fa5ecbb02b3ff467c5300b'
#   ## add json header Bearer token
#   [uri, req]
# end

# def send_req(uri, req, content)
#   ## Content is now in a data field
#   content = {data: content}
#   req.body = content.to_json
#   res = Net::HTTP.start(uri.hostname, uri.port) do |http|
#     p "SENDING: #{uri} #{req.body}"
#     http.request(req)
#   end
# end

def post(route, content)
  uri = URI("#{@host}#{route}")
  content = {data: content}.to_json
  p "Sending #{uri} #{content}"
  req = Net::HTTP.post(uri, content, @http_headers)
  p req
  req 
end

def update(route, content)
  uri = URI("#{@host}#{route}")
  http = Net::HTTP.new(uri.host, uri.port)
  http.use_ssl = true
  http.verify_mode = OpenSSL::SSL::VERIFY_NONE
  request = Net::HTTP::Put.new(uri)
  content = {data: content}.to_json
  
  p "Sending #{uri} #{content}"
  request["Content-Type"] = 'application/json'
  request["Authorization"] = 'Bearer c81412fe0cc6d66f69b136b804e3405855b0e5cc8237d68074d0e4cab475874ae934e4bc7cbdda30bc18501b15c9711f8deb3e38630c88c33bf0a5fd528293b59971a1fcb7ea9561ee4c21d4b29e37cb942321b6becddf4ea13ac2a941932552e39d2671279ea26cc50336edd76aa77e168fbbb1a9fa5ecbb02b3ff467c5300b'
  request.body = content
  req = http.request(request)
  p req
  req 
end

## Find ID of the deck 
deck_name = "joue-maths-gie"
uri = URI("#{@host}/decks")
params = { :uuid => deck_name }
uri.query = URI.encode_www_form(params)
res = Net::HTTP.get_response(uri)
 
if res.code.to_i == 200 
  deck_id = JSON.parse(res.body)["data"][0]["id"]
end 

## Send the 
p "Sending card roles..."

roles = YAML.load_file('roles.yaml')
roles.each do |role| 
  role["deck"] = deck_id
  post("/card-roles", role)
end 


#### Send the cards.. 

##  Get the card roles for reference
uri = URI("#{@host}/card-roles")
res = Net::HTTP.get(uri)

roles_hash = {} 
JSON.parse(res)["data"].each do |role|
  roles_hash[role["attributes"]["title"]] = role["id"].to_i
end

p "Sending the cards"

cards = YAML.load_file('cards.yml')
cards.each do |card| 
  role = card[:role]
  if role && roles_hash[role]
    card["card_role"] = roles_hash[role]
    card.delete :role
  end
  card["deck"] = deck_id
  
  ## Get role id 
  post("/cards", card)
end 


##  Get the cards for reference
uri = URI("#{@host}/cards?_limit=3000")
res = Net::HTTP.get(uri)

cards_hash = {} 
JSON.parse(res)["data"].each do |card|
  cards_hash[card["attributes"]["card_id"]] = card
end

## Role update not necessary anymore

# ##  Update the roles of all cards
# uri = URI("#{@host}/cards?_limit=3000")
# res = Net::HTTP.get(uri)
# all_cards = JSON.parse(res) 
# all_cards.each do |card| 
#   #p "Role #{card['card_role']}"
#   card_id = card["card_id"]
#   if card["card_role"]
#     p "skip #{card_id}"
#     next
#   end
#   role = 1 if card_id.start_with? "e3d"
#   role = 2 if card_id.start_with? "em"
#   role = 3 if card_id.start_with? "mdp"
#   role = 4 if card_id.start_with? "p2d"
#   role = 5 if card_id.start_with? "vdn"
#   # role = "Valle des nombres" if card_id.start_with? "vdn"
  
#   card_update = {}
#   if role 
#     #if roles_hash[role]
#       card_update[:card_role] = role # roles_hash[role]["id"]
#       ## Create a request to set the id
#       uri, req = build_put_req("/cards/#{card["id"]}")  
#       ## Send the update
#       send_req(uri, req, card_update)
#     #else
#     #  p "role not found: #{role}"
#     #end 
#   else  
#     p "Role not found for #{card_id}"
#   end
# end; nil ;nil; 


### TODO: Line issues !
## Sending the lines

lines = YAML.load_file('lines.yml')
lines.each do |line| 

  ## Problem of line matching the wrong cards
  card = line[:card_id]
  line[:line] = line[:content]
  line.delete :content 

  ## Card found, set the card_id and remove the text match.
  if card && cards_hash[card]
    line["card"] = cards_hash[card]["id"]
    line.delete :card_id
  else  
    p "cannot link card to line " 
    p line
    next 
  end 
  ## send the line
  post("/lines", line)
end 


### Sounds are mp3 files that have the card name as file name.
## Read all files in ../../assets/sounds

files = Dir["../../../assets/sounds/*.mp3"]

files.each do |file|  
  ## Find the card name
  name = file.split("/")[-1]

  ## Find the card id in the name 
  card_id = name.split(".mp3")[0]

  t,n,x = card_id.split("_")

  card_id = "#{t}_#{n}"

  ## Find the card id
  uri = URI("#{@host}/cards?filters[card_id][$eq]=#{card_id}")
  res = Net::HTTP.get(uri)
  if res == "[]"
    p "Card not found: #{card_id}."
    next
  end
  card = JSON.parse(res)["data"][0]
  card_id = card["id"]

  ## TODO: update this
  if card["voiceover"]
    p "voiceover alread set for card #{card_id}."
    next
  end

  ## Check if the voiceover is already set.
  ## Upload the image
  h = "Authorization: Bearer c81412fe0cc6d66f69b136b804e3405855b0e5cc8237d68074d0e4cab475874ae934e4bc7cbdda30bc18501b15c9711f8deb3e38630c88c33bf0a5fd528293b59971a1fcb7ea9561ee4c21d4b29e37cb942321b6becddf4ea13ac2a941932552e39d2671279ea26cc50336edd76aa77e168fbbb1a9fa5ecbb02b3ff467c5300b"
  res = `curl  --header '#{h}'  -X POST -F 'files=@./#{file}' #{@host}/upload`
  answer = JSON.parse(res)

  ## Get the id of the uploaded image
  upload_id = answer[0]["id"]

  ## Create a request to set the id
  card_update = {}

  ## Setting ID is not enough ?
  card_update["voiceover"] = upload_id
  
  ## Send the update
  # uri, req = build_put_req("/cards/#{card_id}")
  # send_req(uri, req, card_update)

  update("/cards/#{card_id}", card_update)
end