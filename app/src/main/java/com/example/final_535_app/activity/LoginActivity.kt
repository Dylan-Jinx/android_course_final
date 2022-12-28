package com.example.final_535_app.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.mvrx.*
import com.example.final_535_app.R
import com.example.final_535_app.databinding.ActivityLoginBinding
import com.example.final_535_app.model.AppUser
import com.example.final_535_app.state.LoginState
import com.example.final_535_app.utils.PicUtil
import com.example.final_535_app.viewmodel.LoginViewModel


@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity(),MavericksView {

    lateinit var binding: ActivityLoginBinding
    lateinit var group: ViewGroup
    lateinit var imageView: ImageView
    val loginViewModel: LoginViewModel by viewModel(LoginViewModel::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        group = window.decorView as ViewGroup
        imageView = ImageView(this);
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        initEvent()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if(hasFocus){
            for (i in 0 until group.childCount) {
                group.removeView(imageView)
            }
        }else{
            group.removeView(imageView)
            imageView = ImageView(baseContext)
            val bitmap = PicUtil.activityShot(this@LoginActivity)
                ?.let { PicUtil.rsBlur(baseContext, it,25) }
            imageView.setImageBitmap(bitmap);
            group.addView(imageView);
        }
    }

    private fun initEvent() {
        // 页面模型侦听
        loginViewModel.onAsync(
            LoginState::loginInfo,
            deliveryMode = uniqueOnly(),
            onSuccess = {
                if(it.code == 0){
                    val sharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
                    var editor = sharedPreferences.edit();
                    it.data?.mid?.let { it1 -> editor.putInt("mid", it1) };
                    editor.commit();
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show()
                }

            },onFail = {
                Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show()
            }
        )
        // 返回上一个页面
        binding.ivLoginBack.setOnClickListener {
            onBackPressed()
        }
        // 焦点事件与页面交互
        binding.etLoginPwd.setOnFocusChangeListener { _, focuState ->
            if (focuState) {
                binding.ivLoginBannerLeft.setImageResource(R.drawable.login_left_close)
                binding.ivLoginBannerRight.setImageResource(R.drawable.login_right_close)
            } else {
                binding.ivLoginBannerLeft.setImageResource(R.drawable.login_left_open)
                binding.ivLoginBannerRight.setImageResource(R.drawable.login_right_open)
            }
        }
        // 用户登录
        binding.btnMyLogin.setOnClickListener{
            val phone = binding.etLoginPhone.text.toString()
            val passwd = binding.etLoginPwd.text.toString()
            loginViewModel.login(AppUser(0,phone,passwd,0))
        }
    }

    override fun invalidate() {
        Log.d("LOGIN_PAGE", "invalidate: ")
    }

}