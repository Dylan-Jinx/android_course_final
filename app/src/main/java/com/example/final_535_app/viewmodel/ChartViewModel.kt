package com.example.final_535_app.viewmodel

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.example.final_535_app.state.ChartState
import com.example.final_535_app.utils.HttpUtils
import kotlinx.coroutines.Dispatchers

class ChartViewModel(initState: ChartState): MavericksViewModel<ChartState>(initState){
    fun getVideoDetail(bv: String) = withState {
        if (it.biliBiliVideo is Loading) return@withState
        suspend {
            HttpUtils.apiService.getVideoDetail(bv)
        }.execute(Dispatchers.IO, retainValue = ChartState::biliBiliVideo) {
                state -> copy(biliBiliVideo = state)
        }
    }
}