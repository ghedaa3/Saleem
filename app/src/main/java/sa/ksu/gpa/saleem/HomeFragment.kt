package sa.ksu.gpa.saleem


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
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
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import kotlinx.android.synthetic.main.add_excercise_dialog.view.*
import kotlinx.android.synthetic.main.add_excercise_dialog.view.addExcercise
import kotlinx.android.synthetic.main.add_excercise_dialog.view.addExcerciseburentCal
import kotlinx.android.synthetic.main.add_excercise_dialog.view.cancelExcercise
import kotlinx.android.synthetic.main.add_fast_food.view.*
import kotlinx.android.synthetic.main.advice_dialog.view.*
import kotlinx.android.synthetic.main.fragment_home_body.*
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.water_field.*
import kotlinx.android.synthetic.main.water_field.view.*
import sa.ksu.gpa.saleem.AddFoodActivity.OnSave
import sa.ksu.gpa.saleem.Timer.TimerSettings
import sa.ksu.gpa.saleem.recipe.ShareRecipeFirst
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    private val CAMERA_REQUEST_CODE=123;

    private lateinit var db: FirebaseFirestore
    var totalcal  = 0.0
    var consumerCal = 0.0
    var remainderCal = 0.0
    var previousDaysCount = 0
    var history_Id = ""
    lateinit var speedDialView:SpeedDialView
     var counter:Int=0

    var currentuser = ""
    private var waterCount=0
    private  lateinit var adviceID:String
    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var date: String

    lateinit var dialog:ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.home_fragment, container, false)
    }



    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        date = getCurrentDate()
        tvDate.text = date
        pagerAdapter = activity?.supportFragmentManager?.let { PagerAdapter(it,date) }!!
        viewPager.adapter = pagerAdapter
        dotsIndicator.setViewPager(viewPager)
        viewPager.adapter?.registerDataSetObserver(dotsIndicator.dataSetObserver)
        pagerAdapter.notifyDataSetChanged()
        iv_previous_date.setOnClickListener {
            previousDaysCount --
            tvDate.text = getSelectedDate(previousDaysCount)
            pagerAdapter = activity?.supportFragmentManager?.let { PagerAdapter(it,getSelectedDate(previousDaysCount)) }!!
            viewPager.adapter = pagerAdapter
            dotsIndicator.setViewPager(viewPager)
        }
        iv_next_date.setOnClickListener {
            if(previousDaysCount != 0){
                previousDaysCount ++
                tvDate.text = getSelectedDate(previousDaysCount)
                pagerAdapter = activity?.supportFragmentManager?.let { PagerAdapter(it,getSelectedDate(previousDaysCount)) }!!
                viewPager.adapter = pagerAdapter
                dotsIndicator.setViewPager(viewPager)
            }

        }


