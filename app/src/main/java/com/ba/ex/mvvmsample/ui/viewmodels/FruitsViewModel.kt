package com.ba.ex.mvvmsample.ui.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.ex.mvvmsample.repository.IFruitsRepository
import com.ba.ex.mvvmsample.repository.RepositoryLoader
import com.ba.ex.mvvmsample.repository.data.Fruit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FruitsViewModel : ViewModel() {
    private val fruitsRepository: IFruitsRepository by lazy {
        RepositoryLoader.provideFruitsRepository()
    }
    //数据都是从数据存储区获取，并且只相信存储区的数据
    val fruits: MutableLiveData<List<Fruit>> = MutableLiveData()

    /**
     *   列表中被选中的项,因为需要该数据生命周期不与fragment一致，
     *   navigation使用fragment跳转时不保存view
     */
    var selectPosition: MutableLiveData<Int> = MutableLiveData(0)

    /**
     * 从互联网加载水果列表，我的建议是就在 viewModelScope操作，
     * 在Repository尽量不要用到协程，保证生命周期和ViewModel 一致
     */
    fun load(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = fruitsRepository.load()
            fruits.postValue(result)
            resetSelectPosition()
        }
    }

    private fun resetSelectPosition() {
        selectPosition.postValue(0)
    }
}