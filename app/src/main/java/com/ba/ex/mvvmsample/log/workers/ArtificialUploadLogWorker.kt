package com.ba.ex.mvvmsample.log.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ba.ex.mvvmsample.log.LogLoader
import com.orhanobut.logger.Logger
import kotlinx.coroutines.coroutineScope
import java.io.File

/*
* 主动上传程序日志，这里只是模拟
* */
class ArtificialUploadLogWorker(context: Context,
                                workerParams: WorkerParameters): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        uploadLog()
        clearLog()
        LogLoader.load() //相当于刷新数据了
        Result.success()
    }

    private fun uploadLog() {
        Logger.d("ArtificialUploadLogWorker uploadLog ... ")
    }

    private fun clearLog() {
        val root = File(LogLoader.dir)
        clearDir(root)
    }

    private fun clearDir(root: File) {
        val files = root.listFiles() ?: return
        for (f in files) {
            if (f.isDirectory) { // 判断是否为文件夹
                clearDir(f)
                f.delete()
            } else {
                clearDir(f)
                f.delete()
            }
        }
    }
}