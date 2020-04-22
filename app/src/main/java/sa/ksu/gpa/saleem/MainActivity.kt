package sa.ksu.gpa.saleem

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import java.util.ArrayList


class MainActivity : AppCompatActivity() {
    private val CAMERA_REQUEST_CODE=123;
    private var btn: Button? = null
    private var addExcercize: Button? = null
    private lateinit var db:FirebaseFirestore
    private var counter=0
    lateinit var speedDialView:SpeedDialView
   val currentuser = "Kgr3rhDXC2kNuq5syHsm"
    val currentuser1 = FirebaseAuth.getInstance().currentUser?.uid



    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db= FirebaseFirestore.getInstance()
        loadFragment(HomeFragment())
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home-> {
                    title="الرئيسية"
                    loadFragment(HomeFragment())
                    speedDialView.visibility = View.VISIBLE
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.profile-> {
                    title="الاعدادات"
                    loadFragment(SettingFragment())
                    speedDialView.visibility = View.GONE
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
        showAddAdvice()
         ubdateBurntCaloris()
        Log.d("main","ID"+currentuser)
         speedDialView = findViewById<SpeedDialView>(R.id.speedDial)
        speedDialView.addActionItem(
            SpeedDialActionItem.Builder(10009, R.drawable.ic_scan)
                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.purble, getTheme()))
                .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()))
                .setLabel("مسح المنتج")
                .create()
        )
        speedDialView.addActionItem(
            SpeedDialActionItem.Builder(10011, R.drawable.ic_dumbbell)
                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBlue, getTheme()))
                .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()))
                .setLabel("تمرين")

                .create()
        )
        speedDialView.addActionItem(
            SpeedDialActionItem.Builder(10012, R.drawable.ic_timer_black_24dp)
                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, getTheme()))
                .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()))
                .setLabel("مؤقت")

                .create()
        )
        speedDialView.addActionItem(
            SpeedDialActionItem.Builder(10013, R.drawable.ic_white_dish)
                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.orange, getTheme()))
                .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()))
                .setLabel("وصفة")

                .create()
        )
        speedDialView.addActionItem(
            SpeedDialActionItem.Builder(10014, R.drawable.ic_idea)
                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.yellow, getTheme()))
                .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()))
                .setLabel("نصيحة")

                .create()
        )
        speedDialView.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->

            when (actionItem.id) {
                10009 -> {
                    //select scan barcode
                    val permisison= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

                    val intent = Intent(this@MainActivity, ScanActivity::class.java)
                    if(permisison!= PackageManager.PERMISSION_GRANTED){
                        makeRequest()
                    }

                    else startActivity(intent)
                    speedDialView.close() // To close the Speed Dial with animation
                    return@OnActionSelectedListener true // false will close it without animation
                }
                10011 ->{
                    Log.d("main1"," clicked")

                    addExcercizeDialog()

                }
                10014 ->{
                    addAdviceDialog()
                }
                10013->{
                    val intent = Intent(this@MainActivity, ShareRecipeFirst::class.java)
                    startActivity(intent)

                }
                10012->{
                    val intent = Intent(this@MainActivity, TimerSettings::class.java)
                    startActivity(intent)

                }
            }
            false
        })

     
        // set on-click listener
    //    addWaterBtn.setOnClickListener {
         //   addWater()
        //}

        speedDialView.setOnChangeListener(object : SpeedDialView.OnChangeListener {
            override fun onMainActionSelected(): Boolean {
                return false // True to keep the Speed Dial open
            }

            override fun onToggleChanged(isOpen: Boolean) {
                Log.d("MAIN", "Speed dial toggle state changed. Open = $isOpen")
            }
        })

    }




    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun addWater(){
        if (counter < 8) {
            val inflater =
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView = inflater.inflate(R.layout.water_field, null)
            waterInnerLL.addView(rowView, waterInnerLL.childCount - 1)
            counter++
            waterAmountTV.text = "$counter"

        }
    }

    fun onDelete(v: View) {
        if (counter > 0) {
            waterInnerLL.removeView(v.parent as View)
            counter--
            waterAmountTV.text = "$counter"
        }
    }


    private fun addAdviceDialog(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.advice_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        val  mAlertDialog = mBuilder.show()
        var body = mDialogView.dialogAdviceET!!.editText!!.text

        mAlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        mDialogView.dialogShareBtn.setOnClickListener{
            if (body.length > 140){
                Toast.makeText(this, "لا يمكن نشر نصيحة أطول من ١٤٠ حرف", LENGTH_LONG).show()
            }
            else if (body.isEmpty()){
                Toast.makeText(this, "لا يمكن ترك هذه الخانة فارغة ", LENGTH_LONG).show()
            }

            else {
                var body1=body.toString()
                val advice = HashMap<String, Any>()
                db.collection("Advices").document()
                //advice.put("text",body) //advice["text"] = body
                advice["text"] = body1
                db.collection("Advices").document().set(advice)
                Toast.makeText(this, "تمت اضافة النصيحة", LENGTH_LONG).show()
                advicesTV.text = body1
                mAlertDialog.dismiss()
            }

        }
        mDialogView.dialogCancelBtn.setOnClickListener{
            mAlertDialog.dismiss()

        }


    }

    private fun showAddAdvice(){
        //set input in TV
        //advicesTV.text = data //advicesTV.setText("النصيحة اليومية: "+data)

        db.collection("Advices")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("exists", "${document.id} => ${document.data}")
                    advicesTV.text = document.getString("text")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("error", "Error getting documents.", exception)
            }

    }


    fun showAddFood(data: ArrayList<String>) {
        val fragment = ItemListDialogFragmentA(data)
        val bundle = Bundle()
        bundle.putStringArrayList("item_data", data)
        this.supportFragmentManager.let { fragment.show(it, "tag") }
        fragment.setOnSelectData(object : ItemListDialogFragmentA.Listener {
            override fun onItemClicked(position: Int) {

            }
        })
    }
    public fun addFood(){
        val list = ArrayList<String>()
        list.add("وجبة مفصلة")
        list.add("وجبة ")
        showAddFood(list)
        // ItemListDialogFragment fragment = ItemListDialogFragment.newInstance(new Gson().toJson(menuDataArrayList));
        //        fragment.show(getActivity().getSupportFragmentManager(), "tag");
        //        fragment.setOnSelectData(position -> {
        //            cetType.setText(data.get(position).getDepartmentName());
        //            typeId = data.get(position).getId();
        //        });


    }

    private fun addExcercizeDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_excercise_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        val  mAlertDialog = mBuilder.show()
        mAlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));


        mDialogView.addExcercise.setOnClickListener{
            var burnt = mDialogView.addExcerciseburentCal!!.text
            var workoutName = mDialogView.addExcerciseWorkoutnameee!!.text.toString()

            if (burnt.isEmpty()){
                Log.d("main1"," empty burnt")
                Toast.makeText(this, "لا يمكن ترك أي خانة فارغة", Toast.LENGTH_LONG).show()
            }
           else if (workoutName.isEmpty()){
                Log.d("main1"," empty workoutName")
                Toast.makeText(this, "لا يمكن ترك أي خانة فارغة", Toast.LENGTH_LONG).show()
            }
            else{
                var  burnt1 = burnt.toString()
                var burntcal=burnt1.toDouble()
                Log.d("main1","not empty")

                val burntCalories = db.collection("Users").document(currentuser)

                burntCalories.update("burntCalories", FieldValue.increment(burntcal))
               // adding a list of excercises
                val docData = hashMapOf(
                        "exerciseName" to workoutName,
                        "exerciseCalories" to burntcal

                )
                db.collection("Users").document(currentuser).collection("Exercises").document().set(docData)
                    .addOnSuccessListener {
                    Log.d("main1","Added to collection")
                    Toast.makeText(this, "تمت اضافة التمرين", LENGTH_LONG).show()

                }.addOnFailureListener {
                    Log.d("main1","not Added to collection"+it)
                        Toast.makeText(this, "حصل خطأ", LENGTH_LONG).show()


                    }
                mAlertDialog.dismiss()

                ubdateBurntCaloris()


            }
            // extra detail add a success shape
        }
        mDialogView.cancelExcercise.setOnClickListener{
            mAlertDialog.dismiss()

        }

    }

    private fun ubdateBurntCaloris() {
        /*
        db.collection("Users").document(currentuser).get().addOnSuccessListener {
            if (it.get("burntCalories")!=0)
                burnt_calories_textview.text=it.get("burntCalories").toString()
            else
                burnt_calories_textview.text="0"

        }
        */

    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.d("tag","Permission Denied")

                } else {
                    Log.d("tag","permisson granted")
                    val intent = Intent(this@MainActivity, ScanActivity::class.java)
                    startActivity(intent)

                }
            }
        }
    }


    }




