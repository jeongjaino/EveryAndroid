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
import com.example.carrotmarket.chatlist.ChatList
import com.example.carrotmarket.databinding.FragmentHomeBinding
import com.example.carrotmarket.utils.Companion.CHILD_CHAT
import com.example.carrotmarket.utils.Companion.DB_ARTICLES
import com.example.carrotmarket.utils.Companion.DB_USERS
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private val binding by lazy{ FragmentHomeBinding.inflate(layoutInflater)}
    private lateinit var articleAdapter: ArticleAdapter
    private val articleList = mutableListOf<ArticleModel>()

    private val listener = object: ChildEventListener{
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val articleModel = snapshot.getValue(ArticleModel::class.java) //mapping
            articleModel ?: return //exception

            articleList.add(articleModel)
            articleAdapter.submitList(articleList)

        }
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) { }
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}
    }

    private val auth : FirebaseAuth by lazy{ Firebase.auth}
    private lateinit var articleDB :DatabaseReference
    private lateinit var userDB :DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        articleDB = Firebase.database.reference.child(DB_ARTICLES)
        userDB = Firebase.database.reference.child(DB_USERS)
        articleAdapter = ArticleAdapter(onItemClicked = { articleModel ->
            if(loginCheck()) {
                if(auth.currentUser!!.uid != articleModel.sellerId) {
                    val charRoom = ChatList(
                        buyerId = auth.currentUser!!.uid,
                        sellerId = articleModel.sellerId,
                        itemTitle = articleModel.title,
                        key = System.currentTimeMillis()
                    )
                    userDB.child(auth.currentUser!!.uid)
                        .child(CHILD_CHAT)
                        .push()
                        .setValue(charRoom)
                    userDB.child(articleModel.sellerId)
                        .child(CHILD_CHAT)
                        .push()
                        .setValue(charRoom)
                    Snackbar.make(requireView(), " 채팅방이 형성되었습니다." , Snackbar.LENGTH_SHORT).show()

                }else{
                    Snackbar.make(requireView(), "내가 올린 물건입니다." , Snackbar.LENGTH_SHORT).show()
                }
            }
        })
        articleList.clear()
        binding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.articleRecyclerView.adapter = articleAdapter

        //데이터 가져올때
        //addChildEventListener -> 이벤트 발생시 계속
        //addListenerForSingleValueEvent -> 즉시성 1회
        articleDB.addChildEventListener(listener)

        binding.addFloatingButton.setOnClickListener{
            context?.let{
                if(loginCheck()){
                    val intent = Intent(it, ArticleAddActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        return binding.root
    }
    private fun loginCheck(): Boolean{
        if(auth.currentUser != null){
            return true
        }else{
            Snackbar.make(requireView(), "로그인을 해야합니다.", Snackbar.LENGTH_LONG).show()
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        articleAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        articleDB.removeEventListener(listener)
    }
}