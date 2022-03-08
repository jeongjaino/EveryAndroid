package com.example.myapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private val binding by lazy{ ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initOnOffButton()
        initChangeAlarmTimeButton()
        val model = fetchDataFromSF()
        renderView(model)
        //뷰 초기화
        //데이터 가져오기
        //데이터 그리기
    }

    private fun initOnOffButton(){
        binding.onOffButton.setOnClickListener {
            //데이터 확인
            val model = it.tag as? AlarmDisplayModel ?: return@setOnClickListener //as는 형변환

            val newModel = saveAlarmModel(model.hour, model.minute, model.onOff.not())
            renderView(newModel)

            if (newModel.onOff){
                // alarm on
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, newModel.hour)
                    set(Calendar.MINUTE, newModel.minute)

                    if(before(Calendar.getInstance())){
                        add(Calendar.DATE, 1) //지금 이전의 시간일 경우 하루뒤로 설정
                    }
                }
                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    this, ALARM_REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT)
                /*
                alarmManager.setAndAllowWhileIdle()
                alarmManager.setExactAndAllowWhileIdle() //doze모드 영향을 받지않는 api
                 */
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }else{
                cancelAlarm()
            }
            //온오프에 따라 처리
        }
    }
    private fun initChangeAlarmTimeButton(){
        binding.changeAlarmTimeButton.setOnClickListener {

            val calendar = Calendar.getInstance()

            TimePickerDialog(this, { picker, hour, minute ->

                val model = saveAlarmModel(hour, minute, false)
                renderView(model)

                cancelAlarm()

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
                .show()
        }

    }
    private fun saveAlarmModel(hour: Int, minute: Int, onOff: Boolean): AlarmDisplayModel{

        val model = AlarmDisplayModel(
            hour = hour,
            minute = minute,
            onOff = onOff
        )
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()){
            putString(ALARM_KEY, model.makeDataForDB())
            putBoolean(ON_OFF_KEY, model.onOff)
            commit() //with 함수의경우 commit
        }
        return model
    }
    private fun fetchDataFromSF(): AlarmDisplayModel{
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE)

        val timeDBValue = sharedPreferences.getString(ALARM_KEY, "9:30") ?: "9:30"
        val onOFFDBValue = sharedPreferences.getBoolean(ON_OFF_KEY, false)
        val alarmData = timeDBValue.split(":")
        val alarmModel = AlarmDisplayModel(
            hour = alarmData[0].toInt(),
            minute = alarmData[1].toInt(),
            onOff = onOFFDBValue
        )
        //보정

        val pendingIntent = PendingIntent.getBroadcast(
            this, ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_NO_CREATE)
        //no create -> 있으면 안만듬
        if ((pendingIntent == null) and alarmModel.onOff){
            //알람 등록이 안되어있는데 데이터가 on 되어잇는 경우
            alarmModel.onOff = false
        } else if ((pendingIntent != null) and alarmModel.onOff.not()){
            //알람 등록은 되어있으나 데이터가 off 되어있는 경우
            pendingIntent.cancel()
        }

        return alarmModel
    }
    private fun renderView(model: AlarmDisplayModel){
        binding.ampmTextView.text = model.ampmText
        binding.timeTextView.text = model.timeText
        binding.onOffButton.text = model.onOffText
        binding.onOffButton.tag = model // tag는 store 기능을 위해 사용

    }
    private fun cancelAlarm(){
        val pendingIntent = PendingIntent.getBroadcast(
            this, ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_NO_CREATE)
        pendingIntent?.cancel()
    }
    companion object{
        private const val ALARM_KEY = "alarm"
        private const val ON_OFF_KEY = "onoff"
        private const val SHARED_PREFERENCE_KEY = "time"
        private const val ALARM_REQUEST_CODE = 1000
    }
}