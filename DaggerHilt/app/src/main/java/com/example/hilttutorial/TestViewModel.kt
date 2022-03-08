package com.example.hilttutorial

import android.util.Log
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import javax.inject.Named

class TestViewModel @Inject constructor(
    @Named("String2") testString2: String
): ViewModel() {
    init{
        Log.d("tag", "Test String from ViewModel: $testString2")
    }
}