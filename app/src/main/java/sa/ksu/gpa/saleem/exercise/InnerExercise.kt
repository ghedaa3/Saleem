package sa.ksu.gpa.saleem.exercise


import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.testdesign.*
import pl.droidsonroids.gif.GifImageView
import sa.ksu.gpa.saleem.R

import kotlin.properties.Delegates
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FieldValue
import kotlinx.android.synthetic.main.home_fragment.*
import sa.ksu.gpa.saleem.recipe.SharedRecipe.viewSharedRecipeActivity
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class InnerExercise : AppCompatActivity(),View.OnClickListener {

    lateinit var userUid: String
    var storage = FirebaseStorage.getInstance()
    var firebaseFirestore = FirebaseFirestore.getInstance()

    private val mAuth: FirebaseAuth? = null

    private val TAG = "exercise"


    /*   private lateinit var timer1: CountDownTimer
       private var t by Delegates.notNull<Long>()*/

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(sa.ksu.gpa.saleem.R.layout.activity_inner_exercise)

        val userUid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()

        var btn= findViewById(R.id.start) as Button

        val docRef = db.collection("exercise").document(userUid)





        var Exercisetitle = findViewById<View>(sa.ksu.gpa.saleem.R.id.title) as TextView

        var descrip = findViewById<View>(sa.ksu.gpa.saleem.R.id.description) as TextView


        var backImg = findViewById<View>(sa.ksu.gpa.saleem.R.id.back_button)


        var giff = findViewById<View>(sa.ksu.gpa.saleem.R.id.gif) as GifImageView



        btn.setOnClickListener{
            var calories = getIntent().getStringExtra("ExerciseCal")
            var title = getIntent().getStringExtra("ExerciseTitle")

            var  cal= calories.toString().toDouble()
            val userUid = FirebaseAuth.getInstance().currentUser!!.uid
            val db = FirebaseFirestore.getInstance()



            var date = getCurrentDate()

            val burntCalories = db.collection("users").document(userUid)

            burntCalories.update("burntCalories", FieldValue.increment(cal))
            // adding a list of excercises
            val docData = hashMapOf(
                "exerciseName" to title,
                "exerciseCalories" to cal,
                "exerciseType" to "FromExercise",
                "date" to getCurrentDate()

            )
            db.collection("users").document(userUid).collection("Exercises").document().set(docData)
                .addOnSuccessListener {
                    Log.d("main1","Added to collection exercise ")
                    Toast.makeText(this, "تم اضافة التمرين", Toast.LENGTH_LONG).show()

                }.addOnFailureListener {
                    Log.d("main1","not Added to collection"+it)
                    Toast.makeText(this, "حصل خطأ", Toast.LENGTH_LONG).show()


                }

            ubdateBurntCaloris()
            finish()

        }

        backImg.setOnClickListener(this)



        var id = getIntent().getStringExtra("ExerciseId")
        var calories = getIntent().getStringExtra("ExerciseCal")
        var title = getIntent().getStringExtra("ExerciseTitle")
        var duration = getIntent().getStringExtra("ExerciseDuration")
        var description=getIntent().getStringExtra("ExerciseDesc")

        descrip.setText(description)
        //   val num = duration.filter { it.isDigit() }
        //   val num1 = parseLong(num)
        Log.d("main1","not Added to collection"+duration)
        Exercisetitle.setText(title)

/*
        calT.setText(calories+" سعرة محروقة ")*/
        //cal.setText(calories)

        if (id == "exercise1") {


            Glide.with(this)
                .load(sa.ksu.gpa.saleem.R.drawable.push_up)
                .into(giff);



        }
        if (id == "exercise2") {

            Glide.with(this)
                .load(sa.ksu.gpa.saleem.R.drawable.set_up)
                .into(giff);



        }


        if (id == "exercise3") {


            Glide.with(this)
                .load(sa.ksu.gpa.saleem.R.drawable.warm_up)
                .into(giff);




        }
        if (id == "exercise4") {

            Glide.with(this)
                .load(sa.ksu.gpa.saleem.R.drawable.man_liftting_dumbells)
                .into(giff);



        }
/*        if (id == "exercise5") {

            Glide.with(this)
                .load(sa.ksu.gpa.saleem.R.drawable.man_jogging)
                .into(giff);




        }*/

        var gif = findViewById<View>(sa.ksu.gpa.saleem.R.id.recipe_image)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            sa.ksu.gpa.saleem.R.id.back_button -> {
                finish()
            }
            /*   sa.ksu.gpa.saleem.R.id.start ->{





               }*/

            else -> {
            }

        }
    }
    private fun ubdateBurntCaloris() {

        val userUid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userUid).get().addOnSuccessListener {
            if (it.get("burntCalories")!=0)
                burnt_calories_textview?.text=it.get("burntCalories").toString()
            else
                burnt_calories_textview?.text="0"

        }
    }

    fun getCurrentDate():String {
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
}

