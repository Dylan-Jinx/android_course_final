package com.example.final_535_app.viewmodel

import android.util.Log
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.example.final_535_app.model.AppUser
import com.example.final_535_app.state.LoginState
import com.example.final_535_app.utils.HttpUtils
import kotlinx.coroutines.Dispatchers

class LoginViewModel(initState: LoginState): MavericksViewModel<LoginState>(initState){
    fun login(appuser: AppUser) = withState {
        if(it.loginInfo is Loading) return@withState
        suspend {
            HttpUtils.apiService.userLogin(appuser)
        }.execute (Dispatchers.IO, retainValue = LoginState::loginInfo){
                state -> copy(loginInfo = state)
        }
    }
}