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
import com.example.final_535_app.utils.PermissionUtils.bestPermissionX
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.permissionx.guolindev.PermissionX

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
                Manifest.permission.LOCATION_HARDWARE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
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