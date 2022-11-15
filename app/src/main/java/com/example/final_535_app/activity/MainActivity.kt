package com.example.final_535_app.activity

import android.Manifest
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.final_535_app.R
import com.example.final_535_app.common.OnFragmentKeyDownListener
import com.example.final_535_app.msgpush.MQTTHelper
import com.example.final_535_app.msgpush.Qos
import com.example.final_535_app.msgpush.Topic
import com.example.final_535_app.utils.NotificationUtil
import com.example.final_535_app.utils.PermissionUtils.bestPermissionX
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.permissionx.guolindev.PermissionX
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import kotlin.random.Random


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
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY
            )
        ,this)
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