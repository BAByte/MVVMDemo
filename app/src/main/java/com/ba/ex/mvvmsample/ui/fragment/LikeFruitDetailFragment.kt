package com.ba.ex.mvvmsample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.ba.ex.mvvmsample.ui.fragment.LikeFruitFragment.Companion.ARG_POSITION
import com.ba.ex.mvvmsample.R
import com.ba.ex.mvvmsample.databinding.FragmentLikeFruitDetailBinding
import com.ba.ex.mvvmsample.ui.fragment.viewmodels.LikeFruitsViewModel
import com.orhanobut.logger.Logger

//该类展示数据，ViewModel的实例和父Fragment共享
class LikeFruitDetailFragment : BaseFragment<FragmentLikeFruitDetailBinding>() {
    companion object {
        const val ARG_FRUIT_URL = "item_fruit_url"
    }

    private lateinit var viewModel: LikeFruitsViewModel

    override fun onBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentLikeFruitDetailBinding = FragmentLikeFruitDetailBinding.inflate(
        inflater, container, false
    )

    override fun setupUI(binding: FragmentLikeFruitDetailBinding) {
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.NewInstanceFactory()
        ).get(LikeFruitsViewModel::class.java)
        Logger.d("LikeFruitDetailFragment : viewModel = $viewModel")

        val position = arguments?.getInt(ARG_POSITION)
        position?.let {
            viewModel.fruits.value?.let {
                binding.fruit = it[position]
            }
        }
        binding.fruitPic.setOnClickListener {
            val bundle = Bundle().apply {
                putString(
                    ARG_FRUIT_URL,
                    binding.fruit?.imageUrl
                )
            }

            NavHostFragment.findNavController(this)
                .navigate(
                    R.id.action_like_fruit_detail_fragment_to_fruit_pic_detail_fragment,
                    bundle
                )
        }
    }

    override fun subscribeUI() {
    }
}
