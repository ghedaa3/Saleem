package sa.ksu.gpa.saleem

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_notification.*
import java.util.*

class NotificationActivity : AppCompatActivity() {

    var count = 1
    lateinit var cal:Calendar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        count = getCounter()
        tv_count.setText("$count")
        back_button.setOnClickListener { onBackPressed() }
        switchw.isChecked = getIsNotification()
        iv_add.setOnClickListener{
            if (count < 6){
                count++
                addCounterToPref(count)
                tv_count.setText("$count")
            }

        }
        iv_minus.setOnClickListener{
            if (count > 1){
                count--
                addCounterToPref(count)
                tv_count.setText("$count")
            }
        }

        switchw.setOnCheckedChangeListener { buttonView, isChecked ->
            run {
                Log.e("switchw", "switchw ==> $isChecked")
                addIsNotificationOnToPref(isChecked)
                if (isChecked){
                    ForegroundService.startService(this, "Foreground Service is running...")

                }else{
                    ForegroundService.stopService(this)
                }
            }
       // }

//        tv_time.setOnClickListener{
//            showTime()
        }
    }

    fun addCounterToPref(counter:Int){
        val sharedPref =getSharedPreferences("saleem_app_shared",Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putInt("counter", counter)
            commit()
        }
    }
    fun getIsNotification():Boolean{
        val sharedPref = getSharedPreferences("saleem_app_shared",Context.MODE_PRIVATE)
        val highScore = sharedPref.getBoolean("isNotificationOn", true)
        return highScore

    }
    fun getCounter():Int{
        val sharedPref = getSharedPreferences("saleem_app_shared",Context.MODE_PRIVATE)
        val highScore = sharedPref.getInt("counter", 1)
        return highScore

    }
    fun addIsNotificationOnToPref(isNotificationOn:Boolean){
        val sharedPref =getSharedPreferences("saleem_app_shared",Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putBoolean("isNotificationOn", isNotificationOn)
            commit()

        }


    }


    private fun showTime() {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(this@NotificationActivity,
            OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
//                tv_time.setText("$selectedHour:$selectedMinute")
                cal = Calendar.getInstance()
                cal.set(Calendar.HOUR, selectedHour)
                cal.set(Calendar.MINUTE, selectedMinute)
            }, hour, minute, true
        ) //Yes 24 hour time
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }
}
