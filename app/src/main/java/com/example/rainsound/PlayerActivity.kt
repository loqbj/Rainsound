package com.example.rainsound

import android.app.TimePickerDialog
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PlayerActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var volumeSeekBar: SeekBar
    private lateinit var imageView: ImageView
    private lateinit var playPauseButton: ImageButton
    private lateinit var timerText: TextView
    private lateinit var addSoundButton: ImageButton
    private lateinit var timerButton: ImageButton

    private var isPlaying = true
    private var timer: CountDownTimer? = null
    private var durationInMillis: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        // View references
        volumeSeekBar = findViewById(R.id.volumeSlider)
        imageView = findViewById(R.id.playerImage)
        playPauseButton = findViewById(R.id.playPauseButton)
        timerText = findViewById(R.id.timerText)
        addSoundButton = findViewById(R.id.addSoundButton)
        timerButton = findViewById(R.id.timerButton)

        // Set image
        val imageResId = intent.getIntExtra("imageResId", R.drawable.img_1)
        imageView.setImageResource(imageResId)

        // Get the dynamic sound resource from intent
        val soundResId = intent.getIntExtra("soundResId", R.raw.thunder)
        mediaPlayer = MediaPlayer.create(this, soundResId)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        // Volume control
        volumeSeekBar.max = 100
        volumeSeekBar.progress = 50
        mediaPlayer.setVolume(0.5f, 0.5f)
        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val volume = progress / 100f
                mediaPlayer.setVolume(volume, volume)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Play / Pause toggle
        playPauseButton.setOnClickListener {
            if (isPlaying) {
                mediaPlayer.pause()
                playPauseButton.setImageResource(R.drawable.play_circle_24dp_e3e3e3_fill0_wght400_grad0_opsz24)
            } else {
                mediaPlayer.start()
                playPauseButton.setImageResource(R.drawable.pause_circle_24dp_e3e3e3_fill0_wght400_grad0_opsz24)
            }
            isPlaying = !isPlaying
        }

        // Add background sound
        addSoundButton.setOnClickListener {
            showSoundPickerDialog()
        }

        // Timer button
        timerButton.setOnClickListener {
            showTimerDialog()
        }

        // Initial timer display
        updateTimerUI()
    }

    private fun showSoundPickerDialog() {
        val sounds = arrayOf("Campfire", "Crickets", "Wind")
        MaterialAlertDialogBuilder(this)
            .setTitle("Choose Background Sound")
            .setItems(sounds) { _, which ->
                val selectedSound = when (which) {
                    0 -> R.raw.campfire
                    1 -> R.raw.crickets
                    2 -> R.raw.wind
                    else -> null
                }
                selectedSound?.let {
                    MediaPlayer.create(this, it).apply {
                        isLooping = true
                        start()
                    }
                }
            }
            .show()
    }

    private fun showTimerDialog() {
        val timePicker = TimePickerDialog(this, { _, hourOfDay, minute ->
            durationInMillis = (hourOfDay * 60 + minute) * 60 * 1000L
            startCountdown(durationInMillis)
        }, 0, 5, true)
        timePicker.setTitle("Set Playback Timer")
        timePicker.show()
    }

    private fun startCountdown(durationMillis: Long) {
        timer?.cancel()
        timer = object : CountDownTimer(durationMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val totalSecs = millisUntilFinished / 1000
                val min = totalSecs / 60
                val sec = totalSecs % 60
                timerText.text = String.format("%02d:%02d", min, sec)
            }

            override fun onFinish() {
                mediaPlayer.pause()
                isPlaying = false
                playPauseButton.setImageResource(R.drawable.play_circle_24dp_e3e3e3_fill0_wght400_grad0_opsz24)
                timerText.text = "00:00"
            }
        }.start()
    }

    private fun updateTimerUI() {
        val totalSecs = mediaPlayer.duration / 1000
        val min = totalSecs / 60
        val sec = totalSecs % 60
        timerText.text = String.format("00:00 / %02d:%02d", min, sec)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        timer?.cancel()
    }
}
