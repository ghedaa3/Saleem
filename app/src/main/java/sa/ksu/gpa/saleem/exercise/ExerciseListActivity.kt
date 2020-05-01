package sa.ksu.gpa.saleem.exercise

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_exercise_list.*
import kotlinx.android.synthetic.main.activity_viewsharedrecipe.bottomNavigation
import kotlinx.android.synthetic.main.fragment_home_body.*
import kotlinx.android.synthetic.main.home_fragment.*
import sa.ksu.gpa.saleem.HomeFragment
import sa.ksu.gpa.saleem.R
import sa.ksu.gpa.saleem.SettingFragment
import sa.ksu.gpa.saleem.recipe.SharedRecipe.viewSharedRecipeActivity
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ExerciseListActivity : AppCompatActivity() {

    private  var  currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()
    private lateinit var db: FirebaseFirestore

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list)
        db = FirebaseFirestore.getInstance()

        var pageTitle = findViewById<View>(R.id.titleb) as TextView
        var pageTitle1 = findViewById<View>(R.id.title1) as TextView
        var pageTitle2 = findViewById<View>(R.id.title2) as TextView



        Log.d("exerActList1", "" + pageTitle.text.toString())

        Log.d("exerActList2", "" + pageTitle1.toString())




        bottomNavigation.selectedItemId = R.id.exercise
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    title = "الرئيسية"
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.profile -> {
                    title = "الاعدادات"
                    loadFragment(SettingFragment())

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.meals -> {
                    title = "وصفات"

                    val intent = Intent(this, viewSharedRecipeActivity::class.java)
                    startActivity(intent)

                    return@setOnNavigationItemSelectedListener true
                }

                R.id.exercise -> {
                    title = "التمارين"
                    val intent = Intent(this, ExerciseListActivity::class.java)
                    startActivity(intent)

                    return@setOnNavigationItemSelectedListener true
                }

            }
            false

        }


        findViewById<LinearLayout>(R.id.beginner).setOnClickListener {


            val intent = Intent(this, ExerciseActivity::class.java)

            intent.putExtra("title", titleb.text)
            startActivity(intent)
        }

        findViewById<LinearLayout>(R.id.intermediate).setOnClickListener {

            val intent = Intent(this, ExerciseActivity::class.java)
            intent.putExtra("title", title1.text)


            startActivity(intent)


        }

        findViewById<LinearLayout>(R.id.advance).setOnClickListener {


            val intent = Intent(this, ExerciseActivity::class.java)


            intent.putExtra("title", title2.text)

            startActivity(intent)


        }



    }


    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun onDeleteWater(view: View) {
        add_water.removeView(view.getParent() as View)
        db.collection("users").document(currentuser!!).collection("Water").document(getCurrentDate()).update("amountOfWater", FieldValue.increment(-1))
        updateWater()


    }
    private fun  updateWater(){
        var totalWaterAmount = 0
        db.collection("users").document(currentuser!!)
            .collection("Water").document(getCurrentDate()).get().addOnSuccessListener {
                if (it.exists()){
                    totalWaterAmount=it.get("amountOfWater").toString().toInt()

                }
                if(totalWaterAmount!=null)
                    waterAmountTV.text= totalWaterAmount.toString()
                else
                    waterAmountTV.text="0"
            }
    }
    fun getCurrentDate():String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formatted = current.format(formatter)
            return formatted
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val currentDate = sdf.format(Date())
        return "$currentDate"}


}


