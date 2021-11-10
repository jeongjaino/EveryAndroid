package com.example.databasepush

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.databasepush.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

const val TOPIC = "/topics/myTopic"

class MainActivity : AppCompatActivity() {

    private val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}

    private val todoCollectionRef = Firebase.firestore.collection("todo")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        binding.button.setOnClickListener{
            val date = binding.dateText.text.toString()
            val message = binding.contentText.text.toString()
            if(date.isNotEmpty() && message.isNotEmpty()){
                PushNotification(
                    NotificationData(date,message),
                     TOPIC
                ).also{
                    sendNotification(it)
                    saveTodo(it)
                }
            }
        }
    }
    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch{
        try{
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful){
                Log.d("tag","success")
            }
            else{
                Log.d("tag","error")
            }
        }catch(e : Exception){
            Log.d("Tag", e.printStackTrace().toString())
        }
    }
    private fun saveTodo(todo: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try{
            todoCollectionRef.add(todo)
            Log.d("tag","success")
        } catch(e: Exception){
            Log.d("tag",e.message.toString())
        }
    }
}
