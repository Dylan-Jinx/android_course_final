package com.example.final_535_app.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.final_535_app.R
import com.example.final_535_app.activity.MessageActivity
import com.example.final_535_app.databinding.RecyclerviewItemHomeVideoInfoBinding
import com.example.final_535_app.model.BiliBiliVideo
import java.text.DecimalFormat

class HomeVideoInfoAdapter(val videoInfoList: MutableList<BiliBiliVideo>): RecyclerView.Adapter<HomeVideoInfoAdapter.HomeVideoInfoViewHolder>() {

    lateinit var binding: RecyclerviewItemHomeVideoInfoBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeVideoInfoViewHolder {
        binding = RecyclerviewItemHomeVideoInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HomeVideoInfoViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.recyclerview_item_home_video_info
    }

    override fun onBindViewHolder(holder: HomeVideoInfoViewHolder, position: Int) {
        holder.bindData(videoInfoList[position])
    }

    override fun getItemCount(): Int = videoInfoList.size

    inner class HomeVideoInfoViewHolder(
        binding: RecyclerviewItemHomeVideoInfoBinding
    ):RecyclerView.ViewHolder(binding.root){
        val context = binding.root.context
        fun bindData(data: BiliBiliVideo){
            binding.tvHomeVideoName.text = data.title
            binding.tvHomeVideoOwner.text = data.ownerName
            Glide.with(context)
                .load(data.pic)
                .placeholder(R.drawable.big_logo)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .dontAnimate()
                .into(binding.homeRvVideoBrand)
            setVideoOpenInfo(data.view, binding.tvHomeVideoOpenCount)
            setVideoOpenInfo(data.share, binding.tvHomeVideoShare)

            binding.cvVideoInfoItem.setOnClickListener{
                var bvid = data.bvid
                context.startActivity(Intent(context, MessageActivity::class.java).putExtra("bvid",bvid))
            }

        }

        private fun setVideoOpenInfo(data: Int?, tvHomeVideoOwner: TextView) {
            if(data!! > 10000){
                tvHomeVideoOwner.text = DecimalFormat("0.0").format(data/10000) + " 万"
            }else{
                tvHomeVideoOwner.text = data.toString()
            }
        }
    }
}