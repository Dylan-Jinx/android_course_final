package com.example.final_535_app.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.lang.ref.SoftReference


object ImageConvertUtil {
    fun byteToBitmap (imgByte: ByteArray?): Bitmap? {
        var imgByte = imgByte
        var input: InputStream? = null
        var bitmap: Bitmap? = null
        val options = BitmapFactory.Options()
        options.inSampleSize = 1
        input = ByteArrayInputStream(imgByte)
        val softRef = SoftReference(
            BitmapFactory.decodeStream(
                input, null, options
            )
        ) //软引用防止OOM
        bitmap = softRef.get()
        if (imgByte != null) {
            imgByte = null
        }
        try {
            if (input != null) {
                input.close()
            }
        } catch (e: IOException) {
            // 异常捕获
            e.printStackTrace()
        }
        return bitmap
    }
}