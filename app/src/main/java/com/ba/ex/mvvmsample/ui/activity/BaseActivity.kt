package com.ba.ex.mvvmsample.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlin.properties.Delegates

abstract class BaseActivity<BINDING:ViewDataBinding> :AppCompatActivity(),CoroutineScope by MainScope() {

    private var binding:BINDING by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = onBinding(savedInstanceState)
        binding.lifecycleOwner = this@BaseActivity
        setupUI(binding)
    }

    protected abstract fun onBinding(savedInstanceState: Bundle?):BINDING

    protected abstract fun setupUI(binding:BINDING)

    fun getDataBinding():BINDING {
        return binding
    }

    override fun onDestroy() {
        super.onDestroy()
        this.cancel()
    }
}