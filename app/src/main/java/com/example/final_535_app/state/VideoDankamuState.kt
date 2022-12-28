package com.example.final_535_app.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.example.final_535_app.common.ApiResponse
import com.example.final_535_app.common.ApiResponseList
import com.example.final_535_app.model.Dankamu

data class VideoDankamuState(
    val dankamu: Async<ApiResponse<MutableList<Dankamu>>> = Uninitialized,
): MavericksState