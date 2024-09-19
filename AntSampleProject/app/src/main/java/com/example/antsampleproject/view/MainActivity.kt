package com.example.antsampleproject.view

import android.content.Intent
import android.graphics.Path
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.antsampleproject.R
import com.example.antsampleproject.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.button.setOnClickListener {
            val intent : Intent = Intent(this,CanvasActivity::class.java)
            startActivity(intent)
        }

    }

}