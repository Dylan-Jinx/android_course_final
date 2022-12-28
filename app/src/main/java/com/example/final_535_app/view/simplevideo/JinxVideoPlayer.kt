package com.example.final_535_app.view.simplevideo

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.os.Handler
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.Nullable
import com.example.final_535_app.R
import com.example.final_535_app.common.Settings
import com.example.final_535_app.view.media.IjkVideoView
import com.example.final_535_app.view.simplevideo.listener.OnMediaListener
import com.nurmemet.nur.nurvideoplayer.SimpleVideoOnTouch
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.util.*

class JinxVideoPlayer @JvmOverloads constructor(
    private val mContext: Context,
    @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :SeekBar.OnSeekBarChangeListener,
    LinearLayout(mContext, attrs, defStyleAttr), View.OnClickListener {
    private val TAG = "NurVideoPlayer"


    init {
    }

    /**
     * 获取media player
     */
    var mediaPlayer: IMediaPlayer? = null
        private set
    private val MAX_LIANG_DU = 255f
    private val am: AudioManager?
    private val maxVolume: Float
    private var mVideoView: IjkVideoView? = null
    private var mTitleControl: View? = null
    private var mBottomControl: View? = null
    private var mVolumeControl: View? = null
    private var mCenterSBLayout: View? = null
    private var mLayoutBox: View? = null
    private var mProgressBar: View? = null
    private var mCenterPlayBtn: View? = null

    /**
     * 获取背景view
     *
     * @return
     */
    var thumbImageView: ImageView? = null
        private set
    private var mBackIv: ImageView? = null

    /**
     * 声道控制器
     *
     * @return
     */
    var volumeImageView: ImageView? = null
        private set

    /**
     * 全屏按钮
     */
    var screenView: ImageView? = null
        private set
    private var mLockImage: ImageView? = null
    private var mRCImage: ImageView? = null
    private var volumeIcon: ImageView? = null
    private var mVideoSeekBarImage: ImageView? = null
    private var mVideoMaxLenTv: TextView? = null
    private var mTitleView: TextView? = null
    private var mVideoDurationTv: TextView? = null
    private var mVideoSeekBarTimeTv: TextView? = null
    private var mVideoSeekBarMaxTime: TextView? = null
    private var mPlayBtn: SimpleVideoPlayButton? = null
    private var mVideoSeekBar: SeekBar? = null
    private var mVolumeSeekBar: SeekBar? = null
    private var mAdverLayout: RelativeLayout? = null
    private var mMaxAdverLayout: RelativeLayout? = null
    private var controlIsShow = true //控制器在是否显示
    private var isTouchProgress = false //正在滑动progress
    private var isLock = false
    private val mControlHandler: Handler
    private val mUiHandler: Handler = Handler()
    private var isShowVolumeControl = false
    private var isTouchLRScreen = false
    private var oldVolumeProgress: Int
    private var oldLiangduProgress = 0
    private var mActivity: Activity? = null
    private var onBackPressListener: OnClickListener? = null
    private var mVideoViewHeight = 0
    private var mVideoViewWidth = 0
    private val mUiRunnable = Runnable { updateUI() }

    /**
     * 控制器自动关闭
     */
    private val mControlRunnable = Runnable {
        if (controlIsShow && !isTouchProgress) {
            changeControl()
        }
    }

    /**
     * （亮度/声音）
     */
    private val mLRControlRunnable = Runnable {
        if (isShowVolumeControl && !isTouchLRScreen) {
            changeVolumeControl()
        }
    }
    private var mediaListener: OnMediaListener? = null

    /**
     * 初始化View
     */
    private fun initLayout() {
        mVideoView = findViewById(R.id.nur_ijk_video_player)
        val mvvView = mVideoView
        mvvView?.setHudView(findViewById<View>(R.id.hud_view) as TableLayout)
        mTitleView = findViewById(R.id.nur_videoName)
        volumeImageView = findViewById(R.id.nur_video_ktvIv)
        mPlayBtn = findViewById(R.id.nur_video_playIv)
        screenView = findViewById(R.id.nur_video_changeWindowTv)
        mBackIv = findViewById(R.id.nur_video_backIv)
        mTitleControl = findViewById(R.id.nur_video_toolbarControl)
        mBottomControl = findViewById(R.id.nur_video_bottomControl)
        mLockImage = findViewById(R.id.nur_video_view_LockIv)
        mRCImage = findViewById(R.id.nur_video_view_RC_btn)
        mAdverLayout = findViewById(R.id.nur_video_adver_layout)
        mMaxAdverLayout = findViewById(R.id.nur_video_max_adver_layout)
        mLayoutBox = findViewById(R.id.nur_ijk_video_player_box)
        mVideoDurationTv = findViewById(R.id.nur_video_videoSeekTv)
        mVideoMaxLenTv = findViewById(R.id.nur_video_videoDur)
        mCenterPlayBtn = findViewById(R.id.nur_video_centerPlayBtn)
        mVideoSeekBar = findViewById(R.id.nur_video_seekBar)
        thumbImageView = findViewById(R.id.nur_video_bgImage)
        mCenterSBLayout = findViewById(R.id.nur_videoSeekBarBox)
        mVideoSeekBarImage = findViewById(R.id.nur_videoSeekBarImage)
        mVideoSeekBarTimeTv = findViewById(R.id.nur_videoSeekBarTimeTv)
        mVideoSeekBarMaxTime = findViewById(R.id.nur_videoSeekBarMaxTime)
        mProgressBar = findViewById(R.id.nur_video_progressBar)
        mLayoutBox?.setOnTouchListener(SimpleVideoOnTouch(mContext, simpleTouchListener) as OnTouchListener)
        mLockImage?.setOnClickListener(this)
        mCenterPlayBtn?.setOnClickListener(this)
        mPlayBtn?.setOnClickListener(this)
        screenView?.setOnClickListener(this)
        mVideoSeekBar?.setOnClickListener(this)
        mBackIv?.setOnClickListener(this)
        mVolumeControl = findViewById(R.id.nur_video_volumeControl)
        mVolumeSeekBar = findViewById(R.id.nur_volumeSeekBar)
        volumeIcon = findViewById(R.id.nur_video_volumeIcon)
    }

    /**
     * 背景色
     */
    fun setBgColor(@ColorInt color: Int) {
        mLayoutBox!!.setBackgroundColor(color)
    }

    /**
     * back image view
     */
    val backIv: View?
        get() = mBackIv
    /**
     * 小广告view
     */
    var minADLayout: RelativeLayout?
        get() = mAdverLayout
        set(v) {
            mAdverLayout!!.removeAllViews()
            mAdverLayout!!.addView(v)
        }
    /**
     * 大广告view
     */
    /**
     * 大广告view
     */
    var maxADLayout: RelativeLayout?
        get() = mMaxAdverLayout
        set(v) {
            mMaxAdverLayout!!.addView(v)
        }
    /**
     * 关闭全部控制器
     */
    fun hideControllers() {
        if (controlIsShow) {
            changeControl()
        }
        mProgressBar!!.visibility = INVISIBLE
        mCenterPlayBtn!!.visibility = INVISIBLE
    }

    /**
     * 打开全部控制器
     */
    fun showControllers() {
        if (!controlIsShow) changeControl()
    }

    /**
     * 更新（播放进度等等）
     */
    private var oldDuration = -1111
    private var videoMaxDuration = -11
    private var _startPlay = false

    /**
     * 更新（播放进度等等）
     */
    private fun updateUI() {
        val progress = mVideoView!!.currentPosition
        val playing = mVideoView!!.isPlaying
        if (playing) {
            if (oldDuration == progress && videoMaxDuration != progress) {
                mProgressBar!!.visibility = VISIBLE
                mUiHandler.postDelayed(mUiRunnable, 1000)
                return
            } else {
                thumbImageView!!.visibility = GONE
                if (mProgressBar!!.visibility != INVISIBLE) mProgressBar!!.visibility =
                    INVISIBLE
            }
        }
        oldDuration = progress

        if (!isTouchProgress) {
            mVideoDurationTv!!.text = stringForTime(progress)
            mVideoSeekBar!!.progress = progress
            if (mediaListener != null) {
                mediaListener?.onProgress(progress, videoMaxDuration)
            }
        }
        if (playing) {
            if (mediaListener != null && !_startPlay) {
                mediaListener!!.onStart()
            }
            _startPlay = true
        }
        if (playing || !_startPlay && progress != videoMaxDuration) {
            if (mCenterPlayBtn!!.visibility != INVISIBLE) {
                mCenterPlayBtn!!.visibility = INVISIBLE
                mPlayBtn?.change(false)
            }
            mUiHandler.postDelayed(mUiRunnable, 1000)
        } else {
            mCenterPlayBtn!!.visibility = VISIBLE
            mPlayBtn?.change(true)
            if (mediaListener != null) mediaListener!!.onEndPlay()
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.nur_video_centerPlayBtn) {
            start()
        } else if (id == R.id.nur_video_view_LockIv) {
            controlIsShow = true
            changeControl()
            isLock = !isLock
            if (isLock) mLockImage!!.setImageResource(R.drawable.nur_ic_lock) else mLockImage!!.setImageResource(
                R.drawable.nur_ic_unlock
            )
        } else if (id == R.id.nur_video_playIv) {
            if (!isLock) {
                if (mVideoView!!.isPlaying) pause() else start()
            }
        } else if (id == R.id.nur_video_backIv) {
            if (onBackPressListener != null) {
                onBackPressListener!!.onClick(v)
            }
        }
    }

    /**
     * 返回按钮点击
     */
    fun setOnBackPressListener(onBackPressListener: OnClickListener?) {
        this.onBackPressListener = onBackPressListener
    }

    /**
     * Sets video URI.
     *
     * @param uri the URI of the video.
     */
    fun initPlayer(activity: Activity?, uri: Uri?, title: String?) {
        if (title != null) mTitleView!!.text = title
        mActivity = activity
        mVideoView!!.setVideoURI(uri)
        mUiHandler.postDelayed({
            mCenterPlayBtn!!.visibility = INVISIBLE
            mProgressBar!!.visibility = VISIBLE
            mPlayBtn?.change(false)
        }, 60)
        autoDismiss()
    }

    fun setTitle(title: String?) {
        mTitleView!!.text = title
    }
    /**
     * 开始播放
     */
    @JvmOverloads
    fun start(progress: Int = 0) {
        mCenterPlayBtn!!.visibility = INVISIBLE
        mProgressBar!!.visibility = VISIBLE
        mPlayBtn?.change(false)
        if (progress > 0) {
            mVideoView!!.seekTo(progress)
        }
        mVideoView!!.start()
        autoDismiss()
        mUiHandler.removeCallbacks(mUiRunnable)
        mUiHandler.postDelayed(mUiRunnable, 1000)
        if (mediaListener != null) {
            mediaListener!!.onPause(false)
        }
    }

    /**
     * 暂停
     */
    fun pause() {
        mUiHandler.removeCallbacks(mUiRunnable)
        mCenterPlayBtn!!.visibility = VISIBLE
        mProgressBar!!.visibility = INVISIBLE
        mPlayBtn?.change(true)
        mVideoView!!.pause()
        if (mediaListener != null) {
            mediaListener!!.onPause(true)
        }
    }

    /**
     * 视频加载完成, 准备好播放视频的回调
     */
    private val preparedListener: IMediaPlayer.OnPreparedListener =
        object : IMediaPlayer.OnPreparedListener {
            override fun onPrepared(mp: IMediaPlayer) {
                mediaPlayer = mp
                videoMaxDuration = mVideoView!!.duration
                mVideoViewHeight = mp.videoHeight
                mVideoViewWidth = mp.videoWidth
                mVideoSeekBar!!.max = videoMaxDuration
                mVideoMaxLenTv!!.text = stringForTime(videoMaxDuration)
                mProgressBar!!.visibility = INVISIBLE

                mUiHandler.postDelayed(mUiRunnable, 1000)
            }
        }

    /**
     * 获取视频高宽度
     */
    val videoWH: IntArray
        get() = intArrayOf(mVideoViewWidth, mVideoViewHeight)

    /**
     * 获取ObjectAnimator
     */
    private fun getObjectAnimator(
        start: Float,
        end: Float,
        propertyName: String,
        view: View?
    ): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, propertyName, start, end)
    }

    /**
     * 获取ObjectAnimators
     */
    private fun getObjectAnimator(
        start: Float,
        end: Float,
        propertyName: String,
        vararg view: View
    ): MutableList<Animator>? {
        if (view == null) {
            return null
        }
        val animators: MutableList<Animator> = mutableListOf()
        var length:Int = view.size
//        for (i in 0 until length) {
//            animators.add(getObjectAnimator(start, end, propertyName, view[i]) as Animator)
//        }
        return animators
    }

    /**
     * 开始动画
     */
    private fun startAnim(view: View?, start: Float, end: Float, propertyName: String) {
        val anim = ObjectAnimator.ofFloat(view, propertyName, start, end)
        anim.duration = 350
        anim.start()
    }

    /**
     * 开始动画
     */
    private fun startAnim(animators: List<Animator>?) {
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animators)
        animatorSet.duration = 350
        animatorSet.start()
    }

    /**
     * 显示(隐藏)控制器
     */
    private fun changeControl() {
        val dp_56 = dip2px(-56f)
        var anim1Start = 0
        var anim2Start = 0
        var anim3Start = 0
        var rcAnimStart = 0
        var anim1End = dip2px(40f)
        var anim2End = dip2px(-66f)
        var anim3End = dp_56
        var rcAnimEnd = dip2px(56f)
        if (!controlIsShow) { //要显示（现在的状态是隐藏）
            anim1Start = anim1End
            anim2Start = anim2End
            anim3Start = anim3End
            rcAnimStart = rcAnimEnd
            anim1End = 0
            anim2End = 0
            anim3End = 0
            rcAnimEnd = 0
        }
        val translationY = "translationY"
        val translationX = "translationX"
        val objectAnimator =
            getObjectAnimator(anim3Start.toFloat(), anim3End.toFloat(), translationX, mLockImage)
        if (isLock) {
            val animators = ArrayList<Animator>()
            animators.add(objectAnimator)
            startAnim(animators)
        } else {
            val animator = getObjectAnimator(
                anim1Start.toFloat(), anim1End.toFloat(), translationY,
                mAdverLayout!!,
                mBottomControl!!
            )
            animator!!.add(
                getObjectAnimator(
                    anim2Start.toFloat(),
                    anim2End.toFloat(),
                    translationY,
                    mTitleControl
                )
            )
            animator.add(
                getObjectAnimator(
                    rcAnimStart.toFloat(),
                    rcAnimEnd.toFloat(),
                    translationX,
                    mRCImage
                )
            )
            animator.add(objectAnimator)
            startAnim(animator)
        }
        controlIsShow = !controlIsShow
        autoDismiss()
    }

    /**
     * 3秒后知道关闭（隐藏）
     * 显示的话3秒后知道隐藏
     */
    private fun autoDismiss() {
        if (controlIsShow) {
            mControlHandler.removeCallbacks(mControlRunnable)
            mControlHandler.postDelayed(mControlRunnable, 3000)
        }
    }

    /**
     * 3秒后知道关闭（隐藏）
     * 显示的话3秒后知道隐藏
     */
    private fun autoDismiss(runnable: Runnable?) {
        if (runnable != null) {
            mControlHandler.removeCallbacks(runnable)
            mControlHandler.postDelayed(runnable, 1000)
        }
    }


    /**
     * 显示中间seek
     */
    private fun videoSeekChange(progress: Int) {
        var progress = progress
        if (mCenterSBLayout!!.visibility != VISIBLE) {
            mCenterSBLayout!!.visibility = VISIBLE
            _playBtnIsShow = mCenterPlayBtn!!.visibility == VISIBLE
            mCenterPlayBtn!!.visibility = INVISIBLE
        }
        if (progress > oldProgress) {
            mVideoSeekBarImage!!.setImageResource(R.drawable.nur_ic_kuaijin_r)
        } else {
            mVideoSeekBarImage!!.setImageResource(R.drawable.nur_ic_kuaijin)
        }
        val max = videoMaxDuration
        if (progress < 0) {
            progress = 0
        } else if (progress > max) {
            progress = max
        }
        val play_time = stringForTime(progress)
        val play_sum_time = stringForTime(max)
        mVideoSeekBarTimeTv!!.text = play_time
        mVideoSeekBarMaxTime!!.text = "/ $play_sum_time"
        if (oldProgress == 0) {
            oldProgress = progress
        }
        moveProgress = progress
    }

    private var oldProgress = 0
    private var moveProgress = 0
    private var _playBtnIsShow = false

    /**
     * 关闭中间seek
     */
    private fun hideVideoSeek() {
        oldProgress = 0
        mVideoView!!.seekTo(moveProgress)
        if (_playBtnIsShow) {
            mCenterPlayBtn!!.visibility = VISIBLE
        }
        mCenterSBLayout!!.visibility = INVISIBLE
    }

    /**
     * NurTouchListener
     * （单）双击-滑动等等
     */
    private val simpleTouchListener: SimpleVideoOnTouch.SimpleTouchListener = object : SimpleVideoOnTouch.SimpleTouchListener {
        override fun onClick() {
            changeControl()
        }

        override fun onDoubleClick() {
            if (!isLock) {
                if (mVideoView!!.isPlaying) pause() else start()
            }
        }

        override fun onMoveSeek(f: Float) {
            if (isLock || !mVideoView!!.isPlaying) {
                return
            }
            val progress = (mVideoView!!.currentPosition + 100 * f).toInt()
            videoSeekChange(progress)
        }

        override fun onMoveLeft(f: Float) {
            if (isLock) {
                return
            }
            setVolume(f)
        }

        override fun onMoveRight(f: Float) {
            if (isLock) {
                return
            }
            var progress = f.toInt() + oldLiangduProgress
            if (progress > MAX_LIANG_DU) {
                progress = MAX_LIANG_DU.toInt()
            }
            if (progress < 0) {
                progress = 1
            }
            mVolumeSeekBar!!.max = MAX_LIANG_DU.toInt()
            setWindowBrightness(progress)
            setProgress(progress, R.drawable.nur_ic_brightness)
        }

        override fun onActionUp(changeType: Int) {
            if (isLock) {
                return
            }
            if (changeType == SimpleVideoOnTouch.changeTypeVideoSeek) {
                hideVideoSeek()
            } else if (changeType == SimpleVideoOnTouch.changeTypeVideoSeek) {
                oldLiangduProgress = mVolumeSeekBar!!.progress
                isTouchLRScreen = false
                autoDismiss(mLRControlRunnable)
            } else if (changeType == SimpleVideoOnTouch.changeTypeVolume) {
                isTouchLRScreen = false
                autoDismiss(mLRControlRunnable)
                oldVolumeProgress = mVolumeSeekBar!!.progress
            }
        }
    }

    /**
     * 设置亮度
     */
    private fun setWindowBrightness(brightness: Int) {
        if (mActivity == null) return
        val window = mActivity!!.window
        val lp = window.attributes
        lp.screenBrightness = brightness / 255.0f
        window.attributes = lp
    }

    /**
     * 显示-隐藏 ---亮度&声音
     */
    private fun changeVolumeControl() {
        var start = dip2px(80f).toFloat()
        var end = dip2px(-30f).toFloat()
        if (!isShowVolumeControl) {
            start = end
            end = dip2px(80f).toFloat()
        }
        startAnim(mVolumeControl, start, end, "translationY")
        isShowVolumeControl = !isShowVolumeControl
    }

    /**
     * 声音
     */
    private fun setVolume(f: Float) {
        mVolumeSeekBar!!.max = 200
        val progress = f.toInt() + oldVolumeProgress
        var res: Int = R.drawable.nur_ic_volume
        if (progress <= 0) {
            res = R.drawable.nur_ic_volume_x
        }
        setProgress(progress, res)
        if (am == null) {
            return
        }
        am.setStreamVolume(AudioManager.STREAM_MUSIC, (maxVolume / 200 * progress).toInt(), 0)
    }

    /**
     * 监听声音大小
     */
    fun onKeyDown(keyCode: Int): Boolean {
        val value = 10
        var ret = false
        when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                setVolume(value.toFloat())
                oldVolumeProgress += value
                ret = true
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                setVolume(-value.toFloat())
                oldVolumeProgress -= value
                ret = true
            }
        }
        isTouchLRScreen = false
        autoDismiss(mLRControlRunnable)
        return ret
    }

    /**
     * 亮度（声音）的seek bar
     */
    private fun setProgress(progress: Int, res: Int) {
        isTouchLRScreen = true
        volumeIcon!!.setImageResource(res)
        mVolumeSeekBar!!.progress = progress
        if (!isShowVolumeControl) changeVolumeControl()
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private fun dip2px(dpValue: Float): Int {
        val scale = mContext.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    var mFormatBuilder = StringBuilder()
    var mFormatter = Formatter(mFormatBuilder, Locale.getDefault())

    init {
        LayoutInflater.from(mContext).inflate(R.layout.simple_video_layout, this)
        val mSettings = Settings(mContext)
        mControlHandler = Handler()
        initLayout()
        am = mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        maxVolume = am!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat() // 3
        val current = am.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
        val a = maxVolume / 200
        oldVolumeProgress = (current / a).toInt()
        mVideoView!!.setOnPreparedListener(preparedListener)
        mVideoView!!.setOnErrorListener(IMediaPlayer.OnErrorListener { mp, what, extra ->
            if (what == -10000) {
                Toast.makeText(mContext, "网络未连接，请检查网络设置", Toast.LENGTH_SHORT).show()
                pause()
                return@OnErrorListener true
            }
            false
        })
    }

    /**
     * 将长度转换为时间
     *
     * @param timeMs
     * @return
     */
    private fun stringForTime(timeMs: Int): String {
        val totalSeconds = timeMs / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        mFormatBuilder.setLength(0)
        return if (hours > 0) {
            mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter.format("%02d:%02d", minutes, seconds).toString()
        }
    }

    //      /**
    //     * release
    //     */
    fun stopPlayback() {
        mVideoView!!.stopPlayback()
        mVideoView!!.release(true)
        mVideoView!!.stopBackgroundPlay()
        IjkMediaPlayer.native_profileEnd()
    }

    /**
     * 监听（播放，暂停）
     */
    fun setOnMediaListener(mediaListener: OnMediaListener?) {
        this.mediaListener = mediaListener
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (isTouchProgress) {
            videoSeekChange(progress)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        isTouchProgress = true
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        mVideoView!!.seekTo(seekBar.progress)
        autoDismiss()
        hideVideoSeek()
        isTouchProgress = false
    }
}