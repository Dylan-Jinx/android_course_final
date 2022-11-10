package com.example.final_535_app.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.example.final_535_app.common.ApiResponse
import com.example.final_535_app.model.BilibiliUserInfo
import com.example.final_535_app.model.PageDataModel

data class MessageChatState (
    val messageChats: Async<ApiResponse<PageDataModel<BilibiliUserInfo>>> = Uninitialized
):MavericksState