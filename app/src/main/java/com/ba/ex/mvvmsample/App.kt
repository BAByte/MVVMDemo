package com.ba.ex.mvvmsample

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.work.*
import com.ba.ex.mvvmsample.repository.RepositoryLoader
import com.ba.ex.mvvmsample.log.workers.AutoLogInfoCollectorWorker
import com.ba.ex.mvvmsample.utils.ProcessUtils
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import java.util.concurrent.TimeUnit

class App : Application() {

    companion object {
        val TAG = "MVVMSample"

        @SuppressLint("StaticFieldLeak")
        lateinit var ctx: Context
    }

    override fun onCreate() {
        super.onCreate()
        ctx = this
        startLogCat()   //定时收集日志任务
        initLogger()    //打印日志框架初始化
        initRepository()//初始化Repository
    }

    private fun initRepository() {
        if (ProcessUtils.isMainProcessInApplication(this)) {
           RepositoryLoader.init(this)
        }
    }

    private fun initLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)  // Whether to show thread info or not. Default true
            .methodCount(0)         // How many method line to show. Default 2
            .methodOffset(7)        // Hides internal method calls up to offset. Default 5
            .tag(TAG)   //Global tag for every log.
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }

    private fun startLogCat() {
        //启动15分钟收集日志服务
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED).build()  // 网络状态

        val request = PeriodicWorkRequest
            .Builder(AutoLogInfoCollectorWorker::class.java, 1, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )//这串代码是加入任务队列的意思
    }
}
