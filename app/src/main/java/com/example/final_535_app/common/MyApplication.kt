package com.example.final_535_app.common

import android.app.Application
import com.airbnb.mvrx.Mavericks

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Mavericks.initialize(this)
    }
}