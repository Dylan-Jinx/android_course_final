package com.example.final_535_app.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.TextView
import android.widget.Toast
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.viewModel
import com.airbnb.mvrx.withState
import com.bumptech.glide.Glide
import com.example.final_535_app.common.DownloadControl
import com.example.final_535_app.common.DownloadControl.initOkDownload
import com.example.final_535_app.databinding.ActivityVideoDetailBinding
import com.example.final_535_app.db.DBInjection
import com.example.final_535_app.db.DownloadInfoDB
import com.example.final_535_app.db.model.DownloadInfoModel
import com.example.final_535_app.state.VideoDetailState
import com.example.final_535_app.utils.DownloadUtil.getRootDir
import com.example.final_535_app.viewmodel.VideoDetailViewModel
import com.liulishuo.okdownload.DownloadListener
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.Util
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.listener.DownloadListener2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
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
        Util.enableConsoleLog()

        initOkDownload(this)
        binding.vdVideoView.onBackPressClickListener{
            onBackPressed()
        }

        videoDetailViewModel.onAsync(
            VideoDetailState::videoDetail,
            deliveryMode = uniqueOnly(),
            onSuccess = {
                invalidate()
                it.data?.videoUrl?.let { it1 -> binding.vdVideoView.setUp(this, it1) }
                binding.vdVideoView.start()
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



        binding.btnVideodetailCache.setOnClickListener{
            val downLoadVideo: String? = videoInfo?.videoUrl

            var task = DownloadControl.createTask(
                url = videoInfo?.videoUrl.toString(), getRootDir(this, "mp4").toString(),videoInfo?.title.toString()
            )

            GlobalScope.launch {
                withContext(Dispatchers.IO){
                    task?.execute(object: DownloadListener2(){
                        override fun taskStart(task: DownloadTask) {
                            Log.d("TAG", "taskStart: "+task)
                        }

                        override fun taskEnd(task: DownloadTask, cause: EndCause, realCause: Exception?) {
                            Log.d("TAG", "taskEnd: "+cause+" // "+ realCause)
                        }
                    })
                }
            }



//            GlobalScope.launch {
//                withContext(Dispatchers.IO){
//                    var dbInstance = DBInjection.provideDownloadInfoDataSource(this@VideoDetailActivity)
//                    var dx = dbInstance.insertDownloadInfo(
//                        DownloadInfoModel(
//                            fileName = videoInfo?.title,
//                            bid = videoInfo?.bvid)
//                    )
//                    var result  = dbInstance.getDownloadInfoById(videoInfo?.bvid.toString())
//                    withContext(Dispatchers.Main){
//                        Toast.makeText(this@VideoDetailActivity, "正在下载中"+ result, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }

        }

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