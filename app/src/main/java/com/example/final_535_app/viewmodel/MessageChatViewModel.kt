package com.example.final_535_app.viewmodel

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.example.final_535_app.state.MessageChatState
import com.example.final_535_app.utils.HttpUtils
import kotlinx.coroutines.Dispatchers

class MessageChatViewModel(initState: MessageChatState) :
    MavericksViewModel<MessageChatState>(initState) {

    fun messageChats(pageNumber: Number, pageSize: Number) = withState {
        if (it.messageChats is Loading) return@withState
        suspend {
            HttpUtils.apiService.getUserInfoByPage(pageNumber, pageSize)
        }.execute(Dispatchers.IO, retainValue = MessageChatState::messageChats) {
            state-> copy(messageChats = state)
        }
    }
}