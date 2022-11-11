package com.example.final_535_app.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.final_535_app.R
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment

class AppIntroActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addSlide(AppIntroFragment.createInstance(
            backgroundDrawable = R.drawable.appintro
        ))
        addSlide(AppIntroFragment.createInstance(
            backgroundDrawable = R.drawable.appintro2
        ))
        addSlide(AppIntroFragment.createInstance(
            backgroundDrawable = R.drawable.appintro3
        ))
        setSkipText("跳过")
        setSkipText("完成")
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}