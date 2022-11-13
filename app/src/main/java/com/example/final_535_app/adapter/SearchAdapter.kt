package com.example.final_535_app.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.final_535_app.R
import com.example.final_535_app.activity.VideoDetailActivity
import com.example.final_535_app.databinding.RecyclerviewItemSearchBinding
import com.example.final_535_app.model.BiliBiliVideo
import java.text.DecimalFormat

class SearchAdapter(val videoInfoList: MutableList<BiliBiliVideo>?): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>()  {

    lateinit var binding: RecyclerviewItemSearchBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        binding = RecyclerviewItemSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bindData(videoInfoList!![position])
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.recyclerview_item_search
    }

    override fun getItemCount(): Int = videoInfoList?.size!!

    inner class SearchViewHolder(binding: RecyclerviewItemSearchBinding
    ):RecyclerView.ViewHolder(binding.root) {
        val context = binding.root.context
        fun bindData(biliBiliVideo: BiliBiliVideo) {
            Glide.with(context).load(biliBiliVideo.pic).into(binding.rvSearchBanner)
            binding.tvSearchVideoTitle.text = biliBiliVideo.title
            binding.tvUpName.text = biliBiliVideo.ownerName

            binding.tvSearchVideoOpenInfo.text = setVideoOpenInfo(biliBiliVideo.view)+"  "+biliBiliVideo.ctime
            // 跳转到视频详情页
            binding.llItemFilterShow.setOnClickListener{
                var bvid = biliBiliVideo.bvid
                context.startActivity(Intent(context, VideoDetailActivity::class.java).putExtra("bvid",bvid))
            }


        }
        private fun setVideoOpenInfo(data: Int?): String {
            if(data!! > 10000){
                return DecimalFormat("0.0").format(data/10000) + " 万"
            }else{
                return data.toString()
            }
        }
    }

}