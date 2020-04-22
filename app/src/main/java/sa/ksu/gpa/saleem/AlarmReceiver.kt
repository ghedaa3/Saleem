package sa.ksu.gpa.saleem


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {

    var NOTIFICATION_ID = "notification-id"
    var NOTIFICATION = "notification"
    var NOTIFICATION_CHANNEL_ID = "10001"

    override fun onReceive(
        context: Context,
        intent: Intent
    ) { // For our recurring task, we'll just display a message
        //Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show()
        Log.e("AlarmReceiver","I'm running")
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification =
            intent.getParcelableExtra<Notification>(NOTIFICATION)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "NOTIFICATION_CHANNEL_NAME",
                importance
            )
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val id = intent.getIntExtra(
            NOTIFICATION_ID,
            0
        )
        assert(notificationManager != null)
        if (notificationManager != null){
            Log.e("notificationManager","notificationManager ==> != null")
        }
        if (notification != null){
            Log.e("notification","notification ==> != null")
        }
        notificationManager.notify(id,
            notification)

    }


}