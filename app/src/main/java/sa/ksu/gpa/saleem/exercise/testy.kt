package sa.ksu.gpa.saleem.exercise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import sa.ksu.gpa.saleem.R
import java.util.HashMap

class testy : AppCompatActivity() {

    val exercise = HashMap<String, Any>()

    private lateinit var auth: FirebaseAuth

    private val TAG = "Sign up"

    // private var length by Delegates.notNull<Double>()

    private var goal =0

    private var caloriesBurned:Double = 0.0

    private lateinit var name :String
    private lateinit var gif:String

    //private var goal by Delegates.notNull<Int>()



    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testy)

        createExersise()
    }

    private fun createExersise() {
        val exercise = HashMap<String, Any>()



        exercise.put("name","warm up")
        exercise.put("duration",999)
    }
}
