package battleship.server.controllers

import battleship.server.model.Game
import battleship.server.model.Player
import battleship.server.model.User

val users = mutableListOf(
    User(0, Player("Cyb3r", 3, 1), "0f165f7e869c8d6f08ae67b14ba0cf3f23fa76bed822c369"), //passoword=1234
    User(1, Player("ligma", 1, 3), "e5bc22d238c73b81fd8f36ef38aadde3149eb160fc3d2f51"), //password=4321
    User(2, Player("paulo",2 ,2), "85b082f620662a8946b16999652a57aa60ad9ab129cb3e01") //password=ay
)

val games = mutableListOf(
    Game("new lobby", "host", "invited user", null, mutableListOf()),
    Game("new lobby2", "host", null, null, mutableListOf()),
    Game("new lobby3", "host", null, null, mutableListOf())
)

var nextUserID = users.size+1

