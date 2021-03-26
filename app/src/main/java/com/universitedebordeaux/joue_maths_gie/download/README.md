# Download

## Information

The original server who stored the cards has been closed. So, we decided to use a local database
with local directories.

This is temporary and must be changed in the future. (Go to the json_database branch to see the
new version in development)

## How does it work for now ?

All data is stored inside the **asserts** directory. (this directory is like the res directory)

You will find :

- the *fonts* folder : Fonts available for the static web page describing the rules.
- the *images* folder : Empty for now.
- the *sounds* folder : Contains all sounds linked with their card's id.
- the *joue_maths_gie.db* file : The SQLite file containing the local database.
N.B : This database is always duplicated into the apk's data folder inside the phone.
- the *rules.css* file : The css style file to color the html file.
- the *rules.html* file : The rules writing with the html format.

The *fonts* folder and the *rules.html* and *rules.css* file are used inside the RulesActivity class.
The *joue_maths_gie.db* is used inside the AppDatabase class.
The *images* and *sounds* folder are used inside the CardActivity class.