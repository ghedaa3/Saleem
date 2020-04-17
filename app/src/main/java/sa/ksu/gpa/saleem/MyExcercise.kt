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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.add_excercise_dialog.view.*
import kotlinx.android.synthetic.main.add_excercise_dialog.view.addExcercise
import kotlinx.android.synthetic.main.add_excercise_dialog.view.addExcerciseburentCal
import kotlinx.android.synthetic.main.add_excercise_dialog.view.cancelExcercise
import kotlinx.android.synthetic.main.add_fast_food.view.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MyExcercise : AppCompatActivity() {
    lateinit var adapter: MyExcersieAdapter
    private lateinit var db: FirebaseFirestore
    lateinit var recyclerView: RecyclerView
    var cal : Double = 0.0
    var key_list:ArrayList<String> = ArrayList()
    var list:ArrayList<MyExcersie> = ArrayList()
    val currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_excercise)

        db = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.recyclerViewExcer)
        getExcerciseData()
        //TODO(get currrent user id)


    }

    private fun getExcerciseData() {

        db.collection("users").document(currentuser).collection("Exercises").
         get().addOnSuccessListener{ documents ->
            for(document in documents){
                key_list.add(document.id)
                var title =document.get("exerciseName").toString()
                var calori =document.get("exerciseCalories").toString()
                var myExcercise=MyExcersie(title,calori)
                list.add(myExcercise)
                Log.d("EX","List : "+list)


            }
            adapter = MyExcersieAdapter(list,  object  : MyExcersieAdapter.OnActionClick {
                override fun onClick(item: MyExcersie, position: Int) {
                    showDescItem(item,position)
                }

                override fun onEdit(item: MyExcersie, position: Int) {
                    showEditItem(item,position)
                }

                override fun onDelete(item: MyExcersie, position: Int) {
                    deleteItem(item,position, key_list[position])
                }
            })
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
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

    private fun showDescItem(item: MyExcersie, position: Int) {
        adapter.notifyDataSetChanged()

    }
    private fun deleteItem(item: MyExcersie, position: Int, key: String) {
        list.removeAt(position)
        key_list.removeAt(position)
        adapter.notifyDataSetChanged()
        db.collection("users").document(currentuser).collection("Exercises").document(key).delete()
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }


    }

    private fun showEditItem(item: MyExcersie, position: Int) {
            editExcercizeDialog(item,key_list[position], position)
        adapter.notifyDataSetChanged()

    }
    private fun  editExcercizeDialog(item: MyExcersie,key:String,position: Int) {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.add_excercise_dialog, null)

        val mBuilder =  AlertDialog.Builder(this)
            .setView(mDialogView)


        val  mAlertDialog = mBuilder?.show()
        mAlertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));


        mDialogView.addExcercise.text = "تعديل"
        mDialogView.addExcerciseWorkoutnameee.setText(item.Title)
        mDialogView.addExcerciseburentCal.setText(item.Claories)
        mDialogView.addExcercise.setOnClickListener{

            var burnt = mDialogView.addExcerciseburentCal!!.text
            val workoutName = mDialogView.addExcerciseWorkoutnameee!!.text.toString()
            if (burnt.isEmpty()||workoutName.isEmpty()){
                Toast.makeText(this, "لا يمكن ترك أي خانة فارغة", Toast.LENGTH_LONG).show()
            }
            else{

                val data1 = hashMapOf(
                    "exerciseName" to workoutName,
                    "exerciseCalories" to (burnt.toString().toDouble())
                )


                db.collection("users").document(currentuser).collection("Exercises").document(key)
                    .set(data1, SetOptions.merge())

                mAlertDialog?.dismiss()
                Toast.makeText(this, "تم تعديل التمرين", Toast.LENGTH_LONG).show()


//TODO(update burnt calories filed)
            }
            list.get(position).Title=workoutName
            list.get(position).Claories=burnt.toString()
            adapter.notifyDataSetChanged()

            // extra detail add a success shape

        }
        mDialogView.cancelExcercise.setOnClickListener{
            mAlertDialog?.dismiss()

        }

    }


}
