package com.ba.ex.mvvmsample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ba.ex.mvvmsample.databinding.FragmentHomeBinding
import com.ba.ex.mvvmsample.ui.fragment.viewmodels.FruitsViewModel
import com.ba.ex.mvvmsample.ui.recycler.adapter.FruitListAdapter
import com.ba.ex.mvvmsample.ui.views.LoadingDialog
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * 水果列表和水果详情的展示
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private lateinit var adapter: FruitListAdapter
    private var loadingDialog: LoadingDialog? = null     //加载dialog
    private val fruitsViewModel: FruitsViewModel by viewModels()

    override fun onBinding(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun setupUI(binding: FragmentHomeBinding) {
        initList()
        setHasOptionsMenu(true)
        binding.swipeRefreshLayout.setOnRefreshListener { load() }
        load()
        Logger.d(">>> HomeFragment setupUI")
    }

    fun load() {
        lifecycleScope.launch {
            showLoading()
            fruitsViewModel.load()
            dismissLoading()
        }
    }

    private fun showLoading() {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.swipeRefreshLayout.isRefreshing = true
        }
    }

    private fun dismissLoading() {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initList() {
        adapter = FruitListAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun subscribeUI() {
        //使用 launchIn 替换 collect 我们可以在单独的协程中启动流的收集，这样就可以立即继续进一步执行代码
        lifecycleScope.launch {
            //onEach相当于回调，每次数据到了后会执行onEach
            fruitsViewModel.fruitsFlow.onEach {
                Logger.d("HomeFragment observe")
                adapter.submitList(it) {
                    smooth2Top()
                    Logger.d("HomeFragment submitList")
                }
            }.launchIn(this)

            adapter.selectPositionFlow.onEach {
                Logger.d("HomeFragment collectLatest position = $it")
                fruitsViewModel.setSelectFruit(it)
            }.launchIn(this)
        }
    }

    private fun smooth2Top() {
        lifecycleScope.launch {
            try {
                binding.recyclerView.smoothScrollToPosition(0)
                adapter.resetSelectPosition()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        super.onDestroy()
        loadingDialog?.cancel()
        loadingDialog = null
    }
}