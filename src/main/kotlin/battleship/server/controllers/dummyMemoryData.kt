package battleship.server.controllers

import battleship.server.model.User

val users = mutableListOf(
    User(0, "Cyb3r", "0f165f7e869c8d6f08ae67b14ba0cf3f23fa76bed822c369", 3, 1), //passoword=1234
    User(1,"ligma", "e5bc22d238c73b81fd8f36ef38aadde3149eb160fc3d2f51", 1, 3), //password=4321
    User(2, "paulo", "85b082f620662a8946b16999652a57aa60ad9ab129cb3e01", 2, 2) //password=ay
)

var nextUserID = users.size+1

