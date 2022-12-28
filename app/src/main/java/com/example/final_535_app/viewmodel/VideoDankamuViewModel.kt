package com.example.final_535_app.viewmodel

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.example.final_535_app.model.BiliBiliVideo
import com.example.final_535_app.state.VideoDankamuState
import com.example.final_535_app.utils.HttpUtils
import kotlinx.coroutines.Dispatchers

class VideoDankamuViewModel(initState: VideoDankamuState): MavericksViewModel<VideoDankamuState>(initState) {

    fun getVideoDankamu(bv: String) = withState {
        if (it.dankamu is Loading) return@withState
        suspend {
            HttpUtils.apiService.getVideoDankamu(bv)
        }.execute(Dispatchers.IO, retainValue = VideoDankamuState::dankamu) {
                state-> copy(dankamu = state)
        }
    }

}
