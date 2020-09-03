package com.ba.ex.mvvmsample.repository

import android.content.Context
import com.ba.ex.mvvmsample.repository.database.AppDatabase
import com.ba.ex.mvvmsample.repository.impl.*
import kotlin.properties.Delegates


object RepositoryLoader {
    private val fruitsRepository:IFruitsRepository by lazy {
        FruitsRepository(context)
    }

    private val likeFruitsRepository:ILikeFruitsRepository by lazy {
        LikeFruitsRepository(AppDatabase.getInstance(context).fruitDao())
    }

    private val logInfoRepository: ILogInfoRepository by lazy {
        LogInfoRepository()
    }

    private var context: Context by Delegates.notNull()

    //this method must call in application to ensure repository load successful
    @JvmStatic
    fun init(context: Context) {
        this.context = context.applicationContext
    }

    @JvmStatic
    fun provideFruitsRepository(): IFruitsRepository {
        return fruitsRepository
    }

    @JvmStatic
    fun provideLikeFruitsRepository(): ILikeFruitsRepository {
        return likeFruitsRepository
    }

    @JvmStatic
    fun provideLogInfoRepository(): ILogInfoRepository {
        return logInfoRepository
    }
}