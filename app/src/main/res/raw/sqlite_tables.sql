CREATE TABLE IF NOT EXISTS Card (
    id          TEXT        NOT NULL        PRIMARY KEY,
    type        TEXT        NOT NULL,
    title       TEXT        NOT NULL,
    line        INTEGER     NOT NULL,
    answer      TEXT        NOT NULL,
    number      INTEGER     NOT NULL,
    tip         TEXT,
    image_path  TEXT,
    sound_path  TEXT
);

CREATE TABLE IF NOT EXISTS Line (
    id          INTEGER     PRIMARY KEY     AUTOINCREMENT,
    card_id     TEXT        NOT NULL,
    line        TEXT        NOT NULL,
    FOREIGN KEY (card_id) REFERENCES Card(id)
        ON DELETE CASCADE
);