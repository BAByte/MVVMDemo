package com.ba.ex.mvvmsample.repository.impl

import androidx.lifecycle.LiveData
import com.ba.ex.mvvmsample.repository.ILogInfoRepository
import com.ba.ex.mvvmsample.repository.data.LogInfo
import com.ba.ex.mvvmsample.log.LogLoader

/**
 * 收集程序日志，数据从业务层的LoggerCollector获取
 * 数据来自本地，我们可以让存储区感知数据的变化，当定时收集日志任务执行后，这里的数据会得到更新
 * 保证本地设备的情况能及时的反馈到视图中
 */
class LogInfoRepository : ILogInfoRepository {
    override fun loadLogInfo(): LiveData<List<LogInfo>> {
        return LogLoader.logInfo
    }
}