package com.example.final_535_app.activity

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.viewModel
import com.airbnb.mvrx.withState
import com.bumptech.glide.Glide
import com.example.final_535_app.common.DownloadControl
import com.example.final_535_app.databinding.ActivityVideoDetailBinding
import com.example.final_535_app.db.DBInjection
import com.example.final_535_app.db.model.DownloadInfoModel
import com.example.final_535_app.model.Dankamu
import com.example.final_535_app.state.VideoDankamuState
import com.example.final_535_app.state.VideoDetailState
import com.example.final_535_app.utils.DownloadUtil.getRootDir
import com.example.final_535_app.view.simplevideo.listener.OnMediaListener
import com.example.final_535_app.viewmodel.VideoDankamuViewModel
import com.example.final_535_app.viewmodel.VideoDetailViewModel
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.core.Util
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.listener.DownloadListener2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import master.flame.danmaku.controller.DrawHandler
import master.flame.danmaku.controller.IDanmakuView
import master.flame.danmaku.controller.IDanmakuView.OnDanmakuClickListener
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import master.flame.danmaku.danmaku.model.Duration
import master.flame.danmaku.danmaku.model.IDanmakus
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.model.android.Danmakus
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import master.flame.danmaku.ui.widget.DanmakuView
import java.io.File
import java.text.DecimalFormat


class VideoDetailActivity : AppCompatActivity(), MavericksView {
    lateinit var binding: ActivityVideoDetailBinding
    val videoDetailViewModel: VideoDetailViewModel by viewModel(VideoDetailViewModel::class)
    val videoDankamuViewModel: VideoDankamuViewModel by viewModel(VideoDankamuViewModel::class)
    var showDanmaku: Boolean = false
    private var dankamuMap: HashMap<Int,Dankamu> = HashMap()
    private var danmakuView: DanmakuView? = null
    private var danmakuContext: DanmakuContext? = null

