package com.example.carrotmarket.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carrotmarket.ArticleAdapter
import com.example.carrotmarket.ArticleModel
import com.example.carrotmarket.databinding.FragmentHomeBinding
import com.example.carrotmarket.utils.Companion.DB_ARTICLES
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private val binding by lazy{ FragmentHomeBinding.inflate(layoutInflater)}
    private lateinit var articleAdapter: ArticleAdapter
    private val auth : FirebaseAuth by lazy{ Firebase.auth}
    private lateinit var articleDB :DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        articleAdapter = ArticleAdapter()
        articleAdapter.submitList(mutableListOf<ArticleModel>().apply {
            add(ArticleModel("0", "aaaa", 100000, "6000원",""))
            add(ArticleModel("0", "aaaa", 1200000, "62000원",""))
        })
        binding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.articleRecyclerView.adapter = articleAdapter

        articleDB = Firebase.database.reference.child(DB_ARTICLES)

        binding.addFloatingButton.setOnClickListener{
            context?.let{
                //if(auth.currentUser != null){
                    val intent = Intent(it, ArticleAddActivity::class.java)
                    startActivity(intent)
                //}
                //else{
                  //  Snackbar.make(requireView(), "로그인을 해야합니다.", Snackbar.LENGTH_LONG).show()
                //}
            }
        }

        return binding.root
    }
}