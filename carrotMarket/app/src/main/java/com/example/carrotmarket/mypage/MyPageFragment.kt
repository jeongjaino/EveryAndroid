package com.example.carrotmarket.mypage

import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.carrotmarket.R
import com.example.carrotmarket.databinding.FragmentMyPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyPageFragment : Fragment() {

    private val binding by lazy{ FragmentMyPageBinding.inflate(layoutInflater)}
    private val auth: FirebaseAuth by lazy{ Firebase.auth}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        if(auth.currentUser == null) {
            buttonLock()
        }
        binding.signInOutButton.setOnClickListener {
            if(auth.currentUser == null){
                uiUpdate(true, isSignIn = true)
            }else{
                uiUpdate(false, isSignIn = false)
            }
        }

        binding.signUpButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener{ task ->
                        if(task.isSuccessful){
                            Toast.makeText(context, "회원가입에 성공하였습니다. 로그인 버튼을 눌러주세요.",
                                Toast.LENGTH_SHORT).show()
                        } else{
                            Toast.makeText(context, "이미 존재하는 이메일입니다. 다시 시도 해주세요.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser == null){
            uiUpdate(true, isSignIn = false)
        }else{
            successSignIn()
        }
    }

    private fun buttonLock(){
        binding.passwordEditText.addTextChangedListener{
            val enable = binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
            binding.signUpButton.isEnabled = enable
            binding.signInOutButton.isEnabled = enable
        }
        binding.emailEditText.addTextChangedListener{
            val enable = binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
            binding.signUpButton.isEnabled = enable
            binding.signInOutButton.isEnabled = enable
        }
    }
    private fun uiUpdate(currentLoginUser: Boolean, isSignIn: Boolean){
       if(!currentLoginUser){
           auth.signOut()
           binding.emailEditText.text.clear()
           binding.emailEditText.isEnabled = true
           binding.passwordEditText.text.clear()
           binding.passwordEditText.isEnabled = true

           binding.signUpButton.isEnabled = true
           binding.signInOutButton.isEnabled = true
           binding.signInOutButton.text = "로그인"
       } else{
           if(isSignIn) {
               val email = binding.emailEditText.text.toString()
               val password = binding.passwordEditText.text.toString()

               auth.signInWithEmailAndPassword(email, password)
                   .addOnCompleteListener(requireActivity()) { task ->
                       if (task.isSuccessful) {
                           successSignIn()
                       } else {
                           Toast.makeText(context, "로그인에 실패하였습니다. 이메일 혹은 비밀번호를 확인해주세요.",
                               Toast.LENGTH_SHORT).show()
                       }
                   }
           }
       }
    }
    private fun successSignIn(){
        if(auth.currentUser == null){
            Toast.makeText(context, "로그인에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        binding.emailEditText.isEnabled = false
        binding.passwordEditText.isEnabled = false
        binding.signUpButton.isEnabled = false
        binding.signInOutButton.text = "로그아웃"
    }
}