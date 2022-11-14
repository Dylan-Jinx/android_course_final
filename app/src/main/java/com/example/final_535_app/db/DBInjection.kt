package com.example.final_535_app.db

import android.content.Context
import com.example.final_535_app.db.dao.DownloadInfoDao

object DBInjection {

    fun provideDownloadInfoDataSource(context: Context): DownloadInfoDao {
        val database = DownloadInfoDB.getInstance(context)
        return database.downloadInfoDao()
    }

}