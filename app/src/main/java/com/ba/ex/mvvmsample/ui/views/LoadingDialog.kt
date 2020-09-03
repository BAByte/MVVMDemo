package com.ba.ex.mvvmsample.ui.views


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window.FEATURE_NO_TITLE
import com.ba.ex.mvvmsample.databinding.LoadingDialogBinding


class LoadingDialog(context: Context, private val content: String) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(FEATURE_NO_TITLE)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        val binding = LoadingDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.content = content
    }
}