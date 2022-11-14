package com.example.final_535_app.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.final_535_app.activity.VideoDetailActivity
import com.example.final_535_app.databinding.RecyclerviewItemLocalCacheBinding
import com.example.final_535_app.db.model.DownloadInfoModel

class LocalCacheAdapter(var data: List<DownloadInfoModel>):
    BaseQuickAdapter<DownloadInfoModel, LocalCacheAdapter.LocalCacheViewHolder>() {

    lateinit var binding: RecyclerviewItemLocalCacheBinding

    override fun onBindViewHolder(
        holder: LocalCacheViewHolder,
        position: Int,
        item: DownloadInfoModel?
    ) {
        holder.bindData(data[position])
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): LocalCacheViewHolder {
        binding = RecyclerviewItemLocalCacheBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LocalCacheViewHolder(binding)
    }


    override fun getItemCount(items: List<DownloadInfoModel>): Int = data.size

    inner class LocalCacheViewHolder(binding:RecyclerviewItemLocalCacheBinding): RecyclerView.ViewHolder(binding.root){
        val context = binding.root.context
        fun bindData(downloadInfoModel: DownloadInfoModel) {
            binding.tvLocalCacheTitle.text = downloadInfoModel.fileName.toString()
            binding.rvLocalCacheBanner.setImageDrawable(Drawable.createFromPath(downloadInfoModel.path.toString()))
            binding.llItemLocalCache.setOnClickListener{
                var intent = Intent(context, VideoDetailActivity::class.java)
                    .putExtra("local", true)
                    .putExtra("bvid_local", downloadInfoModel.bid)
                context.startActivity(intent)
                Toast.makeText(binding.root.context, ""+downloadInfoModel, Toast.LENGTH_SHORT).show()
            }
        }
    }
}