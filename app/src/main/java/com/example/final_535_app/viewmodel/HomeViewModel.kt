package com.example.final_535_app.viewmodel

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.example.final_535_app.state.HomeState
import com.example.final_535_app.utils.HttpUtils
import kotlinx.coroutines.Dispatchers

class HomeViewModel(initState: HomeState): MavericksViewModel<HomeState>(initState){

    init{
        getHomeVideoInfo(1, 10)
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