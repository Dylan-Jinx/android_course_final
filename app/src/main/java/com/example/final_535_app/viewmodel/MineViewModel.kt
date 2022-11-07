package com.example.final_535_app.viewmodel

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.example.final_535_app.state.MineState
import com.example.final_535_app.utils.HttpUtils
import kotlinx.coroutines.Dispatchers

class MineViewModel(initState:MineState): MavericksViewModel<MineState>(initState) {

    fun getInfo(mid: Int) = withState{
        if(it.userInfo is Loading)return@withState
        suspend {
            HttpUtils.apiService.getUserInfo(mid)
        }.execute(Dispatchers.IO, retainValue = MineState::userInfo){
            state -> copy(userInfo = state)
        }
    }
}