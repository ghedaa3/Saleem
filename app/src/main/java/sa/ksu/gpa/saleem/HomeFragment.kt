package sa.ksu.gpa.saleem


import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
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
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.add_excercise_dialog.view.addExcercise
import kotlinx.android.synthetic.main.add_excercise_dialog.view.addExcerciseburentCal
import kotlinx.android.synthetic.main.add_excercise_dialog.view.cancelExcercise
import kotlinx.android.synthetic.main.add_fast_food.view.*
import kotlinx.android.synthetic.main.add_water.view.*
import kotlinx.android.synthetic.main.advice_dialog.view.*
import kotlinx.android.synthetic.main.fragment_home_body.*
import kotlinx.android.synthetic.main.home_fragment.*
import sa.ksu.gpa.saleem.AddFoodActivity.OnSave
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    var totalcal  = 0.0
    var consumerCal = 0.0
    var remainderCal = 0.0
    var previousDaysCount = 0
    var history_Id = ""
    var currentuser = ""
    private var waterCount=0
    private lateinit var adviceID:String
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
        view.findViewById<LinearLayout>(R.id.add_water).setOnClickListener { addWater() }


        view.findViewById<LinearLayout>(R.id.add_snack).setOnClickListener { addFood("snack") }
        db= FirebaseFirestore.getInstance()
        currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        ubdateBurntCaloris()
        updateWater()




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
    private fun addWater(){
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.add_water, null)
        val mBuilder = activity?.let {
            AlertDialog.Builder(it)
                .setView(mDialogView)
        }

        val  mAlertDialog = mBuilder?.show()
        mAlertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        var water = mDialogView.addWaterAmount!!.text
        Log.d("this",""+water)

        mDialogView.addWaterBtn.setOnClickListener{

            if (water.isEmpty()){
                Toast.makeText(context, "لا يمكن ترك هذه الخانة فارغة", Toast.LENGTH_LONG).show()
            }
            else{
                var waterStringData = water.toString()
                var data =  waterStringData.toInt()
                waterCount += data

                if(data>15 || waterCount>15){
                    waterCount -= data
                    Toast.makeText(context,"هذه الكمية أكثر من الاحتياج اليومي للماء",Toast.LENGTH_SHORT).show() }

                else{
                    val docData = hashMapOf(
                        "date" to getCurrentDate(),
                        "user_id" to currentuser,
                        "amountOfWater" to waterStringData.toInt() )
                    db.collection("users").document(currentuser!!).collection("Water")
                        .document().set(docData)
                        .addOnSuccessListener {
                            Toast.makeText(context,"تمت إضافة كمية الماء",Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(context,"حصل خطأ في عملية الإضافة",Toast.LENGTH_SHORT).show();
                        }
                    mAlertDialog?.dismiss()
                    updateWater()
                }
            }
        }
        mDialogView.cancelWaterBtn.setOnClickListener{
            mAlertDialog?.dismiss()
        }
    }

    private fun  updateWater(){
        var totalWaterAmount = 0
        db.collection("users").document(currentuser!!)
            .collection("Water").whereEqualTo("date",getCurrentDate()).get().addOnSuccessListener {
                for (documents in it){
                    totalWaterAmount+=documents.get("amountOfWater").toString().toInt()

                }
                if(totalWaterAmount!=null)
                    waterAmountTV.text= totalWaterAmount.toString()
                else
                    waterAmountTV.text="0"
            }
    }

//    private fun showAddAdvice() {
//        var advicesList:ArrayList<String> = ArrayList()
//        val rand = Random()
////        db.collection("Advices").whereEqualTo("date",getCurrentDate())
////            .get().addOnSuccessListener {documents ->
//        db.collection("Advices").whereEqualTo("date",getCurrentDate()).get().addOnSuccessListener {
//                for (documents in it){
//                    advicesList.add(documents.get("text").toString())
//                }
//                if(advicesList!=null){
//            val index = rand.nextInt(advicesList.size - 1)
//                    advicesTV.text = (advicesList[index])
//                }
//                else
//                    showLastAdv()
//            }
//    }
          private fun showAddAdvice() {
        db.collection("Advices").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    adviceID = document.id
                    var title = document.get("text").toString()
                    advicesTV.text = title
                }
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

                    db.collection("ReportedAdvices").document(adviceID).set(docData).addOnSuccessListener {
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
        var totalBurntCalories:Double=0.0
        db.collection("users").document(currentuser!!).collection("Exercises").whereEqualTo("date",getCurrentDate()).get().addOnSuccessListener {
            for (documents in it){
                totalBurntCalories+=documents.get("exerciseCalories").toString().toDouble()

            }
            if(totalBurntCalories!=null)
                burnt_calories_textview.text= totalBurntCalories.toString()
            else
                burnt_calories_textview.text="0"


        }


    }

//    private fun addWater() {
//        if (counter < 8) {
//            val inflater = activity
//                ?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            val rowView = inflater.inflate(R.layout.water_field, null)
//            waterInnerLL.addView(rowView, waterInnerLL.childCount - 1)
//            counter++
//            waterAmountTV.text = "$counter"
//            rowView.setOnClickListener { myOnClick(rowView) }
//        }
//    }
//    fun myOnClick(v: View) {
//        if (counter > 0) {
//            waterInnerLL.removeView(v.parent as View)
//            counter--
//            waterAmountTV.text = "$counter"
//        }
//    }


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

//        var intent = Intent(activity,AddFoodActivity::class.java).apply{
//
//        }
//        startActivityForResult(intent,1000)
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
            "Loading. Please wait...", true
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
}