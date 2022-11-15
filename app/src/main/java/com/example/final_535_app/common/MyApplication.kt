package com.example.final_535_app.common

import android.app.Application
import android.util.Log
import com.airbnb.mvrx.Mavericks
import com.example.final_535_app.common.DownloadControl.initOkDownload
import com.example.final_535_app.msgpush.MQTTHelper
import com.example.final_535_app.msgpush.Qos
import com.example.final_535_app.msgpush.Topic
import com.example.final_535_app.utils.NotificationUtil
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.lang.Thread.UncaughtExceptionHandler


class MyApplication: Application(),UncaughtExceptionHandler {
    override fun onCreate() {
        super.onCreate()
        Mavericks.initialize(this)
        initOkDownload(this)
        initMsgPush()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }
    override fun uncaughtException(p0: Thread, p1: Throwable) {
        Log.d("Central Exception:", "uncaughtException: $p0")
        Log.d("Central Exception:", "uncaughtException: $p1")
    }
    private fun initMsgPush() {
        val server = "tcp://192.168.123.52:1879" //服务端地址
        val mqttHelper = MQTTHelper(this,server,"admin","public")
        mqttHelper.connect(Topic.TOPIC_MSG, Qos.QOS_TWO,false,object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {

            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                //收到消息
                NotificationUtil.showNotification(applicationContext, String(message?.payload!!))
                Log.d("MyApplication", "messageArrived: "+ message?.payload?.let { String(it) })
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {

            }
        })
    }
}