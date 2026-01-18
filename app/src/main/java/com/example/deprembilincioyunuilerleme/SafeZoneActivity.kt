package com.example.deprembilincioyunuilerleme

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deprembilincioyunuilerleme.databinding.ActivitySafeZoneBinding

class SafeZoneActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySafeZoneBinding
    private var score = 0
    private val totalTarget = 2 // Bulunması gereken toplam güvenli nokta sayısı

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySafeZoneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Başlangıçta skoru yazdır
        updateScoreDisplay()

        // GÜVENLİ NOKTA: MASA
        binding.safeMasa.setOnClickListener {
            if (it.isEnabled) { // Daha önce tıklanmadıysa
                it.isEnabled = false // Tekrar tıklanmasın
                handleAction(true, "Harika! Masanın altı çök-kapan-tutun için çok güvenli! ✅")
            }
        }

        // GÜVENLİ NOKTA: KOLTUK YANI
        binding.safeKoltuk.setOnClickListener {
            if (it.isEnabled) {
                it.isEnabled = false
                handleAction(true, "Süper! Sağlam koltukların yanı yaşam üçgeni oluşturur! ✅")
            }
        }

        // TEHLİKELİ NOKTA: PENCERE
        binding.dangerPencere.setOnClickListener {
            Toast.makeText(this, "Dikkat! Pencerelerden ve camlardan uzak durmalıyız! ❌", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleAction(isCorrect: Boolean, message: String) {
        if (isCorrect) {
            score++
            updateScoreDisplay()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            // Eğer tüm güvenli noktalar bulunduysa sertifikaya geç
            if (score >= totalTarget) {
                Toast.makeText(this, "Tebrikler! Güvenli yerleri öğrendin. Şimdi ödül zamanı!", Toast.LENGTH_LONG).show()

                // Sertifika ekranına yönlendir
                val intent = Intent(this, CertificateActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun updateScoreDisplay() {
        binding.scoreText.text = "Güvenli Yerleri Bul: $score / $totalTarget"
    }
}