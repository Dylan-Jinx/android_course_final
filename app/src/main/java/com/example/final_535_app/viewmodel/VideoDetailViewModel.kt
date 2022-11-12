package com.example.final_535_app.viewmodel

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.example.final_535_app.state.VideoDetailState
import com.example.final_535_app.utils.HttpUtils
import kotlinx.coroutines.Dispatchers

class VideoDetailViewModel(initState: VideoDetailState): MavericksViewModel<VideoDetailState>(initState) {

    fun getVideoDetail(bv: String) = withState {
        if (it.videoDetail is Loading) return@withState
        suspend {
            HttpUtils.apiService.getVideoDetail(bv)
        }.execute(Dispatchers.IO, retainValue = VideoDetailState::videoDetail) {
                state -> copy(videoDetail = state)
        }
    }

}