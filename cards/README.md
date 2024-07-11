## How to build a deck 


Just run the following command: 

```bash
ruby download.rb
```

The deck will be saved in the assets folder along with the MP3. 

### Download the cards from the API: 

Download: https://joue-maths-gie-manager.herokuapp.com/cards?_limit=3000
save as "cards.json". 

To use in the Android APP, the file must be saved in the assets folder 
in `app/src/main/assets/jmg.json`.

Doc: https://docs.strapi.io/dev-docs/api/rest/guides/understanding-populate#example-1st-level-and-2nd-level-component


New API:
https://cards.natar.fr/api/cards?populate=*?&pagination[page]=0&pagination[pageSize]=1000
https://cards.natar.fr/api/cards?populate[voiceover][fields][0]=url&pagination[pageSize]=10&pagination[page]=1&publicationState=live
https://cards.natar.fr/api/cards?populate[deck][fields][0]=id&populate[voiceover][fields][0]=url&pagination[pageSize]=10&pagination[page]=1&publicationState=live

Download from the deck endpoint instead : 
https://cards.natar.fr/api/decks?filters[name][$eq]=joue-maths-gie&populate[cards][populate]=*


## Card structure 

### names 

Example: 
`e3dcm1q_7`

Reads as: 
`[cardtype][cardlevel]q_[id]`

̏̏```
Deck 
---- 
has_many cards
has_many roles

Card 
---- 
_id 
card_id  (String Unique per deck)
card_type (type question ?)
role  (type) 
title (String)
answer (String)
tip (String)
number_in_role (String)
has_many lines
belongs_to deck 

#-> Can remove   role + card_number ar contained in ID

Roles 
----
_id 
role_id
title 
has_many cards
belongs_to deck

Card Types
---- 
name 
belongs_to deck

Line
---- 
belongs_to card
content
position (auto-generated?)
```
