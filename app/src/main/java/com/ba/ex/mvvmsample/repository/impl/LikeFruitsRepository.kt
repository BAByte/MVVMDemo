package com.ba.ex.mvvmsample.repository.impl

import androidx.lifecycle.LiveData
import com.ba.ex.mvvmsample.repository.ILikeFruitsRepository
import com.ba.ex.mvvmsample.repository.data.Fruit
import com.ba.ex.mvvmsample.repository.database.FruitDao
import kotlinx.coroutines.delay

/**
 * 从数据库加载，向viewModel暴露接口访问数据接口
 * 数据库为唯一数据来源
 * 数据来自本地，我们可以让存储区感知数据的变化
 * 需要感知数据库内容的变化，fruitDao.getFruits()的方法返为LiveData
 * 但主动获取LiveData的value会为null，可在Observer方法被调用时获取
 *
 */
class LikeFruitsRepository(private val fruitDao: FruitDao) : ILikeFruitsRepository {

    override fun load(): LiveData<List<Fruit>> = fruitDao.getFruits()

    override fun getFruit(id: String): Fruit {
        return fruitDao.getFruit(id)
    }

    override suspend fun deleteFromDataBase(fruit: Fruit) {
        fruitDao.delete(fruit)
    }

    override suspend fun saveToDataBase(fruits: List<Fruit>): List<Long> {
        delay(1000)
        return fruitDao.insert(fruits)
    }

}