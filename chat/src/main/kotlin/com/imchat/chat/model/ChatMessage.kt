package com.imchat.chat.model

class ChatMessage(val type: MessageType, val sender: String, val content: String?) {
    enum class MessageType {
        CHAT, JOIN, LEAVE
    }
}