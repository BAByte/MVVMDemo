package com.ba.ex.mvvmsample.ui.activity

import android.util.Log
import junit.framework.TestCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Test

class FlowTest : TestCase() {
    val TAG = "FlowTest"
    fun simple(): Flow<Int> = flow { // 流构建器
        for (i in 1..3) {
            delay(100) // 假装我们在这里做了一些有用的事情
            emit(i) // 发送下一个值
        }
    }

    @Test
    fun collect() = runBlocking{
        simple().collect{
            Log.d(TAG,"FlowTest $it")
        }
    }
}