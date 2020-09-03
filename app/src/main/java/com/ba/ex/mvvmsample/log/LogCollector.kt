package com.ba.ex.mvvmsample.log

import android.os.SystemClock
import com.orhanobut.logger.Logger


/**
 * 收集程序运行日志, 这里只是模拟收集,所以返回一个字符串就好了
 */
object LogCollector{
    //收集
    fun collector(): String {
        Logger.d("collecting log")
        return "logger date : ${SystemClock.currentThreadTimeMillis()} logger size: 11KB "
    }
}