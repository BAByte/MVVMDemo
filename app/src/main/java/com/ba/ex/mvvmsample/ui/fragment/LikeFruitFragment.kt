package com.ba.ex.mvvmsample.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.ba.ex.mvvmsample.R
import com.ba.ex.mvvmsample.ui.recycler.adapter.LikeFruitsAdapter
import com.ba.ex.mvvmsample.databinding.FragmentLikeFruitsBinding
import com.ba.ex.mvvmsample.ui.fragment.viewmodels.LikeFruitsViewModel
import com.ba.ex.mvvmsample.ui.views.LoadingDialog
import com.orhanobut.logger.Logger
import kotlinx.coroutines.*

//该类展示数据，ViewModel的实例和父Fragment共享
class LikeFruitFragment : BaseFragment<FragmentLikeFruitsBinding>() {
    companion object {
        const val ARG_POSITION = "position"
    }

    private lateinit var likeFruitsViewModel: LikeFruitsViewModel
    private lateinit var adapter: LikeFruitsAdapter
    private var confirmDialog: Dialog? = null

    //加载框变量
    private var loadingDialog: LoadingDialog? = null

    override fun onBinding(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): FragmentLikeFruitsBinding = FragmentLikeFruitsBinding.inflate(
            inflater, container, false
    )

    override fun setupUI(binding: FragmentLikeFruitsBinding) {
        likeFruitsViewModel = ViewModelProvider(
                requireActivity(), ViewModelProvider.NewInstanceFactory()
        ).get(LikeFruitsViewModel::class.java)

        Logger.d("LikeFruitFragment : viewModel = $likeFruitsViewModel")

        activity?.title = getString(R.string.title_like)

        adapter =
                LikeFruitsAdapter(
                        adapterClickCallBack, adapterLongPressCallBack
                )

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.recyclerView.adapter = adapter
    }

    override fun subscribeUI() {
        likeFruitsViewModel.fruits.observe(viewLifecycleOwner) {
            Logger.d("LikeFruitsActivity observe = $it")
            adapter.submitList(it)
        }
    }

    private fun showDeleteDialog(position: Int) {
        confirmDialog = AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.delete_dialog_title))
                .setMessage(getString(R.string.delete_dialog_message))
                .setPositiveButton(getString(R.string.dialog_ok)) { dialog, which ->
                    launch {
                        showLoadingDialog()
                        withContext(Dispatchers.IO) {
                            likeFruitsViewModel.deleteFromDataBase(position)
                        }
                        loadingDialog?.cancel()
//                    refresh(position)
                    }
                }
                .setNegativeButton(getString(R.string.dialog_cancel)) { dialog, which ->
                }.create()

        confirmDialog?.show()
    }

    private fun showLoadingDialog() {
        loadingDialog = LoadingDialog(requireContext(), getString(R.string.loading_dialog_delete))
                .apply {
                    setCanceledOnTouchOutside(false)
                    setCancelable(false)
                }
        loadingDialog?.show()
    }

    private val adapterClickCallBack = { position: Int ->
        toDetailFragment(position)

    }

    private fun toDetailFragment(position: Int) {
        val bundle = Bundle().apply {
            putInt(
                    ARG_POSITION,
                    position
            )
        }
        Navigation.findNavController(binding.root)
                .navigate(R.id.action_like_fruit_fragment_to_like_fruit_detail_fragment, bundle)
    }

    private val adapterLongPressCallBack = { position: Int ->
        showDeleteDialog(position)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loadingDialog?.cancel()
        confirmDialog?.cancel()
    }
}
