package com.ba.ex.mvvmsample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.NavHostFragment
import com.ba.ex.mvvmsample.R
import com.ba.ex.mvvmsample.databinding.FragmentItemDetailBinding
import com.ba.ex.mvvmsample.repository.data.Fruit
import com.ba.ex.mvvmsample.ui.fragment.viewmodels.FruitsViewModel
import com.ba.ex.mvvmsample.ui.fragment.viewmodels.LikeFruitsViewModel
import com.ba.ex.mvvmsample.ui.views.LoadingDialog
import com.orhanobut.logger.Logger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

//该类展示数据，ViewModel的实例和父Fragment共享
class ItemDetailFragment : BaseFragment<FragmentItemDetailBinding>() {
    private lateinit var viewModel: FruitsViewModel
    private val likeFruitsViewModel: LikeFruitsViewModel by viewModels()

    //加载框变量
    private var loadingDialog: LoadingDialog? = null

    companion object {
        const val ARG_FRUIT_URL = "item_fruit_url"
    }

    override fun onBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentItemDetailBinding = FragmentItemDetailBinding.inflate(
        inflater, container, false
    )

    override fun setupUI(binding: FragmentItemDetailBinding) {
        viewModel =
            ViewModelProvider(requireParentFragment()).get(FruitsViewModel::class.java)
        binding.fruitPic.setOnClickListener {
            val bundle = Bundle().apply {
                putString(
                    ARG_FRUIT_URL,
                    binding.fruit?.imageUrl
                )
            }

            NavHostFragment.findNavController(this)
                .navigate(
                    R.id.action_view_pager_fragment_to_plant_detail_fragment,
                    bundle
                )
        }

        binding.fabLike.setOnClickListener {
            launch {
                if (it.isSelected) {
                    unLikeFruit(binding.fruit)
                } else {
                    likeFruit(binding.fruit)
                }
                setFabIcon(binding.fruit)
            }
        }
    }

    private suspend fun likeFruit(fruit: Fruit?) {
        if (fruit == null) {
            return
        }

        showLoadingDialog(R.string.loading_dialog_liking)
        withContext(Dispatchers.IO) {
            likeFruitsViewModel.saveToDataBase(listOf(fruit))
        }
        loadingDialog?.cancel()
    }

    private suspend fun unLikeFruit(fruit: Fruit?) {
        Logger.d(">>> unLikeFruit = $fruit")
        if (fruit == null) {
            return
        }
        showLoadingDialog(R.string.loading_dialog_delete)
        withContext(Dispatchers.IO) {
            likeFruitsViewModel.deleteFromDataBase(fruit)
        }
        loadingDialog?.cancel()
    }

    override fun subscribeUI() {
        lifecycleScope.launch {
            viewModel.getSelectFruit().collectLatest {
                Logger.d(">>> ItemDetailFragment collectLatest")
                binding.fruit = it
                setFabIcon(it)
                resetImage(it)
            }
        }
    }

    private fun resetImage(fruit: Fruit?) {
        if (fruit == null) {
            binding.fruitPic.setImageBitmap(null)
        }
    }

    private suspend fun isLiked(fruit: Fruit?): Boolean {
        val result = withContext(Dispatchers.IO) {
            return@withContext likeFruitsViewModel.getFruit(fruit?.id)
        }
        return result != null
    }

    private fun setFabIcon(fruit: Fruit?) {
        likeFruitsViewModel.viewModelScope.launch(Dispatchers.Main) {
            binding.fabLike.isSelected = isLiked(fruit)
        }
    }

    private fun showLoadingDialog(txtId: Int) {
        loadingDialog = LoadingDialog(requireContext(), getString(txtId))
            .apply {
                setCanceledOnTouchOutside(false)
                setCancelable(false)
            }
        loadingDialog?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog?.cancel()
    }
}
