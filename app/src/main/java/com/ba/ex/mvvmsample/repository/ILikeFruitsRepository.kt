package com.ba.ex.mvvmsample.repository

import androidx.lifecycle.LiveData
import com.ba.ex.mvvmsample.repository.data.Fruit

/**
 * 从数据库加载，向viewModel暴露接口访问数据接口
 * 数据库为唯一数据来源
 * 数据来自本地，我们可以让存储区感知数据的变化
 * 需要感知数据库内容的变化，fruitDao.getFruits()的方法返为LiveData
 * 但主动获取LiveData的value会为null，可在Observer方法被调用时获取
 *
 */
interface ILikeFruitsRepository {

    fun load(): LiveData<List<Fruit>>

    fun getFruit(id: String): Fruit

    suspend fun deleteFromDataBase(fruit: Fruit)

    suspend fun saveToDataBase(fruits: List<Fruit>): List<Long>
}