    private val parser: BaseDanmakuParser = object : BaseDanmakuParser() {
        override fun parse(): IDanmakus {
            return Danmakus()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Util.enableConsoleLog()
        binding.vdVideoView.onBackPressClickListener{
            onBackPressed()
        }

        var isLocalVideo = intent.getBooleanExtra("local",false)

        binding.vdVideoView.setOnMediaListener(mediaListener = object:OnMediaListener{
            override fun onStart() {
                if(!isLocalVideo){
                    danmakuLoading()
                }
            }

            override fun onPause(isPause: Boolean) {
                if(!isPause){
                    danmakuView?.resume()
                }else{
                    danmakuView?.pause()
                }
            }

            override fun onProgress(progress: Int, duration: Int) {
                var realTime = progress/1000
                var dankamu = dankamuMap?.get(realTime)
                if(dankamu != null){
                    Log.d("TAG", "onProgress Color: "+dankamu.contentColor+" bordercolor"+dankamu.borderColor)
                    if(dankamu?.isBorder?.equals("1")!!){
                        addDanmaku(dankamu?.content, true, dankamu?.contentColor, dankamu?.borderColor)
                    }else{
                        addDanmaku(dankamu?.content, false, dankamu?.contentColor, null)
                    }
                }
            }

            override fun onChangeScreen(isPortrait: Boolean) {

            }

            override fun onEndPlay() {
                danmakuView?.pause()
            }

        })

        binding.btnOpenDankamu.setOnClickListener {
            if(!binding.danmakuView.isVisible){
                binding.danmakuView.isVisible = true
            }
        }

        binding.btnCloseDankamu.setOnClickListener {
            if(binding.danmakuView.isVisible){
                binding.danmakuView.isVisible = false
            }
        }

        binding.btnSendDankamu.setOnClickListener {
            var sendDankamu = binding.etDankamuText.text.toString()
            if(!TextUtils.isEmpty(sendDankamu)){
                addDanmaku(
                    sendDankamu,
                    false,
                    "WHITE",
                    null
                )
                binding.etDankamuText.text = null
            }
        }

//        GlobalScope.launch{
//            withContext(Dispatchers.IO){
//                var dbInstance = DBInjection.provideDownloadInfoDataSource(this@VideoDetailActivity)
//                dbInstance.deleteAllInfo()
//                var x = dbInstance.getAllLocalDownloadInfo()
//                Toast.makeText(this@VideoDetailActivity, ""+x.size, Toast.LENGTH_SHORT).show()
//            }
//        }

        // 不是本地
        if(!isLocalVideo){
            var datas = intent.getStringExtra("bvid")
            datas?.let { videoDetailViewModel.getVideoDetail(it) }
            datas?.let { videoDankamuViewModel.getVideoDankamu(it) }
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
            videoDankamuViewModel.onAsync(
                VideoDankamuState::dankamu,
                deliveryMode = uniqueOnly(),
                onSuccess = {
                    var count = 1
                    for (dankamu in it.data!!) {
                        dankamuMap.put(count, dankamu)
                        count ++
                    }
                },
                onFail = {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show()
                }
            )
        }
        //是本地
        else{
            binding.llVideoInfo.visibility = View.INVISIBLE
            binding.llBtnCache.visibility = View.INVISIBLE
            binding.llVideoOwner.visibility = View.INVISIBLE
            var bvid_local = intent.getStringExtra("bvid_local")
            GlobalScope.launch {
                withContext(Dispatchers.IO){
                    var dbInstance = DBInjection.provideDownloadInfoDataSource(this@VideoDetailActivity)
                    var result  = bvid_local?.let { dbInstance.getDownloadInfoById(it) }
                    withContext(Dispatchers.Main.immediate){
                        binding.vdVideoView.setUp(this@VideoDetailActivity,
                            getRootDir(this@VideoDetailActivity, "mp4")
                                .toString()+"/"+result?.fileName+".mp4")
                        binding.vdVideoView.start()
                    }
                }
            }
        }
        intent.putExtra("local", false)

    }

    fun danmakuLoading(){
        danmakuView = binding.danmakuView
        danmakuView!!.enableDanmakuDrawingCache(true)
        danmakuView!!.setCallback(object : DrawHandler.Callback {
            override fun prepared() {
                showDanmaku = true
                danmakuView!!.start()
            }

            override fun updateTimer(timer: DanmakuTimer) {}
            override fun danmakuShown(danmaku: BaseDanmaku) {}
            override fun drawingFinished() {}
        })
        danmakuContext = DanmakuContext.create()
        danmakuView!!.prepare(parser, danmakuContext)
        danmakuView!!.setOnDanmakuClickListener(object: OnDanmakuClickListener{
            override fun onDanmakuClick(danmakus: IDanmakus?): Boolean {
                Toast.makeText(this@VideoDetailActivity, ""+danmakus?.first()?.text, Toast.LENGTH_SHORT).show()
                danmakus?.last()?.duration = Duration(0)
                return true
            }

            override fun onDanmakuLongClick(danmakus: IDanmakus?): Boolean {
                Toast.makeText(this@VideoDetailActivity, "点赞成功", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onViewClick(view: IDanmakuView?): Boolean {
                return true
            }

        })
    }

    /**
     * 向弹幕View中添加一条弹幕
     * @param content
     * 弹幕的具体内容
     * @param  withBorder
     * 弹幕是否有边框
     */
    private fun addDanmaku(content: String?, withBorder: Boolean
                           ,textColor: String?,borderColor: String?,) {
        val danmaku = danmakuContext?.mDanmakuFactory?.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL)
        danmaku!!.text = content
        danmaku.padding = 5
        danmaku.textSize = sp2px(20F).toFloat()
        danmaku.textColor = Color.parseColor(textColor.toString())
        danmaku.time = danmakuView!!.currentTime
        if (withBorder) {
            danmaku.borderColor = Color.parseColor("WHITE")
        }
        danmakuView?.addDanmaku(danmaku)
    }

    /**
     * sp转px的方法。
     */
    fun sp2px(spValue: Float): Int {
        val fontScale = resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    override fun invalidate() = withState(videoDetailViewModel,videoDankamuViewModel){
            state,state2 ->
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
            var videoTask = DownloadControl.createTask(
                url = videoInfo?.videoUrl.toString(), getRootDir(this, "mp4").toString(),videoInfo?.title.toString()+".mp4"
            )

            var img_fileName = getRootDir(this, "mp4").toString()+File.separator+videoInfo?.bvid.toString()+".png"
            var imageTask = DownloadControl.createTask(
                url = videoInfo?.pic.toString(), getRootDir(this, "mp4").toString(),videoInfo?.bvid.toString()+".png"
            )

            // 下载视频
            GlobalScope.launch {
                withContext(Dispatchers.IO){
                    videoTask?.execute(object :DownloadListener2(){
                        override fun taskStart(task: DownloadTask) {
                        }
                        override fun taskEnd(task: DownloadTask, cause: EndCause, realCause: Exception?) {
                            GlobalScope.launch {
                                withContext(Dispatchers.IO){
                                    var dbInstance = DBInjection.provideDownloadInfoDataSource(this@VideoDetailActivity)
                                    dbInstance.insertDownloadInfo(DownloadInfoModel(
                                        bid = videoInfo?.bvid.toString(),
                                        fileName = videoInfo?.title,
                                        path = img_fileName
                                    ))
                                }
                            }

                            Toast.makeText(this@VideoDetailActivity, "下载完成", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            // 下载封面
            GlobalScope.launch {
                withContext(Dispatchers.IO){
                    imageTask?.execute(object : DownloadListener2() {
                        override fun taskStart(task: DownloadTask) {
                            Toast.makeText(this@VideoDetailActivity, "开始下载", Toast.LENGTH_SHORT).show()
                        }

                        override fun taskEnd(
                            task: DownloadTask,
                            cause: EndCause,
                            realCause: java.lang.Exception?
                        ) {

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
        showDanmaku = false
        danmakuView?.release()
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
        if (danmakuView != null && danmakuView!!.isPrepared() && danmakuView!!.isPaused()) {
            danmakuView!!.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.vdVideoView.start()
        if (danmakuView != null && danmakuView!!.isPrepared() && danmakuView!!.isPaused()) {
            danmakuView!!.resume()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        var b: Boolean = binding.vdVideoView.onKeyDown(keyCode)
        return b || super.onKeyDown(keyCode, event)
    }

}