package com.example.final_535_app.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.example.final_535_app.R
import com.example.final_535_app.databinding.ActivitySelfWebViewBinding

class SelfWebViewActivity : AppCompatActivity() {

    lateinit var binding: ActivitySelfWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelfWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val webViewUrl = intent.getStringExtra("url")!!

        binding.selfWebview.settings.javaScriptEnabled = true
        binding.selfWebview.webViewClient = WebViewClient()
        binding.selfWebview.loadUrl(webViewUrl)
    }
}