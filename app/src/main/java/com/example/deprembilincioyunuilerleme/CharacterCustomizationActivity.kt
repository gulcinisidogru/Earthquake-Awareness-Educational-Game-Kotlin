package com.example.deprembilincioyunuilerleme

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deprembilincioyunuilerleme.databinding.ActivityCharacterCustomizationBinding

class CharacterCustomizationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterCustomizationBinding

    // Varsayılan karakterimiz Ali olsun
    private var selectedImg = R.drawable.character_ali
    private var selectedName = "ALİ"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterCustomizationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Başlangıçta Ali seçili görünsün
        updateSelection(binding.boxAli, R.drawable.character_ali, "ALİ")

        // Karakter Kutularına Tıklama Olayları
        binding.boxAli.setOnClickListener { updateSelection(it, R.drawable.character_ali, "ALİ") }
        binding.boxAyse.setOnClickListener { updateSelection(it, R.drawable.character_ayse, "AYŞE") }
        binding.boxBurak.setOnClickListener { updateSelection(it, R.drawable.character_burak, "BURAK") }
        binding.boxNermin.setOnClickListener { updateSelection(it, R.drawable.character_nermin, "NERMİN") }

        // Maceraya Başla Butonu
        binding.continueButton.setOnClickListener {
            // Seçimi kalıcı olarak kaydet
            val sharedPref = getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putInt("charRes", selectedImg)
                putString("charName", selectedName)
                apply()
            }

            Toast.makeText(this, "Kahramanın $selectedName hazır! 🚀", Toast.LENGTH_SHORT).show()

            // Quiz ekranına geçiş
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateSelection(selectedView: View, resId: Int, name: String) {
        selectedImg = resId
        selectedName = name

        // 1. Tüm kutuların arka plan rengini sıfırla (Beyaz yap)
        resetBoxColors()

        // 2. Seçilen kutunun arka planını soft mor yap
        selectedView.setBackgroundResource(R.drawable.character_box_bg)
        selectedView.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#E1BEE7"))
    }

    private fun resetBoxColors() {
        // Tüm LinearLayout kutularını varsayılan beyaz hallerine döndürür
        binding.boxAli.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.WHITE)
        binding.boxAyse.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.WHITE)
        binding.boxBurak.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.WHITE)
        binding.boxNermin.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.WHITE)
    }
}