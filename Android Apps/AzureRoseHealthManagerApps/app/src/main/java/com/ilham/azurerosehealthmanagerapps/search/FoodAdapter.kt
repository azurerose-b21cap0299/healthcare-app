package com.ilham.azurerosehealthmanagerapps.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilham.azurerosehealthmanagerapps.core.domain.model.Food
import com.ilham.azurerosehealthmanagerapps.databinding.ItemRowFoodBinding

class FoodAdapter : RecyclerView.Adapter<FoodAdapter.SearchViewHolder>() {

    private var listFood = ArrayList<Food>()
    var onItemClick: ((Food) -> Unit)? = null

    fun setFood(food: List<Food>?) {
        if (food == null) return
        this.listFood.clear()
        this.listFood.addAll(food)
    }

    inner class SearchViewHolder(private val binding: ItemRowFoodBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(food: Food) {
            with(binding) {
                tvFoodName.text = food.label
                tvItemCalorie.text = food.kcalorie.toString()
                Glide.with(itemView.context)
                    .load(food.image)
                    .into(imgFood)
            }
        }
        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listFood[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodAdapter.SearchViewHolder {
        val itemsFoodBinding = ItemRowFoodBinding.inflate(LayoutInflater.from(parent.context))
        return SearchViewHolder(itemsFoodBinding)
    }

    override fun onBindViewHolder(holder: FoodAdapter.SearchViewHolder, position: Int) {
        val food = listFood[position]
        holder.bind(food)
    }

    override fun getItemCount(): Int {
        return listFood.size
    }
}