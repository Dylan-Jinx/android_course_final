package com.example.final_535_app.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.final_535_app.db.dao.DownloadInfoDao
import com.example.final_535_app.db.model.DownloadInfoModel

@Database(entities = [DownloadInfoModel::class], version = 1, exportSchema = false)
abstract class DownloadInfoDB : RoomDatabase() {

    abstract fun downloadInfoDao(): DownloadInfoDao

    companion object {
        @Volatile private var INSTANCE: DownloadInfoDB? = null

        fun getInstance(context: Context): DownloadInfoDB =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                DownloadInfoDB::class.java, "app_local.db")
                .build()

    }

}