package com.example.deprembilincioyunuilerleme

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.deprembilincioyunuilerleme.databinding.ActivityLocationBinding

class LocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btnOpenMaps.setOnClickListener {
            // "Deprem Toplanma Alanı" araması için Google Maps Intent'i
            // "nearby" ifadesi haritaya "şu an olduğum yere en yakın olanları bul" talimatı verir.
            val gmmIntentUri = Uri.parse("geo:0,0?q=en+yakın+deprem+toplanma+alanı")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            // Eğer telefonlarda Google Maps yüklüyse aç, değilse tarayıcıdan aç
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            } else {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/Deprem+Toplanma+Alanı"))
                startActivity(browserIntent)
            }
        }
    }
}