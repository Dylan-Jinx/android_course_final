package com.example.final_535_app.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.util.BannerUtils

class HomeBannerAdapter(bannerImageUrls: List<String>?): BannerAdapter<String, HomeBannerAdapter.HomeBannerViewHolder>(bannerImageUrls) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): HomeBannerViewHolder {
        val imageView = ImageView(parent?.context)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        imageView.layoutParams = params
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        //通过裁剪实现圆角
        BannerUtils.setBannerRound(imageView, 20f)
        return HomeBannerViewHolder(imageView)
    }

    override fun onBindView(
        holder: HomeBannerViewHolder,
        data: String?,
        position: Int,
        size: Int
    ) {
        Glide.with(holder.imageView)
            .load(data)
            .into(holder.imageView)
    }

    inner class HomeBannerViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view as ImageView
    }

}