package com.imchat.chat.model

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

class ChatMessage(val type: MessageType, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) val timestamp: LocalDateTime, val sender: String, val content: String?) {
    enum class MessageType {
        CHAT, JOIN, LEAVE
    }
}