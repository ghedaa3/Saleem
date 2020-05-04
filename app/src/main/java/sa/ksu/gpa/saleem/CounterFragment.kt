package sa.ksu.gpa.saleem


import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.android.synthetic.main.fragment_counter.*
import sa.ksu.gpa.saleem.stepsCounterUtils.StepDetector
import sa.ksu.gpa.saleem.stepsCounterUtils.StepListener
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class CounterFragment(position: Int, date: String) : Fragment(), SensorEventListener, StepListener {
    var position = position
    var date = date
    var isRunning = false
    lateinit var valcurrentuser:String

    private var simpleStepDetector: StepDetector? = null
    private var sensorManager: SensorManager? = null
    private var accel: Sensor? = null
    private val TEXT_NUM_STEPS = "Number of Steps: "

    companion object {
        @JvmStatic
        var numSteps = 0
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_counter, container, false)
    }

    override fun onResume() {
        super.onResume()
        isRunning = true
        var sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (sensor != null)
            sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        else {
            Toast.makeText(activity, "هذا الجوال لا يدعم مستشعر عداد الخطوات ", Toast.LENGTH_LONG)

        }

    }

    override fun onPause() {
        super.onPause()
        isRunning = false

    }

    private lateinit var db: FirebaseFirestore
    var totalcal = 0.0
    var consumerCal = 0.0
    var remainderCal = 0.0
    var history_Id = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (position == 1) {
            tv_title.setText("خطوة/ 5000")
        } else {
            tv_title.setText("سعرة متبقية")
        }
        valcurrentuser = FirebaseAuth.getInstance().currentUser?.uid!!
            Log.e("valcurrentuser",valcurrentuser);

        db = FirebaseFirestore.getInstance()
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accel = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        simpleStepDetector = StepDetector()
        simpleStepDetector!!.registerListener(this)


        db.collection("users")
            .document(valcurrentuser)//test user
            .addSnapshotListener { documentSnapshot: DocumentSnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                if (documentSnapshot != null) {
                    var neededcal = documentSnapshot?.get("needed cal")
                    totalcal = neededcal as Double

                    tv_main_number1.setText("${totalcal.toInt()}")
                    pb_counter1.progress = remainderCal.toInt()
                    pb_counter1.max = totalcal.toInt()

                    Log.e("hhhh", "${totalcal.toInt()}")
                    Log.e("wwww", "${consumerCal.toInt()}")
                }


            }

        db.collection("History")
            .whereEqualTo("date", date)
            .whereEqualTo("user_id", valcurrentuser)
            .get().addOnSuccessListener { documents ->
                if (documents.isEmpty && date == getCurrentDate()) {
                    val data = hashMapOf(
                        "cal" to remainderCal,
                        "date" to getCurrentDate(),
                        "steps_count" to numSteps,
                        "user_id" to valcurrentuser
                    )
                    db.collection("History").document().set(data).addOnSuccessListener {
                        for (document in documents) {
                            Log.d("hhh", "${document.id} => ${document.data}")
                            history_Id = document.id


                        }
                    }
                }
                for (document in documents) {
                    Log.d("hhh", "${document.id} => ${document.data}")
                    history_Id = document.id
                    remainderCal = document.get("cal") as Double
                    pb_counter1.progress = remainderCal.toInt()
//                    remainder_cal.setText("${remainderCal.toInt()}")
                    tv_main_number1.setText("${(totalcal - remainderCal).toInt()}")
                }
            }



        db.collection("History")
            .whereEqualTo("date", getCurrentDate())
            .whereEqualTo("user_id", valcurrentuser)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                for (doc in value!!) {
                    history_Id = doc.id
                    remainderCal = doc.get("cal") as Double
                    pb_counter1.progress = remainderCal.toInt()
//                    remainder_cal.setText("${remainderCal.toInt()}")
                    tv_main_number1.setText("${(totalcal - remainderCal).toInt()}")
                    if (position == 1) {
                        tv_main_number1.setText("${doc.getDouble("steps_count")?.toInt()}")
                        pb_counter1.progress = doc.getDouble("steps_count")?.toInt()!!
                        pb_counter1.max = 5000
                    }
                }


            }


    }

    fun getCurrentDate(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formatted = current.format(formatter)
            return formatted
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val currentDate = sdf.format(Date())
        return "$currentDate"
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {


        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector?.updateAccel(
                event.timestamp, event.values[0], event.values[1], event.values[2]
            );
        }
    }

    var isEnable = true;
    override fun step(timeNs: Long) {
        if (!isEnable || position ==0) {
            return
        }
        isEnable = false
        if (!isEnable) {
            val background = object : Thread() {
                override fun run() {
                    try {
                        Thread.sleep(200)
                        isEnable = true

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            background.start()
            numSteps += 1
            Log.e("Ay", "step === >" + numSteps)
            fun updateHistory() {
                val data = hashMapOf(
                    "cal" to remainderCal,
                    "date" to getCurrentDate(),
                    "steps_count" to numSteps,
                    "user_id" to valcurrentuser
                )
                if (!history_Id.equals(""))
                    db.collection("History").document(history_Id).update(data as Map<String, Any>);
            }
            if (position == 1) {
                pb_counter1.progress = numSteps
                pb_counter1.max = 5000

            }
            updateHistory()

        }
//        Toast.makeText(activity, "${timeNs}", Toast.LENGTH_LONG).show()
    }

}
