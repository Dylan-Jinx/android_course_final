@file:Suppress("DEPRECATION")

package com.example.final_535_app.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.DisplayMetrics
import android.view.View

object PicUtil {

    /***
     * 截取当前activity
     * @param activity
     * @return
     */
    fun activityShot(activity: Activity): Bitmap? {
        /*获取windows中最顶层的view*/
        val view: View = activity.window.decorView

        //允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true)
        view.buildDrawingCache()

        //获取状态栏高度
        val rect = Rect()
        view.getWindowVisibleDisplayFrame(rect)
        val statusBarHeight: Int = rect.top
        val windowManager = activity.windowManager

        //获取屏幕宽和高
        val outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(outMetrics)
        val width = outMetrics.widthPixels
        val height = outMetrics.heightPixels

        //去掉状态栏
        val bitmap = Bitmap.createBitmap(
            view.getDrawingCache(), 0, statusBarHeight, width,
            height - statusBarHeight
        )

        //销毁缓存信息
        view.destroyDrawingCache()
        view.setDrawingCacheEnabled(false)
        return bitmap
    }
    fun rsBlur(context: Context?, source: Bitmap, radius: Int): Bitmap? {
        //初始化一个RenderScript Context
        val renderScript = RenderScript.create(context)
        // Allocate memory for Renderscript to work with
        //创建输入输出的allocation
        val input = Allocation.createFromBitmap(renderScript, source)
        val output = Allocation.createTyped(renderScript, input.type)
        // Load up an instance of the specific script that we want to use.
        //创建ScriptIntrinsic
        val scriptIntrinsicBlur =
            ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        //(4)//填充数据
        scriptIntrinsicBlur.setInput(input)
        // Set the blur radius
        //设置模糊半径
        scriptIntrinsicBlur.setRadius(radius.toFloat())
        // Start the ScriptIntrinisicBlur
        //启动内核
        scriptIntrinsicBlur.forEach(output)
        // Copy the output to the blurred bitmap
        //copy数据
        output.copyTo(source)
        //销毁renderScript
        renderScript.destroy()
        return source
    }
}