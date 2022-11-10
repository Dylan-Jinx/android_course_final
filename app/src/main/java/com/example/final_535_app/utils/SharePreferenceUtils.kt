package com.example.final_535_app.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.final_535_app.model.BilibiliUserInfo
import com.google.gson.Gson
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

object SharePreferenceUtils {

    fun setValueToSharePreference(context:Context?, key: String?, value: Any?){
        val sharedPreferences = context?.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        var editor = sharedPreferences?.edit();
        var datas = Gson().toJson(value)
        editor?.putString(key, datas)
        editor?.commit()
    }

    fun <T> getValueToSharePreference(context:Context?, key: String?, model: Class<T>): T? {
        val sharedPreferences = context?.getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        var datas = sharedPreferences?.getString(key, "")
        if(datas != null){
            return Gson().fromJson(datas, model)
        }else{
            return null
        }
    }
}