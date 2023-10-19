package com.example.hangmangame

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity<ViewGroup : View?> : AppCompatActivity() {

    private lateinit var currentWord: String
    private lateinit var maskedWord: StringBuilder
    private var incorrectGuesses = 0
    private var hintUsed = 0
    private val gameEngine = GameEngine()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (savedInstanceState == null) {
            gameEngine.startNewGame()
        }

        setUpLetterButtons()
        setUpHintButton()
    }

    private val letterButtonIds = listOf(
        R.id.buttonA,
        R.id.buttonB,
        R.id.buttonC,
        R.id.buttonD,
        R.id.buttonE,
        R.id.buttonF,
        R.id.buttonG,
        R.id.buttonH,
        R.id.buttonI,
        R.id.buttonJ,
        R.id.buttonK,
        R.id.buttonL,
        R.id.buttonM,
        R.id.buttonN,
        R.id.buttonO,
        R.id.buttonP,
        R.id.buttonQ,
        R.id.buttonR,
        R.id.buttonS,
        R.id.buttonT,
        R.id.buttonU,
        R.id.buttonV,
        R.id.buttonW,
        R.id.buttonX,
        R.id.buttonY,
        R.id.buttonZ
    )


    private fun setUpLetterButtons() {
        for (id in letterButtonIds) {
            val button = findViewById<Button>(id)
            button.setOnClickListener {
                val chosenLetter = button.text.toString().first()
                handleLetterGuess(chosenLetter)
                button.isEnabled = false  // Disable the button after it's clicked
            }
        }
    }

    private fun setUpHintButton() {
        val hintButton: Button = findViewById(R.id.hintButton)
        hintButton.setOnClickListener {
            when (hintUsed) {
                0 -> {
                    // Display a hint message.
                    val hintTextView: TextView = findViewById(R.id.hintTextView)
                    hintTextView.text = "Your hint message here"
                    hintUsed++
                }

                1 -> {
                    // Disable half of the remaining letters not in the word and cost a turn.
                    disableHalfLettersNotInWord()
                    incorrectGuesses++
                    hintUsed++
                }

                2 -> {
                    // Show all vowels, cost a turn, and disable vowel buttons.
                    showAllVowels()
                    incorrectGuesses++
                    hintUsed++
                }

                else -> {
                    Toast.makeText(this, "Hint not available!", Toast.LENGTH_SHORT).show()
                }
            }
            updateUIFromState()
        }
    }






    private fun handleLetterGuess(letter: Char) {
        when (gameEngine.guessLetter(letter)) {
            GameEngine.GuessResult.CORRECT -> {
                // If you correctly guess, update the word display
            }

            GameEngine.GuessResult.INCORRECT -> {
                // If incorrect guess, update the hangman drawing or check if game over
                incorrectGuesses++
            }
        updateUIFromState()
    }

    private fun updateUIFromState() {
        val wordTextView: TextView = findViewById(R.id.wordTextView)
        wordTextView.text = gameEngine.getMaskedWord()

        val hangmanImageView: ImageView = findViewById(R.id.hangmanImageView)
        val drawableId = resources.getIdentifier("hm${incorrectGuesses + 1}", "drawable", packageName)
        hangmanImageView.setImageResource(drawableId)

        if (gameEngine.isGameComplete()) {
            if (maskedWord.contains("_")) {
                Toast.makeText(this, "You lost!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "You won!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun disableHalfLettersNotInWord() {
        val lettersNotInWord = mutableListOf<Button>()

        for (id in letterButtonIds) {
            val button = findViewById<Button>(id)
            val letter = button.text.toString().first()
            if (letter !in currentWord && button.isEnabled) {
                lettersNotInWord.add(button)
            }
        }

        lettersNotInWord.shuffle()
        for (i in 0 until lettersNotInWord.size / 2) {
            lettersNotInWord[i].isEnabled = false
        }
    }

    private fun showAllVowels() {
        val vowels = listOf('A', 'E', 'I', 'O', 'U')

        for (id in letterButtonIds) {
            val button = findViewById<Button>(id)
            val letter = button.text.toString().first()
            if (letter in vowels) {
                button.isEnabled = false
                if (letter in currentWord) {
                    val index = currentWord.indexOf(letter)
                    maskedWord[index] = letter
                }
            }
        }

    }
    fun restoreGameState(savedInstanceState: Bundle) {
        gameEngine.restoreState(savedInstanceState)
        updateUIFromState()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        gameEngine.saveState(outState)
    }
}
