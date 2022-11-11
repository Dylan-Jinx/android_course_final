package com.example.final_535_app.activity

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.final_535_app.R
import com.example.final_535_app.base.BaseActivity
import com.example.final_535_app.common.OnFragmentKeyDownListener
import com.example.final_535_app.utils.PermissionRequestUtils.verifyStoragePermissions
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var appbarConfiguration: AppBarConfiguration
    lateinit var navController: NavController
    var onFragmentKeyDownListener:MutableList<OnFragmentKeyDownListener> = ArrayList()
    var exitTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()

        var flag = ActivityCompat.shouldShowRequestPermissionRationale(this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (!flag){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1);
        }
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