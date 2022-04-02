package com.example.notificationfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notificationfinder.databinding.ActivityMainBinding
import com.example.notificationfinder.model.LocationLatLngEntitiy
import com.example.notificationfinder.model.SearchResultEntity

class MainActivity : AppCompatActivity() {

    private val binding by lazy{ ActivityMainBinding.inflate(layoutInflater)}
    private lateinit var adapter :SearchRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapter()
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.searchRecyclerView.adapter = adapter
        setData()
        adapter.notifyDataSetChanged()
        setContentView(binding.root)
    }
    private fun initAdapter(){
        adapter = SearchRecyclerAdapter()
    }
    private fun setData(){
        val dataList = (0..10).map{
            SearchResultEntity(
                buildingName = "빌딩 $it",
                fullAddress = "주소 $it",
                locationLatLng = LocationLatLngEntitiy(
                    it.toFloat(),
                    it.toFloat()
                )
            )

        }
        adapter.setSearchResultList(dataList){
            Toast.makeText(this, "빌딩이름: ${it.buildingName} 주소: ${it.fullAddress}", Toast.LENGTH_SHORT).show()
        }
    }
}