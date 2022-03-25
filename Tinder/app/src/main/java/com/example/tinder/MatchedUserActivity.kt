package com.example.tinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tinder.databinding.ActivityMatchedUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MatchedUserActivity : AppCompatActivity() {

    private val binding by lazy{ ActivityMatchedUserBinding.inflate(layoutInflater)}

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var userDB: DatabaseReference

    private val adapter = MatchedUserAdapter()
    private val cardItem = mutableListOf<CardItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        userDB = Firebase.database.reference.child("Users")

        initMatchedUserRecyclerView()
        getMatchUsers()
    }

    private fun initMatchedUserRecyclerView(){

        val recyclerView = binding.matchedRecyclerView

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
    private fun getMatchUsers(){
        val matchedDB = userDB.child(getCurrentUserId()).child("likedBy").child("match")

        matchedDB.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapShot: DataSnapshot, previousChildName: String?) {
                if(snapShot.key?.isNotEmpty() == true){
                    getUserByKey(snapShot.key.orEmpty())
                }
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

            override fun onChildRemoved(p0: DataSnapshot) {}

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onCancelled(p0: DatabaseError) {}
        })
    }
    private fun getUserByKey(userId: String){
        userDB.child(userId).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                cardItem.add(CardItem(userId, snapshot.child("name").value.toString()))
                adapter.submitList(cardItem)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun getCurrentUserId(): String{
        if(auth.currentUser == null){
            Toast.makeText(this, "로그인이 되어있지 않습니다." ,Toast.LENGTH_SHORT).show()
            finish()
        }
        return auth.currentUser?.uid.orEmpty()
    }

}