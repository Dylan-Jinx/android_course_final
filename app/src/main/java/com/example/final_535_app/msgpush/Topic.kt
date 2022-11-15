package com.example.final_535_app.msgpush

enum class Topic {
    //订阅主题
    TOPIC_MSG{
        override fun value():String{
            return "android_subscribe"
        }
    },
    //发布主题
    TOPIC_SEND{
        override fun value():String{
            return "android_publish"
        }
    };
    abstract fun value(): String
}
