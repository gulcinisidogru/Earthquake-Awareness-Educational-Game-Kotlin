package com.example.deprembilincioyunuilerleme

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deprembilincioyunuilerleme.databinding.ActivityMainBinding
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Üst barı gizle
        supportActionBar?.hide()

        binding.startButton.setOnClickListener {
            val name = binding.etPlayerName.text.toString().trim()

            if (name.isNotEmpty()) {
                val sharedPref = getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()

                editor.putString("playerName", name)

                /* TODO: Firebase Realtime Database bağlantınızın çalıştığından emin olun.
                   Eğer bağlantıda sorun yaşıyorsanız Firebase konsolundaki URL'yi
                   getInstance("URL_BURAYA") içine parametre olarak ekleyebilirsiniz. */
                val database = FirebaseDatabase.getInstance("URL_BURAYA")

                // 'database' değişkenini kullanarak referans alıyoruz
                val playersRef = database.getReference("players")
                val newPlayerId = playersRef.push().key

                editor.putString("playerId", newPlayerId)
                editor.apply()

                newPlayerId?.let { id ->
                    val dbRef = playersRef.child(id)
                    dbRef.child("name").setValue(name)
                    dbRef.child("score").setValue(0)

                    // TODO: Buraya 'tarih' veya 'cihaz_modeli' gibi ek bilgiler ekleyerek geliştirebilirsiniz.
                }

                // Karakter Seçim ekranına geçiş
                val intent = Intent(this, CharacterCustomizationActivity::class.java)
                startActivity(intent)
                finish() // Giriş ekranını kapatmak için ekledik

            } else {
                Toast.makeText(this, "Lütfen adını yazar mısın kahraman? 😊", Toast.LENGTH_SHORT).show()
            }
        }
    }
}