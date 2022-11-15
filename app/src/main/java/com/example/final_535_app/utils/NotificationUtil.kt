package com.example.final_535_app.utils

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.final_535_app.R
import kotlin.random.Random


object NotificationUtil {
    fun showNotification(context: Context, content: String?) {
        //1.创建通知管理器
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        Log.i("TAG", "showNotification:version: " + Build.VERSION.SDK_INT)
        val builder: NotificationCompat.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //Android 8.0版本适配
            val channel =
                NotificationChannel("default", "default", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
            NotificationCompat.Builder(context, "default")
        } else {
            NotificationCompat.Builder(context)
        }
        val intent = Intent(context, Activity::class.java)
        intent.putExtra("msgContent", content)
        //2.创建通知实例
        val notification: Notification = builder
            .setContentTitle("通知标题")
            .setContentText(content)
            .setWhen(System.currentTimeMillis()) //smallIcon 通知栏显示小图标
            //android5.0 之后通知栏图标都修改了，小图标不能含有RGB图层，也就是说图片不能带颜色，否则显示的就成白色方格了
            //解决方法一:为图片带颜色，targetSdkVersion改为21以下
            //解决方法二:只能用白色透明底的图片
            .setSmallIcon(R.drawable.icon) //LargeIcon 下拉后显示的图标
//            .setLargeIcon(
//                BitmapFactory.decodeResource(
//                    getResources(),
//                    R.mipmap.ic_launcher
//                )
//            )
            //收到通知时的效果，这里是默认声音
            .setDefaults(Notification.DEFAULT_SOUND)
            .setAutoCancel(true)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT
                )
            )
            .build()
        //3.notify
        //notifyId每次要不一致，不然下一次的通知会覆盖上一次
        val notifyId: Int = Random(System.currentTimeMillis()).nextInt()
        notificationManager.notify(notifyId, notification)
    }
}