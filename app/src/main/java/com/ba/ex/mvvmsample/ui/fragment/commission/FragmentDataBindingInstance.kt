package com.ba.ex.mvvmsample.ui.fragment.commission

import androidx.annotation.MainThread
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentDataBindingInstance<BINDING : ViewDataBinding> : ReadOnlyProperty<Fragment, BINDING> {
    private val lifecycleObserver = BindingLifecycleObserver()
    private var binding: BINDING? = null

    @MainThread
    override fun getValue(thisRef: Fragment, property: KProperty<*>): BINDING {
        binding?.let {
            return it
        }
        thisRef.viewLifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        return getDataBinding(thisRef)
    }

    private inner class BindingLifecycleObserver : DefaultLifecycleObserver {
        @MainThread
        override fun onDestroy(owner: LifecycleOwner) {
            owner.lifecycle.removeObserver(this)
            binding = null
        }
    }

    private fun getDataBinding(thisRef: Fragment): BINDING {
        DataBindingUtil.findBinding<BINDING>(thisRef.requireView()).apply {
            return this as BINDING
        }
    }
}