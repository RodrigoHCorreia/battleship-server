package battleship.server.http

import battleship.server.infra.LinkRelation

object Rels {

    val self = LinkRelation("self")
    val next = LinkRelation("next")
    val previous = LinkRelation("previous")

    // Home Related
    val ranking = LinkRelation("ranking");

    // User Related
    val user = LinkRelation("user");

    // Game Related
    val gameHub = LinkRelation("game-hub");
    val game = LinkRelation("game");

    val playerBoard = LinkRelation("board");
    val enemyBoard = LinkRelation("enemy_board");
    val opponent = LinkRelation("opponent");
};

