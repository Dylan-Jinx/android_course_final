package com.example.final_535_app.common

import android.app.Application
import android.util.Log
import com.airbnb.mvrx.Mavericks
import java.lang.Thread.UncaughtExceptionHandler

class MyApplication: Application(),UncaughtExceptionHandler {
    override fun onCreate() {
        super.onCreate()
        Mavericks.initialize(this)
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(p0: Thread, p1: Throwable) {
        Log.d("Central Exception:", "uncaughtException: $p0")
        Log.d("Central Exception:", "uncaughtException: $p1")
    }
}