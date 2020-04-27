package sa.ksu.gpa.saleem.register

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register_two.*
import sa.ksu.gpa.saleem.R
import java.text.SimpleDateFormat
import java.util.*
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import android.widget.Toast

import android.R.id
import android.widget.RadioGroup
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.appcompat.widget.Toolbar
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.common.base.Verify.verify
import com.wajahatkarim3.easyvalidation.core.collection_ktx.noNumbersList


class registerTwoActivity : AppCompatActivity(),View.OnClickListener {



    var button_date: Button? = null
    var textview_date: TextView? = null
    var cal = Calendar.getInstance()
    val TAG = "MyActivity"
    val user = HashMap<String, Any>()
    lateinit var genderr: String
    var userAge="0"
    var pic:ImageButton?=null
    var level = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_two)
        var RadioG = findViewById<View>(R.id.radio_group) as RadioGroup
        val db = FirebaseFirestore.getInstance()
        val intent = Intent(this, registerThreeActivity::class.java)

        val btn = findViewById<View>(R.id.nxtTwoBtn) as Button?
        val toolbar = findViewById<View>(R.id.toolbar)
        pic=findViewById(R.id.datepic)as ImageButton
        textview_date=findViewById(R.id.text_view_date_1)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar!!.setTitle("")



        var backImg = findViewById<View>(R.id.back_button)

        backImg.setOnClickListener(this)


        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }


        btn?.setOnClickListener {


            var name = getIntent().getStringExtra("name")
            var pass = getIntent().getStringExtra("password")
            var email = getIntent().getStringExtra("email")
            var id: Int = radio_group.checkedRadioButtonId


            if (verify()) {
                val wightTxt = findViewById<View>(R.id.wight) as EditText?
                val heightTxt = findViewById<View>(R.id.height) as EditText?
                var id: Int = radio_group.checkedRadioButtonId

                var wight = wightTxt?.text.toString().toDouble()
                var height = heightTxt?.text.toString().toDouble()

                val radio: RadioButton = findViewById(id)
                var gender = radio?.text.toString();

                if (gender == "ذكر")
                    genderr = "male"
                if (gender == "انثى")
                    genderr = "female"



                Log.d("this2", "" + genderr)
                intent.putExtra("gender", genderr)

                intent.putExtra("wight", wight)
                intent.putExtra("height", height)






                intent.putExtra("name", name)


                intent.putExtra("password", pass)
                intent.putExtra("email", email)


                Log.d("this2", "" + email)
                Log.d("this2", "" + name)
                Log.d("this2", "" + pass)
                Log.d("this2", "" + height)
                Log.d("this2", "" + wight)
                /*     Log.d("this2", "" + type)
            Log.d("this2", "" + bmi)*/

                // intent.putExtra("age", age)
                //   if (verify()) {


                startActivity(intent)
                //   }//------------------------------------------

            }

        }


        // get the references from layout file
        textview_date = this.text_view_date_1


        textview_date!!.text = "--/--/----"

        // create an OnDateSetListener
        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dob = Calendar.getInstance()
                val today = Calendar.getInstance()

                dob.set(year, monthOfYear, dayOfMonth)

                var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)

                if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                    age--
                }

                val ageInt = age + 1

                // val user = HashMap<String, Any>()
               // user.put("DOB", age)

                if(age>115){

                    showDialogWithOkButton("الرجاء إدخال العمر الصحيح " +
                            "\n العمر الأعلى 115 سنة ")

                }else if(age<=6){
                    showDialogWithOkButton("الرجاء إدخال العمر الصحيح " +
                            "\nالعمر الأدنى 6 سنوات ")

                }else {

                    userAge=age?.toString()



                    getIntent().putExtra("userAge",userAge)
                    intent.putExtra("userAge",userAge)



                    Log.d("this2","age in 2nd activity"+userAge)
                    updateDateInView()

                }



            }

        }

        // when you click on the button, show DatePickerDialog that is set with OnDateSetListener
        pic!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(
                    this@registerTwoActivity,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()

            }

        })
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

    private fun verify(): Boolean {

        val wightTxt = findViewById<View>(R.id.wight) as EditText?
        val heightTxt = findViewById<View>(R.id.height) as EditText?


        var wight = wightTxt?.text.toString()
        var height = heightTxt?.text.toString()
        var age=userAge.toInt()

        var id: Int = radio_group.checkedRadioButtonId


        if (wight == ""||wight=="0") {
            showDialogWithOkButton("الرجاء إدخال الوزن")
            return false
        } else if (height == ""||height=="0") {

            showDialogWithOkButton("الرجاء إدخال الطول")
            return false


        } else if (height.toDouble()<=107.9) {

            showDialogWithOkButton("الرجاء إدخال الطول الصحيح" +
                    "\nالطول الأدنى 107.9 سم ")
            return false


        } else if (wight.toDouble()<=17.9) {

            showDialogWithOkButton("الرجاء إدخال الوزن الصحيح" +
                    "\nالوزن الأدنى 17.9 كلجم ")
            return false


        } else if (id == -1) {

            showDialogWithOkButton("الرجاء إختيار الجنس")
            return false

        }else       if(age==0||age==-1){

            showDialogWithOkButton("الرجاء إدخال العمر ")
            return false
        }


        /*    else if(radio_group.c){

        }*/
        else return true

    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date!!.text = sdf.format(cal.getTime())
    }


    fun message(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
    }






    fun calculateBmi(wight: Double, height: Double): Int {

        var bmi = (wight)/(height/100*height/100)
        intent.putExtra("BMI",bmi)


        // return
        when {
            18.5 > bmi -> {
                level= 1
                showDialogWithOkButton(" نحافة $bmi ")
                intent.putExtra("type", level)


            }
            18.5 <= bmi || bmi < 25.0 -> {
                showDialogWithOkButton(" طبيعي $bmi")
                level= 2
                intent.putExtra("type", level)
            }
            25.0 <= bmi || bmi < 30.0 -> {
                showDialogWithOkButton("زيادة وزن $bmi")
                level= 3
                intent.putExtra("type", level)
            }

            30.0 <= bmi || bmi < 35.0 -> {
                showDialogWithOkButton("سمنة درجة اولى BMI $bmi")
                level= 4
                intent.putExtra("type", level )
            }
            35.0 <= bmi || bmi < 40.0 -> {
                showDialogWithOkButton(" سمنى درجة ثانية$bmi")
                level= 5
                intent.putExtra("type", level)

            }
            bmi >= 40.0 -> {
                showDialogWithOkButton("$bmi سمنة مفرطة")
               level= 6
                intent.putExtra("type", level)
            }

            else -> print("")
        }
return level
    }


/*    private fun showDialogWithOkButton(msg: String) {
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
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(msg)
            .setConfirmButton("حسناً") { sDialog ->
                sDialog.dismissWithAnimation()


            }
            .show()
    }



}





