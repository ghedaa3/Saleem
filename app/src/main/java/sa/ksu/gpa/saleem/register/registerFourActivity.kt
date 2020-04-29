package sa.ksu.gpa.saleem.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.gms.common.internal.Constants
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_fragment_one.*
import kotlinx.android.synthetic.main.activity_register_one.*
import kotlinx.android.synthetic.main.activity_register_two.*
import sa.ksu.gpa.saleem.*
import java.util.*
import java.util.logging.Level
import kotlin.properties.Delegates

class registerFourActivity : AppCompatActivity() ,View.OnClickListener {

    val user = HashMap<String, Any>()

    private lateinit var auth: FirebaseAuth

    private val TAG = "Sign up"

   // private var length by Delegates.notNull<Double>()

   private var goal =0


    private var weight:Double = 0.0
    private var level:Int = 0
    private lateinit var name :String
    private lateinit var gender:String
    private lateinit var email:String
    private var neededCal:Double = 0.0
    private var agee:Int = 0

    private var length:Double = 0.0
    //private var goal by Delegates.notNull<Int>()



    val db = FirebaseFirestore.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_four)
       // val intent = Intent(this, MainActivity::class.java)


        val intent = Intent(this, loginn::class.java)

        val toolbar = findViewById<View>(R.id.toolbar)
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.setTitle("")


       auth = FirebaseAuth.getInstance()


        val btn=findViewById<View>(R.id.startBtn) as Button?
        val one=findViewById<View>(R.id.goalOneBtn) as Button?
        val two=findViewById<View>(R.id.goalTwoBtn) as Button?
        val three=findViewById<View>(R.id.goalThreeBtn) as Button?


        var backImg = findViewById<View>(R.id.back_button)

        backImg.setOnClickListener(this)







        var button_background : Int = 1;

        var clicked=false;

        one?.setOnClickListener {



            if(button_background==2){
                one.setBackgroundResource(R.drawable.unclick);
                button_background=1;
            } else if(button_background==1){
                one.setBackgroundResource(R.drawable.register_btn);
                two?.setBackgroundResource(R.drawable.unclick)
                three?.setBackgroundResource(R.drawable.unclick)
                button_background=2;
            }
         //   user.put("level","beginner")
            goal = 1
           clicked=true;
        }
        two?.setOnClickListener{
            if(button_background==2){
                two?.setBackgroundResource(R.drawable.unclick);
                button_background=1;
            } else if(button_background==1){
                two?.setBackgroundResource(R.drawable.register_btn);
                one?.setBackgroundResource(R.drawable.unclick)
                three?.setBackgroundResource(R.drawable.unclick)
                button_background=2;
                two.invalidate()
            }

           // user.put("level","Intermediate")
            goal = 2
          clicked=true;
        }

        three?.setOnClickListener{
            if(button_background==2){
                three?.setBackgroundResource(R.drawable.unclick);
                button_background=1;
            } else if(button_background==1){
                three?.setBackgroundResource(R.drawable.register_btn);
                two?.setBackgroundResource(R.drawable.unclick)
                one?.setBackgroundResource(R.drawable.unclick)
                button_background=2;
            }

           // user.put("level","advance")
           goal = 3
          clicked=true;
        }




        var length = getIntent().getDoubleExtra("height",0.0)
        var weight=getIntent().getDoubleExtra("wight",0.0)
        var gender = getIntent().getStringExtra("gender")
        var bmi = getIntent().getDoubleExtra("bmi",0.0)
        level = getIntent().getIntExtra("level",0)
        var userAge= getIntent().getStringExtra("userAge")


        var name = getIntent().getStringExtra("name")
        var pass = getIntent().getStringExtra("password")
        var email = getIntent().getStringExtra("email")

        var agee= getIntent().getIntExtra("agee",0)


        intent.putExtra("wight", weight)
        intent.putExtra("height", length)
        intent.putExtra("BMI", bmi)
        intent.putExtra("gender", gender)
        // intent.putExtra("age",age)
        intent.putExtra("userAge", userAge)
        intent.putExtra("name", name)
        intent.putExtra("password", pass)
        intent.putExtra("email", email)
        intent.putExtra("goal",goal)

        //intent.putExtra("agee",agee)

        Log.d("this",""+email)
        Log.d("this",""+name)
        Log.d("this",""+pass)
        Log.d("this",""+length)
        Log.d("this",""+weight)
        Log.d("this",""+gender)
        Log.d("this",""+level)
        Log.d("this",""+userAge)
        Log.d("this",""+goal)
       // Log.d("this needed cal",""+neededCal)

        var neededCal=-1.0

        if (gender == "male")
         neededCal = calcualteCaloriesMen(level.toDouble(), weight!!, length!!, goal!!)



        if (gender == "female")
           neededCal = calcualteCaloriesWomen(level.toDouble(), weight!!, length!!, goal!!)



        btn?.setOnClickListener {

            if (verify(clicked)) {


            //  val intent = Intent(this, MainActivity::class.java)
          //  var neededCal = 0.0




            createAccount(email,pass)

           // createUserCollection(weight, length, level, goal, gender, name, email, neededCal, agee)
              //  Log.d("this needed cal inside",""+neededCal)

               // startActivity(Intent(this, loginn::class.java))
            }
        }
