package com.example.final_535_app.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.final_535_app.R
import com.example.final_535_app.adapter.LocalCacheAdapter
import com.example.final_535_app.databinding.ActivityLocalDownloadBinding
import com.example.final_535_app.db.DBInjection
import com.example.final_535_app.db.model.DownloadInfoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocalDownloadActivity : AppCompatActivity() {

    lateinit var binding: ActivityLocalDownloadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocalDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rcLocalCache.layoutManager = LinearLayoutManager(this)
        initEvent()
}

    private fun initEvent() {
        binding.ivLocalCacheBack.setOnClickListener{
            onBackPressed()
        }
    }

    override fun onResume() {
        var instance = DBInjection.provideDownloadInfoDataSource(this)
        GlobalScope.launch {
            withContext(Dispatchers.IO){
                var datas = instance.getAllLocalDownloadInfo()
                var localAdapter = LocalCacheAdapter(datas)
                if(datas.isEmpty()){
                    withContext(Dispatchers.Main){
                        localAdapter.setEmptyViewLayout(this@LocalDownloadActivity, R.layout.error_no_data)
                        localAdapter.isEmptyViewEnable = true
                    }
                }else{
                    withContext(Dispatchers.Main){
                        localAdapter.isEmptyViewEnable = false
                    }
                }
                binding.rcLocalCache.adapter = localAdapter
            }
        }
        super.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}