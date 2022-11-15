package com.example.final_535_app.viewmodel

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.example.final_535_app.state.InfoArticleState
import com.example.final_535_app.utils.HttpUtils
import kotlinx.coroutines.Dispatchers

class InfoArticleViewModel(initState: InfoArticleState): MavericksViewModel<InfoArticleState>(initState) {

    fun getInfoArticle(id: Int) = withState {
        if (it.noteDatas is Loading) return@withState
        suspend {
            HttpUtils.apiService.getArticleInfo(id)
        }.execute(Dispatchers.IO, retainValue = InfoArticleState::noteDatas) {
                state-> copy(noteDatas = state)
        }
    }

}