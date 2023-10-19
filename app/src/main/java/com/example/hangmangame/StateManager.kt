package com.example.hangmangame

import android.os.Bundle

class StateManager {

    companion object {
        private const val KEY_CURRENT_WORD = "key_current_word"
        private const val KEY_MASKED_WORD = "key_masked_word"
        private const val KEY_INCORRECT_GUESSES = "key_incorrect_guesses"
    }

    fun saveGameState(bundle: Bundle, gameEngine: GameEngine) {
        bundle.putString(KEY_CURRENT_WORD, gameEngine.getCurrentWord())
        bundle.putString(KEY_MASKED_WORD, gameEngine.getMaskedWord())
        bundle.putInt(KEY_INCORRECT_GUESSES, gameEngine.getIncorrectGuesses())
    }

    fun restoreGameState(bundle: Bundle, gameEngine: GameEngine) {
        val currentWord = bundle.getString(KEY_CURRENT_WORD) ?: return
        val maskedWord = bundle.getString(KEY_MASKED_WORD) ?: return
        val incorrectGuesses = bundle.getInt(KEY_INCORRECT_GUESSES)
        gameEngine.restoreGameState(bundle)
    }
}
