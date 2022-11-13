package com.example.final_535_app.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Environment
import android.os.StatFs

object SystemInfoUtils {
    /**
     * 检查是否有网络
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val info = getNetworkInfo(context)
        return info != null && info.isAvailable
    }


    /**
     * 检查是否是WIFI
     */
    fun isWifi(context: Context): Boolean {
        val info = getNetworkInfo(context)
        if (info != null) {
            if (info.type == ConnectivityManager.TYPE_WIFI) {
                return true
            }
        }
        return false
    }


    /**
     * 检查是否是移动网络
     */
    fun isMobile(context: Context): Boolean {
        val info = getNetworkInfo(context)
        if (info != null) {
            if (info.type == ConnectivityManager.TYPE_MOBILE) {
                return true
            }
        }
        return false
    }

    private fun getNetworkInfo(context: Context): NetworkInfo? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo
    }


    /**
     * 检查SD卡是否存在
     */
    private fun checkSdCard(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }


    /**
     * 获取手机SD卡总空间
     */
    private fun getSDcardTotalSize(): Long {
        return if (checkSdCard()) {
            val path = Environment.getExternalStorageDirectory()
            val mStatFs = StatFs(path.path)
            val blockSizeLong = mStatFs.blockSizeLong
            val blockCountLong = mStatFs.blockCountLong
            blockSizeLong * blockCountLong
        } else {
            0
        }
    }


    /**
     * 获取SDka可用空间
     */
    private fun getSDcardAvailableSize(): Long {
        return if (checkSdCard()) {
            val path = Environment.getExternalStorageDirectory()
            val mStatFs = StatFs(path.path)
            val blockSizeLong = mStatFs.blockSizeLong
            val availableBlocksLong = mStatFs.availableBlocksLong
            blockSizeLong * availableBlocksLong
        } else {
            0
        }
    }


    /**
     * 获取手机内部存储总空间
     */
    fun getPhoneTotalSize(): Long {
        return if (!checkSdCard()) {
            val path = Environment.getDataDirectory()
            val mStatFs = StatFs(path.path)
            val blockSizeLong = mStatFs.blockSizeLong
            val blockCountLong = mStatFs.blockCountLong
            blockSizeLong * blockCountLong
        } else {
            getSDcardTotalSize()
        }
    }


    /**
     * 获取手机内存存储可用空间
     */
    fun getPhoneAvailableSize(): Long {
        return if (!checkSdCard()) {
            val path = Environment.getDataDirectory()
            val mStatFs = StatFs(path.path)
            val blockSizeLong = mStatFs.blockSizeLong
            val availableBlocksLong = mStatFs.availableBlocksLong
            blockSizeLong * availableBlocksLong
        } else getSDcardAvailableSize()
    }
}