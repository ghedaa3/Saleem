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

class sharedRecipeInformaion : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
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
        recipeDescription= findViewById(R.id.recipe_info_dirctions)
        recipeIngLayout= findViewById(R.id.recipe_info_ings)
        recipeID = intent.extras!!.getString("RecipeId").toString()
        currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()

        backButton.setOnClickListener {
            onBackPressed()
        }




        isReporting()

        getcheckBox()
        getRecipeInfo()
    }
    fun showPopup(v: View) {
        PopupMenu(this, v).apply {
            // MainActivity implements OnMenuItemClickListener
            setOnMenuItemClickListener(this@sharedRecipeInformaion)
            inflate(R.menu.recipe)
            show()
        }
    }
    private fun isReporting() {
        db.collection("ReportedRecipes").whereEqualTo("UIDrporter",currentuser).whereEqualTo("recipeID",recipeID)
            .get().addOnSuccessListener { documents ->
                        if (documents.isEmpty()){
                            flag=false

                        }



            }.addOnFailureListener {
                Log.d("flag1", "isReporting is inside else =")

            }



    }


    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addAsAMeal -> {
                addasAmealDialog()
                true
            }
            R.id.reporting -> {

                isReporting()

                if (flag){
                    Log.d("flag1", "isReporting is ="+isReporting())
                    Toast.makeText(this, "لقد أبلغت عن هذه الوصفه سابقا", Toast.LENGTH_LONG).show()

                }
                else reportingDialog()

                true
            }
            else -> false
        }
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
    private fun reportingDialog(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.advice_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)

        val  mAlertDialog = mBuilder.show()
        mDialogView.textinadvice.text="بلاغك"
        mDialogView.dialogShareBtn.text="تبليغ"

        mAlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        mDialogView.dialogShareBtn.setOnClickListener{
            var body = mDialogView.dialogAdviceET!!.editText!!.text

            if (body.length > 140){
                Toast.makeText(this, "لا يمكن ان يكون البلاغ أطول من ١٤٠ حرف", Toast.LENGTH_LONG).show()
            }
            else if (body.isEmpty()){
                Toast.makeText(this, "لا يمكن ترك هذه الخانة فارغة ", Toast.LENGTH_LONG).show()
            }

            else {
                var body1=body.toString()

                val docData = hashMapOf(
                    "text" to body1,
                    "UIDrporter" to currentuser,
                    "recipeID" to recipeID


                )


              db.collection("ReportedRecipes").document(recipeID).set(docData).addOnSuccessListener {
                  Log.d("sharedRecipeInformaion", "added rports:" )


              }.addOnFailureListener {
                  Log.d("sharedRecipeInformaion", "error added rports:" )

              }
                Toast.makeText(this, "تم نشر البلاغ ", Toast.LENGTH_LONG).show()
                mAlertDialog.dismiss()
            }

        }
        mDialogView.dialogCancelBtn.setOnClickListener{
            mAlertDialog.dismiss()

        }


    }
    private fun addasAmealDialog(){


                 val data1 = hashMapOf(
                     "food_name" to recipeName.text.toString(),
                     "type" to "fromRecipes",
                     "date" to getCurrentDate(),
                     "user_id" to currentuser,
                     "cal_of_food" to recipeCalories.text.toString().toDouble()
                 )
                 db.collection("Foods").document().set(data1 as Map<String, Any>).addOnSuccessListener {
                     Toast.makeText(this,"تمت اضافة الوجبة",Toast.LENGTH_SHORT).show()
                 }.addOnFailureListener {
                     Toast.makeText(this,"حصل خطأ في عملية الاضافة",Toast.LENGTH_SHORT).show();
                 }

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
