package com.ba.ex.mvvmsample.ui.recycler.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ba.ex.mvvmsample.R
import com.ba.ex.mvvmsample.databinding.ItemListBinding
import com.ba.ex.mvvmsample.repository.data.Fruit
import com.ba.ex.mvvmsample.ui.viewmodels.FruitsViewModel


class FruitListAdapter(
    private val fruitsViewModel: FruitsViewModel,
    private val adapterClickCallBack: (Int) -> Unit?
) : ListAdapter<Fruit, RecyclerView.ViewHolder>(FruitDiffCallback()) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        holder.binding.run {
            fruit = getItem(position)
            fruitsViewModel.selectPosition.value?.let {
                if (position != it) {
                    itemLayout.setBackgroundColor(root.context.getColor(R.color.not_select_bg))
                } else {
                    itemLayout.setBackgroundColor(root.context.getColor(R.color.select_bg))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_list, parent, false
            )
        )
    }


    inner class ViewHolder(
        val binding: ItemListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.itemLayout.setOnClickListener {
                adapterClickCallBack(adapterPosition)
            }
        }
    }

    private class FruitDiffCallback : DiffUtil.ItemCallback<Fruit>() {

        override fun areItemsTheSame(oldItem: Fruit, newItem: Fruit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Fruit, newItem: Fruit): Boolean {
            return false
        }
    }
}