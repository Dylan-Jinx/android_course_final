package com.example.final_535_app.viewmodel

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.example.final_535_app.state.SearchState
import com.example.final_535_app.utils.HttpUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchViewModel(initState: SearchState): MavericksViewModel<SearchState>(initState) {

    fun filterVideo(filterName: String) = withState {
        if(it.filterData is Loading)return@withState
        suspend {
            withContext(Dispatchers.IO) {
                Thread.sleep(2000)
            }
            HttpUtils.apiService.getVideoInfoByNameBlurSearch(filterName)
        }.execute (Dispatchers.IO, retainValue = SearchState::filterData){ state ->
            copy(filterData = state)
        }
    }

}