package com.ba.ex.mvvmsample.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ba.ex.mvvmsample.repository.ILikeFruitsRepository

import com.ba.ex.mvvmsample.repository.RepositoryLoader
import com.ba.ex.mvvmsample.repository.data.Fruit

class LikeFruitsViewModel(
) : ViewModel() {
    private val likeFruitsRepository: ILikeFruitsRepository by lazy {
        RepositoryLoader.provideLikeFruitsRepository()
    }

    //模拟该数据是向数据库获取，数据库为唯一数据源
    var fruits: LiveData<List<Fruit>> = likeFruitsRepository.load()

    suspend fun deleteFromDataBase(fruit: Fruit) {
        fruits.value?.let {
            likeFruitsRepository.deleteFromDataBase(fruit)
        }
    }

    fun getFruit(id: String): Fruit? {
        return likeFruitsRepository.getFruit(id)
    }

    suspend fun deleteFromDataBase(position: Int) {
        fruits.value?.let {
            likeFruitsRepository.deleteFromDataBase(it[position])
        }
    }

    suspend fun saveToDataBase(fruits: List<Fruit>): List<Long> {
        return likeFruitsRepository.saveToDataBase(fruits)
    }
}