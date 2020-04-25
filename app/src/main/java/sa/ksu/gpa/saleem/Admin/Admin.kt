package sa.ksu.gpa.saleem.Admin

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.user_list.*
import sa.ksu.gpa.saleem.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class Admin : AppCompatActivity() {

    lateinit var adapter: AdminAdapter
    private lateinit var db: FirebaseFirestore
    lateinit var recyclerView: RecyclerView
    var cal : Double = 0.0
    var key_list:ArrayList<String> = ArrayList()
    var list:ArrayList<MyAdmin> = ArrayList()
    val currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_list)

        db = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.recyclerViewUsers)
        users_back_button.setOnClickListener{
            onBackPressed()
        }
        getUserData()
    }

    private fun getUserData() {
        db.collection("users").get()
            .addOnSuccessListener{ documents ->
                for(document in documents){
                    key_list.add(document.id)
                    var title = document.get("name").toString()
                    var email = document.get("email").toString()
                    var admin = MyAdmin(title, email)
                    list.add(admin)
                    Log.d("ADMIN","List : "+list)

                }

                adapter = AdminAdapter(list,  object  : AdminAdapter.OnActionClick {
                    override fun onClick(item: MyAdmin, position: Int) {

                        showDescItem(item,position)
                    }

                    override fun onEdit(item: MyAdmin, position: Int) {
                        showEditItem(item,position)
                    }


                    override fun onDelete(item: MyAdmin, position: Int) {
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

    private fun showDescItem(item: MyAdmin, position: Int) {

        adapter.notifyDataSetChanged()

    }
    private fun deleteItem(item: MyAdmin, position: Int, key: String) {
        list.removeAt(position)
        key_list.removeAt(position)
        adapter.notifyDataSetChanged()
        db.collection("users").document(key).delete()
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }


    }

    private fun showEditItem(item: MyAdmin, position: Int) {
        adapter.notifyDataSetChanged()
    }


}
