package com.example.notificationfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notificationfinder.databinding.ActivityMainBinding
import com.example.notificationfinder.model.LocationLatLngEntitiy
import com.example.notificationfinder.model.SearchResultEntity
import com.example.notificationfinder.utils.RetrofitUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val binding by lazy{ ActivityMainBinding.inflate(layoutInflater)}
    private lateinit var adapter :SearchRecyclerAdapter

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()

        initAdapter()
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.searchRecyclerView.adapter = adapter
        setData()
        adapter.notifyDataSetChanged()
        bindViews()
        setContentView(binding.root)
    }
    private fun initAdapter(){
        adapter = SearchRecyclerAdapter()
    }
    private fun bindViews() = with(binding){
        searchButton.setOnClickListener {
            searchKeyWord(searchTextView.text.toString())
        }
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
    private fun searchKeyWord(keyWordString: String){
        launch(coroutineContext) { //mainThread
            try{
                withContext(Dispatchers.IO){
                    val response = RetrofitUtil.apiService.getSearchLocation(
                        keyword = keyWordString
                    )
                    if(response.isSuccessful){
                        val body = response.body()
                        withContext(Dispatchers.Main){
                            Log.e("response",body.toString())
                        }
                    }
                    else{
                        Toast.makeText(this@MainActivity, "에러가 발생하였습니다. ", Toast.LENGTH_SHORT).show()
                    }
                }
            }catch(e:Exception){
                e.printStackTrace()
            }
        }
    }
}