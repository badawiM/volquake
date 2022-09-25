package com.volquake.pricerequest.configuration

object StompTopics{
    const val topicPrefix = "/topic"
}

fun stompTopic(topic: String) = StompTopics.topicPrefix.joinSafely("/", topic)

fun String.joinSafely(separator: String, other: String): String{
    return this.removeSuffix(separator) + separator + other.removePrefix("/")
}

