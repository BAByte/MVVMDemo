package com.ba.ex.mvvmsample.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ba.ex.mvvmsample.repository.ILogInfoRepository
import com.ba.ex.mvvmsample.repository.RepositoryLoader
import com.ba.ex.mvvmsample.repository.data.LogInfo

class LoggerInfoViewModel : ViewModel() {
    private val logInfoRepository: ILogInfoRepository by lazy {
        RepositoryLoader.provideLogInfoRepository()
    }

    val logInfo: LiveData<List<LogInfo>> = logInfoRepository.loadLogInfo()
}