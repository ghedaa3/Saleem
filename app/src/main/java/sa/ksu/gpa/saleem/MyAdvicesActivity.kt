package sa.ksu.gpa.saleem

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MyAdvicesActivity : AppCompatActivity() {
    lateinit var adapter: MyAdvicesAdapter
    private lateinit var db: FirebaseFirestore
    lateinit var recyclerView: RecyclerView
    var key_list:ArrayList<String> = ArrayList()
    var list:ArrayList<MyAdvice> = ArrayList()
    val currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_advices)

        db = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.recyclerViewAdv)
        getAdviceData()
        Log.d("ADVICE","Current"+currentuser)
    }

    private fun getAdviceData() {
        db.collection("Advices").whereEqualTo("UID",currentuser).get().
        addOnSuccessListener{ documents ->
            for(document in documents){
                key_list.add(document.id)
                var adviceId= document.id
                var advicename= document.get("body").toString()
                var advice = MyAdvice( advicename)
                list.add(advice)
                Log.d("ADVICE","List : "+list)


            }
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


    }


}