//        view.findViewById<ImageView>(R.id.ivAddView).setOnClickListener { addFood() }
        view.findViewById<LinearLayout>(R.id.add_breakfast).setOnClickListener { addFood("breakfast") }
        view.findViewById<LinearLayout>(R.id.add_lunch).setOnClickListener { addFood("lunch") }
        view.findViewById<LinearLayout>(R.id.add_dinner).setOnClickListener { addFood("dinner") }
        view.findViewById<ImageView>(R.id.adviceFlag).setOnClickListener { onFlagClicked() }
        view.findViewById<ImageView>(R.id.addGlass).setOnClickListener { addWater() }

        view.findViewById<LinearLayout>(R.id.add_snack).setOnClickListener { addFood("snack") }

        db= FirebaseFirestore.getInstance()
        currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        ubdateBurntCaloris()
        updateWater()
        retriveWater()
        initSpeedDialView()





        db.collection("History")
            .whereEqualTo("date",getCurrentDate())
            .whereEqualTo("user_id",currentuser)
            .get().addOnSuccessListener { documents ->
                if(documents.isEmpty){
                    val data = hashMapOf(
                        "cal" to remainderCal,
                        "date" to getCurrentDate(),
                        "steps_count" to 1,
                        "user_id" to currentuser
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
                    pb_counter.progress = remainderCal.toInt()
                    remainder_cal.setText("${remainderCal.toInt()}")
                    tv_main_number.setText("${(totalcal-remainderCal).toInt()}")
                }
            }
    }



    @SuppressLint("ResourceType")
    private fun initSpeedDialView() {
        speedDialView = view!!.findViewById<SpeedDialView>(R.id.speedDial)
        speedDialView.addActionItem(
            SpeedDialActionItem.Builder(10009, R.drawable.ic_scan)
                .setFabBackgroundColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.purble,
                        getContext()!!.getTheme()

                    )
                )
                .setFabImageTintColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.white,
                        getContext()!!.getTheme()
                    )
                )
                .setLabel("مسح المنتج")
                .create()
        )
        speedDialView.addActionItem(
            SpeedDialActionItem.Builder(10011, R.drawable.ic_dumbbell)
                .setFabBackgroundColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.colorBlue,
                        getContext()!!.getTheme()
                    )
                )
                .setFabImageTintColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.white,
                        getContext()!!.getTheme()
                    )
                )
                .setLabel("تمرين")

                .create()
        )
        speedDialView.addActionItem(
            SpeedDialActionItem.Builder(10012, R.drawable.ic_timer_black_24dp)
                .setFabBackgroundColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.green,
                        getContext()!!.getTheme()
                    )
                )
                .setFabImageTintColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.white,
                        getContext()!!.getTheme()
                    )
                )
                .setLabel("مؤقت")

                .create()
        )
        speedDialView.addActionItem(
            SpeedDialActionItem.Builder(10013, R.drawable.ic_white_dish)
                .setFabBackgroundColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.orange,
                        getContext()!!.getTheme()
                    )
                )
                .setFabImageTintColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.white,
                        getContext()!!.getTheme()
                    )
                )
                .setLabel("وصفة")

                .create()
        )
        speedDialView.addActionItem(
            SpeedDialActionItem.Builder(10014, R.drawable.ic_idea)
                .setFabBackgroundColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.yellow,
                        getContext()!!.getTheme()
                    )
                )
                .setFabImageTintColor(
                    ResourcesCompat.getColor(
                        getResources(),
                        R.color.white,
                        getContext()!!.getTheme()
                    )
                )
                .setLabel("نصيحة")

                .create()
        )
        speedDialView.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->

            when (actionItem.id) {
                10009 -> {
                    //select scan barcode
                    val permisison =
                        ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)

                    val intent = Intent(context, ScanActivity::class.java)
                    if (permisison != PackageManager.PERMISSION_GRANTED) {
                        makeRequest()
                    } else startActivity(intent)
                    speedDialView.close() // To close the Speed Dial with animation
                    return@OnActionSelectedListener true // false will close it without animation
                }
                10011 -> {
                    Log.d("main1", " clicked")

                    addExcercizeeDialog()

                }
                10014 -> {
                    addAdviceDialog()
                }
                10013 -> {
                    val intent = Intent(context, ShareRecipeFirst::class.java)
                    startActivity(intent)

                }
                10012 -> {
                    val intent = Intent(context, TimerSettings::class.java)
                    startActivity(intent)

                }
            }
            false
        })


        speedDialView.setOnChangeListener(object : SpeedDialView.OnChangeListener {
            override fun onMainActionSelected(): Boolean {
                return false // True to keep the Speed Dial open
            }

            override fun onToggleChanged(isOpen: Boolean) {
                Log.d("MAIN", "Speed dial toggle state changed. Open = $isOpen")
            }
        })
    }

    private fun addExcercizeeDialog() {
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.add_excercise_dialog, null)
        val mBuilder = AlertDialog.Builder(context!!)
            .setView(mDialogView)

        val  mAlertDialog = mBuilder.show()
        mAlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));


        mDialogView.addExcercise.setOnClickListener{
            var burnt = mDialogView.addExcerciseburentCal!!.text
            var workoutName = mDialogView.addExcerciseWorkoutnameee!!.text.toString()

            if (burnt.isEmpty()){
                Log.d("main1"," empty burnt")
                Toast.makeText(context, "لا يمكن ترك أي خانة فارغة", Toast.LENGTH_LONG).show()
            }
            else if (workoutName.isEmpty()){
                Log.d("main1"," empty workoutName")
                Toast.makeText(context, "لا يمكن ترك أي خانة فارغة", Toast.LENGTH_LONG).show()
            }
            else{
                var  burnt1 = burnt.toString()
                var burntcal=burnt1.toInt()
                Log.d("main1","not empty")

                // adding a list of excercises
                val docData = hashMapOf(
                    "exerciseName" to workoutName,
                    "exerciseCalories" to burntcal,
                    "date"  to  getCurrentDate(),
                    "exerciseType" to "normal"

                )
                db.collection("users").document(currentuser!!).collection("Exercises").document().set(docData)
                    .addOnSuccessListener {
                        Log.d("main1","Added to collection")
                        Toast.makeText(context, "تمت إضافة التمرين", Toast.LENGTH_LONG).show()

                    }.addOnFailureListener {
                        Log.d("main1","not Added to collection"+it)
                        Toast.makeText(context, "حصل خطأ", Toast.LENGTH_LONG).show()


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

    private fun showAddAdvice() {
    var advicesList:ArrayList<String> = ArrayList()
    var IdList:ArrayList<String> = ArrayList()
        val rand = Random()
        db.collection("Advices").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    IdList.add(document.id)
                    advicesList.add(document.get("text").toString())

                }
                val index = rand.nextInt(advicesList.size)
                advicesTV.text = (advicesList[index])
                adviceID = IdList[index]

            }
            .addOnFailureListener { exception ->
                Log.w("error", "Error getting documents.", exception)
            }
    }


    private fun onFlagClicked(){
        db.collection("ReportedAdvices").whereEqualTo("reporterUID",currentuser).whereEqualTo("adviceID",adviceID)
            .get().addOnSuccessListener { documents ->
                if (documents.isEmpty){
                    reportAdviceDialog()
                }
                else
                    Toast.makeText(context, "لقد أبلغت عن هذه النصحية مسبقا", Toast.LENGTH_LONG).show()

            }.addOnFailureListener {
                Log.d("flag", "onFlagClicked inside else")
            }
    }
    private fun reportAdviceDialog(){
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.advice_dialog, null)
        val mBuilder = activity?.let {
            AlertDialog.Builder(it)
                .setView(mDialogView)
        }

        val  mAlertDialog = mBuilder?.show()
        mAlertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        mDialogView.textinadvice.text="بلاغك"
        mDialogView.dialogShareBtn.text="تبليغ"

        mDialogView.dialogShareBtn.setOnClickListener{
            var body = mDialogView.dialogAdviceET!!.editText!!.text

            when {
                body.length > 140 -> {
                    Toast.makeText(context, "لا يمكن ان يكون البلاغ أطول من ١٤٠ حرف", Toast.LENGTH_LONG).show()
                }
                body.isEmpty() -> {
                    Toast.makeText(context, "لا يمكن ترك هذه الخانة فارغة ", Toast.LENGTH_LONG).show()
                }
                else -> {
                    var body1=body.toString()

                    val docData = hashMapOf(
                        "text" to body1,
                        "reporterUID" to currentuser,
                        "adviceID" to adviceID,
                        "date" to getCurrentDate()
                    )

                    db.collection("ReportedAdvices").document().set(docData).addOnSuccessListener {
                        Log.d("advice", "added reports:" )

                    }.addOnFailureListener {
                        Log.d("advice", "error added reports:" )

                    }
                    Toast.makeText(context, "تم نشر البلاغ ", Toast.LENGTH_LONG).show()
                    mAlertDialog?.dismiss()
                }
            }
        }

        mDialogView.dialogCancelBtn.setOnClickListener{
            mAlertDialog?.dismiss()
        }
    }



    private fun ubdateBurntCaloris() {
        var totalBurntCalories:Int=0
        db.collection("users").document(currentuser!!).collection("Exercises").whereEqualTo("date",getCurrentDate()).get().addOnSuccessListener {
            for (documents in it){
                totalBurntCalories+=documents.get("exerciseCalories").toString().toDouble().toInt()

            }
            if(totalBurntCalories!=null)
                burnt_calories_textview.text= totalBurntCalories.toString()
            else
                burnt_calories_textview.text="0"


        }


    }

    private fun addWater() {
        val inflater = context
            ?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.water_field, null)
       if(add_water.childCount!=9) {
           val docData = hashMapOf(
               // "UID" to currentUser!!.toString(),
               "amountOfWater" to 1

           )
           add_water.addView(rowView, add_water.childCount - 1)
           db.collection("users").document(currentuser).collection("Water")
               .document(getCurrentDate()).get().addOnSuccessListener {
               if (!it.exists()) {
                   db.collection("users").document(currentuser).collection("Water")
                       .document(getCurrentDate()).set(docData)
                   updateWater()
               } else {
                   db.collection("users").document(currentuser).collection("Water")
                       .document(getCurrentDate()).update("amountOfWater", FieldValue.increment(1))
                   updateWater()
               }
           }


       }
    }

    private fun retriveWater() {
          db.collection("users").document(currentuser).collection("Water").document(getCurrentDate()).get().addOnSuccessListener {
              if(it.exists()){


             counter= it.get("amountOfWater").toString().toInt()
              Log.e("inside if success","counter"+counter)
              if (counter!=null||counter!=0){
                  Log.e("inside if counter","counter"+counter)

                  retriveViews()
              }
              }

          }

    }

    private fun retriveViews() {
       var i = counter
        Log.e("counter","counter"+counter)

        while (i!=0){
                val inflater = context
                    ?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val rowView = inflater.inflate(R.layout.water_field, null)
                add_water.addView(rowView, add_water.childCount - 1)
                i--
            }



    }


    fun showAddFood(data: ArrayList<String>,type_of_food:String) {
        val fragment = ItemListDialogFragmentA(data)
        val bundle = Bundle()
        bundle.putStringArrayList("item_data", data)
        this.activity?.supportFragmentManager?.let { fragment.show(it, "tag") }
        fragment.setOnSelectData(object : ItemListDialogFragmentA.Listener {
            override fun onItemClicked(position: Int) {
                when(position){
                    0 -> {
                        startAddFoodActivity(type_of_food)
                    }
                    1 -> {
                        addExcercizeDialog(type_of_food)
                    }
                }
            }
        })
    }


    private fun startAddFoodActivity( type_of_food:String) {

        var onsave  = object : OnSave {
            override fun onSaveSuccess(sum: Double) {
                var d:Int = 0
                d = sum.toInt()
                Log.e("onActivityResult","$d")
                consumerCal -= d
                remainderCal += d
                pb_counter.progress =remainderCal.toInt()
                pb_counter.max = totalcal.toInt()
                remainder_cal.setText("${remainderCal.toInt()}")
                tv_main_number.setText("${(totalcal-remainderCal).toInt()}")
                Toast.makeText(context,"تمت اضافة الوجبة", Toast.LENGTH_LONG)
                updateHistory()
            }
        }
        var dialog: AddFoodActivity? = context?.let { AddFoodActivity(it,null,type_of_food,null,onsave) }
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        dialog?.show()

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK){

        }
    }


    fun addFood(type_of_food:String){
        val list = ArrayList<String>()
        list.add("اضافة بالمكونات")
        list.add("اضافة سريعة")
        showAddFood(list,type_of_food)

    }


    private fun addExcercizeDialog(type_of_food:String) {
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.add_fast_food, null)
        val mBuilder = activity?.let {
            AlertDialog.Builder(it)
                .setView(mDialogView)
        }

        val  mAlertDialog = mBuilder?.show()
        mAlertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        var burnt = mDialogView.addExcerciseburentCal!!.text
        val workoutName = mDialogView.addExcerciseWorkoutname!!.text
        Log.d("this",""+burnt+workoutName)

        mDialogView.addExcercise.setOnClickListener{


            if (burnt.isEmpty()||workoutName.isEmpty()){
                Toast.makeText(context, "لا يمكن ترك أي خانة فارغة", Toast.LENGTH_LONG).show()
            }
            else{
                var burntStringData = burnt.toString()
                var data =  burntStringData.toInt()
                consumerCal -= data
                remainderCal += data

                pb_counter.progress =remainderCal.toInt()
                pb_counter.max = totalcal.toInt()
                remainder_cal.setText("${remainderCal.toInt()}")
                tv_main_number.setText("${(totalcal-remainderCal).toInt()}")
                val data1 = hashMapOf(
                    "food_name" to workoutName.toString(),
                    "type" to "unDetailed",
                    "foods" to ArrayList<AddFoodActivity.Item>(),
                    "date" to getCurrentDate(),
                    "user_id" to currentuser,
                    "cal_of_food" to burntStringData.toInt(),
                    "type_of_food" to type_of_food
                )
                showLoadingDialog()
                db.collection("Foods").document().set(data1 as Map<String, Any>).addOnSuccessListener {
                    dialog.dismiss()
                    Toast.makeText(context,"تمت اضافة الوجبة",Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    dialog.dismiss()
                    Toast.makeText(context,"حصل خطأ في عملية الاضافة",Toast.LENGTH_SHORT).show();
                };


                mAlertDialog?.dismiss()
                updateHistory()

            }
            // extra detail add a success shape
        }
        mDialogView.cancelExcercise.setOnClickListener{
            mAlertDialog?.dismiss()

        }

    }

    private fun showLoadingDialog() {
        dialog = ProgressDialog.show(
            context, "",
            "الرجاء الانتظار", true
        )
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

    fun getSelectedDate(days:Int):String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formatted = current.format(formatter)

            return formatted
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        var dateobj = Date()
        var calender = Calendar.getInstance()
        calender.setTime(dateobj)
        calender.add(Calendar.DATE,days);
        val currentDate = sdf.format(calender.time)
        return "$currentDate"
    }
    fun updateHistory(){
        val data = hashMapOf(
            "cal" to remainderCal,
            "date" to getCurrentDate(),
            "user_id" to currentuser
        )
        if (!history_Id.equals(""))
            db.collection("History").document(history_Id).update(data as Map<String, Any>);
    }


    override fun onResume() {
        super.onResume()
        tvDate.text = getSelectedDate(previousDaysCount)
        advicesTV.text = showAddAdvice().toString()
    }
    private fun makeRequest() {
        ActivityCompat.requestPermissions((context as Activity?)!!, arrayOf(Manifest.permission.CAMERA),CAMERA_REQUEST_CODE)
    }
    private fun addAdviceDialog(){
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.advice_dialog, null)
        val mBuilder = AlertDialog.Builder(context!!)
            .setView(mDialogView)

        val  mAlertDialog = mBuilder.show()
        var body = mDialogView.dialogAdviceET!!.editText!!.text

        mAlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        mDialogView.dialogShareBtn.setOnClickListener{
            if (body.length > 140){
                Toast.makeText(context, "لا يمكن نشر نصيحة أطول من ١٤٠ حرف", Toast.LENGTH_LONG).show()
            }
            else if (body.isEmpty()){
                Toast.makeText(context, "لا يمكن ترك هذه الخانة فارغة ", Toast.LENGTH_LONG).show()
            }

            else {
                var body1=body.toString()
                val docData = hashMapOf(
                    "UID" to currentuser!!.toString(),
                    "text" to body1,
                    "date" to getCurrentDate()
                )
                db.collection("Advices").document().set(docData)
                    .addOnSuccessListener {
                        Log.d("main1","Added to collection")
                        Toast.makeText(context, "تمت  إضافة النصيحة", Toast.LENGTH_LONG).show()

                    }.addOnFailureListener {
                        Log.d("main1","not Added to collection"+it)
                        Toast.makeText(context, "حصل خطأ", Toast.LENGTH_LONG).show()
                    }
                mAlertDialog.dismiss()
            }

        }
        mDialogView.dialogCancelBtn.setOnClickListener{
            mAlertDialog.dismiss()

        }

    }

}