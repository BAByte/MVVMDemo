package com.ba.ex.mvvmsample.ui.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ba.ex.mvvmsample.R
import com.ba.ex.mvvmsample.databinding.ItemLikeFruitBinding
import com.ba.ex.mvvmsample.repository.data.Fruit

class LikeFruitsAdapter(
    private val adapterClickCallBack: (position: Int) -> Unit,
    private val adapterLongPressCallBack: (position: Int) -> Unit
) : ListAdapter<Fruit, RecyclerView.ViewHolder>(FruitDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_like_fruit, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        holder.binding.fruit = getItem(position)
    }

    inner class ViewHolder(
        val binding: ItemLikeFruitBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.itemLayout.setOnClickListener {
                adapterClickCallBack(adapterPosition)
            }

            binding.itemLayout.setOnLongClickListener {
                adapterLongPressCallBack(adapterPosition)
                true
            }
        }
    }

    private class FruitDiffCallback : DiffUtil.ItemCallback<Fruit>() {

        override fun areItemsTheSame(oldItem: Fruit, newItem: Fruit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Fruit, newItem: Fruit): Boolean {
            return oldItem == newItem
        }
    }
}