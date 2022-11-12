package com.example.final_535_app.view.simplevideo.listener

/**
 * 监听播放进度
 */
interface OnMediaListener {
    /**
     * 开发播放
     */
    fun onStart()

    /**
     * 暂停播放
     */
    fun onPause()

    /**
     * 播放进度
     *
     * @param progress // 正在播放到哪儿了
     * @param duration // 视频总长都
     */
    fun onProgress(progress: Int, duration: Int)

    /**
     * 更改全（单）屏
     */
    fun onChangeScreen(isPortrait: Boolean)

    /**
     * 播放完成
     */
    fun onEndPlay()
}