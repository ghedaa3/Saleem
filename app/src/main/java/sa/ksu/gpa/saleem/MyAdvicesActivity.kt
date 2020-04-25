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
import kotlinx.android.synthetic.main.advice_dialog.view.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList



    class MyAdvicesActivity : AppCompatActivity() {

        lateinit var adapter: MyAdvicesAdapter
        private lateinit var db: FirebaseFirestore
        lateinit var recyclerView: RecyclerView
        var cal : Double = 0.0
        var key_list:ArrayList<String> = ArrayList()
        var list:ArrayList<MyAdvice> = ArrayList()
        val currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_my_advices)

            db = FirebaseFirestore.getInstance()
            recyclerView = findViewById(R.id.recyclerViewAdv)
            getAdviceData()
        }

        private fun getAdviceData() {
            db.collection("Advices").whereEqualTo("UID",currentuser).get()
                .addOnSuccessListener{ documents ->
                for(document in documents){
                    key_list.add(document.id)
                    var title =document.get("text").toString()
                    var date =document.get("date").toString()
                    var myAdvice=MyAdvice(title,date)
                    list.add(myAdvice)
                    Log.d("ADV","List : "+list)


                }
                    list.sortByDescending { it.Date }

                    adapter = MyAdvicesAdapter(list,  object  : MyAdvicesAdapter.OnActionClick {
                    override fun onClick(item: MyAdvice, position: Int) {
                        showDescItem(item,position)
                    }

                    override fun onEdit(item: MyAdvice, position: Int) {
                        showEditItem(item,position)
                    }

                    override fun onDelete(item: MyAdvice, position: Int) {
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

        private fun showDescItem(item: MyAdvice, position: Int) {
            adapter.notifyDataSetChanged()

        }
        private fun deleteItem(item: MyAdvice, position: Int, key: String) {
            list.removeAt(position)
            key_list.removeAt(position)
            adapter.notifyDataSetChanged()
            db.collection("Advices").document(key).delete()
                .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }


        }

        private fun showEditItem(item: MyAdvice, position: Int) {
            editAdviceDialog(item,key_list[position], position)
            adapter.notifyDataSetChanged()

        }
        private fun  editAdviceDialog(item: MyAdvice,key:String,position: Int) {

            val mDialogView = LayoutInflater.from(this).inflate(R.layout.advice_dialog, null)

            val mBuilder =  AlertDialog.Builder(this)
                .setView(mDialogView)


            val  mAlertDialog = mBuilder?.show()
            mAlertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));


            mDialogView.dialogShareBtn.text = "تعديل"
            mDialogView.dialogShareBtn.setOnClickListener{

                val adviceName = mDialogView.dialogAdviceET!!.editText!!.text.toString()
                if (adviceName.isEmpty()){
                    Toast.makeText(this, "لا يمكن ترك أي خانة فارغة", Toast.LENGTH_LONG).show()
                }
                else{


                    val advice = hashMapOf(
                        "UID" to currentuser!!.toString(),
                        "text" to adviceName
                    )
                    db.collection("Advices").document(key).set(advice, SetOptions.merge())

//                    db.collection("users").document(currentuser).collection("Advices").document(key)
//                        .set(advice, SetOptions.merge())

                    mAlertDialog?.dismiss()
                    Toast.makeText(this, "تم تعديل النصيحة", Toast.LENGTH_LONG).show()

                }
                list.get(position).Title=adviceName
                adapter.notifyDataSetChanged()

                // extra detail add a success shape

            }
            mDialogView.dialogCancelBtn.setOnClickListener{
                mAlertDialog?.dismiss()

            }

        }


    }

