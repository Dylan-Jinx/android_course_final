package com.example.final_535_app.fragment

import LocalCacheUtils.getBitmapFromLocal
import LocalCacheUtils.setBitmap2Local
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.bumptech.glide.Glide
import com.example.final_535_app.R
import com.example.final_535_app.activity.*
import com.example.final_535_app.databinding.FragmentMineBinding
import com.example.final_535_app.model.BilibiliUserInfo
import com.example.final_535_app.state.MineState
import com.example.final_535_app.utils.HttpUtils
import com.example.final_535_app.utils.ImageConvertUtil.byteToBitmap
import com.example.final_535_app.utils.SharePreferenceUtils.getValueToSharePreference
import com.example.final_535_app.utils.SharePreferenceUtils.setValueToSharePreference
import com.example.final_535_app.viewmodel.MineViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.random.Random

@RequiresApi(Build.VERSION_CODES.O)
class MineFragment : Fragment(R.layout.fragment_mine), MavericksView {

    var isLogin: Boolean = false
    // 绑定界面
    lateinit var binding: FragmentMineBinding
    val mineViewModel: MineViewModel by fragmentViewModel(MineViewModel::class)
    var mid = 0

    private fun loginStateViewVisual() {
        if(!isLogin){
            binding.llMyIslogin.isVisible=false
            binding.tvMySpace.isVisible=false
            binding.llMyUnlogin.isVisible=true
            binding.ivMyPic.setImageResource(R.drawable.icon)
            binding.tvMyLogin.isVisible = true
        }else {
            binding.llMyUnlogin.isVisible = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMineBinding.inflate(layoutInflater)
        val view = binding.root
        val sharedPreferences = context?.getSharedPreferences("user", MODE_PRIVATE)
        mid = sharedPreferences?.getInt("mid",0)!!
        if(mid != 0){
            isLogin = true
            mid.let { mineViewModel.getInfo(it) }
        }else{
            isLogin = false
        }
        loginStateViewVisual()
        initEvent()
        return view
    }

    private fun initEvent() {
        binding.tvMyLogin.setOnClickListener{
            startActivity(Intent(context, LoginActivity::class.java))
        }
        binding.createIndex.setOnClickListener{
            startActivity(Intent(context, ChartActivity::class.java))
        }
        binding.myDraft.setOnClickListener{
            startActivity(Intent(context, MyVideoActivity::class.java))
        }
        binding.llMap.setOnClickListener{
            startActivity(Intent(context, MapActivity::class.java))
        }
        binding.llMineLocalCache.setOnClickListener{
            startActivity(Intent(context, LocalDownloadActivity::class.java))
        }
        mineViewModel.onAsync(
            MineState:: userInfo,
            deliveryMode = uniqueOnly(),
            onSuccess = { it ->
                // 绑定头像
                if (getBitmapFromLocal(context, mid) == null) {
                    withContext(Dispatchers.IO){
                        var face_mip = byteToBitmap(URL(it.data?.face).openStream().readBytes())!!
                        setBitmap2Local(context, mid, face_mip)
                        withContext(Dispatchers.Main){
                            context?.let { it1 ->
                                Glide.with(it1)
                                    .load(it.data?.face)
                                    .into(binding.ivMyPic)
                            }
                        }
                    }
                }else{
                    binding.ivMyPic.setImageBitmap(getBitmapFromLocal(context, mid))
                }
            },onFail = {
                Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun invalidate() = withState(mineViewModel) { state ->
        var it = state.userInfo.invoke()
        setValueToSharePreference(context, "user_data", it?.data)
        if(isLogin){
            var test = getValueToSharePreference(context,"user_data",BilibiliUserInfo::class.java)
            UIComponentRefresh(test)
        }
    }

    private fun UIComponentRefresh(it: BilibiliUserInfo?) {
        binding.tvMyNickname.text = it?.name
        binding.tvMyCoininfo.text = "B币："+(it?.coins)?.toFloat()+"  硬币：1364"
        if(it?.vipStatus.equals("1")){
            binding.ivMyVip.isVisible = true
        }
        binding.tvMyDynamicCount.text = "2333"
        binding.tvMyFocus.text = it?.following.toString()
        binding.tvMyFan.text = it?.fans.toString()
    }
}
