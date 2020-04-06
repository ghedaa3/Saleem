package sa.ksu.gpa.saleem.exercise

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sa.ksu.gpa.saleem.util.NotificationUtil
import sa.ksu.gpa.saleem.util.PrefUtil

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtil.showTimerExpired(context)

        PrefUtil.setTimerState(InnerExercise.TimerState.Stopped, context)
        PrefUtil.setAlarmSetTime(0, context)
    }
}
