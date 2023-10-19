package com.example.hangmangame

object WordDatabase {

    private val words = listOf(
        "apple",
        "auditory",
        "cherry",
        "purple",
        "binders",
        "fighting",
        "grape",
        "computers"
    )
    fun getRandomWord(): String {
        return words.random()
    }
}