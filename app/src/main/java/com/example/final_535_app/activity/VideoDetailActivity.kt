package com.example.final_535_app.activity

import android.os.Bundle
import android.view.KeyEvent
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.viewModel
import com.airbnb.mvrx.withState
import com.bumptech.glide.Glide
import com.example.final_535_app.common.DownloadControl
import com.example.final_535_app.databinding.ActivityVideoDetailBinding
import com.example.final_535_app.db.DBInjection
import com.example.final_535_app.db.model.DownloadInfoModel
import com.example.final_535_app.state.VideoDetailState
import com.example.final_535_app.utils.DownloadUtil.getRootDir
import com.example.final_535_app.viewmodel.VideoDetailViewModel
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.Util
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.listener.DownloadListener2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        binding.vdVideoView.onBackPressClickListener{
            onBackPressed()
        }

        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                var dbInstance = DBInjection.provideDownloadInfoDataSource(this@VideoDetailActivity)
                var r = dbInstance.getAllLocalDownloadInfo()
                withContext(Dispatchers.Main){
                    Toast.makeText(this@VideoDetailActivity, ""+r.size, Toast.LENGTH_SHORT).show()
                }
            }
        }

        var isLocalVideo = intent.getBooleanExtra("local",false)
        if(!isLocalVideo){
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
        }else{
            var bvid_local = intent.getStringExtra("bvid_local")
            GlobalScope.launch {
                withContext(Dispatchers.IO){
                    var dbInstance = DBInjection.provideDownloadInfoDataSource(this@VideoDetailActivity)
                    var result  = bvid_local?.let { dbInstance.getDownloadInfoById(it) }
                    withContext(Dispatchers.Main.immediate){
                        binding.vdVideoView.setUp(this@VideoDetailActivity, getRootDir(this@VideoDetailActivity, "mp4").toString()+"/"+result?.fileName)
                        binding.vdVideoView.start()
                        Toast.makeText(this@VideoDetailActivity, ""+result, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        intent.putExtra("local", false)
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
            var task = DownloadControl.createTask(
                url = videoInfo?.videoUrl.toString(), getRootDir(this, "mp4").toString(),videoInfo?.title.toString()+".mp4"
            )

            var db_fileName = getRootDir(this, "mp4").toString()+""+videoInfo?.title.toString()+".mp4"
            GlobalScope.launch {
                withContext(Dispatchers.IO){
                    var dbInstance = DBInjection.provideDownloadInfoDataSource(this@VideoDetailActivity)
                    dbInstance.insertDownloadInfo(DownloadInfoModel(
                        bid = videoInfo?.bvid.toString(),
                        fileName = videoInfo?.title.toString(),
                        path = db_fileName
                    ))
                }
            }
            GlobalScope.launch {
                withContext(Dispatchers.IO){
                    task?.execute(object :DownloadListener2(){
                        override fun taskStart(task: DownloadTask) {
//                                Toast.makeText(this@VideoDetailActivity, "开始下载", Toast.LENGTH_SHORT).show()
                        }

                        override fun taskEnd(task: DownloadTask, cause: EndCause, realCause: Exception?) {
//                            Toast.makeText(this@VideoDetailActivity, "下载完成", Toast.LENGTH_SHORT).show()
                        }
                    })

                }
            }
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
        }else{
            super.onBackPressed()
            finish()
        }
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