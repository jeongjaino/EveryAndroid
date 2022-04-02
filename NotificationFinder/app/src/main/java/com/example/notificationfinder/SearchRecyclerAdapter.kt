package com.example.notificationfinder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notificationfinder.databinding.ViewholderSearchResultItemBinding
import com.example.notificationfinder.model.SearchResultEntity

class SearchRecyclerAdapter: RecyclerView.Adapter<SearchRecyclerAdapter.SearchResultItemViewHolder>() {

    private var searchResultList : List<SearchResultEntity> = listOf()
    lateinit var searchResultClickListener : (SearchResultEntity) -> Unit

    class SearchResultItemViewHolder(
        private val binding: ViewholderSearchResultItemBinding,
        val searchResultClickListener: (SearchResultEntity) -> Unit) :RecyclerView.ViewHolder(binding.root){
            fun bindItem(data: SearchResultEntity){
                binding.titleTextView.text = data.buildingName
                binding.subTitleTextView.text = data.fullAddress
            }
            fun bindViews(data: SearchResultEntity){
                binding.root.setOnClickListener{
                    searchResultClickListener(data)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultItemViewHolder {
        val view = ViewholderSearchResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultItemViewHolder(view, searchResultClickListener)
    }

    override fun onBindViewHolder(holder: SearchResultItemViewHolder, position: Int) {
        holder.bindItem(searchResultList[position])
        holder.bindViews(searchResultList[position])
    }

    override fun getItemCount(): Int {
        return searchResultList.size
    }

    fun setSearchResultList(searchResultList: List<SearchResultEntity>, searchResultClickListener: (SearchResultEntity) -> Unit){
        this.searchResultClickListener = searchResultClickListener
        this.searchResultList = searchResultList
    }
}
