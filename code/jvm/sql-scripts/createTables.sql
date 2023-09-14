
create table USERS
(
    uuid                int                 PRIMARY KEY
        generated always as identity,
    username            varchar(64)         NOT NULL,
    email               varchar(64)         NOT NULL,
    playCount           int                 NOT NULL default 0,
    elo                 int                 NOT NULL default 0,
    password_validation varchar(256)        NOT NULL,

    check(playCount >= 0),
    check(elo >= 0),

    unique(username),
    unique(email)
);

create table TOKENS
(
    token_validation    varchar(256) PRIMARY KEY,
    user_id             int references USERS (uuid),
    created_at          bigint,
    last_used_at        bigint
);

CREATE TYPE game_state as ENUM (
    'WAITING',
    'PLANNING',
    'FIGHTING',
    'FINISHED'
);

CREATE TYPE eplayer as ENUM (
    'PLAYER1',
    'PLAYER2'
);

create table GAME
(
    id          int             PRIMARY KEY generated always as identity,
    ranked      boolean         NOT NULL,
    state       game_state      NOT NULL,
    player1     int             NOT NULL,
    player2     int,
    rules       jsonb           NOT NULL,
    turn        eplayer,
    board1      jsonb,
    board2      jsonb,

    FOREIGN KEY (player1) REFERENCES USERS (uuid),
    FOREIGN KEY (player2) REFERENCES USERS (uuid),
    CHECK(player1 != player2)
);



