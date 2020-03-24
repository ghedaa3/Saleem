package sa.ksu.gpa.saleem.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.fragment_item_list_dialog_item.*
import kotlinx.android.synthetic.main.reset_password.*
import sa.ksu.gpa.saleem.register.registerFourActivity
import com.google.firebase.auth.FirebaseUser

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.R
import android.app.Dialog
import android.content.Intent

import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import sa.ksu.gpa.saleem.loginn
import sa.ksu.gpa.saleem.register.registerOneActivity
import java.math.RoundingMode
import java.text.DecimalFormat


class Profile : AppCompatActivity() {

    private val TAG = "DocSnippets"
    private lateinit var auth: FirebaseAuth
    private lateinit var neededCall:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(sa.ksu.gpa.saleem.R.layout.activity_profile)

        val toolbar = findViewById<View>(sa.ksu.gpa.saleem.R.id.toolbar)
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(false)


        val userUid = FirebaseAuth.getInstance().currentUser!!.uid


        Log.d(registerFourActivity.TAG, "kkkkkkkkkkkkkkkkkkkkk"+userUid)


        val db = FirebaseFirestore.getInstance()

        val docRef = db.collection("users").document(userUid)

        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d(registerFourActivity.TAG, "${document.id} => ${document.data}")

                val name = document.get("name").toString()
                val email = document.get("email").toString()
                val wight = document.get("weight").toString()
                val hight = document.get("height").toString()
                //  val neededCal = document.get("needed cal").toString().toDouble()
                val gender = document.get("gender").toString()
                val age = document.get("age").toString()

                Log.d(registerFourActivity.TAG, "kkkkkkkkkkkkkkkkkkkkk"+userUid+name+wight)

                val weight=wight.toString().toDouble()
                val length=hight.toString().toDouble()

                val goalll = document.get("goal").toString()
                val levelll = document.get("level").toString()
                val goal=goalll.toString().toInt()
                var level=levelll.toString().toInt()

                Log.e(registerOneActivity.TAG, "kkkkkkkkkkkkkkkkkkkkkoo"+level)

                var bmi = (weight) / (length / 100 * length / 100)

                if (gender=="female") {
                    var neededCal: Double = 0.0
                    var Mifflin = ((10 * weight) + (6.25 * length) - (5 * level) - 161)
                    var Revised =
                            ((9.247 * weight) + (3.098 * length) - (4.330 * level) + 447.593)

                    var Calories = (Mifflin + Revised) / 2

                    when (goal) {
                        1 -> neededCal = Calories - 500
                        2 -> neededCal = Calories + 500
                        3 -> neededCal = Calories
                    }
                    neededCall=roundOffDecimal(neededCal).toString()

                } else if (gender=="male"){
                    var neededCal:Double=0.0
                    var Mifflin =((10*weight)+ (6.25*length)-(5*level )+5)
                    var Revised =((13.397*weight) +(4.799*length) - (5.677*level) + 88.362)

                    var Calories= (Mifflin+Revised)/2

                    when(goal){
                        1 -> neededCal= Calories-500
                        2->  neededCal= Calories+500
                        3 -> neededCal= Calories
                    }
                   neededCall=roundOffDecimal(neededCal).toString()

                }


                var bmii=roundOffDecimal(bmi)

                // var neededCall=roundOffDecimal(neededCal)
                //val BMI = bmii.toString()

                var goall="goal"
                when(goal){
                    1 -> goall= "خسارة الوزن"
                    2->  goall= "الحفاظ على الوزن"
                    3 -> goall= "زيادة الوزن"
                }
                var levell="level"
                when(level){
                    1 -> levell= "مبتدى"
                    2->  levell= "متوسط"
                    3 -> levell= "متقدم"
                }
                var genderr="gender"
                when(gender){
                    "male" -> genderr= "ذكر"
                    "female"->  genderr= "انثى"

                }


                val nameTxt: TextView = findViewById(sa.ksu.gpa.saleem.R.id.nameSignUpHin)
                nameTxt.setText(name)
                val emailTxt:TextView=findViewById(sa.ksu.gpa.saleem.R.id.emailSignUpHin)
                emailTxt.setText(email)
                val wightTxt: TextView = findViewById(sa.ksu.gpa.saleem.R.id.wightHin)
                wightTxt.setText(wight)
                val heightTxt:TextView=findViewById(sa.ksu.gpa.saleem.R.id.heighHin)
                heightTxt.setText(hight)
                val calTxt: TextView = findViewById(sa.ksu.gpa.saleem.R.id.neededCalHin)
                calTxt.setText(neededCall)
                val genderTxt:TextView=findViewById(sa.ksu.gpa.saleem.R.id.genderHin)
                genderTxt.setText(genderr)
                val bmiTxt:TextView=findViewById(sa.ksu.gpa.saleem.R.id.bmiHin)
                bmiTxt.setText(bmii)
                val ageTxt:TextView=findViewById(sa.ksu.gpa.saleem.R.id.ageHin)
                ageTxt.setText(age)
                val goalTxt:TextView=findViewById(sa.ksu.gpa.saleem.R.id.goalHin)
                goalTxt.setText(goall)

                val levelTxt:TextView=findViewById(sa.ksu.gpa.saleem.R.id.levelHin)
                levelTxt.setText(levell)



            }
        }
                .addOnFailureListener { exception ->
                    Log.d(registerFourActivity.TAG, "get failed with ", exception)
                }

        var btn=findViewById<View>(sa.ksu.gpa.saleem.R.id.edit_profile)
        var rebtn=findViewById<View>(sa.ksu.gpa.saleem.R.id.reedit_profile)

        btn.setOnClickListener() {

            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle("تسجيل الخروج")
            //set message for alert dialog
            builder.setMessage("هل متاكد من تسجيل الخروج ؟ ")


            //performing positive action
            builder.setPositiveButton("نعم"){dialogInterface, which ->

                FirebaseAuth.getInstance().signOut();
                val intent = Intent(this, loginn::class.java)
                startActivity(intent)
            }
            //performing cancel action
         /*   builder.setNeutralButton("Cancel"){dialogInterface , which ->

            }*/
            //performing negative action
            builder.setNegativeButton("لا"){dialogInterface, which ->
            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()

        }

        rebtn.setOnClickListener {
            val intent= Intent(this, editProf::class.java)
            startActivity(intent)
        }
        //getCollection()


    }




    fun roundOffDecimal(number: Double): String? {


        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        val num=df.format(number)
        return num
    }
}


       // var ref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)

  /*      val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user = dataSnapshot.getValue() as User
                textView.text = user?.getName()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // handle error
            }
        }*/
      //  ref.addListenerForSingleValueEvent(menuListener)





