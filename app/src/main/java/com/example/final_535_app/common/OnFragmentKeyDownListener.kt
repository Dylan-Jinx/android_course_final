package com.example.final_535_app.common

import android.view.KeyEvent

interface OnFragmentKeyDownListener {
    fun onKeyDown(keyCode: Int, event:KeyEvent):Boolean
}