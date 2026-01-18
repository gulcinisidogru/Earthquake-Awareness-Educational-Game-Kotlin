package com.example.deprembilincioyunuilerleme

import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deprembilincioyunuilerleme.databinding.ActivityBackpackBinding

class BackpackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBackpackBinding
    private var correctCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackpackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Nesnelere uzun basınca sürükleme başlasın
        setupDrag(binding.itemFlashlight)
        setupDrag(binding.itemWater)
        setupDrag(binding.itemPizza)

        // Çantanın bir şeyi kabul etmesi için dinleyici
        binding.imgBackpack.setOnDragListener(dragListener)
    }

    private fun setupDrag(view: View) {
        view.setOnLongClickListener {
            val clipText = view.tag as String
            val item = ClipData.Item(clipText)
            val dragData = ClipData(clipText, arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), item)
            val shadow = View.DragShadowBuilder(view)

            view.startDragAndDrop(dragData, shadow, view, 0)
            view.visibility = View.INVISIBLE // Sürüklenirken yerinde görünmesin
            true
        }
    }

    private val dragListener = View.OnDragListener { v, event ->
        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> true
            DragEvent.ACTION_DRAG_ENTERED -> {
                v.animate().scaleX(1.2f).scaleY(1.2f).duration = 200
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                v.animate().scaleX(1.0f).scaleY(1.0f).duration = 200
                true
            }
            DragEvent.ACTION_DROP -> {
                val item = event.clipData.getItemAt(0)
                val dragData = item.text.toString()
                val sourceView = event.localState as View

                // "YANLIS" (Pizza) değilse puan artar
                if (dragData != "YANLIS") {
                    Toast.makeText(this, "Aferin! $dragData çantada!", Toast.LENGTH_SHORT).show()
                    sourceView.visibility = View.GONE
                    correctCount++

                    // 2 Doğru nesne (Fener ve Su) toplandığında geçiş yap
                    if (correctCount == 2) {
                        Toast.makeText(this, "Çanta Hazır! Şimdi Güvenli Alanı Bul!", Toast.LENGTH_LONG).show()

                        // SafeZoneActivity'ye geçiş yapıyoruz
                        val intent = Intent(this, SafeZoneActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // Pizza buraya düşer
                    Toast.makeText(this, "Pizza çabuk bozulabilir, başka bir şey seçelim!", Toast.LENGTH_SHORT).show()
                    sourceView.visibility = View.VISIBLE
                }
                v.animate().scaleX(1.0f).scaleY(1.0f).duration = 200
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                val sourceView = event.localState as View
                // Eğer çanta dışına bırakıldıysa tekrar görünür yap
                if (!event.result) {
                    sourceView.visibility = View.VISIBLE
                }
                true
            }
            else -> false
        }
    }
}