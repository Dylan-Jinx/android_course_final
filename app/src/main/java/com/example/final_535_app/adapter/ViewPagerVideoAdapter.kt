package com.example.instantmusicvideotest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.final_535_app.R
import com.example.final_535_app.model.BiliBiliVideo
import com.example.final_535_app.view.video.holder.RecyclerItemNormalHolder

class ViewPagerVideoAdapter(var context: Context?, var itemDataList: List<BiliBiliVideo>?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val v: View =
            LayoutInflater.from(context).inflate(R.layout.item_view_pager_video, parent, false)
        return RecyclerItemNormalHolder(context, v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val recyclerItemViewHolder: RecyclerItemNormalHolder = holder as RecyclerItemNormalHolder
        recyclerItemViewHolder.recyclerBaseAdapter = this
        recyclerItemViewHolder.onBind(position, itemDataList!![position])
    }

    override fun getItemCount(): Int {
        return itemDataList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    companion object {
        private const val TAG = "ViewPagerAdapter"
    }
}