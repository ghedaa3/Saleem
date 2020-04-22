package sa.ksu.gpa.saleem

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.thread


class ForegroundService : Service() {


    private lateinit var db: FirebaseFirestore
    var currentUser = ""

    private val CHANNEL_ID = "ForegroundService Kotlin"

    companion object {

        fun startService(context: Context, message: String) {
            val startIntent = Intent(context, ForegroundService::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, ForegroundService::class.java)
            context.stopService(stopIntent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //do heavy work on a background thread
        val input = intent?.getStringExtra("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service Kotlin Example")
            .setContentText(input)
//            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
        //stopSelf();
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        db = FirebaseFirestore.getInstance()
        currentUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        thread {
            while (true) {
//                Toast.makeText(applicationContext,"test any thine ",Toast.LENGTH_SHORT).show()
                Thread.sleep(60*60*1000)
               // Thread.sleep(10 * 1000)
                checkIfEat()

            }
        }

    }

    private fun checkIfEat() {
        var message: String = ""
        var calender = Calendar.getInstance()
        Log.e("checkIfEat","${calender.get(Calendar.HOUR)}");
        var hour = calender.get(Calendar.HOUR) ;
        val a: Int = calender.get(Calendar.AM_PM)
        if (a == Calendar.PM) {
            hour+=12
        }

        if (hour == 12) {
            db.collection("Foods")
                .whereEqualTo("user_id", currentUser)
                .whereEqualTo("type_of_food", "lunch")
                .whereEqualTo("date", getCurrentDate()).get().addOnSuccessListener {
                    if (it.isEmpty) {
                        getNotification("حان وقت الغداء")?.let {
                            scheduleNotification(
                                it,
                                0
                            )
                        }
                    }
                }


        }
        if (hour == 7) {
            db.collection("Foods")
                .whereEqualTo("user_id", currentUser)
                .whereEqualTo("type_of_food", "breakfast")
                .whereEqualTo("date", getCurrentDate()).get().addOnSuccessListener {
                    if (it.isEmpty) {
                        getNotification("حان وقت الفطور")?.let {
                            scheduleNotification(
                                it,
                                0
                            )
                        }
                    }
                }


        }
        if (hour == 20) {
            db.collection("Foods")
                .whereEqualTo("user_id", currentUser)
                .whereEqualTo("type_of_food", "dinner")
                .whereEqualTo("date", getCurrentDate()).get().addOnSuccessListener {
                    if (it.isEmpty) {
                        getNotification("حان وقت العشاء")?.let {
                            scheduleNotification(
                                it,
                                0
                            )
                        }
                    }
                }
        }

        if (calender.get(Calendar.HOUR) in 9..20) {


            getNotification("وقت شرب الماء")?.let {
                scheduleNotification(
                    it,
                    0
                )
            }
        }


    }

    fun getCurrentDate(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
            val formatted = current.format(formatter)
            return formatted
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val currentDate = sdf.format(Date())
        return "$currentDate"
    }

    private fun scheduleNotification(
        notification: Notification,
        delay: Int
    ) {
        val notificationIntent = Intent(this, AlarmReceiver::class.java)
        notificationIntent.putExtra("notification-id", 1)
        notificationIntent.putExtra("notification", notification)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val futureInMillis = SystemClock.elapsedRealtime() + delay
        val alarmManager =
            (getSystemService(Context.ALARM_SERVICE) as AlarmManager)
        alarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis] = pendingIntent
    }

    private fun getNotification(content: String): Notification? {
        val builder =
            NotificationCompat.Builder(
                this,
                "default"
            )
        builder.setContentTitle(getString(R.string.app_name))
        builder.setContentText(content)
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
        builder.setAutoCancel(true)
        builder.setChannelId("10002"+Random(4))
        return builder.build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }
}