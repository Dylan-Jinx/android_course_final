package com.example.final_535_app.adapter

import LocalCacheUtils.getBitmapFromLocal
import LocalCacheUtils.setBitmap2Local
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.final_535_app.R
import com.example.final_535_app.databinding.RecyclerviewItemMessageChatBinding
import com.example.final_535_app.model.BilibiliUserInfo
import com.example.final_535_app.utils.HttpUtils
import com.example.final_535_app.utils.ImageConvertUtil
import kotlinx.coroutines.*
import okhttp3.internal.notify
import java.net.URL

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


    inner class MessageChatViewHolder(binding: RecyclerviewItemMessageChatBinding): RecyclerView.ViewHolder(binding.root) {
        val context = binding.root.context
        fun bindData(data: BilibiliUserInfo) {
            binding.tvRecyclerviewUsername.text = data.name
            var face_mip: Bitmap? = null
            if (getBitmapFromLocal(context, data.mid) == null){
                GlobalScope.launch(Dispatchers.IO) {
                    var face_url = data.face?.let { HttpUtils.apiService.getMinioFile(it).data?.resUrl }
                    face_mip =
                        ImageConvertUtil.byteToBitmap(URL(face_url).openStream().readBytes())!!
                }.apply {
                    setBitmap2Local(context, data.mid, face_mip)
                    GlobalScope.launch {
                        withContext(Dispatchers.Main.immediate){
                            binding.ivMessageRecircleItemPic.setImageBitmap(face_mip)
                        }
                    }
                }
            }
            else{
                binding.ivMessageRecircleItemPic.setImageBitmap(getBitmapFromLocal(context, data.mid))
            }
        }
    }
}