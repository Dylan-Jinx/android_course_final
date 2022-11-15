package com.example.final_535_app.msgpush

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.itfitness.mqttlibrary.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import java.util.UUID

class MQTTHelper {
    private val mqttClient: MqttAndroidClient
    private val connectOptions: MqttConnectOptions
    private var mqttActionListener: IMqttActionListener? = null

    constructor(context: Context, serverUrl:String, name:String, pass:String){
        val deviceId = UUID.randomUUID().toString()
        mqttClient = MqttAndroidClient(context, serverUrl, "myAndroid"
        )
        connectOptions = MqttConnectOptions().apply {
            isCleanSession = false
            connectionTimeout = 30
            keepAliveInterval = 10
            userName = "admin"
            password = "public".toCharArray()
        }

    }

    /**
     * 连接
     * @param mqttCallback 接到订阅的消息的回调
     * @param isFailRetry 失败是否重新连接
     */
    fun connect(topic: Topic, qos: Qos, isFailRetry:Boolean, mqttCallback: MqttCallback){
        mqttClient.setCallback(mqttCallback)
        if(mqttActionListener == null){
            mqttActionListener = object :IMqttActionListener{
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("连接","连接成功")
                    subscribe(topic,qos)
                }
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    //失败重连
                    Log.d("连接","连接失败重试${exception?.message}")
                    if (isFailRetry){
                        mqttClient.connect(connectOptions,null,mqttActionListener)
                    }
                }
            }
        }
        mqttClient.connect(connectOptions,null,mqttActionListener)
    }
    /**
     * 订阅
     */
    private fun subscribe(topic: Topic,qos:Qos){
        mqttClient.subscribe(topic.value(),qos.value())
    }

    /**
     * 发布
     */
    fun publish(topic:Topic,message:String,qos:Qos){
        val msg = MqttMessage()
        msg.isRetained = false
        msg.payload = message.toByteArray()
        msg.qos = qos.value()
        mqttClient.publish(topic.value(),msg)
    }

    /**
     * 断开连接
     */
    fun disconnect(){
        mqttClient.disconnect()
    }

}