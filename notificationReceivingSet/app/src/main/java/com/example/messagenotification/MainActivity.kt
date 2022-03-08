package com.example.messagenotification

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.messagenotification.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private val binding by lazy{ ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initFirebase()
        updateResult()
    }
    private fun initFirebase(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener{ task->
                if(task.isSuccessful){
                    binding.firebaseToken.text = task.result
                    Log.d("tag", task.result.toString())
                }
            }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        updateResult(true) //이미 앱이 실행되고 있을 경우
        setIntent(intent) // notification으로 들어온 경우 intent를 가져오기
    }
    private fun updateResult(isNewIntent: Boolean = false){
        binding.resultText.text = (intent.getStringExtra("NotificationType") ?: "앱 런처") +
        if(isNewIntent){
            "(으)로 갱신하엿습니다."
        } else{
            "(으)로 실행하였습니다."
        }
    }
}