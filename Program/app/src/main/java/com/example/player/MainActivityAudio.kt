package com.example.player

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivityAudio : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var selectedAudioUri: Uri? = null
    private var isRestart = false
    private var currentVolume: Float = 0.5f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_audio)

        val editTextUrl: EditText = findViewById(R.id.editTextUrl)
        val textView: TextView = findViewById((R.id.textView))
        val textVol: TextView = findViewById((R.id.textVolum))
        val btnSelectAudio: Button = findViewById(R.id.btnSelectAudio)
        val btnUrlSelectAudio: Button = findViewById(R.id.btnUrlSelectAudio)
        val btnPlayAudio: Button = findViewById(R.id.btnPlayAudio)
        val btnPauseAudio: Button = findViewById(R.id.btnPauseAudio)
        val btnStopAudio: Button = findViewById(R.id.btnStopAudio)
        val btnAddVolume: Button = findViewById(R.id.btnAddVolume)
        val btnSubVolume: Button = findViewById(R.id.btnSubVolume)
        val btnRestart: Button = findViewById(R.id.btnRestart)

        val buttons: List<Button> = listOf(btnPlayAudio, btnPauseAudio, btnStopAudio)

        val selectAudioLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedAudioUri = result.data?.data
                selectedAudioUri?.let { uri ->
                    mediaPlayer = MediaPlayer().apply {
                        setDataSource(this@MainActivityAudio, uri)
                        prepareAsync()
                        setOnPreparedListener {
                            currentVolume = 0.5f
                            textVol.text = "Гуч " + String.format("%.1f", currentVolume)
                            setVolume(currentVolume, currentVolume)
                            textView.setTextColor(ContextCompat.getColor(this@MainActivityAudio, android.R.color.holo_green_dark))
                            textView.text = "Музика вибрана"
                            activeButton(buttons, btnPauseAudio)
                        }
                    }
                }
            }
        }

        btnUrlSelectAudio.setOnClickListener {
            //http://192.168.0.124:5000/audio
            val url = editTextUrl.text.toString()
            if (url.isNotEmpty()) {
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(url)
                    setOnPreparedListener {
                        start()
                        currentVolume = 0.5f
                        textVol.text = "Гуч " + String.format("%.1f", currentVolume)
                        setVolume(currentVolume, currentVolume)
                        textView.setTextColor(ContextCompat.getColor(this@MainActivityAudio, android.R.color.holo_green_dark))
                        textView.text = "Музика вибрана"
                        activeButton(buttons, btnPauseAudio)
                    }
                    setOnErrorListener { _, _, _ ->
                        Toast.makeText(this@MainActivityAudio, "Помилка відтворення аудіо!", Toast.LENGTH_SHORT).show()
                        false
                    }
                    prepareAsync()
                }
            } else {
                Toast.makeText(this, "Введіть URL!", Toast.LENGTH_SHORT).show()
            }
        }

        btnSelectAudio.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "audio/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            selectAudioLauncher.launch(intent)
        }

        btnPlayAudio.setOnClickListener {
            if (mediaPlayer != null){
                if (!mediaPlayer!!.isPlaying) {
                    mediaPlayer!!.start()
                    activeButton(buttons, btnPlayAudio)
                }
            }
        }

        btnPauseAudio.setOnClickListener {
            if (mediaPlayer != null) {
                mediaPlayer?.pause()
                activeButton(buttons, btnPauseAudio)
            }
        }

        btnStopAudio.setOnClickListener {
            if (mediaPlayer != null) {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
                textView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
                textView.text = "Музика не вибрана"
                activeButton(buttons, btnStopAudio)
            }
        }

        btnAddVolume.setOnClickListener {
            currentVolume = (currentVolume + 0.1f).coerceAtMost(1.0f)
            textVol.text = "Гуч " + String.format("%.1f", currentVolume)
            mediaPlayer?.setVolume(currentVolume, currentVolume)
        }

        btnSubVolume.setOnClickListener {
            currentVolume = (currentVolume - 0.1f).coerceAtLeast(0.0f)
            textVol.text = "Гуч " + String.format("%.1f", currentVolume)
            mediaPlayer?.setVolume(currentVolume, currentVolume)
        }

        btnRestart.setOnClickListener {
            if (isRestart){
                isRestart =false
                btnRestart.text = "->"
                mediaPlayer?.isLooping = isRestart
            }
            else{
                isRestart =true
                btnRestart.text = "O"
                mediaPlayer?.isLooping = isRestart
            }
        }
    }

    private fun activeButton(buttons: List<Button>, selectedButton: Button) {
        buttons.forEach { button ->
            button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_purple))
        }
        selectedButton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
    }

}