package com.ba.ex.mvvmsample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ba.ex.mvvmsample.databinding.FragmentFruitPicDetailBinding

class FruitPicDetailFragment : BaseFragment<FragmentFruitPicDetailBinding>() {

    override fun onBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentFruitPicDetailBinding = FragmentFruitPicDetailBinding.inflate(
        inflater, container, false
    )

    override fun setupUI(binding: FragmentFruitPicDetailBinding) {
        binding.url = arguments?.getString(ItemDetailFragment.ARG_FRUIT_URL)
    }

    override fun subscribeUI() {

    }
}