package com.example.deprembilincioyunuilerleme

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.deprembilincioyunuilerleme.databinding.ActivityFlashCardsBinding

class FlashCardsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFlashCardsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlashCardsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Kart içeriklerini çocukların anlayacağı kısa ve öz cümlelerle kuruyoruz
        setupFlipCard(binding.card1.root, "FENER 🔦", "Karanlıkta yolumuzu aydınlatır.")
        setupFlipCard(binding.card2.root, "SU 💧", "Vücudumuzun susuz kalmaması için şart!")
        setupFlipCard(binding.card3.root, "DÜDÜK 🔊", "Sesimizi uzaklara duyurmamızı sağlar.")
        setupFlipCard(binding.card4.root, "BİSKÜVİ 🍪", "Acıktığımızda bize enerji verir.")

        // "HEPSİNİ ÖĞRENDİM" butonuna tıklayınca sürükle-bırak oyununa geç
        binding.btnNext.setOnClickListener {
            val intent = Intent(this, BackpackActivity::class.java)
            startActivity(intent)
            // finish() // İstersen kartlara geri dönemesinler diye burayı açabilirsin
        }
    }

    private fun setupFlipCard(cardRoot: View, frontName: String, backInfo: String) {
        val front = cardRoot.findViewById<View>(R.id.cardFront)
        val back = cardRoot.findViewById<View>(R.id.cardBack)
        val txtFront = cardRoot.findViewById<TextView>(R.id.frontText)
        val txtBack = cardRoot.findViewById<TextView>(R.id.backText)

        txtFront.text = frontName
        txtBack.text = backInfo

        var isFront = true

        // 'animator' klasöründeki 3D dönüş dosyalarını yüklüyoruz
        val outAnim = AnimatorInflater.loadAnimator(this, R.animator.out_animation) as AnimatorSet
        val inAnim = AnimatorInflater.loadAnimator(this, R.animator.in_animation) as AnimatorSet

        cardRoot.setOnClickListener {
            if (isFront) {
                // Ön yüz gider, arka yüz gelir
                outAnim.setTarget(front)
                inAnim.setTarget(back)
                isFront = false
            } else {
                // Arka yüz gider, ön yüz gelir
                outAnim.setTarget(back)
                inAnim.setTarget(front)
                isFront = true
            }
            outAnim.start()
            inAnim.start()
        }
    }
}