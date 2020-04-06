package sa.ksu.gpa.saleem.exercise

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import sa.ksu.gpa.saleem.util.NotificationUtil
import sa.ksu.gpa.saleem.util.PrefUtil


class TimerNotificationActionReceiver : BroadcastReceiver() {
    // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action){
            AppConstants.ACTION_STOP -> {
                InnerExercise.removeAlarm(context)
                PrefUtil.setTimerState(InnerExercise.TimerState.Stopped, context)
                NotificationUtil.hideTimerNotification(context)
            }
            AppConstants.ACTION_PAUSE -> {
                var secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val alarmSetTime = PrefUtil.getAlarmSetTime(context)
                val nowSeconds = InnerExercise.nowSeconds

                secondsRemaining -= nowSeconds - alarmSetTime
                PrefUtil.setSecondsRemaining(secondsRemaining, context)

                InnerExercise.removeAlarm(context)
                PrefUtil.setTimerState(InnerExercise.TimerState.Paused, context)
                NotificationUtil.showTimerPaused(context)
            }
            AppConstants.ACTION_RESUME -> {
                val secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val wakeUpTime = InnerExercise.setAlarm(context, InnerExercise.nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(InnerExercise.TimerState.Running, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
            AppConstants.ACTION_START -> {
                val minutesRemaining = PrefUtil.getTimerLength(context)
                val secondsRemaining = minutesRemaining * 60L
                val wakeUpTime = InnerExercise.setAlarm(context, InnerExercise.nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(InnerExercise.TimerState.Running, context)
                PrefUtil.setSecondsRemaining(secondsRemaining, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
        }
    }
}
