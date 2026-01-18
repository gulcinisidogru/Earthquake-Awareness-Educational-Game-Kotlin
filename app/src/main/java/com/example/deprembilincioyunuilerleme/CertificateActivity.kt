package com.example.deprembilincioyunuilerleme

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deprembilincioyunuilerleme.databinding.ActivityCertificateBinding

class CertificateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCertificateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCertificateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Kullanıcı ismini çekiyoruz (Default olarak "Kahraman" atanır)
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userName = sharedPref.getString("userName", "Kahraman")

        // İsmi ekrana yazdırıyoruz
        binding.certificateMessage.text = "Tebrikler $userName!\nArtık bir Deprem Kahramanısın!"

        // AR Uygulamasını İndirme Butonu
        binding.btnNextToAR.setOnClickListener {
            try {
                /* TODO: Kendi GitHub Release linkinizi veya Google Drive indirme bağlantınızı
                   aşağıdaki 'apkUrl' değişkenine yapıştırın.
                */
                val apkUrl = "BURAYA_KENDI_APK_INDIRME_LINKINIZI_YAZIN"

                // Tarayıcıyı veya indirme yöneticisini açan Intent
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(apkUrl)

                // Kullanıcıya bilgi veriyoruz
                Toast.makeText(this, "AR Uygulaması İndiriliyor... Hazır ol Kahraman! 🚀", Toast.LENGTH_LONG).show()

                startActivity(intent)
            } catch (e: Exception) {
                // Herhangi bir hata durumunda (tarayıcı yoksa vb.) kullanıcıyı bilgilendirelim
                Toast.makeText(this, "İndirme başlatılamadı, lütfen internet bağlantını kontrol et.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}