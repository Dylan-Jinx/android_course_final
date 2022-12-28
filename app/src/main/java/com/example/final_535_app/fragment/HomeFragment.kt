package com.example.final_535_app.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.final_535_app.R
import com.example.final_535_app.activity.MessageActivity
import com.example.final_535_app.activity.SearchActivity
import com.example.final_535_app.activity.ViewPagerVideoActivity
import com.example.final_535_app.adapter.HomeBannerAdapter
import com.example.final_535_app.adapter.HomeVideoInfoAdapter
import com.example.final_535_app.databinding.FragmentHomeBinding
import com.example.final_535_app.model.BiliBiliVideo
import com.example.final_535_app.viewmodel.HomeViewModel
import com.youth.banner.Banner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        // 跳转viewpager视频
        binding.homeViewpagerVideo.setOnClickListener{
            startActivity(Intent(context, ViewPagerVideoActivity::class.java))
        }
        //上拉刷新与下拉刷新
        binding.homeRefreshVideoInfo.setOnRefreshListener{
            homeViewModel.getHomeVideoInfo(1, 10)
            binding.homeRefreshVideoInfo.finishRefresh(true)
        }
        binding.homeRefreshVideoInfo.setOnLoadMoreListener{
            binding.homeRefreshVideoInfo.finishLoadMore(true)
            homeViewModel.addHomeVideoInfo()
        }
    }

    override fun invalidate() = withState(homeViewModel){state->
        var gridLayoutManager = GridLayoutManager(binding.root.context,2)
        binding.rcHomeVideoInfo.layoutManager = gridLayoutManager
        var homeVideoInfoAdapter = state.biliBiliVideo
            .invoke()?.data?.records?.let { it1 -> HomeVideoInfoAdapter(it1) }
        homeVideoInfoAdapter?.setEmptyViewLayout(binding.root.context, R.layout.error_no_data)
        binding.rcHomeVideoInfo.adapter = homeVideoInfoAdapter

        if(!binding.homeRefreshVideoInfo.state.isFooter){
            var banner = (binding.bannerHome as Banner<String, HomeBannerAdapter>)
            banner.apply {
                setBannerRound(20f)
                setAdapter(HomeBannerAdapter(state.bannerUrls.invoke()?.data))
            }
        }else{
        }
        return@withState
    }

}