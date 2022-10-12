
create table PLAYERS (
    uuid        serial          not null primary key,
    name        varchar(100)    not null,
    playCount   int             not null,
    winCount    int             not null,
    password    varchar(100)    not null,

    CHECK ( winCount <= playCount)
);

create table GAMES (
    id          serial
);



