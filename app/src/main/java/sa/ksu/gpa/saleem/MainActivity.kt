package sa.ksu.gpa.saleem

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialOverlayLayout
import com.leinardi.android.speeddial.SpeedDialView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_excercise_dialog.view.*
import kotlinx.android.synthetic.main.advice_dialog.view.*
import kotlinx.android.synthetic.main.fragment_home_body.*
import kotlinx.android.synthetic.main.home_fragment.*
import sa.ksu.gpa.saleem.Timer.TimerSettings
import sa.ksu.gpa.saleem.exercise.ExerciseActivity
import sa.ksu.gpa.saleem.exercise.ExerciseListActivity
import sa.ksu.gpa.saleem.recipe.ShareRecipeFirst
import sa.ksu.gpa.saleem.recipe.SharedRecipe.viewSharedRecipeActivity
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var db:FirebaseFirestore
    val currentuser = FirebaseAuth.getInstance().currentUser?.uid




    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db= FirebaseFirestore.getInstance()

        if(getIsNotification()){
            ForegroundService.startService(this, "Foreground Service is running...")
        }

        loadFragment(HomeFragment())
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home-> {
                    title="الرئيسية"
                    loadFragment(HomeFragment())
                   // speedDialView.visibility = View.VISIBLE
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.profile-> {
                    title="الاعدادات"
                    loadFragment(SettingFragment())
                  //  speedDialView.visibility = View.GONE
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.meals-> {
                    title = "وصفات"

                    val intent = Intent(this@MainActivity, viewSharedRecipeActivity::class.java)
                    startActivity(intent)

                    return@setOnNavigationItemSelectedListener true
                }

                R.id.exercise-> {
                    title = "التمارين"

                    val intent = Intent(this@MainActivity, ExerciseListActivity::class.java)
                    startActivity(intent)

                    return@setOnNavigationItemSelectedListener true
                }

            }
            false

        }

    }




    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
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
        return "$currentDate"
    }



    fun getIsNotification():Boolean{
        val sharedPref = getSharedPreferences("saleem_app_shared",Context.MODE_PRIVATE)
        val highScore = sharedPref.getBoolean("isNotificationOn", true)
        return highScore

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
    }




