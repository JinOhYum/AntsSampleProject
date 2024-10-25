package com.ant.ecg.view

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ant.ecg.R
import com.ant.ecg.databinding.ActivityDetailHistoryBinding

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)

        setContentView(binding.root)

        init()
    }

    private fun init(){
        binding.layoutTopBar.layoutTopLeft.visibility = View.INVISIBLE
        binding.layoutTopBar.tvTitle.visibility = View.VISIBLE
        binding.layoutTopBar.layoutConnection.visibility = View.INVISIBLE

        binding.layoutTopBar.tvTitle.text = getText(R.string.text_history_detail)
    }
}