package com.example.final_535_app.view.simplevideo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.Nullable

class SimpleVideoPlayButton : View {
    private var mPaint: Paint? = null
    private var isPlay = false
    private var status = 0 //1正在改变播放,2正在改变暂停
    private var xValue = 0
    private var onClickListener: OnClickListener? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.color = Color.WHITE
    }

    fun change(changePlayStyle: Boolean) {
        if (isPlay == changePlayStyle) {
            return
        }
        isPlay = changePlayStyle
        startAnim(changePlayStyle)
    }

    private fun startAnim(isPlay: Boolean) {
        status = if (isPlay) 1 else 2
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val height = height
        val width = height / 10
        mPaint!!.strokeWidth = width.toFloat()
        mPaint!!.strokeCap = Paint.Cap.ROUND
        val center = height / 2
        val startY = (center / 2.5).toInt()
        val startX = height / 6
        val _xValue = width / 4
        if (status == 1) { //改变播放
            isPlay = true
            xValue -= _xValue
        } else if (status == 2) { //改变暂停
            isPlay = false
            xValue += _xValue
        }
        val rx = center + startX
        val lx = center - startX
        var rxValue = rx + xValue
        canvas.drawLines(
            floatArrayOf(
                lx.toFloat(),
                (center - startY).toFloat(),
                lx.toFloat(),
                (center + startY).toFloat()
            ),
            mPaint!!
        )
        if (rxValue >= rx) {
            rxValue = rx
        }
        canvas.drawLines(
            floatArrayOf(
                rxValue.toFloat(), (center - startY).toFloat(), rx.toFloat(), center.toFloat(),
                rx.toFloat(), center.toFloat(), rxValue.toFloat(), (center + startY).toFloat()
            ), mPaint!!
        )
        if (isPlay) {
            if (rxValue - _xValue > lx) {
                invalidate()
            } else {
                status = 0
            }
        } else {
            if (rxValue < rx) {
                invalidate()
            } else {
                status = 0
            }
        }
    }

    override fun setOnClickListener(@Nullable l: OnClickListener?) {
        onClickListener = l
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            onClickListener!!.onClick(this)
        }
        return true
    }
}