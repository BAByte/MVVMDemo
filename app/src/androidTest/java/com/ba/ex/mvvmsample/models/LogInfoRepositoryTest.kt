package com.ba.ex.mvvmsample.models

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.WorkManager
import androidx.work.ListenableWorker.Result
import androidx.work.testing.TestListenableWorkerBuilder
import com.ba.ex.mvvmsample.module.loggerinfo.workers.AutoLogInfoCollectorWorker
import com.orhanobut.logger.Logger
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matcher
import org.hamcrest.Matchers.greaterThan
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

/**
 * 数据由Worker收集获得,所以先启动Worker，再去拿数据
 */
class LogInfoRepositoryTest {
    private lateinit var context: Context
    private lateinit var workManager: WorkManager
    private lateinit var logInfoRepository: LogInfoRepository

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        workManager = WorkManager.getInstance(context)
        logInfoRepository = LogInfoRepository.getInstance()
    }

    @Test
    fun getLogInfo() {
        // Get the ListenableWorker
        val worker = TestListenableWorkerBuilder<AutoLogInfoCollectorWorker>(context).build()

        // Start the work synchronously
        val result = worker.startWork().get()

        assertThat(result, `is`(Result.success()))

        assertNotNull(logInfoRepository.logInfo.value)
        val size = logInfoRepository.logInfo.value?.size
        assertThat(size, greaterThan(0) as Matcher<in Int?>?)
        Logger.d("LogInfoRepositoryTest : getLogInfo >>> logInfo =${logInfoRepository.logInfo.value}")
    }
}
