package com.ba.ex.mvvmsample.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ba.ex.mvvmsample.R
import com.ba.ex.mvvmsample.databinding.FragmentHomeBinding
import com.ba.ex.mvvmsample.log.workers.ArtificialUploadLogWorker
import com.ba.ex.mvvmsample.ui.recycler.adapter.FruitListAdapter
import com.ba.ex.mvvmsample.ui.fragment.viewmodels.FruitsViewModel
import com.ba.ex.mvvmsample.ui.fragment.viewmodels.LoggerInfoViewModel
import com.ba.ex.mvvmsample.ui.views.LoadingDialog
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 水果列表和水果详情的展示
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private lateinit var adapter: FruitListAdapter
    private var confirmDialog: Dialog? = null //删除dialog
    private var loadingDialog: LoadingDialog? = null     //加载dialog

    private val fruitsViewModel: FruitsViewModel by viewModels()
    private val loggerInfoViewModel: LoggerInfoViewModel by viewModels()

    override fun onBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun setupUI(binding: FragmentHomeBinding) {
        activity?.title = getString(R.string.main_activity_title)
        initList()
        setHasOptionsMenu(true)
        binding.swipeRefreshLayout.setOnRefreshListener { load() }
        load()
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
        lifecycleScope.launch {
            //只有在数据有变化时，且生命周期处与STARTED后才能受到
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                fruitsViewModel.fruitsFlow.collectLatest {
                    Logger.d("HomeFragment observe")
                    adapter.submitList(it) {
                        smooth2Top()
                    }
                }
            }
        }

        lifecycleScope.launch {
            //只有在数据有变化时，且生命周期处与STARTED后才能受到
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.selectPositionFlow.collectLatest {
                    Logger.d("HomeFragment collectLatest position = $it")
                    fruitsViewModel.setSelectFruit(it)
                }
            }
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_like -> {
                NavHostFragment.findNavController(this)
                    .navigate(R.id.home_fragment_to_like_fruit_fragment)
                true
            }
            R.id.menu_upload_logger -> {
                showUpLoadLogDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showUpLoadLogDialog() {
        var num = 0
        loggerInfoViewModel.logInfo.value?.let {
            num = it.size
        }
        //日志数量
        confirmDialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.upload_log_dialog_title))
            .setMessage("${getString(R.string.upload_log_dialog_message)} : $num")
            .setPositiveButton(getString(R.string.dialog_ok)) { dialog, which ->
                val request = OneTimeWorkRequestBuilder<ArtificialUploadLogWorker>().build()
                WorkManager.getInstance(requireContext()).enqueue(request)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_upload_loginfo),
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton(getString(R.string.dialog_cancel)) { dialog, which ->
            }.create().apply { show() }
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog?.cancel()
        confirmDialog?.cancel()
    }

}