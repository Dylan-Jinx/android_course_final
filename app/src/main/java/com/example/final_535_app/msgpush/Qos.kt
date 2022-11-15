package com.example.final_535_app.msgpush

enum class Qos {
    QOS_ZERO{
        override fun value():Int{
            return 0
        }
    },
    QOS_ONE{
        override fun value():Int{
            return 1
        }
    },
    QOS_TWO{
        override fun value():Int{
            return 2
        }
    };
    abstract fun value(): Int
}
