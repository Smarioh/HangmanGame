package com.example.hangmangame

import android.os.Bundle

class GameEngine {

    private var currentWord: String = ""
    private var maskedWord: StringBuilder = StringBuilder()
    private var incorrectGuesses: Int = 0
    private val maxIncorrectGuesses: Int = 6

    private var wordToGuess: String = WordDatabase.getRandomWord()
    private var currentGuess: MutableList<Char> = MutableList(wordToGuess.length) { '_' }
    private var remainingAttempts: Int = 6
    private var usedLetters: MutableList<Char> = mutableListOf()

    fun startNewGame() {
        currentWord = WordDatabase.getRandomWord()
        maskedWord = StringBuilder("_".repeat(currentWord.length))
    }

    fun guessLetter(letter: Char): GuessResult {
        if (letter in currentWord) {
            for (i in currentWord.indices) {
                if (currentWord[i] == letter) {
                    maskedWord[i] = letter
                }
            }
            if (!maskedWord.contains('_')) {
                return GuessResult.WIN
            }
            return GuessResult.CORRECT
        } else {
            incorrectGuesses++
            if (incorrectGuesses >= maxIncorrectGuesses) {
                return GuessResult.LOSE
            }
            return GuessResult.INCORRECT
        }
    }


    fun isGameComplete(): Boolean {
        return !maskedWord.contains("_") || incorrectGuesses >= 6
    }

    fun getCurrentWord(): String {
        return currentWord
    }

    fun getMaskedWord(): String {
        return maskedWord.toString()
    }

    fun saveState(bundle: Bundle) {
        bundle.putString("currentWord", currentWord)
        bundle.putString("maskedWord", maskedWord.toString())
        bundle.putInt("incorrectGuesses", incorrectGuesses)
        bundle.putInt("hintCount", hintCount)
    }

    fun restoreState(bundle: Bundle) {
        currentWord = bundle.getString("currentWord") ?: ""
        maskedWord = StringBuilder(bundle.getString("maskedWord") ?: "")
        incorrectGuesses = bundle.getInt("incorrectGuesses")
        hintCount = bundle.getInt("hintCount")
    }

    fun getIncorrectGuesses(): Int {
        return incorrectGuesses
    }

    fun setIncorrectGuesses(count: Int) {
        this.incorrectGuesses = count
    }
    private fun updateMaskedWord(letter: Char) {
        for (i in currentWord.indices) {
            if (currentWord[i] == letter) {
                maskedWord[i] = letter
            }
        }
    }


    enum class GuessResult {
        CORRECT, INCORRECT, WIN, LOSE
    }

    private var hintCount = 0

    fun getHint(): String {
        return when (hintCount) {
            0 -> {
                hintCount++
                "Your hint message here"
            }

            1 -> {
                hintCount++
                "Half of the letters not in the word have been disabled."
            }

            2 -> {
                hintCount++
                "All vowels have been revealed."
            }

            else -> "No more hints available."
        }
    }
}