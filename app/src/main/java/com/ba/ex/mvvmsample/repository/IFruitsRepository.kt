package com.ba.ex.mvvmsample.repository

import com.ba.ex.mvvmsample.repository.data.Fruit

/**
 * 存储区：加载数据，可从网络或者数据库，这里是从网络加载或删除
 * 生命周期关联着viewModel,该存储区并不能感知服务器数据的变化，
 * 只有用户主动获取
 */
interface IFruitsRepository {
    suspend fun load(): List<Fruit>
}