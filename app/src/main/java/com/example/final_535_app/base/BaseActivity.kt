package com.example.final_535_app.base

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

abstract class BaseActivity(contentLayoutId: Int) : AppCompatActivity(contentLayoutId) {
    fun initToolBar(textView: TextView, toolbar: Toolbar, e:Boolean, txt: String){
        textView.text = txt
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(e)
    }

//    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(this.initLayout())
        initView(savedInstanceState)
        initData()
        initEvent()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    protected abstract fun initLayout():Int
    protected abstract fun initView(savedInstanceState: Bundle?)
    protected abstract fun initData()
    protected abstract fun initEvent()

}