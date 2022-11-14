package com.example.final_535_app.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "download_info")
data class DownloadInfoModel (
    @PrimaryKey
    val bid: String,
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "fileName")
    val fileName: String? = null,
    @ColumnInfo(name = "path")
    val path: String? = null,
)