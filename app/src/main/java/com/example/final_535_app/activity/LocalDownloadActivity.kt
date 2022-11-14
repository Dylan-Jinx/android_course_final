package com.example.final_535_app.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.final_535_app.R
import com.example.final_535_app.db.DBInjection
import com.example.final_535_app.db.model.DownloadInfoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocalDownloadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_download)

        var instance = DBInjection.provideDownloadInfoDataSource(this)
        GlobalScope.launch {
            withContext(Dispatchers.IO){
                var datas = instance.getAllLocalDownloadInfo()
                withContext(Dispatchers.Main){
                    Toast.makeText(this@LocalDownloadActivity, ""+datas, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}