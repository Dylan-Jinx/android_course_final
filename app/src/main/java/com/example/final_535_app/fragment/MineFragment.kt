package com.example.final_535_app.fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableEncoder
import com.example.final_535_app.R
import com.example.final_535_app.activity.LoginActivity
import com.example.final_535_app.databinding.FragmentMineBinding
import com.example.final_535_app.state.MineState
import com.example.final_535_app.utils.HttpUtils
import com.example.final_535_app.utils.ImageConvertUtil.byteToBitmap
import com.example.final_535_app.viewmodel.MineViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.internal.wait
import retrofit2.create
import java.net.URL
import kotlin.random.Random

class MineFragment : Fragment(), MavericksView {

    var isLogin: Boolean = false
    // 绑定界面
    lateinit var binding: FragmentMineBinding
    val mineViewModel: MineViewModel by fragmentViewModel(MineViewModel::class)

    override fun onResume() {
        super.onResume()
        val sharedPreferences = context?.getSharedPreferences("user", MODE_PRIVATE)
        var mid = sharedPreferences?.getInt("mid",0)
        if(mid != 0){
            isLogin = true
            mid?.let { mineViewModel.getInfo(it) }
        }else{
            isLogin = false
        }
        loginStateViewVisual()
    }

    private fun loginStateViewVisual() {
        if(!isLogin){
            binding.llMyIslogin.isVisible=false
            binding.tvMySpace.isVisible=false
            binding.llMyUnlogin.isVisible=true
            binding.ivMyPic.setImageResource(R.drawable.icon)
            binding.tvMyLogin.isVisible = true
            binding.llMyFanfollow.isVisible = false

        }else {
            binding.llMyUnlogin.isVisible = true
            binding.llMyFanfollow.isVisible = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMineBinding.inflate(layoutInflater)
        val view = binding.root
        initEvent()
        return view
    }

    private fun initEvent() {
        binding.tvMyLogin.setOnClickListener{
            var intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
        mineViewModel.onAsync(
            MineState:: userInfo,
            deliveryMode = uniqueOnly(),
            onSuccess = {
                binding.tvMyNickname.text = it.data?.name
                binding.tvMyCoininfo.text = "B币："+(it.data?.coins)?.toFloat()+"  硬币：1364"
                if(it.data?.vipStatus.equals("1")){
                    binding.ivMyVip.isVisible = true
                }
                binding.tvMyDynamicCount.text =
                    Random(System.currentTimeMillis()).nextInt(10,1000).toString()
                binding.tvMyFocus.text = it.data?.following.toString()
                binding.tvMyFan.text = it.data?.fans.toString()
                // 绑定头像
                var face_url = HttpUtils.apiService.getMinioFile(it.data?.face.toString()).data?.resUrl

                //TODO 完成网络地址图片转换赋值到头像控件
                withContext(Dispatchers.IO){
                    var face_mip = byteToBitmap(URL(face_url).openStream().readBytes())
                    activity?.runOnUiThread {
                        binding.ivMyPic.setImageBitmap(face_mip)
                    }
                }
            },onFail = {
                Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun invalidate() {

    }
}
