package sa.ksu.gpa.saleem

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.home_fragment.*
import sa.ksu.gpa.saleem.profile.Profile
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class SettingFragment : Fragment() {
    private lateinit var db: FirebaseFirestore
    var totalcal  = 0.0
    var consumerCal = 0.0
    var remainderCal = 0.0
    var history_Id = ""
    private lateinit var pagerAdapter: PagerAdapter
    lateinit var dialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.activity_setting_fragment, container, false)
    return root
    }

    @SuppressLint("ResourceType")
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
        pagerAdapter = activity?.supportFragmentManager?.let { PagerAdapter(it) }!!
       /* viewPager.adapter = pagerAdapter
        dotsIndicator.setViewPager(viewPager)
        viewPager.adapter?.registerDataSetObserver(dotsIndicator.dataSetObserver)*/

//        view.findViewById<ImageView>(R.id.ivAddView).setOnClickListener { addFood() }
            view.findViewById<LinearLayout>(R.id.profile_page).setOnClickListener {
            val intent = Intent(this.activity, Profile::class.java)
            startActivity(intent)
            }
 /*       view.findViewById<LinearLayout>(R.id.add_lunch).setOnClickListener { addFood() }
        view.findViewById<LinearLayout>(R.id.add_dinner).setOnClickListener { addFood() }
        view.findViewById<LinearLayout>(R.id.add_snack).setOnClickListener { addFood() }*/
        db= FirebaseFirestore.getInstance()
        val currentuser = FirebaseAuth.getInstance().currentUser?.uid


     /*   db.collection("History")
            .whereEqualTo("date",getCurrentDate())
            .whereEqualTo("user_id","ckS3vhq8P8dyOeSI7CE7D4RgMiv1")
            .get().addOnSuccessListener { documents ->
                if(documents.isEmpty){
                    val data = hashMapOf(
                        "cal" to remainderCal,
                        "date" to getCurrentDate(),
                        "steps_count" to 1,
                        "user_id" to "ckS3vhq8P8dyOeSI7CE7D4RgMiv1"
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
            }*/
    }
/*
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


    fun updateHistory(){
        val data = hashMapOf(
            "cal" to remainderCal,
            "date" to getCurrentDate(),
            "user_id" to "ckS3vhq8P8dyOeSI7CE7D4RgMiv1"
        )
        if (!history_Id.equals(""))
            db.collection("History").document(history_Id).update(data as Map<String, Any>);
    }*/
}
