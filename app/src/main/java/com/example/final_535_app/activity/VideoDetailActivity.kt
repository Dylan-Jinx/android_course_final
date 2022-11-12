package com.example.final_535_app.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.TextView
import android.widget.Toast
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.viewModel
import com.airbnb.mvrx.withState
import com.bumptech.glide.Glide
import com.example.final_535_app.databinding.ActivityVideoDetailBinding
import com.example.final_535_app.state.VideoDetailState
import com.example.final_535_app.viewmodel.VideoDetailViewModel
import java.text.DecimalFormat

class VideoDetailActivity : AppCompatActivity(), MavericksView {
    lateinit var binding: ActivityVideoDetailBinding
    val videoDetailViewModel: VideoDetailViewModel by viewModel(VideoDetailViewModel::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var datas = intent.getStringExtra("bvid")
        datas?.let { videoDetailViewModel.getVideoDetail(it) }
        Toast.makeText(this, "视频bv为：$datas", Toast.LENGTH_SHORT).show()

        videoDetailViewModel.onAsync(
            VideoDetailState::videoDetail,
            deliveryMode = uniqueOnly(),
            onSuccess = {
                invalidate()
                it.data?.videoUrl?.let { it1 -> binding.vdVideoView.setUp(this, it1) }
                binding.vdVideoView.start()
                Toast.makeText(this, "！", Toast.LENGTH_SHORT).show()
            },
            onFail = {
                Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun invalidate() = withState(videoDetailViewModel){
            state ->
        val videoInfo = state.videoDetail.invoke()?.data

        binding.tvVideoOwner.text = videoInfo?.ownerName
        Glide.with(this)
            .load(videoInfo?.ownerFace)
            .into(binding.civVideoDetailUserFace)

        binding.tvVideoDetailTitle.text = videoInfo?.title
        setVideoOpenInfo(videoInfo?.view, binding.tvVideoOpenCount)
        binding.tvVideoDetailCtime.text = videoInfo?.ctime
        setVideoOpenInfo(videoInfo?.danmaku, binding.tvVideoDankamu)

        // 中间部分视频信息的输入
        setVideoOpenInfo(videoInfo?.like?.toInt(), binding.tvVideoThumb)
        setVideoOpenInfo(videoInfo?.coin, binding.tvVideoCoin)
        setVideoOpenInfo(videoInfo?.favorite, binding.tvVideoCollect)
        setVideoOpenInfo(videoInfo?.share, binding.tvVideoShare)

        return@withState
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.vdVideoView.stopPlay()
    }

    private fun setVideoOpenInfo(data: Int?, tvHomeVideoOwner: TextView) {
        if(data!! > 10000){
            tvHomeVideoOwner.text = DecimalFormat("0.0").format(data/10000) + " 万"
        }else{
            tvHomeVideoOwner.text = data.toString()
        }
    }

    override fun onBackPressed() {
        if(binding.vdVideoView.isFullScreen){
            binding.vdVideoView.setChangeScreen(false)
        }else super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        binding.vdVideoView.pause()
    }

    override fun onResume() {
        super.onResume()
        binding.vdVideoView.start()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        var b: Boolean = binding.vdVideoView.onKeyDown(keyCode)
        return b || super.onKeyDown(keyCode, event)
    }

}