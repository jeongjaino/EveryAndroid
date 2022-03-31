package com.example.carrotmarket.chatdetail

data class ChatItem (
    val senderId: String,
    val message: String
    ){
    constructor(): this("", "")
}