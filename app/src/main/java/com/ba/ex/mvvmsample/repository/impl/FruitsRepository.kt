package com.ba.ex.mvvmsample.repository.impl

import android.content.Context
import com.ba.ex.mvvmsample.repository.IFruitsRepository
import com.ba.ex.mvvmsample.repository.data.Fruit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import kotlinx.coroutines.delay
import java.io.InputStream

/**
 * 存储区：加载数据，可从网络或者数据库，这里是从网络加载或删除
 * 生命周期关联着viewModel,该存储区并不能感知服务器数据的变化，
 * 只有用户主动获取
 */
class FruitsRepository(val context: Context): IFruitsRepository {
    override suspend fun load(): List<Fruit> {
        delay(2000)  //模拟网络下载
        Logger.d("FruitWebRequest >>> load")
        val inputStream: InputStream = context.assets.open("plants.json")
        val len = inputStream.available()
        val buffer = ByteArray(len)
        inputStream.read(buffer)
        val result = String(buffer)
        return Gson().fromJson(
            result,
            object : TypeToken<List<Fruit>>() {}.type
        )
    }
}