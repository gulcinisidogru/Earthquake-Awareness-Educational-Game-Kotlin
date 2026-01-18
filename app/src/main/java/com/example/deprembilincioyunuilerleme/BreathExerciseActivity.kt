package com.example.deprembilincioyunuilerleme

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.deprembilincioyunuilerleme.databinding.ActivityBreathExerciseBinding
import kotlin.math.cos
import kotlin.math.sin

class BreathExerciseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBreathExerciseBinding
    private val radius = 450f // Yıldızın yörünge genişliği

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreathExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Egzersizi başlat
        startScientificBreathCycle()

        // "HARİKAYDIN! BİTİR" butonuna basınca Eğitici Kartlar ekranına geç
        binding.btnFinishBreath.setOnClickListener {
            val intent = Intent(this, FlashCardsActivity::class.java)
            startActivity(intent)
            finish() // Nefes egzersizi ekranını kapatır
        }
    }

    private fun startScientificBreathCycle() {
        // 1. NEFES AL (4 sn)
        animateStep("NEFES AL", 0f, 180f, 4000) {
            // 2. TUT (4 sn)
            runTimer(4, "NEFESİNİ TUT") {
                // 3. NEFES VER (4 sn)
                animateStep("NEFES VER", 180f, 360f, 4000) {
                    // Egzersiz başarıyla bittiğinde buton görünür olur
                    binding.btnFinishBreath.visibility = View.VISIBLE
                    binding.breathStatusText.text = "MÜKEMMELSİN!"
                    binding.timerText.text = "" // Halkanın ortası boş kalsın
                }
            }
        }
    }

    private fun animateStep(label: String, startAngle: Float, endAngle: Float, durationMs: Long, onEnd: () -> Unit) {
        binding.breathStatusText.text = label

        // Saniye Sayacı
        val timer = ValueAnimator.ofInt((durationMs/1000).toInt(), 1)
        timer.duration = durationMs
        timer.interpolator = LinearInterpolator()
        timer.addUpdateListener { binding.timerText.text = it.animatedValue.toString() }
        timer.start()

        // Yıldız ve ProgressBar Hareketi
        val animator = ValueAnimator.ofFloat(startAngle, endAngle)
        animator.duration = durationMs
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            val angleDeg = it.animatedValue as Float
            val angleRad = Math.toRadians(angleDeg.toDouble() - 90)

            binding.starGuide.translationX = (radius * cos(angleRad)).toFloat()
            binding.starGuide.translationY = (radius * sin(angleRad)).toFloat()

            // ProgressBar'ı doldur
            binding.breathProgressBar.progress = (angleDeg / 360f * 1000).toInt()
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) { onEnd() }
        })
        animator.start()
    }

    private fun runTimer(sec: Int, label: String, onEnd: () -> Unit) {
        binding.breathStatusText.text = label
        val timer = ValueAnimator.ofInt(sec, 1)
        timer.duration = (sec * 1000).toLong()
        timer.interpolator = LinearInterpolator()
        timer.addUpdateListener { binding.timerText.text = it.animatedValue.toString() }
        timer.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) { onEnd() }
        })
        timer.start()
    }
}