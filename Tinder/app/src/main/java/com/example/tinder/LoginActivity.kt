package com.example.tinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.tinder.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private val binding by lazy{ ActivityLoginBinding.inflate(layoutInflater)}

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth

        initLoginButton()
        initSignUpButton()
        initEmailAndPasswordEditText()
    }

    private fun initLoginButton() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this){ task->
                    if(task.isSuccessful){
                        finish()
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
}