package com.ba.ex.mvvmsample.repository.impl

import android.content.Context
import com.ba.ex.mvvmsample.repository.IFruitsRepository
import com.ba.ex.mvvmsample.repository.data.Fruit
import com.ba.ex.mvvmsample.source.FruitsSourceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.InputStream

/**
 * 存储区：加载数据，可从网络或者数据库，这里是从网络加载或删除
 * 生命周期关联着viewModel,该存储区并不能感知服务器数据的变化，
 * 只有用户主动获取
 */
class FruitsRepository(val context: Context) : IFruitsRepository {
    //数据都是从数据存储区获取，并且只相信存储区的数据
    private val fruitsFlow: MutableStateFlow<List<Fruit>> = MutableStateFlow(listOf())

    override suspend fun load() {
        fruitsFlow.emit(FruitsSourceManager.load())
    }

    override fun getFruitsFlow():StateFlow<List<Fruit>>{
       return fruitsFlow
    }
}