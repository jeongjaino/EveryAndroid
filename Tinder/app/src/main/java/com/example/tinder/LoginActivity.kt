package com.example.tinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.tinder.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private val binding by lazy{ ActivityLoginBinding.inflate(layoutInflater)}

    private lateinit var auth: FirebaseAuth
    //private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

       // callbackManager = CallbackManager.Factory.create()

        auth = Firebase.auth

        initLoginButton()
        initSignUpButton()
        //initFacebookLoginButton()
        initEmailAndPasswordEditText()
    }

    private fun initLoginButton() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){ task->
                    if(task.isSuccessful){
                        handleSuccessLogin()
                    }
                    else{
                        Toast.makeText(this, "로그인에 실패하였습니다. 입력 정보를 확인해 주세요.",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun initSignUpButton(){
        binding.signUpButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){ task->
                    if(task.isSuccessful){
                        Toast.makeText(this, "회원가입 되었습니다.",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "회원가입이 실패하였습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    /*
    private fun initFacebookLoginButton(){
        binding.facebookLoginButton.setPermissions("email", "public_profile")
        binding.facebookLoginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult) {
                val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(this@LoginActivity) { task ->
                        if (task.isSuccessful) {
                            handleSuccessLogin()
                        } else {
                            Log.d("Tag", result.toString())
                            Toast.makeText(this@LoginActivity,
                                "페이스북 로그인에 실패하였습니다.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            override fun onCancel() {
            }

            override fun onError(error: FacebookException) {
            }
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }*/


    private fun initEmailAndPasswordEditText(){
        binding.passwordEditText.addTextChangedListener{ //text가 바뀔때마다 호출
            val enable = binding.emailEditText.text.isNotEmpty() and binding.passwordEditText.text.isNotEmpty()
            binding.loginButton.isEnabled = enable
            binding.signUpButton.isEnabled = enable
        }
        binding.emailEditText.addTextChangedListener{ //text가 바뀔때마다 호출
            val enable = binding.emailEditText.text.isNotEmpty() and binding.passwordEditText.text.isNotEmpty()
            binding.loginButton.isEnabled = enable
            binding.signUpButton.isEnabled = enable
        }
    }
    private fun handleSuccessLogin(){
        if(auth.currentUser == null){
            Toast.makeText(this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        val userId = auth.currentUser?.uid.orEmpty() //NULL일경우 EMPTY
        //realtime은 json형태로 저장, reference의 child식으로 가져옴
        val currentUserDB = Firebase.database.reference.child("Users").child(userId)
        val user = mutableMapOf<String, Any>()
        user["userId"] = userId
        currentUserDB.updateChildren(user)
        finish()
    }
}