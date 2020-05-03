package sa.ksu.gpa.saleem.Admin

import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_admin_advices.*
import kotlinx.android.synthetic.main.advice_dialog.view.*
import sa.ksu.gpa.saleem.MyAdvice
import sa.ksu.gpa.saleem.R
import sa.ksu.gpa.saleem.loginn
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class AdminAdvices : AppCompatActivity() {
    lateinit var adapter: ReportedAdvicesAdapter
    private lateinit var db: FirebaseFirestore
    lateinit var recyclerView: RecyclerView
    lateinit var adviceID: String
    lateinit var advice: String
    lateinit var title: String
    var cal: Double = 0.0
    var key_list: ArrayList<String> = ArrayList()
    var list: ArrayList<ReportedAdvices> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_advices)
        db = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.recyclerViewRepAdv)
        rep_adv_back_button.setOnClickListener {
            onBackPressed()
        }
        getReportedAdvices()
    }

    private fun getReportedAdvices() {
        db.collection("ReportedAdvices").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    key_list.add(document.id)
                    var userID = document.get("reporterUID").toString()
                    adviceID = document.get("adviceID").toString()
                    advice = document.get("advice").toString()
                    var report = document.get("text").toString()
                    var reportedAdv = ReportedAdvices(userID, adviceID, report, advice)
                    list.add(reportedAdv)
                    Log.d("ADMIN", "List : " + list)

                }
                adapter =
                    ReportedAdvicesAdapter(list, object : ReportedAdvicesAdapter.OnActionClick {
                        override fun onClick(item: ReportedAdvices, position: Int) {
                            showDescItem(item, position)
                        }


                        override fun onDelete(item: ReportedAdvices, position: Int) {
                            deleteItem(item, position, key_list[position])
                        }
                    })
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = adapter
            }


    }

    fun getCurrentDate(): String {
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
        showAdviceDialog(item, key_list[position], position)
        adapter.notifyDataSetChanged()

    }

    private fun deleteItem(item: ReportedAdvices, position: Int, key: String) {


        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("هل متاكد من حذف النصيحة؟")
            .setConfirmButton("نعم") { sDialog -> sDialog.dismissWithAnimation()
                list.removeAt(position)
                key_list.removeAt(position)
                adapter.notifyDataSetChanged()
                db.collection("ReportedAdvices").document(key).delete()
                    .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully deleted!") }
                    .addOnFailureListener { e ->
                        Log.w("TAG", "Error deleting document", e)
                    }
            }.setCancelButton("إلغاء"){
                it.dismissWithAnimation()

            }
            .show()
    }


    private fun showEditItem(item: ReportedAdvices, position: Int) {
        adapter.notifyDataSetChanged()
    }

    private fun showAdviceDialog(item: ReportedAdvices, key: String, position: Int) {

        db.collection("ReportedAdvices").whereEqualTo("advice", advice).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    advice = document.get("advice").toString()
                    Log.d("ADMIN", "List : " + list)
                }
                var text = list[position].advice
                showDialogWithOkButton(text)
            }
    }

    private fun showDialogWithOkButton(msg: String) {
        SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
            .setTitleText(msg)
            .setConfirmButton("حسناً") { sDialog ->
                sDialog.dismissWithAnimation()


            }
            .show()
    }
}
