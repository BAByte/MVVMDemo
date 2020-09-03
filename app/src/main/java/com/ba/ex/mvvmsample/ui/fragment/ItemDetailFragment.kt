package com.ba.ex.mvvmsample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import com.ba.ex.mvvmsample.R
import com.ba.ex.mvvmsample.databinding.FragmentItemDetailBinding
import com.ba.ex.mvvmsample.repository.data.Fruit
import com.ba.ex.mvvmsample.ui.viewmodels.FruitsViewModel
import com.ba.ex.mvvmsample.ui.viewmodels.LikeFruitsViewModel
import com.ba.ex.mvvmsample.ui.views.LoadingDialog
import kotlinx.coroutines.*

//该类展示数据，ViewModel的实例和父Fragment共享
class ItemDetailFragment : BaseFragment<FragmentItemDetailBinding>() {
    private lateinit var viewModel: FruitsViewModel
    private var isLike = false
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
            binding.fruit?.let { fruit ->
                if (isLike) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_liked),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                likeFruit(fruit)
            }
        }
    }

    private fun likeFruit(fruit: Fruit) {
        launch(Dispatchers.Main) {
            showLoadingDialog()
            val result = withContext(Dispatchers.IO) {
                likeFruitsViewModel.saveToDataBase(listOf(fruit))
            }
            loadingDialog?.cancel()
            if (result.isNotEmpty()) {
                setFabIcon(fruit)
                Toast.makeText(
                    requireContext(), getString(R.string.like_success),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun subscribeUI() {
        viewModel.selectPosition.observe(viewLifecycleOwner) { position ->
            viewModel.fruits.value?.let { fruits ->
                if (fruits.isNotEmpty()) {
                    fruits[position].run {
                        binding.fruit = this
                        setFabIcon(this)
                    }
                }
            }
        }
    }

    private fun setFabIcon(fruit: Fruit) {
        likeFruitsViewModel.viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                return@withContext likeFruitsViewModel.getFruit(fruit.id)
            }

            if (result != null) {
                isLike = true
                binding.fabLike.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_liked))
            } else {
                isLike = false
                binding.fabLike.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_like))
            }
        }
    }

    private fun showLoadingDialog() {
        loadingDialog = LoadingDialog(requireContext(), getString(R.string.loading_dialog_liking))
            .apply {
                setCanceledOnTouchOutside(false)
                setCancelable(false)
            }
        loadingDialog?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
        loadingDialog?.cancel()
    }


}
