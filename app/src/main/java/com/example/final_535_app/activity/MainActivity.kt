package com.example.final_535_app.activity

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.final_535_app.R
import com.example.final_535_app.common.OnFragmentKeyDownListener
import com.example.final_535_app.msgpush.MQTTHelper
import com.example.final_535_app.msgpush.Qos
import com.example.final_535_app.msgpush.Topic
import com.example.final_535_app.utils.PermissionUtils.bestPermissionX
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.permissionx.guolindev.PermissionX
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class MainActivity : AppCompatActivity() {

    lateinit var appbarConfiguration: AppBarConfiguration
    lateinit var navController: NavController
    var onFragmentKeyDownListener:MutableList<OnFragmentKeyDownListener> = ArrayList()
    var exitTime: Long = 0

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()

        bestPermissionX(PermissionX.init(this),
            arrayListOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
            )
        ,this)


        //推送功能核心代码

        val server = "tcp://192.168.123.52:1879" //服务端地址
        val mqttHelper = MQTTHelper(this,server,"admin","public")
        mqttHelper.connect(Topic.TOPIC_MSG, Qos.QOS_TWO,false,object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {

            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                //收到消息
                Toast.makeText(this@MainActivity, ""+ String(message?.payload!!), Toast.LENGTH_SHORT).show()
                message?.payload?.let { String(it) }?.let { Log.d("消息", it) }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {

            }
        })
    }

    fun initView() {
        var navView: BottomNavigationView = findViewById(R.id.nav_view);
        appbarConfiguration = AppBarConfiguration.Builder(
            R.id.homeFragment,R.id.dynamicFragment,R.id.interactFragment,
            R.id.mineFragment
        ).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(navView, navController)
        NavigationUI.navigateUp(navController, appbarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        if(onFragmentKeyDownListener.size != 0){
            onFragmentKeyDownListener.removeAt(onFragmentKeyDownListener.size-1)
        }
        return NavigationUI.navigateUp(navController, appbarConfiguration)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
            if(onFragmentKeyDownListener.size != 0){
                if(onFragmentKeyDownListener.get(onFragmentKeyDownListener.size-1).onKeyDown(keyCode, event)){
                    return true
                }else{
                    onSupportNavigateUp()
                }
            }else if(System.currentTimeMillis() - exitTime > 2000){
                Toast.makeText(this, "再次按返回键退出", Toast.LENGTH_SHORT).show()
            }else{
//                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


}