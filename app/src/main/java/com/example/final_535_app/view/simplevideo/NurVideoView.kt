package com.example.final_535_app.view.simplevideo

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import com.example.final_535_app.R
import com.example.final_535_app.view.simplevideo.listener.OnControlClickListener
import com.example.final_535_app.view.simplevideo.listener.OnMediaListener
import tv.danmaku.ijk.media.player.IMediaPlayer

class NurVideoView @JvmOverloads constructor(
    context: Context,
    @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {
    private var mVideoView: NurVideoPlayer? = null
    private var mVideoViewHeight = 0
    private var bgColor = 0
    private var removeBack = false
    private var mScreenView: ImageView? = null
    private val ID_SCREEN_VIEW = 1010
    private val ID_VOLUME_CONTROL = 2020
    private val ID_BACK_IV = 3030
    private var isPortrait = true //是否全屏
    private var mActivity: Activity? = null

    /**
     * 声道控制器view
     * 默认情况下visibility=gone
     */
    var volumeControl: ImageView? = null
        private set
    private var onControlClickListener: OnControlClickListener? = null
    private var mediaListener: OnMediaListener? = null

    /**
     * 引入播放器
     */
    private fun initVideoView() {
        mVideoView = NurVideoPlayer(context)
        mVideoView!!.setBgColor(bgColor)
        setVideoViewHeight(mVideoViewHeight)
        addView(mVideoView)
        mScreenView = mVideoView!!.screenView
        volumeControl = mVideoView!!.volumeImageView
        val mBackIv = mVideoView!!.backIv
        mBackIv!!.setOnClickListener(this)
        mBackIv.id = ID_BACK_IV
        if (removeBack) {
            mBackIv.visibility = GONE
        }
        volumeControl!!.setOnClickListener(this)
        volumeControl!!.id = ID_VOLUME_CONTROL
        mScreenView!!.setOnClickListener(this)
        mScreenView!!.id = ID_SCREEN_VIEW
    }

    fun setVideoTransitionName(transitionName: String?) {
        mVideoView!!.transitionName = transitionName
    }

    /**
     * 设置高度
     */
    private fun setVideoViewHeight(h: Int) {
        mVideoView!!.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, h)
    }

    /**
     * 初始化播放器
     */
    fun setUp(activity: Activity?, rul: String?) {
        setUp(activity, Uri.parse(rul), null)
    }

    /**
     * 初始化播放器
     */
    fun setUp(activity: Activity?, rul: String?, title: String?) {
        setUp(activity, Uri.parse(rul), title)
    }

    /**
     * 初始化播放器
     */
    fun setUp(activity: Activity?, uri: Uri?, title: String?) {
        mActivity = activity
        mVideoView!!.initPlayer(activity, uri, title)
    }

    /**
     * 监听（播放，暂停）
     */
    fun setOnMediaListener(mediaListener: OnMediaListener?) {
        this.mediaListener = mediaListener
        mVideoView!!.setOnMediaListener(mediaListener)
    }

    /**
     * 开始播放
     */
    fun start(msec: Int) {
        mVideoView!!.start(msec)
    }

    /**
     * 开始播放
     */
    fun start() {
        mVideoView!!.start()
    }

    /**
     * 暂停播放
     */
    fun pause() {
        _isPause = true
        mVideoView!!.pause()
    }

    private var _isPause = false

    init {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.NurVideoView)
            mVideoViewHeight = ta.getDimension(
                R.styleable.NurVideoView_video_view_height,
                ViewGroup.LayoutParams.MATCH_PARENT.toFloat()
            ).toInt()
            bgColor = ta.getColor(R.styleable.NurVideoView_video_background_color, Color.BLACK)
            removeBack = ta.getBoolean(R.styleable.NurVideoView_remove_back_btn, false)
            ta.recycle()
        }
        orientation = VERTICAL
        initVideoView()
    }

    /**
     * 继续播放
     */
    fun resume() {
        if (_isPause) {
            mVideoView!!.start()
            _isPause = false
        }
    }

    /**
     * 禁止播放
     */
    fun stopPlay() {
        mVideoView!!.stopPlayback()
    }
    /**
     * 获取广告view（小）
     */
    /**
     * 小广告view
     */
    var smallADView: RelativeLayout?
        get() = mVideoView!!.minADLayout
        set(view) {
            mVideoView!!.minADLayout = view
        }

    /**
     * volume image view
     */
    val volumeImageView: ImageView?
        get() = mVideoView!!.volumeImageView

    /**
     * 获取背景image view
     */
    val thumbImageView: ImageView?
        get() = mVideoView!!.thumbImageView
    /**
     * 获取广告view（大）
     */
    /**
     * 满（就是播放器的上面）-广告view
     */
    var maxADView: RelativeLayout?
        get() = mVideoView!!.maxADLayout
        set(view) {
            mVideoView!!.maxADLayout = view
        }

    /**
     * @return true 正在是全屏
     */
    val isFullScreen: Boolean
        get() = !isPortrait

    /**
     * 声道控制器view
     */
    fun setVolumeControlImage(@DrawableRes resId: Int) {
        volumeControl!!.visibility = VISIBLE
        volumeControl!!.setImageResource(resId)
    }

    /**
     * 关闭全部控制器
     */
    fun hideControls() {
        mVideoView!!.hideControllers()
    }

    /**
     * 打开全部控制器
     */
    fun showControllers() {
        mVideoView!!.showControllers()
    }

    /**
     * 全屏-单屏
     *
     * @param changeFull true 要更改全屏
     */
    fun setChangeScreen(changeFull: Boolean) {
        isPortrait = changeFull
        changeScreen()
    }

    /**
     * 设置单声道
     *
     * @param isLeft == true 播放左声道
     */
    fun setMonoChannel(isLeft: Boolean) {
        setVolume(if (isLeft) 1.0f else 0.0f, if (isLeft) 0.0f else 1.0f)
    }

    /**
     * 设置声道
     */
    fun setVolume(leftVolume: Float, rightVolume: Float) {
        volumeControl!!.visibility = VISIBLE
        val mediaPlayer = mVideoView!!.mediaPlayer ?: return
        mediaPlayer.setVolume(leftVolume, rightVolume)
    }

    /**
     * 控制器的点击事件
     */
    fun setOnControlClickListener(controlClickListener: OnControlClickListener?) {
        onControlClickListener = controlClickListener
    }

    /**
     * 调声音
     */
    fun onKeyDown(keyCode: Int): Boolean {
        return mVideoView!!.onKeyDown(keyCode)
    }

    /**
     * 获取ijk player 的media player
     */
    val mediaPlayer: IMediaPlayer?
        get() = mVideoView!!.mediaPlayer

    /**
     * 更改全（单）屏
     */
    private fun changeScreen() {
        val videoWH = mVideoView!!.videoWH
        val videoW = videoWH[0]
        val videoH = videoWH[1]
        if (videoH <= 0 || videoW <= 0) {
            return
        }
        if (videoH > videoW) {
            if (isPortrait) {
                setVideoViewHeight(ViewGroup.LayoutParams.MATCH_PARENT)
            } else {
                setVideoViewHeight(mVideoViewHeight)
            }
        } else {
            if (isPortrait) {
                setVideoViewHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                mActivity!!.requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE //横向
            } else {
                setVideoViewHeight(mVideoViewHeight)
                mActivity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //纵行
            }
        }
        if (isPortrait) {
            mActivity!!.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            mScreenView!!.setImageResource(R.drawable.nur_ic_fangxiao)
        } else {
            mActivity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            mScreenView!!.setImageResource(R.drawable.nur_ic_fangda)
        }
        isPortrait = !isPortrait
        if (mediaListener != null) {
            mediaListener!!.onChangeScreen(isPortrait)
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == ID_SCREEN_VIEW) {
            changeScreen()
            if (onControlClickListener != null) {
                onControlClickListener!!.onScreenControlClick()
            }
        } else if (id == ID_VOLUME_CONTROL) {
            if (onControlClickListener != null) {
                onControlClickListener!!.onVolumeControlClick()
            }
        } else if (id == ID_BACK_IV) {
            if (onControlClickListener != null) {
                onControlClickListener!!.onBackBtnClick()
            }
        }
    }
}