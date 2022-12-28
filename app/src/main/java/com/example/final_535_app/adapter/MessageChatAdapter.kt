package com.example.final_535_app.adapter

import android.R.attr.smallIcon
import android.app.Notification
import android.app.PendingIntent
import android.app.Person
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.final_535_app.R
import com.example.final_535_app.databinding.RecyclerviewItemMessageChatBinding
import com.example.final_535_app.model.BilibiliUserInfo
import com.liulishuo.okdownload.OkDownloadProvider.context
import kotlinx.coroutines.*


class MessageChatAdapter(val messageList: MutableList<BilibiliUserInfo>) : RecyclerView.Adapter<MessageChatAdapter.MessageChatViewHolder>() {

    lateinit var binding: RecyclerviewItemMessageChatBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageChatViewHolder {
        binding = RecyclerviewItemMessageChatBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MessageChatViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.recyclerview_item_message_chat
    }

    override fun onBindViewHolder(holder: MessageChatViewHolder, position: Int) {
        holder.bindData(messageList[position])
    }

    override fun getItemCount() = messageList.size

    //TODO: recycleView的每条数据点击事件待完成


    inner class MessageChatViewHolder(
        binding: RecyclerviewItemMessageChatBinding
    ): RecyclerView.ViewHolder(binding.root) {
        val context = binding.root.context
        fun bindData(data: BilibiliUserInfo) {
            binding.tvRecyclerviewUsername.text = data.name
            Glide.with(binding.root)
                .load(data.face)
                .placeholder(R.drawable.icon)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .dontAnimate()
                .into(binding.ivMessageRecircleItemPic)
        }
    }
}