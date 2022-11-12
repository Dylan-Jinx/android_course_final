package com.nurmemet.nur.nurvideoplayer

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager


internal class SimpleVideoOnTouch(context: Context, nurTouchListner: SimpleTouchListener) :
    OnTouchListener {
    private val nurTouchListner: SimpleTouchListener
    private val density: Float
    private var downY = 0f
    private var mCurTime: Long = 0
    private var moveType = 0
    private var changeType = 0
    private var downX = 0f
    private var isMove = false
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val action = motionEvent.action
        val centerX = view.width / 2
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                isMove = false
                downX = motionEvent.x
                downY = motionEvent.y
            }
            MotionEvent.ACTION_MOVE -> {
                val moveX = motionEvent.x
                val moveY = motionEvent.y
                val maxX = getMax(downX, moveX)
                val maxY = getMax(downY, moveY)
                val minY = getMin(downY, moveY)
                val minX = getMin(downX, moveX)
                val _x = maxX - minX
                val _y = maxY - minY
                val minMoveValue = 5 * density
                if (_x > minMoveValue || _y > minMoveValue) {
                    isMove = true
                    val moveTypeVertical = 2
                    if (moveType == 0) {
                        val moveTypeHorizontal = 1
                        moveType = if (_x > _y) moveTypeHorizontal else moveTypeVertical
                    }
                    if (moveType == moveTypeVertical) {
                        val y = downY - moveY
                        if (centerX < downX) {
                            changeType = changeTypeVolume
                            nurTouchListner.onMoveLeft(y / density)
                        } else {
                            changeType = changeTypeLiangdu
                            nurTouchListner.onMoveRight(y / density)
                        }
                    } else {
                        changeType = changeTypeVideoSeek
                        nurTouchListner.onMoveSeek((moveX - downX) / density)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                if (!isMove) {
                    click
                }
                nurTouchListner.onActionUp(changeType)
                moveType = 0
                changeType = 0
            }
        }
        return true
    }

    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1 -> nurTouchListner.onClick()
                2 -> nurTouchListner.onDoubleClick()
            }
        }
    }

    init {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        density = dm.density
        this.nurTouchListner = nurTouchListner
    }//单击事件//双击事件

    /**
     * 双击/单击
     */
    private val click: Unit
        private get() {
            var mLastTime = mCurTime
            mCurTime = System.currentTimeMillis()
            if (mCurTime - mLastTime < 300) { //双击事件
                mCurTime = 0
                mLastTime = 0
                handler.removeMessages(1)
                handler.sendEmptyMessage(2)
            } else { //单击事件
                handler.sendEmptyMessageDelayed(1, 310)
            }
        }

    private fun getMax(a: Float, b: Float): Float {
        return if (a > b) a else b
    }

    private fun getMin(a: Float, b: Float): Float {
        return if (a > b) b else a
    }

    internal interface SimpleTouchListener {
        fun onClick()
        fun onDoubleClick()
        fun onMoveSeek(f: Float)
        fun onMoveLeft(f: Float)
        fun onMoveRight(f: Float)
        fun onActionUp(changeType: Int)
    }

    companion object {
        var changeTypeVolume = 1
        var changeTypeLiangdu = 2
        var changeTypeVideoSeek = 3
    }
}