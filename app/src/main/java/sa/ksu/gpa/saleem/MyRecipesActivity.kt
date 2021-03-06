package sa.ksu.gpa.saleem

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_recipes.*
import kotlinx.android.synthetic.main.activity_notification.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class MyRecipesActivity : AppCompatActivity() {
     lateinit var adapter: MyRecipesApapter
    private lateinit var db: FirebaseFirestore
    lateinit var recyclerView: RecyclerView
    var cal : Double = 0.0
    var key_list:ArrayList<String> = ArrayList()
    var date_list:ArrayList<String> = ArrayList()
    var list:ArrayList<MyRecipe> = ArrayList()
    val currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_recipes)

        db = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.recyclerViewRes)
        Log.d("RECIPE","Cureent"+currentuser)
        my_exe_back_button.setOnClickListener { onBackPressed() }
        getExcerciseData()
    }



    private fun getExcerciseData() {

        db.collection("Recipes").whereEqualTo("UID",currentuser).get().
            addOnSuccessListener{ documents ->
                for(document in documents){
                    key_list.add(document.id)
                    var recipeDate=document.get("date").toString()
                    date_list.add(recipeDate)
                    var recipename= document.get("name").toString()
                    var recipeId= document.get("UID").toString()
                    var recipe = MyRecipe( recipename,recipeDate,recipeId)
                    list.add(recipe)

                }
                 list.sortByDescending { it.Date }


                adapter = MyRecipesApapter(list,  object  : MyRecipesApapter.OnActionClick {
                override fun onClick(item: MyRecipe, position: Int) {
                    showDescItem(item,position)
                }

                override fun onEdit(item: MyRecipe, position: Int) {
                    showEditItem(item,position)
                }

                override fun onDelete(item: MyRecipe, position: Int) {
                    deleteDialog(item,position, key_list[position])
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

    private fun showDescItem(item: MyRecipe, position: Int) {
        adapter.notifyDataSetChanged()

    }
    private fun deleteItem(item: MyRecipe, position: Int, key: String) {
        list.removeAt(position)
        key_list.removeAt(position)
        adapter.notifyDataSetChanged()
        db.collection("Recipes").document(key).delete()
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }


    }
    private fun deleteDialog(
        item: MyRecipe,
        position: Int,
        s: String
    ) {
     SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("هل انت متأكد من حذف الوصفة؟")
            .setConfirmButton("حسنًا") { sDialog -> sDialog.dismissWithAnimation()
                deleteItem(item,position, key_list[position])


            }.setCancelButton("إلغاء"){
                 it.dismissWithAnimation()
            }
            .show()

    }
    private fun showEditItem(item: MyRecipe, position: Int) {
        editExcercizeDialog(key_list[position])
        Log.d("EditMyRecipe","key_list[position]"+key_list[position])

        adapter.notifyDataSetChanged()

    }
    private fun  editExcercizeDialog(key:String) {
        
        var intent=Intent(this,EditMyRecipe::class.java)
        intent.putExtra("RecipeID",key)
        Log.d("EditMyRecipe + key ",key)
        startActivity(intent)

    }


}
