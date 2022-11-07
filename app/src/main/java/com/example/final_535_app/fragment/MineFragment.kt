package com.example.final_535_app.fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.example.final_535_app.R
import com.example.final_535_app.activity.LoginActivity
import com.example.final_535_app.databinding.FragmentMineBinding
import com.example.final_535_app.state.MineState
import com.example.final_535_app.viewmodel.MineViewModel

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
            },onFail = {
                Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun invalidate() {

    }
}
