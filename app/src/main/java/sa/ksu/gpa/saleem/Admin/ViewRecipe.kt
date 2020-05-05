package sa.ksu.gpa.saleem.recipe

import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.add_excercise_dialog.view.*
import kotlinx.android.synthetic.main.add_excercise_dialog.view.addExcercise
import kotlinx.android.synthetic.main.add_excercise_dialog.view.addExcerciseburentCal
import kotlinx.android.synthetic.main.add_fast_food.view.*
import kotlinx.android.synthetic.main.advice_dialog.*
import kotlinx.android.synthetic.main.advice_dialog.view.*
import kotlinx.android.synthetic.main.fragment_home_body.*
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.info_dynamic_ingredients.view.*
import kotlinx.android.synthetic.main.shared_recipe_info.*
import sa.ksu.gpa.saleem.AddFoodActivity
import sa.ksu.gpa.saleem.R
import sa.ksu.gpa.saleem.recipe.SharedRecipe.RecipeModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ViewRecipe : AppCompatActivity() {


    private lateinit var db: FirebaseFirestore
    private lateinit var recipeName: TextView
    private lateinit var recipeCalories: TextView
    private lateinit var recipeDescription:  TextView
    private lateinit var recipeImage:  ImageView
    private lateinit var backButton:  ImageView
    private lateinit var reporton:  ImageView

    private lateinit var recipeIngLayout:  LinearLayout
    lateinit var recipeID:String
    lateinit var currentuser:String
    var flag:Boolean=true



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shared_recipe_info)
        db = FirebaseFirestore.getInstance()
        recipeName= findViewById(R.id.recipe_info_title)
        recipeCalories= findViewById(R.id.recipe_info_calories)
       recipeImage= findViewById(R.id.recipe_info_image)
        backButton= findViewById(R.id.back_button)
        reporton= findViewById(R.id.reporton)
        reporton.visibility=View.INVISIBLE
        recipeDescription= findViewById(R.id.recipe_info_dirctions)
        recipeIngLayout= findViewById(R.id.recipe_info_ings)
        recipeID = intent.extras!!.getString("RecipeId").toString()
      //  currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()

        backButton.setOnClickListener {
            onBackPressed()
        }




        getcheckBox()
        getRecipeInfo()
    }

    private fun getcheckBox() {
        var s:String=""
        var recipeType:ArrayList<String>
        db.collection("Recipes").document(recipeID)
            .get()
            .addOnSuccessListener { document ->
                recipeType= document.get("Type") as ArrayList<String>
                Log.d("sharedRecipeInformaion","size"+recipeType.size)
                if (recipeType.size!=0){

                    var i=recipeType.size-1
                    while(i!=0){
                        if (recipeType[i]!="not")
                            s=s+"\n"+"#"+recipeType[i]
                        Log.d("sharedRecipeInformaion","recipePrepration: "+s)
                        i--


                    }


                }

                recipe_info_types.text=s

            }
        Log.d("sharedRecipeInformaion","recipePrepration: "+s)

    }
    private fun getRecipeInfo() {
        lateinit var  recipename : String
        lateinit var recipeCalproes: String
        lateinit var recipeImage : String
        lateinit var recipePrepration: String
        lateinit var recipeId: String
        db.collection("Recipes").document(recipeID)
            .get()
            .addOnSuccessListener { document ->


                recipeId= document.id
                recipename= document.get("name").toString()
                recipeCalproes=document.get("calories").toString()
                recipeImage= document.get("image").toString()
                recipePrepration= document.get("prepration").toString()
                Log.d("sharedRecipeInformaion","recipePrepration: "+recipePrepration)

                addIngrediants()
                connectRecipesWithVies(recipename,recipeCalproes,recipeImage,recipePrepration)
            }


            .addOnFailureListener { exception ->
                Log.w("sharedRecipeInformaion", "Error getting documents: ", exception)
            }
    }
    private fun addIngrediants() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        db.collection("Recipes").document(recipeID).collection("ingredients").get()
            .addOnSuccessListener {  documents ->
                for (document in documents){
                    var rowView: View = inflater.inflate(R.layout.info_dynamic_ingredients, null)
                    rowView.ingredint_info.text= document.get("ingreidentName").toString()
                    rowView.ingredint_quantity.text=document.get("ingquantity").toString()+" "+document.get("ingredienunit").toString()
                    recipeIngLayout.addView(rowView, recipeIngLayout.getChildCount())

                }
            }
            .addOnFailureListener {
                Log.w("sharedRecipeInformaion", "Error getting documents ings: ", it)

            }


    }
    private fun connectRecipesWithVies(recipename: String, recipeCalproes: String, recipeimage: String, recipePrepration: String) {
        recipeName.text=recipename
        recipeCalories.text=recipeCalproes
        recipeDescription.text=recipePrepration
       Glide.with(this).load(recipeimage).into(recipeImage)
    }


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
}
