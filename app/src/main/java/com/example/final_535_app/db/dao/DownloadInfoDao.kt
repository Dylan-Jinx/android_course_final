package com.example.final_535_app.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.final_535_app.db.model.DownloadInfoModel

@Dao
interface DownloadInfoDao {
    /**
     * Get a user by id.
     * @return the download info from the table with a specific id.
     */
    @Query("SELECT * FROM download_info WHERE bid = :bid")
    fun getDownloadInfoById(bid: String): DownloadInfoModel

    /**
     * Insert a user in the database. If the user already exists, replace it.
     * @param downloadInfoModel the user to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDownloadInfo(
        downloadInfoModel: DownloadInfoModel): Long

    /**
     * Delete single users.
     */
    @Query("DELETE FROM download_info WHERE id = :id")
    fun deleteDownInfoById(id: String)

    /**
     * Delete all users.
     */
    @Query("DELETE FROM download_info")
    fun deleteAllInfo()

    @Query("SELECT * FROM download_info")
    fun getAllLocalDownloadInfo() : List<DownloadInfoModel>
}