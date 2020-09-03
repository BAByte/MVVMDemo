package com.ba.ex.mvvmsample.log.workers

import android.content.Context
import android.os.SystemClock
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ba.ex.mvvmsample.repository.data.LogInfo
import com.ba.ex.mvvmsample.log.LogCollector
import com.ba.ex.mvvmsample.log.LogLoader
import com.ba.ex.mvvmsample.log.LogLoader.dir
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import java.io.File
import java.io.RandomAccessFile


/**
 * 写入程序日志到本地文件 ，这里写一个定时任务，模拟15分钟生成一次日志文件，并且上报
 * 事实上应该是程序崩溃的时候及时的保存的，这里只是做个示例，日志为假数据
 */
class AutoLogInfoCollectorWorker(
    val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Logger.d("AutoLogInfoCollectorWorker >>> doWork")
        //满100上传一次，并且清除内容,100的原因是想看的到效果
        LogLoader.load().let {
            if (it.size > 10) {
                upLogInfo()
            }
        }

        //伪造log日志
        // LogInfoRepository.getInstance().setLogInfo(LogCollectorImpl.LogCollector() )
        val filename = "${SystemClock.currentThreadTimeMillis()}.log"
        val content = LogCollector.collector()
        val logInfo = LogInfo(filename, dir, content)
        val fileContents = Gson().toJson(logInfo)

        writeTxtToFile(fileContents, dir, filename)
        LogLoader.load()

        return Result.success()
    }

    //模拟上传
    private fun upLogInfo() {
        Logger.d("AutoLogInfoCollectorWorker upLoad LogInfo")
        clearLog()
        LogLoader.load()
    }


    // 将字符串写入到文本文件中
    private fun writeTxtToFile(content: String, filePath: String, fileName: String) {

        val strFilePath = filePath + fileName
        // 每次写入时，都换行写
        val strContent = content + "\r\n"
        try {
            val file = File(strFilePath)
            if (!file.exists()) {
                Logger.d("TestFile", "Create the file:$strFilePath")
                file.parentFile?.let {
                    it.mkdirs()
                    file.createNewFile()
                }
            }
            val raf = RandomAccessFile(file, "rwd")
            raf.seek(file.length())
            raf.write(strContent.toByteArray())
            raf.close()
        } catch (e: Exception) {
            Logger.e("TestFile Error on write File:$e")
        }
    }

    private fun clearLog() {
        val root = File(dir)
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