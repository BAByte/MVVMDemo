package com.ba.ex.mvvmsample.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ba.ex.mvvmsample.R
import com.ba.ex.mvvmsample.ui.recycler.adapter.FruitListAdapter
import com.ba.ex.mvvmsample.databinding.FragmentHomeBinding
import com.ba.ex.mvvmsample.ui.viewmodels.FruitsViewModel
import com.ba.ex.mvvmsample.ui.viewmodels.LoggerInfoViewModel
import com.ba.ex.mvvmsample.ui.views.LoadingDialog
import com.ba.ex.mvvmsample.log.workers.ArtificialUploadLogWorker
import com.orhanobut.logger.Logger
import kotlinx.coroutines.*

/**
 * 水果列表和水果详情的展示
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private lateinit var adapter: FruitListAdapter
    private var confirmDialog: Dialog? = null //删除dialog
    private var loadingDialog: LoadingDialog? = null     //加载dialog

    private val fruitsViewModel: FruitsViewModel by viewModels()

    private val loggerInfoViewModel: LoggerInfoViewModel by viewModels()

    private val adapterClickCallBack = { position: Int ->
        fruitsViewModel.selectPosition.value?.let {
            fruitsViewModel.selectPosition.value = position
            adapter.notifyItemChanged(it)
            adapter.notifyItemChanged(position)
        }
    }

    override fun onBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding {
        fruitsViewModel.load(requireContext())
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun setupUI(binding: FragmentHomeBinding) {
        activity?.title = getString(R.string.main_activity_title)
        binding.swipeRefreshLayout.isRefreshing = true
        initList()
        setHasOptionsMenu(true)
        binding.swipeRefreshLayout.setOnRefreshListener {
            fruitsViewModel.load(requireContext())
        }
    }

    private fun initList() {
        adapter =
            FruitListAdapter(
                fruitsViewModel,
                adapterClickCallBack
            )
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun subscribeUI() {
        //数据改变
        fruitsViewModel.fruits.observe(viewLifecycleOwner) { result ->
            Logger.d("observe : $result")
            adapter.submitList(result)
            binding.swipeRefreshLayout.isRefreshing = false
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
        cancel()
        loadingDialog?.cancel()
        confirmDialog?.cancel()
    }

}