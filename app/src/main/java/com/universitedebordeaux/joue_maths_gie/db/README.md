# Database
The cards are stored in a SQLite database. We used **Room** to manipulate the database.
> Room provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite.

## Tables
The database contains only two tables. The text of a **card** is separated into several **lines**.

| Card | Line | 
|---:|---:|
| id (STR) | id (INT)|
| type (STR) | cardId (STR) |
| title (STR) | contents (STR) |
| answer (STR) |
| tip (STR) |
| number (INT) |
| cnn (BOOL) |

**Card** table :
- **id** : unique card identifier, example : "e3dcm1q_1".
- **type**  : currently "question" or "mathematicien" but not used.
- **title** : subject area, example "Espaces 3D".
- **answer** : the answer to the question asked.
- **tip**: help to the question.
- **number** : question number.
- **cnn** : should a CNN be used to identify this card (y/n).

**Line** table :
- **id** : auto-generated incremental identifier.
- **cardId** : card identifier associated with this line.
- **contents** : line content.

## Architecture

The main class is *AppDatabase*. It is a singleton whose methods must be called in an asynchronous task (different from the main thread, see for example the class *CardSearchTask*). It gives access to the DAOs of *Line* and *Card*. The tables are represented by three classes of entities (POJO) :
- *Card*
- *Line*
- *CardWithLines*

The first two classes match the tables perfectly. The last one combines a *Card* with a list of *Line*. This is the only class that contains a card with its full text. These three classes implement Parcelable in order to be serialized from one activity to another.

The DAO contains the different methods to perform queries on the database.

## Import

The database is filled from YAML files. **Room** is in charge of generating the SQL tables.
YAML files are loaded by **SnakeYaml**. The DAOs classes did not match the format of the YAML files.
We had to create two more POJO classes to match the YAML format. These are the classes :
- *CardConstructor*
- *CardYaml*

The main class is *CardYaml*. *CardConstructor* is only there to build a list of *CardYaml* from a file.
*CardYaml* also manages the conversion to DAOs (card & line) classes.

*AppDatabase* has a method to *reload* the database from a folder containing YAML files. Previous data is dropped.
