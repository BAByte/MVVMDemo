package com.ba.ex.mvvmsample.ui.recycler.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ba.ex.mvvmsample.R
import com.ba.ex.mvvmsample.databinding.ItemListBinding
import com.ba.ex.mvvmsample.repository.data.Fruit
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class FruitListAdapter : ListAdapter<Fruit, RecyclerView.ViewHolder>(FruitDiffCallback()) {
    private var selectPosition = 0
    val selectPositionFlow: MutableSharedFlow<Int> = MutableSharedFlow(1)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as ViewHolder
        holder.binding.run {
            fruit = getItem(position)
            Logger.d(">>> onBindViewHolder position = ${holder.absoluteAdapterPosition}")
            Logger.d(">>> onBindViewHolder selectPosition = $selectPosition")
            itemLayout.isSelected = position == selectPosition
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
                setSelectAndNotify(it.findViewTreeLifecycleOwner(), absoluteAdapterPosition)
            }
        }
    }

    private fun setSelectAndNotify(lifecycleOwner: LifecycleOwner?, position: Int) {
        val lastPosition = selectPosition
        selectPosition = position
        notifyItemChanged(lastPosition)
        notifyItemChanged(selectPosition)
        lifecycleOwner?.lifecycleScope?.launch {
            selectPositionFlow.emit(selectPosition)
        }
    }

    private class FruitDiffCallback : DiffUtil.ItemCallback<Fruit>() {

        //布局是否需要改变
        override fun areItemsTheSame(oldItem: Fruit, newItem: Fruit): Boolean {
            return oldItem == newItem
        }

        /**
         * 布局内容是否改变,
         * 这里不同于普通的列表，它需要更新select的视图,有viewHolder的复用与位置的变化
         * 所以不好控制局部刷新，还是全局更新状态吧
         */
        override fun areContentsTheSame(oldItem: Fruit, newItem: Fruit): Boolean {
            return false
        }
    }

    suspend fun resetSelectPosition() {
        selectPositionFlow.emit(0)
        selectPosition = 0
    }
}