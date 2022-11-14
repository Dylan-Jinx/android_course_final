package com.example.final_535_app.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.final_535_app.activity.ChartActivity
import com.example.final_535_app.databinding.ItemFgMyVideoBinding
import com.example.final_535_app.model.BiliBiliVideo
import java.text.DecimalFormat

class ItemFragmentRCMyVideoAdapter(val datas: MutableList<BiliBiliVideo>):BaseQuickAdapter<BiliBiliVideo,ItemFragmentRCMyVideoAdapter.IFRCMVAViewHolder>(){
    lateinit var binding: ItemFgMyVideoBinding

    class IFRCMVAViewHolder(var binding:ItemFgMyVideoBinding): RecyclerView.ViewHolder(binding.root) {
        val context = binding.root.context
        fun bindData(item: BiliBiliVideo?) {
            binding.tvFgMyVideoTitle.text = item?.title
            Glide.with(context).load(item?.pic).into(binding.rvSearchBanner)
            setVideoOpenInfo(item?.danmaku, binding.tvFgMyvideoDakamu)

            setVideoOpenInfo(item?.like?.toInt(), binding.tvFgThumv)
            setVideoOpenInfo(item?.share, binding.tvFgMyvideoShare)
            setVideoOpenInfo(item?.view, binding.tvFgMyvideoView)
            setVideoOpenInfo(item?.coin, binding.tvFgMyvideoFavorite)

            binding.itemFgBtnDatacenter.setOnClickListener {
                context.startActivity(Intent(context, ChartActivity::class.java).putExtra("data_center_bvid", item?.bvid))
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

    override fun onBindViewHolder(holder: IFRCMVAViewHolder, position: Int, item: BiliBiliVideo?) {
        holder.bindData(datas?.get(position))
    }

    override fun getItemCount(items: List<BiliBiliVideo>): Int = datas.size


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): IFRCMVAViewHolder {
        binding = ItemFgMyVideoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )


        return IFRCMVAViewHolder(binding)
    }


}