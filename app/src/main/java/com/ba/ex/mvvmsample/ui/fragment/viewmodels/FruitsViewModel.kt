package com.ba.ex.mvvmsample.ui.fragment.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.ex.mvvmsample.repository.IFruitsRepository
import com.ba.ex.mvvmsample.repository.RepositoryLoader
import com.ba.ex.mvvmsample.repository.data.Fruit
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class FruitsViewModel : ViewModel() {
    private val fruitsRepository: IFruitsRepository by lazy {
        RepositoryLoader.provideFruitsRepository()
    }

    //SharedFlow,热流，replay设为1：当新的订阅者到来时，将最后一个数据发送
    private val selectFruit: MutableSharedFlow<Fruit?> = MutableSharedFlow(1)

    //StateFlow,热流，后面的订阅者无法收到前面的消息，并且只有数据更新了才会通知订阅者，可以通过value获取流中的状态值
    val fruitsFlow: StateFlow<List<Fruit>> = fruitsRepository.getFruitsFlow()

    /**
     * 从互联网加载水果列表，我的建议是就在 viewModelScope操作，
     * 在Repository尽量不要用到协程，保证生命周期和ViewModel 一致
     */
    suspend fun load() = withContext(viewModelScope.coroutineContext) {
        fruitsRepository.load()
    }

    fun getSelectFruit(): SharedFlow<Fruit?> {
        return selectFruit
    }

    suspend fun setSelectFruit(position: Int) {
        val fruits = fruitsFlow.value
        if (fruits.isEmpty() || position < 0 || position >= fruits.size) {
            selectFruit.emit(null)
            return
        }
        selectFruit.emit(fruits[position])
    }
}