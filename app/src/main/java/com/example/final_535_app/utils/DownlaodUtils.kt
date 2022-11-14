package com.example.final_535_app.utils

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import java.io.File


object DownloadUtil {
    /**
     * 获取下载根目录
     *
     * @param context
     * @param type
     * @return
     */
    fun getRootDir(context: Context, type: String): File {
        val state = Environment.getExternalStorageState()
        val root: File
        if (state == Environment.MEDIA_MOUNTED) {
            root = context.applicationContext.getExternalFilesDir(type)!!
        } else {
            val path: String = context.applicationContext.filesDir.path
            root = File(path + File.separator.toString() + type)
            if (!root.exists() || !root.isFile) {
                root.mkdirs()
            }
        }
        return root
    }

    fun getDownloadUrl(videoUrl: String): String {
        return if (!TextUtils.isEmpty(videoUrl)) videoUrl.replace(
            "/index.m3u8",
            ".mp4"
        ) else videoUrl
    }

    /**
     * 下载文件名
     *
     * @param fileName
     * @param videoUrl
     * @return
     */
    fun getDownloadFileName(fileName: String, videoUrl: String): String {
        return if (videoUrl.lastIndexOf(".") > 0) fileName + videoUrl.substring(
            videoUrl.lastIndexOf(
                "."
            )
        ) else "$fileName.$videoUrl"
    }

    /**
     * 获取下载文件路径,
     *
     * @param courseId 直播或者录播id
     * @param fileName 文件存储的名字,前期用的视频名字,后面改用视频id
     * @return
     */
    fun getDownloadFilePath(courseId: String, fileName: String, context: Context): String {
        val dir: String = getRootDir(
            context.applicationContext,
            Environment.DIRECTORY_DOWNLOADS
        ).toString()+File.separator.toString() + "." + courseId
        val file = File(dir)
        if (!file.exists() || !file.isDirectory) {
            file.mkdirs()
        }
        return dir + File.separator.toString() + fileName
    }

}