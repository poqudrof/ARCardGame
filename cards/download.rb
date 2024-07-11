require 'json'
require 'net/http'
require 'fileutils'
require 'uri'
require 'open-uri'

Dir.mkdir('assets') unless Dir.exist?('assets')
Dir.mkdir('assets/sounds') unless Dir.exist?('assets/sounds')

# URL pour récupérer le JSON
json_url = 'https://cards.natar.fr/api/decks?filters[name][$eq]=joue-maths-gie&populate[cards][populate]=*'

# Récupère le JSON depuis l'URL
uri = URI(json_url)
response = Net::HTTP.get(uri)
data = JSON.parse(response)

# Base URL pour compléter les URLs des fichiers audio
base_url = 'http://cards.natar.fr'

# Extrait la liste des cartes
cards_list = data['data'][0]['attributes']['cards']['data']

# Sauvegarde le JSON avec la liste de `cards` uniquement
File.open('assets/jmg.json', 'w') do |file|
  file.write(JSON.pretty_generate(cards_list))
end

# Parcours chaque carte dans le JSON
cards_list.each do |item|

  # item = cards_list[0]
  
  card_id = item['attributes']['card_id']
  voiceover_data = item['attributes']['voiceover']['data']
  
  if voiceover_data.nil?
    puts "Aucun fichier audio pour la carte #{card_id}."
    next
  end

  audio_url = base_url + voiceover_data['attributes']['url']
  filename = "assets/sounds/#{card_id}.mp3"


  # Télécharge le fichier audio et le sauvegarde
  URI.open(audio_url) do |audio|
    File.open(filename, 'wb') do |file|
      file.write(audio.read)
    end
  end

  puts "Fichier #{filename} téléchargé et sauvegardé."
end
