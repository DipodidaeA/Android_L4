package com.example.player

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAudio: Button = findViewById(R.id.btnAudio)
        val btnVideo: Button = findViewById(R.id.btnVideo)

        btnAudio.setOnClickListener {
            val intent = Intent(this, MainActivityAudio::class.java)
            startActivity(intent)
        }

        btnVideo.setOnClickListener {
            val intent = Intent(this, MainActivityVideo::class.java)
            startActivity(intent)
        }

    }
}