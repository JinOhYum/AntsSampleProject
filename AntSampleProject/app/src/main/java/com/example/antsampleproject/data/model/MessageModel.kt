package com.example.antsampleproject.data.model


/**
 * viewType = 0 : 내가 보낸메세지 ,  1:상대방이 보낸메세지
 * deviceId = 유저 고유번호
 * message = 채팅 내용
 * **/
data class MessageModel(val viewType: Int, val deviceId: String, val message: String)