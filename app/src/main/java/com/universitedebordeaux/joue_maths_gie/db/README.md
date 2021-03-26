# Database
The cards are stored in a SQLite database. We used **Room** to manipulate the database.
> Room provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite.

## Tables
The database contains only two tables. The text of a **card** is separated into several **lines**.

| Card | Line | 
|---:|---:|
| id (TEXT) | id (INT)|
| type (TEXT) | card_id (TEXT) |
| title (TEXT) | contents (TEXT) |
| answer (TEXT) |
| tip (TEXT) |
| card_number (INT) |

**Card** table :
- **id** : unique card identifier, example : "e3dcm1q_1".
- **type**  : currently "question" or "mathematicien" but not used.
- **title** : subject area, example "Espaces 3D".
- **answer** : the answer to the question asked.
- **tip**: help to the question.
- **card_number** : question number.

**Line** table :
- **id** : auto-generated incremental identifier.
- **card_id** : card identifier associated with this line.
- **contents** : line content.

## Architecture

The main class is *AppDatabase*. It is a singleton whose methods must be called in an asynchronous task (different from the main thread, see for example the class *CardSearchTask*). It gives access to the DAOs of *Line* and *Card*. The tables are represented by three classes of entities (POJO) :
- *Card*
- *Line*
- *CardWithLines*

The first two classes match the tables perfectly. The last one combines a *Card* with a list of *Line*. This is the only class that contains a card with its full text. These three classes implement Parcelable in order to be serialized from one activity to another.

The DAO contains the different methods to perform queries on the database.

## How it works ?

The ***AppDatabase*** class loads the local .db file, downloaded through the *UpdateDBTask* class, located in the assets' folder when the application starts.