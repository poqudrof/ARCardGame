require 'json'

file_data = File.read("lines.txt").split("\n")

output = {}

file_data.each do |entry|
  line_parts = entry.split(",")
  card = line_parts[0].split("'")[1]
  line_content = line_parts[1].split("'")[1]

  output[card] ||= []
  output[card] << line_content
  p "#{card} #{line_content}"
end

File.open("out.json","w") do |f|
  f.write(output.to_json)
end