## How to build a deck 


### Download the cards from the API: 

Download: https://joue-maths-gie-manager.herokuapp.com/cards?_limit=3000
save as "cards.json"

New API:

https://cards.natar.fr/api/cards?populate=*?&pagination[page]=0&pagination[pageSize]=1000


### Get the additionnal data from the data server



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
