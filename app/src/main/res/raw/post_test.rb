# post_test.rb 

require 'net/http'
require 'json'
require 'yaml'

host = "localhost:1337"

def build_req(route)
  uri = URI("http://#{host}#{route}")
  req = Net::HTTP::Post.new(uri, 'Content-Type' => 'application/json')
  [uri, req]
end

def send_req(uri, req, content)
  req.body = content.to_json
  res = Net::HTTP.start(uri.hostname, uri.port) do |http|
    http.request(req)
  end
end

## Find ID of the deck 
deck_name = "joue-maths-gie"
uri = URI("http://#{host}/decks")
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

# 
('e3dcm1q_1', 'question', 'Espaces 3D', 'Il y a 50 cubes : il y a 5 cubes dans une rangée, il y a 10 rangées, 5 x 10 = 50.' , NULL, 1),

puts roles.inspect
