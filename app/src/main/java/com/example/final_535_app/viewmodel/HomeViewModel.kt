package com.example.final_535_app.viewmodel

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.example.final_535_app.model.BiliBiliVideo
import com.example.final_535_app.state.HomeState
import com.example.final_535_app.utils.HttpUtils
import kotlinx.coroutines.Dispatchers
import java.util.Collections

class HomeViewModel(initState: HomeState): MavericksViewModel<HomeState>(initState){

    init{
        getHomeHeadBanner()
        getHomeVideoInfo(1, 10)

    }

    fun getHomeHeadBanner() = withState {
        if(it.bannerUrls is Loading) return@withState
        suspend {
            HttpUtils.apiService.getVideoBanner()
        }.execute(Dispatchers.IO, retainValue = HomeState::bannerUrls) {
            state -> copy(bannerUrls = state)
        }
    }

    fun addHomeVideoInfo() = withState {
        var currentDatas = it.biliBiliVideo.invoke()?.data?.records
        suspend {
            HttpUtils.apiService.getVideoInfo(1,10)
        }.execute { state ->
            state.invoke()?.data?.records?.addAll(0,currentDatas as Collection<BiliBiliVideo>)
            copy(biliBiliVideo = state)
        }
    }

    fun getHomeVideoInfo(pageNumber: Number, pageSize: Number) = withState {
        if (it.biliBiliVideo is Loading) return@withState
        suspend {
            HttpUtils.apiService.getVideoInfo(pageNumber, pageSize)
        }.execute(Dispatchers.IO, retainValue = HomeState::biliBiliVideo) {
                state-> copy(biliBiliVideo = state)
        }
    }

}