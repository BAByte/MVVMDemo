package com.ba.ex.mvvmsample.source

import com.ba.ex.mvvmsample.App
import com.ba.ex.mvvmsample.repository.data.Fruit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import kotlinx.coroutines.delay
import java.io.InputStream

object FruitsSourceManager {
    private var i = -1
    private const val switchingFactor = 3
    private const val switchFullFlag = 0
    private const val switchFewFlag = 1

    suspend fun load(): List<Fruit> {
        delay(2000)  //模拟网络下载
        val inputStream = simulationDataChange()
        val len = inputStream.available()
        val buffer = ByteArray(len)
        inputStream.read(buffer)
        val result = String(buffer)
        return Gson().fromJson(
            result,
            object : TypeToken<List<Fruit>>() {}.type
        )
    }

    private fun simulationDataChange(): InputStream {
        i++
        return when (i % switchingFactor) {
            switchFullFlag -> {
                loadFull()
            }
            switchFewFlag -> {
                loadFew()
            }
            else -> {
                loadNUll()
            }
        }
    }

    private fun loadFull(): InputStream {
        Logger.d("FruitWebRequest >>> load")
        return App.ctx.assets.open("plants.json")
    }

    private fun loadFew(): InputStream {
        Logger.d("FruitWebRequest >>> load")
        return App.ctx.assets.open("plants2.json")
    }


    private fun loadNUll(): InputStream {
        Logger.d("FruitWebRequest >>> load")
        return App.ctx.assets.open("plants3.json")
    }
}