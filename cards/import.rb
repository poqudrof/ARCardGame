# post_test.rb 
require 'http'
require 'json'
require 'yaml'

host = "https://cards.natar.fr"Ê
# @host = "http://localhost:1337"
deck_name = "joue-maths-gie"

## Query to fetch back
## https://cards.natar.fr/api/cards?populate=*?&pagination[page]=0&pagination[pageSize]=1000

## card example 
# {"id":1,
# "title":"Espaces 3D",
# "short_name":"e3d","published_at":"2021-11-24T21:40:08.946Z",
# "created_at":"2021-11-24T21:40:08.952Z","updated_at":"2021-11-24T21:40:08.959Z"}

## load the cards from cards.json 

token = "c81412fe0cc6d66f69b136b804e3405855b0e5cc8237d68074d0e4cab475874ae934e4bc7cbdda30bc18501b15c9711f8deb3e38630c88c33bf0a5fd528293b59971a1fcb7ea9561ee4c21d4b29e37cb942321b6becddf4ea13ac2a941932552e39d2671279ea26cc50336edd76aa77e168fbbb1a9fa5ecbb02b3ff467c5300b"

HTTP.auth("Bearer #{token}")

## Create a deck 

## Index
res = HTTP.auth("Bearer #{token}").get("#{host}/api/decks/")

## Create
res = HTTP.auth("Bearer #{token}").post("#{host}/api/decks", json: {name: "joue-maths-gie"})


cards = JSON.parse(File.read('cards.json'))

cards = [cards[0]]

## Send the cards.. 
cards.each do |card| 
  p "Sending card: #{card["title"]}"
  res = HTTP.post("#{@host}/cards", :json => card)
  p res
end

## Load the lines from lines.json 

lines = JSON.parse(File.read('lines.json'))

## Send the lines.. 

lines.each do |line| 
  p "Sending line: #{line["title"]}"
  res = HTTP.post("#{@host}/lines", :json => line)
  p res
end

## Load the roles from roles.json 

roles = JSON.parse(File.read('roles.json'))

## Send the roles.. 

roles.each do |role| 
  p "Sending role: #{role["title"]}"
  res = HTTP.post("#{@host}/roles", :json => role)
  p res
end

## Load the cards_roles from cards_roles.json 

cards_roles = JSON.parse(File.read('cards_roles.json'))

## Send the cards_roles.. 

cards_roles.each do |card_role| 
  p "Sending card_role: #{card_role["title"]}"
  res = HTTP.post("#{@host}/cards_roles", :json => card_role)
  p res
end

## Load the lines_roles from lines_roles.json 

lines_roles = JSON.parse(File.read('lines_roles.json'))

## Send the lines_roles.. 

lines_roles.each do |line_role| 
  p "Sending line_role: #{line_role["title"]}"
  res = HTTP.post("#{@host}/lines_roles", :json => line_role)
  p res
end

## Load the cards_lines from cards_lines.json 

cards_lines = JSON.parse(File.read('cards_lines.json'))

## Send the cards_lines.. 

cards_lines.each do |card_line| 
  p "Sending card_line: #{card_line["title"]}"
  res = HTTP.post("#{@host}/cards_lines", :json => card_line)
  p res
end