package sa.ksu.gpa.saleem.Admin

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_admin_advices.*
import sa.ksu.gpa.saleem.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class AdminAdvices : AppCompatActivity() {
    lateinit var adapter: ReportedAdvicesAdapter
    private lateinit var db: FirebaseFirestore
    lateinit var recyclerView: RecyclerView
    var cal : Double = 0.0
    var key_list:ArrayList<String> = ArrayList()
    var list:ArrayList<ReportedAdvices> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_advices)
        db = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.recyclerViewRepAdv)
        rep_adv_back_button.setOnClickListener{
            onBackPressed()
        }
        getReportedAdvices()
    }

    private fun getReportedAdvices() {
        db.collection("ReportedAdvices").get()
            .addOnSuccessListener{ documents ->
                for(document in documents){
                    key_list.add(document.id)
                    var userID = document.get("reporterUID").toString()
                    var adviceID = document.get("adviceID").toString()
                    var report = document.get("text").toString()
                    var reportedAdv = ReportedAdvices(userID, adviceID, report)
                    list.add(reportedAdv)
                    Log.d("ADMIN","List : "+list)

                }
                adapter = ReportedAdvicesAdapter(list,  object  : ReportedAdvicesAdapter.OnActionClick {
                    override fun onClick(item: ReportedAdvices, position: Int) {
                        showDescItem(item,position)
                    }

                    override fun onEdit(item: ReportedAdvices, position: Int) {
                        showEditItem(item,position)
                    }

                    override fun onDelete(item: ReportedAdvices, position: Int) {
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

    private fun showDescItem(item: ReportedAdvices, position: Int) {
        adapter.notifyDataSetChanged()

    }
    private fun deleteItem(item: ReportedAdvices, position: Int, key: String) {
        list.removeAt(position)
        key_list.removeAt(position)
        adapter.notifyDataSetChanged()
        db.collection("Advices").document(key).delete()
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }


    }

    private fun showEditItem(item: ReportedAdvices, position: Int) {
        adapter.notifyDataSetChanged()
    }
}
