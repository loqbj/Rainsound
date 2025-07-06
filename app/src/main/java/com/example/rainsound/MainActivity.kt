package com.example.rainsound

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example for one card
        val thunderCard = findViewById<CardView>(R.id.draggableCard)

        thunderCard.setOnClickListener {
            openSoundPlayer(
                imageResId = R.drawable.img_3,
                soundResId = R.raw.thunderstorm_sound
            )
        }

        // You can add more cards like this:
        // val rainCard = findViewById<CardView>(R.id.rainCard)
        // rainCard.setOnClickListener {
        //     openSoundPlayer(R.drawable.img_rain, R.raw.rain_sound)
        // }
    }

    private fun openSoundPlayer(imageResId: Int, soundResId: Int) {
        val intent = Intent(this, SoundPlayerActivity::class.java).apply {
            putExtra("imageResId", imageResId)
            putExtra("soundResId", soundResId)
        }
        startActivity(intent)
    }
}
