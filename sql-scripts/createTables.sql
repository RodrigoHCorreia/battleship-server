
create table PLAYER (
    uuid            serial          NOT NULL PRIMARY KEY ,
    name            varchar(100)    NOT NULL,
    password        varchar(100)    NOT NULL,
    playCount       int             NOT NULL,
    winCount        int             NOT NULL
        CHECK ( winCount <= playCount),

    UNIQUE(uuid)

);

create table GAME (
    id              serial          NOT NULL,
    host            int             NOT NULL,
    guest           int,
    result          varchar(10)
        CHECK (result = 'waiting'
            OR result = 'ongoing'
            OR result = 'finished'),

    rules           int,
    /* get a better type here
    boardHost       int,
    boardJoiner     int,
       */

    CONSTRAINT game_id PRIMARY KEY(id),
    FOREIGN KEY (host) REFERENCES PLAYER (uuid),
    FOREIGN KEY (guest) REFERENCES PLAYER (uuid)

    ON DELETE CASCADE
    ON UPDATE CASCADE
);



