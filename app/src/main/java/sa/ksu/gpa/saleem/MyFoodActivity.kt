package sa.ksu.gpa.saleem

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_my_food.*
import kotlinx.android.synthetic.main.add_excercise_dialog.view.addExcercise
import kotlinx.android.synthetic.main.add_excercise_dialog.view.addExcerciseburentCal
import kotlinx.android.synthetic.main.add_excercise_dialog.view.cancelExcercise
import kotlinx.android.synthetic.main.add_fast_food.view.*
import sa.ksu.gpa.saleem.MyFoodAdapter.OnActionClick
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MyFoodActivity : AppCompatActivity() {
    //MyFoodAdapter
    lateinit var adapter: MyFoodAdapter
    private lateinit var db: FirebaseFirestore
    lateinit var recyclerView: RecyclerView
     var foods1:ArrayList<AddFoodActivity.Item> =ArrayList<AddFoodActivity.Item>()

    var sum : Double = 0.0
    var  history_Id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_food)
        db = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.rv_my_food)

        my_meals_back_button.setOnClickListener{
            onBackPressed()
        }
        val currentuser = FirebaseAuth.getInstance().currentUser?.uid
        db.collection("History")
            .whereEqualTo("date", getCurrentDate())
            .whereEqualTo("user_id", currentuser)
            .get().addOnSuccessListener { documents ->

                for (document in documents) {
                    Log.d("hhh", "${document.id} => ${document.data}")
                    history_Id = document.id

                }
            }
        getMyFoodsData()



    }
    var list:ArrayList<MyFood> = ArrayList()
    var key_list:ArrayList<String> = ArrayList()
    fun getMyFoodsData() {
        val currentuser = FirebaseAuth.getInstance().currentUser?.uid



        db.collection("Foods")
            .whereEqualTo("user_id",currentuser)
            .get().addOnSuccessListener{ documents ->
            for(document in documents){
                key_list.add(document.id)
                var foods=document.get("foods")
                if (foods!=null){
                    foods as ArrayList<AddFoodActivity.Item>
                }else  foods=foods1

                var Id =document.id
                var foodName =document.get("food_name").toString()
                var type =document.get("type").toString()
                var date =document.get("date").toString()
                var uid =document.get("user_id").toString()
                var cal_of_food =document.get("cal_of_food").toString().toDouble()
                var type_of_food =document.get("type_of_food").toString()
                val myFood =MyFood(Id,foodName,type,foods,date,uid,cal_of_food,type_of_food)

                list.add(myFood)
                sum += myFood.cal_of_food

            }
                list.sortByDescending { it.Date }

                adapter = MyFoodAdapter(list,  object  : OnActionClick {
                    override fun onClick(item: MyFood, position: Int) {
                        showDescItem(item,position)
                    }

                    override fun onEdit(item: MyFood, position: Int) {
                        showEditItem(item,position)
                    }

                    override fun onDelete(item: MyFood, position: Int) {
                        deleteDialog(item,position, key_list[position])

                    }
                })
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = adapter
        }



    }

    private fun deleteDialog(item: MyFood, position: Int, s: String) {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("هل انت متأكد من حذف الوجبة؟")
            .setConfirmButton("نعم") { sDialog -> sDialog.dismissWithAnimation()
                deleteItem(item,position, item.Id)


            }.setCancelButton("إلغاء"){
                it.dismissWithAnimation()

            }
            .show()

    }

    private fun showDescItem(item: MyFood, position: Int) {

    }

    private fun showEditItem(item: MyFood, position: Int) {
        if(item.type == "Detailed"){
            var dialog: AddFoodActivity? = AddFoodActivity(this,item,item.type_of_food,item.Id,object :AddFoodActivity.OnSave{
                override fun onSaveSuccess(sum: Double) {

                }

            })
            dialog?.show()
            adapter.notifyDataSetChanged()

        }else if(item.type == "unDetailed"){
            addExcercizeDialog(item,item.Id,position)

        }

    }

    private fun deleteItem(item: MyFood, position: Int, key: String) {
        list.removeAt(position)
        key_list.removeAt(position)
        adapter.notifyDataSetChanged()
        db.collection("Foods").document(key)
            .delete()
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }
        var cal = sum - item.cal_of_food
        val data = hashMapOf("cal" to cal)

        db.collection("History").document(key)
            .set(data, SetOptions.merge()).addOnSuccessListener {
                Toast.makeText(this, "تم الحذف بنجاح", Toast.LENGTH_LONG).show()

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

    private fun addExcercizeDialog(
        item: MyFood,
        key: String,
        position: Int
    ) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_fast_food, null)

        val mBuilder =  AlertDialog.Builder(this)
                .setView(mDialogView)


        val  mAlertDialog = mBuilder?.show()
        mAlertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        mDialogView.addExcercise.text = "تعديل"
        mDialogView.addExcerciseWorkoutname.setText(item.food_name)
        mDialogView.addExcerciseburentCal.setText(item.cal_of_food.toString())

        mDialogView.addExcercise.setOnClickListener{
            var burnt = mDialogView.addExcerciseburentCal!!.text
            val workoutName = mDialogView.addExcerciseWorkoutname!!.text
            if (burnt.isEmpty()||workoutName.isEmpty()){
                Toast.makeText(this, "لا يمكن ترك أي خانة فارغة", Toast.LENGTH_LONG).show()
            }
            else{

                val data1 = hashMapOf(
                    "food_name" to workoutName.toString(),
                    "cal_of_food" to (burnt.toString().toDouble())
                )


                db.collection("Foods").document(key)
                    .set(data1, SetOptions.merge())

                mAlertDialog?.dismiss()
            }
            // extra detail add a success shape
            Toast.makeText(this, "تم تعديل الوجبة", Toast.LENGTH_LONG).show()

            list.get(position).food_name=workoutName.toString()
            list.get(position).cal_of_food=burnt.toString().toDouble()
            adapter.notifyDataSetChanged()
        }
        mDialogView.cancelExcercise.setOnClickListener{
            mAlertDialog?.dismiss()

        }

    }

}
