package com.example.hilttutorial

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import javax.inject.Named

class TestViewModel @ViewModelInject constructor(
    @Named("String 1") testString: String
) :ViewModel(){

    init{
        Log.d("tag", "Test String from ViewModel: $testString")
    }
}