//


    }

    private fun verify(clicked:Boolean): Boolean {


        if(!clicked){
            showDialogWithOkButton("الرجاء اختيار الهدف")
            return false
        }
else return true

    }

    fun calcualteCaloriesWomen(activityLevel:Double,weight:Double,length:Double,goal:Int):Double{

        var neededCalories:Double=0.0
        var Mifflin =((10*weight)+ (6.25*length)-(5*activityLevel )-161)
        var Revised =((9.247*weight) +(3.098*length) - (4.330*activityLevel) + 447.593)

        var Calories= (Mifflin+Revised)/2

        when(goal){
            1 -> neededCalories= Calories-500
            2->  neededCalories= Calories+500
            3 -> neededCalories= Calories
        }



       // if(neededCalories!=0.0)



        return neededCalories


    }
    fun calcualteCaloriesMen(activityLevel:Double,weight:Double,length:Double,goal:Int):Double{
        var neededCalories:Double=0.0
        var Mifflin =((10*weight)+ (6.25*length)-(5*activityLevel )+5)
        var Revised =((13.397*weight) +(4.799*length) - (5.677*activityLevel) + 88.362)

        var Calories= (Mifflin+Revised)/2

        when(goal){
            1 -> neededCalories= Calories-500
            2->  neededCalories= Calories+500
            3 -> neededCalories= Calories
        }

    /*    if(neededCalories!=0.0)
        showDialogWithOkButton4("السعرات الحرارية" +
                "\n"+neededCalories)*/

        return neededCalories

    }

 /*   private fun showDialogWithOkButton(msg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("حسناً") { dialog, id ->
                //do things
            }
        val alert = builder.create()
        alert.show()
    }*/

    private fun showDialogWithOkButton(msg: String) {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText(msg)
            .setConfirmButton("حسناً") { sDialog ->
                sDialog.dismissWithAnimation()


            }
            .show()
    }

    private fun showDialogWithOkButton4(msg: String) {
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText(msg)
            .setConfirmButton("حسناً") { sDialog ->
                startActivity(Intent(this, loginn::class.java))



            }
            .show()
    }

    private fun createUserCollection(weight:Double,length:Double,level:Int,goal:Int,gender:String,name:String,email:String,neededCal:Double,
                                     userAge:Int,userIId:String):Double {
        val user = HashMap<String, Any>()


       // db.collection("Users").document("user")
        user.put("name",name)
        user.put("email",email)
       // user.put("pass",pass)
        user.put("weight",weight)
        user.put("height",length)
        user.put("level",level)
        user.put("goal",goal)
        user.put("gender",gender)

        user.put("age",userAge)

        var neededCal = 0.0



        if (gender == "male")
            neededCal = calcualteCaloriesMen(level.toDouble(), weight!!, length!!, goal!!)



        if (gender == "female")
            neededCal = calcualteCaloriesWomen(level.toDouble(), weight!!, length!!, goal!!)

        user.put("needed cal",neededCal)



        val db = FirebaseFirestore.getInstance()
        val userUid = FirebaseAuth.getInstance().currentUser!!.uid

        Log.d(registerFourActivity.TAG, "createAccountcolloection:$userIId")



        db.collection("users").document(userIId).set(user)
            .addOnSuccessListener { docRef ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${docRef}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        return neededCal
    }

    val actionCodeSettings = ActionCodeSettings.newBuilder()
        // URL you want to redirect back to. The domain (www.example.com) for this
        // URL must be whitelisted in the Firebase Console.
        .setUrl("https://www.example.com/finishSignUp?cartId=1234")
        // This must be true
        .setHandleCodeInApp(true)
        .setIOSBundleId("com.example.ios")
        .setAndroidPackageName(
            "com.example.android",
            true, /* installIfNotAvailable */
            "12" /* minimumVersion */)
        .build()


    private fun createAccount(email:String,pass: String) {


        var length = getIntent().getDoubleExtra("height",0.0)
        var weight=getIntent().getDoubleExtra("wight",0.0)
        var gender = getIntent().getStringExtra("gender")
        var bmi = getIntent().getDoubleExtra("bmi",0.0)
        level = getIntent().getIntExtra("level",0)
        var userAge= getIntent().getStringExtra("userAge")


        var name = getIntent().getStringExtra("name")
        var pass = getIntent().getStringExtra("password")
        var email = getIntent().getStringExtra("email")

       // var agee= getIntent().getIntExtra("agee",0)
        var agee=userAge.toString().toInt()
        if (email != "" && pass != "") {

            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = auth.getCurrentUser()
                        var userIId=FirebaseAuth.getInstance().currentUser!!.uid
                        FirebaseAuth.getInstance().currentUser!!.sendEmailVerification()
                        Log.d(registerFourActivity.TAG, "createAccountsend:$email"+userIId)

                       var calNeeded= createUserCollection(
                            weight,
                            length,
                            level,
                            goal,
                            gender,
                            name,
                            email,
                            neededCal,
                            agee,
                            userIId
                        )
                       // startActivity(Intent(this, loginn::class.java))

                        showDialogWithOkButton4(" " +
                                "تم انشاء الحساب بنجاح  \n السعرات الحرارية" +
                                "\n"+calNeeded)


                  /*      if (auth.getCurrentUser()!!.isEmailVerified()) {
                            // FirebaseAuth.getInstance().currentUser!!.sendEmailVerification()

                            Log.d(registerFourActivity.TAG, "createAccount:$email")

                            startActivity(Intent(this, loginn::class.java))
                        } else {
                            FirebaseAuth.getInstance().currentUser!!.sendEmailVerification()
                            showDialogWithOkButton("تحقق من الرابط المرسل على بريدك لإكمال عملية تسجيل الدخول ")
                        }*/

                        //val user = auth.currentUser
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                  /*      Toast.makeText(
                            this, "Authentication succeeded",
                            Toast.LENGTH_SHORT
                        ).show()*/


                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                    /*    Toast.makeText(
                            this, "Authentication failed google.",
                            Toast.LENGTH_SHORT
                        ).show()*/
                        showDialogWithOkButton("البريد الإلكتروني غير صالح")


                    }

                    /*    MySharedPreference.clearData(this)
            MySharedPreference.putString(this, Constants.Keys.ID, auth.getInstance().getUid())
    */

                }
        }

    }



    private fun sendEmailVerification(email: String) {

        val auth = FirebaseAuth.getInstance()

        auth.sendSignInLinkToEmail(email, actionCodeSettings)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                    showDialogWithOkButton("send :)YAYAYYYYY")

                }

                else {
                    Log.e(registerFourActivity.TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT
                    ).show()
                    showDialogWithOkButton("NO NO")

                }
            }

        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->
                // [START_EXCLUDE]
                // Re-enable button
                // verifyEmailButton.isEnabled = true

                if (task.isSuccessful) {
                    Toast.makeText(
                        baseContext,
                        "Verification email sent to ${user.email} ",
                        Toast.LENGTH_SHORT
                    ).show()
                    showDialogWithOkButton("send :)")
                } else {
                    Log.e(registerFourActivity.TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT
                    ).show()
                    showDialogWithOkButton("NOT send :(")

                }
                // [END_EXCLUDE]
            }
        // [END send_email_verification]
    }


    companion object {
        const val TAG = "register Four "
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_button -> {
                finish()
            }
            else -> {

            }
        }


    }

}
