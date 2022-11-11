package com.example.final_535_app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.example.final_535_app.R
import com.example.final_535_app.activity.MessageActivity
import com.example.final_535_app.activity.SearchActivity
import com.example.final_535_app.adapter.HomeVideoInfoAdapter
import com.example.final_535_app.databinding.FragmentHomeBinding
import com.example.final_535_app.state.HomeState
import com.example.final_535_app.viewmodel.HomeViewModel

class HomeFragment : Fragment(R.layout.fragment_home), MavericksView{

    lateinit var binding: FragmentHomeBinding
    val homeViewModel: HomeViewModel by fragmentViewModel(HomeViewModel::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        val view = binding.root
        initEvent()
        homeViewModel.getHomeVideoInfo(1,10)
        return view
    }
    private fun initEvent() {
        binding.llHomeSearch.setOnClickListener{
            startActivity(Intent(context, SearchActivity::class.java))
        }
        // 跳转信息页面
        binding.ivMyMessage.setOnClickListener{
            startActivity(Intent(context, MessageActivity::class.java))
        }

        homeViewModel.onAsync(
            HomeState::biliBiliVideo,
            deliveryMode = uniqueOnly(),
            onSuccess = {
                binding.rcHomeVideoInfo.layoutManager = LinearLayoutManager(binding.root.context)
                var messageChatAdapter = it.data?.records?.let { it1 -> HomeVideoInfoAdapter(it1) }
                binding.rcHomeVideoInfo.adapter = messageChatAdapter
            },
            onFail = {
                Toast.makeText(context, "网络开小差啦～", Toast.LENGTH_SHORT).show()
            }
        )

    }

    override fun invalidate() {
    }

}