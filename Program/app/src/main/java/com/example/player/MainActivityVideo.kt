package com.example.player

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivityVideo : AppCompatActivity() {
    private lateinit var videoView: VideoView
    private var selectedVideoUri: Uri? = null
    private var isUri: Boolean = false
    private var isRestart: Boolean = false
    private var currentSpeed: Float = 1.0f
    private var currentVolume: Float = 0.5f

    private lateinit var mediaPlayer: MediaPlayer
    private val stepForwardTime = 1000
    private val stepBackwardTime = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_video)

        val editTextUrl: EditText = findViewById(R.id.editTextUrl)
        val textVol: TextView = findViewById((R.id.textVolum))
        val textSpeed: TextView = findViewById((R.id.textSpeed))
        val btnUrlSelectVideo: Button = findViewById(R.id.btnUrlSelectVideo)
        val btnSelectVideo: Button = findViewById(R.id.btnSelectVideo)
        val btnPlayVideo: Button = findViewById(R.id.btnPlayVideo)
        val btnPauseVideo: Button = findViewById(R.id.btnPauseVideo)
        val btnStopVideo: Button = findViewById(R.id.btnStopVideo)
        val btnAddSpeed: Button = findViewById(R.id.btnAddSpeed)
        val btnSubSpeed: Button = findViewById(R.id.btnSubSpeed)
        val btnAddVolume: Button = findViewById(R.id.btnAddVolume)
        val btnSubVolume: Button = findViewById(R.id.btnSubVolume)
        val btnStepForward: Button = findViewById(R.id.btnStepForward)
        val btnStepBackward: Button = findViewById(R.id.btnStepBackward)
        val btnRestart: Button = findViewById(R.id.btnRestart)

        val buttons: List<Button> = listOf(btnPlayVideo, btnPauseVideo, btnStopVideo)

        videoView = findViewById(R.id.videoView)

        videoView.setOnPreparedListener { mp ->
            mediaPlayer = mp
            currentVolume = 0.5f
            textVol.text = "Гуч " + String.format("%.1f", currentVolume)
            textSpeed.text = "Швид " + String.format("%.1f", currentSpeed)
            mediaPlayer.setVolume(currentVolume, currentVolume)
            mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(currentSpeed)
        }

        videoView.setOnCompletionListener {
            if (isUri) {
                if (isRestart) {
                    videoView.start()
                }
            }
        }

        val selectVideoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedVideoUri = result.data?.data
                selectedVideoUri?.let {
                    videoView.setVideoURI(it)
                    videoView.seekTo(1)
                    isUri = true
                    videoView.visibility = View.VISIBLE
                    activeButton(buttons, btnPauseVideo)
                }
            }
        }

        btnUrlSelectVideo.setOnClickListener {
            //http://192.168.0.124:5000/video
            val url = editTextUrl.text.toString()
            if (url.isNotEmpty()) {
                val uri = Uri.parse(url)
                videoView.setVideoURI(uri)
                videoView.seekTo(1)
                isUri = true
                videoView.visibility = View.VISIBLE
                videoView.start()
                activeButton(buttons, btnPlayVideo)
            } else {
                Toast.makeText(this, "Введіть URL відео!", Toast.LENGTH_SHORT).show()
            }
        }

        btnSelectVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "video/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            selectVideoLauncher.launch(intent)
        }

        btnPlayVideo.setOnClickListener {
            if (isUri) {
                videoView.start()
                activeButton(buttons, btnPlayVideo)
            }
        }

        btnPauseVideo.setOnClickListener {
            if (isUri) {
                videoView.pause()
                activeButton(buttons, btnPauseVideo)
            }
        }

        btnStopVideo.setOnClickListener {
            if (isUri) {
                videoView.stopPlayback()
                videoView.setVideoURI(null)
                isUri = false
                videoView.visibility = View.INVISIBLE
                activeButton(buttons, btnStopVideo)
            }
        }

        btnAddSpeed.setOnClickListener {
            if (isUri) {
                currentSpeed = (currentSpeed + 0.1f).coerceAtMost(2.0f)
                textSpeed.text = "Швид " + String.format("%.1f", currentSpeed)
                mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(currentSpeed)
            }
        }

        btnSubSpeed.setOnClickListener {
            if (isUri) {
                currentSpeed = (currentSpeed - 0.1f).coerceAtLeast(0.5f)
                textSpeed.text = "Швид " + String.format("%.1f", currentSpeed)
                mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(currentSpeed)
            }
        }

        btnAddVolume.setOnClickListener {
            if (isUri) {
                currentVolume = (currentVolume + 0.1f).coerceAtMost(1.0f)
                textVol.text = "Гуч " + String.format("%.1f", currentVolume)
                mediaPlayer.setVolume(currentVolume, currentVolume)
            }
        }

        btnSubVolume.setOnClickListener {
            if (isUri) {
                currentVolume = (currentVolume - 0.1f).coerceAtLeast(0.0f)
                textVol.text = "Гуч " + String.format("%.1f", currentVolume)
                mediaPlayer.setVolume(currentVolume, currentVolume)
            }
        }

        btnStepForward.setOnClickListener {
            if (isUri) {
                val currentPosition = videoView.currentPosition
                val newPosition = currentPosition + stepForwardTime
                videoView.seekTo(newPosition)
            }
        }

        btnStepBackward.setOnClickListener {
            if (isUri) {
                val currentPosition = videoView.currentPosition
                val newPosition = currentPosition - stepBackwardTime
                if (newPosition >= 0) {
                    videoView.seekTo(newPosition)
                } else {
                    videoView.seekTo(0)
                }
            }
        }

        btnRestart.setOnClickListener{
            if (isRestart){
                isRestart = false
                btnRestart.text = "->"
            }
            else{
                isRestart = true
                btnRestart.text = "O"
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