package sa.ksu.gpa.saleem.Admin

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_admin_recipes.*
import sa.ksu.gpa.saleem.R
import sa.ksu.gpa.saleem.recipe.SharedRecipe.viewSharedRecipeActivity
import sa.ksu.gpa.saleem.recipe.sharedRecipeInformaion
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AdminRecipes : AppCompatActivity() {
    lateinit var adapter: ReportedRecipesAdapter
    private lateinit var db: FirebaseFirestore
    lateinit var recyclerView: RecyclerView
    var cal: Double = 0.0
    var key_list: ArrayList<String> = ArrayList()
    var list: ArrayList<ReportedRecipes> = ArrayList()
    lateinit var recipeID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_recipes)
        db = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.recyclerViewRepRec)
        rep_rep_back_button.setOnClickListener {
            onBackPressed()
        }
        getReportedRecipes()
    }

    private fun getReportedRecipes() {
        db.collection("ReportedRecipes").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    key_list.add(document.id)
                    var userID = document.get("UIDrporter").toString()
                    recipeID = document.get("recipeID").toString()
                    var report = document.get("text").toString()

                    var reportedAdv = ReportedRecipes(userID, recipeID, report)
                    list.add(reportedAdv)
                    Log.d("ADMIN", "List : " + list)

                }
                adapter =
                    ReportedRecipesAdapter(list, object : ReportedRecipesAdapter.OnActionClick {

                        override fun onClick(item: ReportedRecipes, position: Int) {
                            showDescItem(item, position)
                            // editExcercizeDialog(item,key_list[position], position)
                        }


                        override fun onDelete(item: ReportedRecipes, position: Int) {
                            deleteItem(item, position, key_list[position])
                        }
                    }, this)
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

    private fun showDescItem(item: ReportedRecipes, position: Int) {
        adapter.notifyDataSetChanged()

    }

    private fun deleteItem(item: ReportedRecipes, position: Int, key: String) {
   /*     list.removeAt(position)
        key_list.removeAt(position)
        adapter.notifyDataSetChanged()
        db.collection("Recipes").document(key).delete()
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }*/
        val intent = Intent(this, sharedRecipeInformaion::class.java)
        intent.putExtra("RecipeId",key)

        startActivity(intent)


    }

    private fun showEditItem(item: ReportedRecipes, position: Int) {
        adapter.notifyDataSetChanged()
    }

}

