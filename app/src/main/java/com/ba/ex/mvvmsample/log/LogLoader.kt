package com.ba.ex.mvvmsample.log

import android.os.Environment
import androidx.lifecycle.MutableLiveData
import com.ba.ex.mvvmsample.repository.data.LogInfo
import com.orhanobut.logger.Logger
import java.io.File

/***
 * 从本地文件读取日志
 */
object LogLoader  {
    val logInfo: MutableLiveData<List<LogInfo>> = MutableLiveData()
    val dir
        get() =
            "${Environment.getExternalStorageDirectory().absolutePath}/MVVMSample/logger/".apply {
                val dir = File(this)
                if (!dir.exists()) {
                    dir.mkdirs()
                } else {
                    dir.delete()
                    dir.mkdirs()
                }
            }

    //从本地文件读取日志信息
     fun load(): List<LogInfo> {
        Logger.d(">>> load")
        val fileMap =
            getAllFiles(
                dir,
                "log"
            )
        val log: MutableList<LogInfo> = mutableListOf()

        for ((fileName, filePath) in fileMap) {
            //做个展示，内容就不读了
            log.add(LogInfo(fileName, filePath, null))
        }
        logInfo.postValue(log)
        return log
    }


    /**
     * 获取指定目录内所有文件路径
     * @param dirPath 需要查询的文件目录
     * @param type 查询类型
     */
    private fun getAllFiles(dirPath: String, type: String): Map<String, String> {
        val fileMap = mutableMapOf<String, String>()
        val f = File(dirPath)
        if (!f.exists()) {//判断路径是否存在
            return fileMap
        }

        val files = f.listFiles()
                ?: //判断权限
                return fileMap


        for (file in files) {//遍历目录
            if (file.isFile && file.name.endsWith(type)) {
                val filePath = file.absolutePath//获取文件路径
                val end = file.name.lastIndexOf('.')
                val fileName = file.name.substring(0, end)//获取文件名

                fileMap[fileName] = filePath

            } else if (file.isDirectory) {//查询子目录
                getAllFiles(
                    file.absolutePath,
                    type
                )
            }
        }
        return fileMap
    }
}