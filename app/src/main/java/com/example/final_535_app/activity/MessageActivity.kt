package com.example.final_535_app.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.final_535_app.R
import com.example.final_535_app.databinding.ActivityMessageBinding

@Suppress("DEPRECATION")
class MessageActivity : AppCompatActivity() {

    lateinit var binding: ActivityMessageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)
        initEvent()
    }

    private fun initEvent() {
        binding.ivMessageBack.setOnClickListener{
            onBackPressed()
        }
    }
}