package com.example.final_535_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.MavericksView
import com.chad.library.adapter.base.BaseMultiItemAdapter
import com.example.final_535_app.databinding.FragmentMyVideoAudioBinding
import com.example.final_535_app.databinding.FragmentMyVideoBinding
import com.example.final_535_app.databinding.FragmentMyVideoSpecialBinding
import com.example.final_535_app.model.BiliBiliVideo
import com.example.final_535_app.viewmodel.MyVideoViewModel

class MyVideoAdapter(itemType:Int): BaseMultiItemAdapter<BiliBiliVideo>() {

    init {
        addItemType(VIDEOPAGE, object : OnMultiItemAdapterListener<BiliBiliVideo, VideoVH>{
            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): VideoVH {
                // 创建 viewholder
                val viewBinding = FragmentMyVideoBinding.inflate(LayoutInflater.from(context), parent, false)
                return VideoVH(viewBinding)
            }

            override fun onBind(holder: VideoVH, position: Int, item: BiliBiliVideo?) {

            }
        }).addItemType(SPECIALPAGE, object : OnMultiItemAdapterListener<BiliBiliVideo, SpecialVH>{

            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): SpecialVH {
                // 创建 viewholder
                val viewBinding = FragmentMyVideoSpecialBinding.inflate(LayoutInflater.from(context), parent, false)

                return SpecialVH(viewBinding)
            }

            override fun onBind(holder: SpecialVH, position: Int, item: BiliBiliVideo?) {

            }

        }).addItemType(AUDIOPAGE, object : OnMultiItemAdapterListener<BiliBiliVideo, AudioVH>{

            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): AudioVH {
                // 创建 viewholder
                val viewBinding = FragmentMyVideoAudioBinding.inflate(LayoutInflater.from(context), parent, false)
                return AudioVH(viewBinding)
            }

            override fun onBind(holder: AudioVH, position: Int, item: BiliBiliVideo?) {

            }
        }).onItemViewType { position, list -> // 根据数据，返回对应的 ItemViewType
            if (itemType == 0){
                VIDEOPAGE
            } else if (itemType == 1) {
                SPECIALPAGE
            } else {
                AUDIOPAGE
            }
        }
    }

    inner class VideoVH(val binding: FragmentMyVideoBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    inner class SpecialVH(val binding: FragmentMyVideoSpecialBinding) : RecyclerView.ViewHolder(binding.root){

    }

    inner class AudioVH(val binding: FragmentMyVideoAudioBinding) : RecyclerView.ViewHolder(binding.root){

    }

    companion object{
        private const val VIDEOPAGE = 0
        private const val SPECIALPAGE = 1
        private const val AUDIOPAGE = 2
    }

}