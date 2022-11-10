package com.example.final_535_app.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.final_535_app.R

@Suppress("DEPRECATION")
class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        },2000)
    }
}