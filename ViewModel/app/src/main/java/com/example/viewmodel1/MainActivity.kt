package com.example.viewmodel1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.viewmodel1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var myNumberViewModel: MyNumberViewModel

    private val binding by lazy{ ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        myNumberViewModel = ViewModelProvider(this).get(MyNumberViewModel::class.java)

        myNumberViewModel.currentValue.observe(this, Observer {
            Log.d("tag","MainActivity - MyNumberViewModel - currentValue 라이브 데이터 값 변경 : $it")
            binding.numberText.text = it.toString()

        })
        binding.subButton.setOnClickListener{
            val userInput = binding.userInput.text.toString().toInt()
            myNumberViewModel.updateValue(actionType = ActionType.MINUS, userInput)
        }
        binding.addButton.setOnClickListener{
            val userInput = binding.userInput.text.toString().toInt()
            myNumberViewModel.updateValue(actionType = ActionType.PLUS, userInput)
        }
    }

}
