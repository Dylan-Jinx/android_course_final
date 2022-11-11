package com.example.final_535_app.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.ImageView

/**
 * 自定义的圆角矩形ImageView，可以直接当组件在布局中使用。
 * @author se7en
 */
class RectangleView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) :
    ImageView(context, attrs, defStyle) {
    private val paint: Paint

    init {
        paint = Paint()
    }

    /**
     * 绘制圆角矩形图片
     * @author se7en
     */
    override fun onDraw(canvas: Canvas) {
        val drawable = drawable
        if (null != drawable) {
            val bitmap = (drawable as BitmapDrawable).bitmap
            val b = getRoundBitmap(bitmap, 30)
            val rectSrc = Rect(0, 0, b.width, b.height)
            val rectDest = Rect(0, 0, width, height)
            paint.reset()
            canvas.drawBitmap(b, rectSrc, rectDest, paint)
        } else {
            super.onDraw(canvas)
        }
    }

    /**
     * 获取圆角矩形图片方法
     * @param bitmap
     * @param roundPx 这个属性是设置弧度，一般设置为14，也可以结合实际效果。本例中是30，为了实现明显一点。
     * @return se7en
     */
    private fun getRoundBitmap(bitmap: Bitmap, roundPx: Int): Bitmap {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        val x = bitmap.width
        canvas.drawRoundRect(rectF, roundPx.toFloat(), roundPx.toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }
}