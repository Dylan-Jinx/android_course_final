package com.example.final_535_app.viewmodel

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.example.final_535_app.state.ViewPagerVideoState
import com.example.final_535_app.utils.HttpUtils
import kotlinx.coroutines.Dispatchers

class ViewPageVideoViewModel (initState: ViewPagerVideoState): MavericksViewModel<ViewPagerVideoState>(initState){
    init{
        getVideoInfo(1, 10)

    }
    fun getVideoInfo(pageNumber: Number, pageSize: Number) = withState {
        if (it.biliBiliVideo is Loading) return@withState
        suspend {
            HttpUtils.apiService.getVideoInfo(pageNumber, pageSize)
        }.execute(Dispatchers.IO, retainValue = ViewPagerVideoState::biliBiliVideo) {
                state-> copy(biliBiliVideo = state)
        }
    }

}
