package com.example.final_535_app.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyBoardUtil {
    fun closeKeyboard(view: View) {
        val imm: InputMethodManager =
            view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

