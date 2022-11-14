package com.example.final_535_app.common

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.liulishuo.okdownload.DownloadMonitor
import com.liulishuo.okdownload.DownloadTask
import com.liulishuo.okdownload.OkDownload
import com.liulishuo.okdownload.core.Util
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo
import com.liulishuo.okdownload.core.cause.EndCause
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.liulishuo.okdownload.core.dispatcher.CallbackDispatcher
import com.liulishuo.okdownload.core.dispatcher.DownloadDispatcher
import com.liulishuo.okdownload.core.download.DownloadStrategy
import com.liulishuo.okdownload.core.file.DownloadUriOutputStream
import com.liulishuo.okdownload.core.file.ProcessFileStrategy

object DownloadControl {

    fun initOkDownload(context: Context){

        var builder = OkDownload.Builder(context)
            .downloadStore(Util.createDefaultDatabase(context))
            .callbackDispatcher(CallbackDispatcher())//监听回调监视器
            .downloadDispatcher(DownloadDispatcher())
            .connectionFactory(Util.createDefaultConnectionFactory())
            .outputStreamFactory(DownloadUriOutputStream.Factory())
            .downloadStrategy(DownloadStrategy())
            .processFileStrategy(ProcessFileStrategy())
            .monitor(object : DownloadMonitor{
                override fun taskStart(task: DownloadTask?) {
                    Log.d("DownloadUtils", "下载任务开始 "+task)
                }

                override fun taskDownloadFromBreakpoint(task: DownloadTask, info: BreakpointInfo) {
                    Log.d("DownloadUtils", "断点下载 "+task)
                }

                override fun taskDownloadFromBeginning(
                    task: DownloadTask,
                    info: BreakpointInfo,
                    cause: ResumeFailedCause?
                ) {
                    Log.d("DownloadUtils", "break原因 "+cause)
                }

                override fun taskEnd(task: DownloadTask?, cause: EndCause?, realCause: Exception?) {

                    Log.d("taskEnd", "任务结束 "+cause +"//"+realCause+"//"+task)
                }

            })
        OkDownload.setSingletonInstance(builder.build())
    }

    /**
     * 创建下载任务实例
     *
     * @param url
     * @param parentPath
     * @param fileName
     * @return
     */
    fun createTask(url: String?, parentPath: String?, fileName: String?): DownloadTask? {
        return DownloadTask.Builder(url!!, parentPath!!, fileName)
            .setFilenameFromResponse(false) //是否使用 response header or url path 作为文件名，此时会忽略指定的文件名，默认false
            .setPassIfAlreadyCompleted(true) //如果文件已经下载完成，再次下载时，是否忽略下载，默认为true(忽略)，设为false会从头下载
            .setConnectionCount(1) //需要用几个线程来下载文件，默认根据文件大小确定；如果文件已经 split block，则设置后无效
            .setPreAllocateLength(false) //在获取资源长度后，设置是否需要为文件预分配长度，默认false
            .setMinIntervalMillisCallbackProcess(1500) //通知调用者的频率，避免anr，默认3000
            .setWifiRequired(false) //是否只允许wifi下载，默认为false
            .setAutoCallbackToUIThread(true) //是否在主线程通知调用者，默认为true
            //.setHeaderMapFields(new HashMap<String, List<String>>())//设置请求头
            //.addHeader(String key, String value)//追加请求头
            .setPriority(0) //设置优先级，默认值是0，值越大下载优先级越高
            .setReadBufferSize(4096) //设置读取缓存区大小，默认4096
            .setFlushBufferSize(16384) //设置写入缓存区大小，默认16384
            .setSyncBufferSize(65536) //写入到文件的缓冲区大小，默认65536
            .setSyncBufferIntervalMillis(2000) //写入文件的最小时间间隔，默认2000
            .build()
    }
}