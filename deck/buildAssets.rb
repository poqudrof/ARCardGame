

## Get the database 
`mkdir newAssets`
`cd newAssets`

pip3 install db-to-sqlite
pip3 install psycopg2-binary

### WORKS!
db-to-sqlite postgres://fryxucceqvrrml:3235f0963ff3e72eff290a7c4e85abbbe594cfc8d636955258c3804307e25a4e@ec2-54-155-92-75.eu-west-1.compute.amazonaws.com:5432/dbj3fms1plcpui out.sql --all
mv out.sql app/src/main/assets/jmg.db 


## Connect

psql postgres://fryxucceqvrrml:3235f0963ff3e72eff290a7c4e85abbbe594cfc8d636955258c3804307e25a4e@ec2-54-155-92-75.eu-west-1.compute.amazonaws.com:5432/dbj3fms1plcpui

## Update the db 
> alter table cards alter column id set not null;


iii@macbook pfe-copy % sqlite3     
SQLite version 3.32.2 2020-06-04 12:58:43
Enter ".help" for usage hints.
Connected to a transient in-memory database.
Use ".open FILENAME" to reopen on a persistent database.
sqlite> .open out.sql
sqlite> .once Store.sql
sqlite> .dump

## add not null
CREATE TABLE [cards] (
   [id] INTEGER  NOT NULL PRIMARY KEY,
   [card_id] TEXT,
## Build adagin

sqlite3 out2.sql < Store.sql
# copy
mv out2.sql app/src/main/assets/jmg.db 
