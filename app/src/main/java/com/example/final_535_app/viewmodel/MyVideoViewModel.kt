package com.example.final_535_app.viewmodel

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.example.final_535_app.state.MyVideoState
import com.example.final_535_app.utils.HttpUtils
import kotlinx.coroutines.Dispatchers

class MyVideoViewModel(initState: MyVideoState): MavericksViewModel<MyVideoState>(initState){

    init {
        getHomeVideoInfo(1, 10)
    }

    fun getHomeVideoInfo(pageNumber: Number, pageSize: Number) = withState {
        if (it.videoInfos is Loading) return@withState
        suspend {
            HttpUtils.apiService.getVideoInfo(pageNumber, pageSize)
        }.execute(Dispatchers.IO, retainValue = MyVideoState::videoInfos) {
                state-> copy(videoInfos = state)
        }
    }
}