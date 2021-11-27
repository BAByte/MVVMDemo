package com.ba.ex.mvvmsample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ba.ex.mvvmsample.ui.fragment.commission.FragmentDataBindingInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class BaseFragment<BINDING : ViewDataBinding> :
        Fragment(), CoroutineScope by MainScope() {
    //防止内存泄漏
    val binding: BINDING by FragmentDataBindingInstance()

    final override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        onBinding(inflater, container, savedInstanceState)?.apply {
            lifecycleOwner = this@BaseFragment
            return root
        }
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI(binding)
        subscribeUI()
    }

    protected abstract fun onBinding(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): BINDING?

    protected abstract fun setupUI(binding: BINDING)

    protected abstract fun subscribeUI()

    override fun onDestroy() {
        super.onDestroy()
        this.cancel()
    }
}