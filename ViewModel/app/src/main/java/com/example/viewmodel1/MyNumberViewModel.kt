package com.example.viewmodel1

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
enum class ActionType{
    PLUS, MINUS
}
class MyNumberViewModel : ViewModel() {

    //mutable - var
    //normal - val
    //값이 변경되는 애는 변수명앞에 '_'을 넣어줌 내부에서 사용

    //외부에서 가져올때 일반 리스트일경우 '_'을 넣지않음

    private val _currentValue = MutableLiveData<Int>()

    //변경되지 않는 데이터를 가져 올때 이름을 언더 스코어 없이 설정
    //공개적으로 가져오는 변수는 private이 아닌 퍼블릭으로 외부에서 가져오게 설정
    //값을 라이브데이터에서 가져오는게아니라 뷰모델에서 가져오게 설정
    val currentValue : LiveData<Int>
    get() = _currentValue

    //뷰모델 생성될때 초기값 설정

    init{
        Log.d("tag","NumberViewModel 생성자 호출")
        _currentValue.value = 0
    }
    fun updateValue(actionType: ActionType, input: Int){
        when(actionType){
            ActionType.PLUS ->
                _currentValue.value = _currentValue.value?.plus(input)
             ActionType.MINUS ->
                 _currentValue.value = _currentValue.value?.minus(input)
        }
    }
}