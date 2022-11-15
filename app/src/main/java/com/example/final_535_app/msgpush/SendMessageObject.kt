package com.example.final_535_app.msgpush

data class SendMessageObject(
    val title:String? = null,
    val content:String? = null,
    val param:String? = null,
    val pageState:Boolean = false
)
