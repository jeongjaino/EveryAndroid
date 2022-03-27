package com.example.carrotmarket


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.carrotmarket.databinding.ItemArticleBinding
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter: ListAdapter<ArticleModel, ArticleAdapter.ViewHolder>(diffUtil) {


    inner class ViewHolder(private val binding: ItemArticleBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ArticleModel){

            val format = SimpleDateFormat("MM월 dd일")
            val date = Date(model.createdAt)

            binding.titleTextView.text = model.title
            binding.dateTextView.text = format.format(date).toString()
            binding.priceTextView.text = model.price
            if(model.imageUrl.isNotEmpty()){
                Glide.with(binding.imageView)
                    .load(model.imageUrl)
                    .into(binding.imageView)
                }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<ArticleModel>(){
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return oldItem.createdAt == newItem.createdAt
            }

            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}