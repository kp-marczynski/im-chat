package com.imchat.chat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [RabbitAutoConfiguration::class])
class ChatApplication

fun main(args: Array<String>) {
    runApplication<ChatApplication>(*args)
}
