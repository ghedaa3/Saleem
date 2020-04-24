package sa.ksu.gpa.saleem

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class Admin : AppCompatActivity() {

    lateinit var adapter: AdminAdapter
    private lateinit var db: FirebaseFirestore
    lateinit var recyclerView: RecyclerView
    var key_list: ArrayList<String> = ArrayList()
    var list: ArrayList<MyAdmin> = ArrayList()
    private val TAG = "Admin"
  //  val currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_list)

        db = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.recyclerViewExcer)

        getUserData()
    }

    private fun getUserData() {

        db.collection("users").get().addOnSuccessListener { result ->
            for (document in result) {
                key_list.add(document.id)
                var title = document.get("name").toString()
                var email = document.get("email").toString()
                var admin = MyAdmin(title, email)
                list.add(admin)
                Log.d(TAG, "${document.id} => ${document.data}")
            }
        }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
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






    private fun deleteItem(item: MyAdmin, position: Int, s: String) {

    }

    private fun showEditItem(item: MyAdmin, position: Int) {


    }

    private fun showDescItem(item: MyAdmin, position: Int) {
        adapter.notifyDataSetChanged()

    }
}
