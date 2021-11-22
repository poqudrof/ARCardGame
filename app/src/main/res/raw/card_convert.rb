require 'json'

cards = File.read("cards.txt").split("\n")
card = cards.first 
all_cards = []
cards.each do |card| 
  card = JSON.parse card
  
  all_cards << {card_id: card[0], 
                card_type: card[1],
                role: card[2],
                answer: card[3], 
                tip: card[4],
                number_in_role: card[5] }
end

File.open("cards.yml", "w") { |file| file.write(all_cards.to_yaml) }

lines = File.read("lines.txt").split("\n")
line = lines.first 
all_lines = []
lines.each do |line| 
  line = JSON.parse line
  all_lines << {card_id: line[0], 
                content: line[1] }
end

File.open("lines.yml", "w") { |file| file.write(all_lines.to_yaml) }

