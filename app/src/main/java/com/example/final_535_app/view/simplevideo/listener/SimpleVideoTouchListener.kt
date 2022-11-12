package com.example.final_535_app.view.simplevideo.listener

interface SimpleVideoTouchListener {

    fun onClick()
    fun onDoubleClick()
    fun onMoveSeek(f: Float)
    fun onMoveLeft(f: Float)
    fun onMoveRight(f: Float)
    fun onActionUp(changeType: Int)

}