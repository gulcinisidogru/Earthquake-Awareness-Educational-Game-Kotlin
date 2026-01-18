package com.example.deprembilincioyunuilerleme

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deprembilincioyunuilerleme.databinding.ActivityQuizBinding
import com.google.firebase.database.*
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var database: DatabaseReference

    private var questionList = mutableListOf<Question>()
    private var currentQuestionIndex = 0
    private var score = 0
    private var realPlayerName = ""
    private var charResId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
        realPlayerName = sharedPref.getString("playerName", "Kahraman") ?: "Kahraman"
        charResId = sharedPref.getInt("charRes", R.drawable.character_ali)

        binding.characterCloseUp.setImageResource(charResId)

        binding.finalResultContainer.visibility = View.GONE
        binding.transitionArea.visibility = View.GONE
        binding.quizArea.visibility = View.VISIBLE

        loadQuestionsFromFirebase()

        binding.btnA.setOnClickListener { checkAnswer("a") }
        binding.btnB.setOnClickListener { checkAnswer("b") }
        binding.btnC.setOnClickListener { checkAnswer("c") }
        binding.btnD.setOnClickListener { checkAnswer("d") }

        binding.btnContinue.setOnClickListener { showTransitionScene() }


        binding.btnNextActivity.setOnClickListener {
            // Bir sonraki aşamaya (Nefes Egzersizi) geçiş
            val intent = Intent(this, BreathExerciseActivity::class.java)
            startActivity(intent)

            // Bu sayfayı kapat
            finish()
        }
    }

    private fun loadQuestionsFromFirebase() {
        /* TODO: Veritabanı bağlantı hatası alıyorsanız .getInstance() içine
           Firebase konsolundaki URL'nizi parametre olarak ekleyin. */
        database = FirebaseDatabase.getInstance().getReference("questions")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                questionList.clear()
                val allQuestions = mutableListOf<Question>()

                for (data in snapshot.children) {
                    val question = data.getValue(Question::class.java)
                    question?.let { allQuestions.add(it) }
                }

                // Soruları karıştır ve sadece ilk 10 tanesini al
                allQuestions.shuffle()
                questionList = allQuestions.take(10).toMutableList()

                if (questionList.isNotEmpty()) displayQuestion(currentQuestionIndex)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@QuizActivity, "Sorular yüklenemedi!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayQuestion(index: Int) {
        val q = questionList[index]
        binding.progressText.text = "Soru: ${index + 1} / ${questionList.size}"
        binding.scoreText.text = "Skor: $score"
        binding.questionText.text = q.text
        binding.btnA.text = q.a
        binding.btnB.text = q.b
        binding.btnC.text = q.c
        binding.btnD.text = q.d
    }

    private fun checkAnswer(selected: String) {
        val correctAnswer = questionList[currentQuestionIndex].correct

        if (selected == correctAnswer) {
            score += 10
            Toast.makeText(this, "Harika! +10 Puan ✅", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Yanlış Cevap! ❌", Toast.LENGTH_SHORT).show()
        }

        if (currentQuestionIndex < questionList.size - 1) {
            currentQuestionIndex++
            displayQuestion(currentQuestionIndex)
        } else {
            showFinalResult()
        }
    }

    private fun showFinalResult() {
        binding.quizArea.visibility = View.GONE
        binding.finalResultContainer.visibility = View.VISIBLE
        binding.finalScoreText.text = "SKOR: $score"

        triggerKonfetti()
        showStarsAnim(score)

        try {
            val mp = MediaPlayer.create(this, R.raw.tada_sound)
            mp.start()
            mp.setOnCompletionListener { it.release() }
        } catch (e: Exception) {}

        saveFinalScoreToFirebase()
    }

    private fun saveFinalScoreToFirebase() {
        val sharedPref = getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
        val playerId = sharedPref.getString("playerId", null)

        if (playerId != null) {
            val dbRef = FirebaseDatabase.getInstance().getReference("players").child(playerId)
            dbRef.child("score").setValue(score)
        }

        with(sharedPref.edit()) {
            putInt("lastScore", score)
            apply()
        }
    }

    private fun showTransitionScene() {
        binding.finalResultContainer.visibility = View.GONE
        binding.transitionArea.visibility = View.VISIBLE
        binding.transitionMessage.text = "Harikaydın $realPlayerName! \n\nŞimdi biraz rahatlamaya ne dersin? Seninle birlikte nefes egzersizi yapacağız. Hazır mısın?"

        val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        binding.characterCloseUp.startAnimation(fadeIn)
    }

    private fun showStarsAnim(puan: Int) {
        val anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        binding.star1.visibility = View.INVISIBLE
        binding.star2.visibility = View.INVISIBLE
        binding.star3.visibility = View.INVISIBLE

        binding.star1.postDelayed({
            binding.star1.visibility = View.VISIBLE
            binding.star1.startAnimation(anim)
        }, 400)

        if (puan >= 40) { // Başarı eşiği %40  2. Yıldız
            binding.star2.postDelayed({
                binding.star2.visibility = View.VISIBLE
                binding.star2.startAnimation(anim)
            }, 800)
        }

        if (puan >= 80) { // Yüksek başarı eşiği %80  3. Yıldız
            binding.star3.postDelayed({
                binding.star3.visibility = View.VISIBLE
                binding.star3.startAnimation(anim)
            }, 1200)
        }
    }
    //And yes, even if they know nothing about it, they get 1 star. I love children. :)

    private fun triggerKonfetti() {
        val party = Party(
            speed = 10f,
            maxSpeed = 35f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xbbf1fa),
            position = Position.Relative(0.5, 0.3),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
        )
        binding.konfettiView.start(party)
    }
}