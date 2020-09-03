package com.ba.ex.mvvmsample.models

import androidx.test.platform.app.InstrumentationRegistry
import com.orhanobut.logger.Logger
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

class FruitsRepositoryTest {

    @Test
    fun load() = runBlocking{
        val context =  InstrumentationRegistry.getInstrumentation().targetContext
        val fruits = FruitsRepository.getInstance().load(context)
        Logger.d("FruitsRepositoryTest : load >>> fruit = $fruits")
        assertNotNull(fruits)
    }
}