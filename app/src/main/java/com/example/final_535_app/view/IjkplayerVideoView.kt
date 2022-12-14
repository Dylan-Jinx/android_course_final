package com.example.final_535_app

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.io.IOException

class IjkplayerVideoView : FrameLayout {
    private var specHeightSize = 0
    private var specWidthSize = 0

    //是否全屏拉伸填满，false等比例最大，不拉伸
    private var fillXY = true

    constructor(context: Context) : super(context) {
        initVideoView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initVideoView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initVideoView(context)
    }

    @SuppressLint("NewApi")
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initVideoView(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        specHeightSize = MeasureSpec.getSize(heightMeasureSpec)
        specWidthSize = MeasureSpec.getSize(widthMeasureSpec)
    }

    /**
     * 由ijkplayer提供，用于播放视频，需要给他传入一个surfaceView
     */
    private var mMediaPlayer: IMediaPlayer? = null

    /**
     * 视频文件地址
     */
    private var mPath = ""
    private var surfaceView: SurfaceView? = null

    // private VideoPlayerListener listener;
    private var mContext: Context? = null
    private fun initVideoView(context: Context) {
        mContext = context
        //获取焦点
//        setFocusable(true);
    }

    private fun setFillXY(fillXY: Boolean) {
        this.fillXY = fillXY
    }

    /**
     * 设置视频地址。
     * 根据是否第一次播放视频，做不同的操作。
     *
     * @param path the path of the video.
     */
    fun setVideoPath(path: String) {
        if (TextUtils.equals("", mPath)) {
            //如果是第一次播放视频，那就创建一个新的surfaceView
            mPath = path
            createSurfaceView()
        } else {
            //否则就直接load
            mPath = path
            load()
        }
    }

    /**
     * 新建一个surfaceview
     */
    private fun createSurfaceView() {
        //生成一个新的surface view
        surfaceView = SurfaceView(mContext)
        surfaceView!!.holder.addCallback(LmnSurfaceCallback())
        val layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER
        )
        surfaceView!!.layoutParams = layoutParams
        this.addView(surfaceView)
    }

    /**
     * surfaceView的监听器
     */
    private inner class LmnSurfaceCallback : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {}
        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            //surfaceview创建成功后，加载视频
            load()
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {}
    }

    /**
     * 加载视频
     */
    private fun load() {
        //每次都要重新创建IMediaPlayer
        createPlayer()
        try {
            mMediaPlayer!!.dataSource = mPath
            mMediaPlayer!!.isLooping = true
            //mMediaPlayer.setVolume(0f,0f);
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //给mediaPlayer设置视图
        mMediaPlayer!!.setDisplay(surfaceView!!.holder)
        mMediaPlayer!!.prepareAsync()
    }

    /**
     * 创建一个新的player
     */
    private fun createPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.setDisplay(null)
            mMediaPlayer!!.release()
        }
        val ijkMediaPlayer = IjkMediaPlayer()
        IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG)

        // 设置播放前的探测时间 1,达到首屏秒开效果
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", 1)

        //开启硬解码
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
        mMediaPlayer = ijkMediaPlayer
        (mMediaPlayer as IjkMediaPlayer).setOnVideoSizeChangedListener(mSizeChangedListener)

//        if (listener != null) {
//            mMediaPlayer.setOnPreparedListener(listener);
//            mMediaPlayer.setOnInfoListener(listener);
//            mMediaPlayer.setOnSeekCompleteListener(listener);
//            mMediaPlayer.setOnBufferingUpdateListener(listener);
//            mMediaPlayer.setOnErrorListener(listener);
//        }
    }

    //    public void setListener(VideoPlayerListener listener) {
    //        this.listener = listener;
    //        if (mMediaPlayer != null) {
    //            mMediaPlayer.setOnPreparedListener(listener);
    //        }
    //    }
    var mSizeChangedListener: IMediaPlayer.OnVideoSizeChangedListener =
        IMediaPlayer.OnVideoSizeChangedListener { mp, width, height, sarNum, sarDen ->
            if (width != 0 && height != 0) {
                if (surfaceView != null) {
                    val lp = surfaceView!!.layoutParams as LayoutParams
                    if (fillXY) {
                        lp.width = -1
                        lp.height = -1
                    } else {
                        val scanXY: Float
                        if ((specHeightSize / specWidthSize) > (height / width)) {
                            //高剩余，以宽填满
                            scanXY = specWidthSize / width.toFloat()
                        } else {
                            scanXY = specHeightSize / height.toFloat()
                        }
                        lp.width = (width * scanXY).toInt()
                        lp.height = (height * scanXY).toInt()
                    }
                    surfaceView!!.layoutParams = lp
                }
                //requestLayout();
            }
        }

    /**
     * 下面封装了控制视频的方法
     */
    fun setVolume(v1: Float, v2: Float) {
        //关闭声音
//        if (mMediaPlayer != null) {
//            mMediaPlayer!!.setVolume(v1, v2)
//        }
    }

    fun start() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.start()
        }
    }

    fun release() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.reset()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    fun pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.pause()
        }
    }

    fun stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
        }
    }

    fun reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.reset()
        }
    }

    val duration: Long
        get() = if (mMediaPlayer != null) {
            mMediaPlayer!!.duration
        } else {
            0
        }
    val currentPosition: Long
        get() {
            return if (mMediaPlayer != null) {
                mMediaPlayer!!.currentPosition
            } else {
                0
            }
        }

    fun seekTo(l: Long) {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.seekTo(l)
        }
    }
}