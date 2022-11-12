package com.example.final_535_app.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.example.final_535_app.common.ApiResponse
import com.example.final_535_app.model.BiliBiliVideo
import com.example.final_535_app.model.PageDataModel

data class HomeState(
    val biliBiliVideo: Async<ApiResponse<PageDataModel<BiliBiliVideo>>> = Uninitialized,
    val bannerUrls: Async<ApiResponse<MutableList<String>>> = Uninitialized
): MavericksState
