package com.ba.ex.mvvmsample.ui.fragment.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MsgViewModel : ViewModel() {
    init {
        //生命周期与程序一致
        GlobalScope.launch(Dispatchers.IO) {
            // 一秒发一个消息
            (1..1000).forEach {
                msgNum.emit(it)
                delay(1000)
            }
        }
    }

    val msgNum: MutableSharedFlow<Int> = MutableSharedFlow(1)
}