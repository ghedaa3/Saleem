package sa.ksu.gpa.saleem

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.core.view.get
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_share_recipe_first.*
import kotlinx.android.synthetic.main.field.view.*
import pl.utkala.searchablespinner.SearchableSpinner
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class EditMyRecipe : AppCompatActivity() , View.OnClickListener {
    var id:String = ""
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var storageReference: StorageReference? = null
    private lateinit var image :TextView
    private lateinit var publishRecipe: Button

    private  var Quantity: EditText? = null
    private  var unit: Spinner? = null
    private  var IngredientNames: SearchableSpinner? = null
    var TotalCalories:Int=0

    private lateinit var addIngrediant:Button
    private lateinit var db: FirebaseFirestore
    private lateinit var recipeName: TextView
    private lateinit var recipeDescription: TextView
    private lateinit var recipeImage: ImageView
    private lateinit var backButton: ImageView
    private lateinit var recipeIngLayout: LinearLayout
    lateinit var IngArray:Array<String>
    lateinit var quanArray:Array<String>
    private  var uri:String?=null
    private lateinit var checkBox: CheckBox
    private lateinit var checkBox1: CheckBox
    private lateinit var checkBox2: CheckBox
    private lateinit var checkBox3: CheckBox
    private lateinit var checkBox4: CheckBox
    private lateinit var checkBox5: CheckBox
    lateinit var currentuser:String
     var currenImage:String=""
    var key_list:ArrayList<String> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_recipe_first)
        currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        id=intent.extras!!.getString("RecipeID").toString()
        IngArray = resources.getStringArray(R.array.Ingredients)
        quanArray= resources.getStringArray(R.array.Quantities)
        recipeName= findViewById(R.id.Recipename)
        recipeImage= findViewById(R.id.sharedrecipeimageReal)
        backButton= findViewById(R.id.back_button)
        recipeDescription= findViewById(R.id.prepration)
        recipeIngLayout= findViewById(R.id.main)
        db = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().getReference().child(currentuser).child("image"+Math.random())
        image=findViewById(R.id.sharedrecipeimage)
        addIngrediant = findViewById(R.id.addIngredient)
        publishRecipe = findViewById(R.id.publishRecipe)
        checkBox = findViewById(R.id.checkBox)
        checkBox1 = findViewById(R.id.checkBox1)
        checkBox2= findViewById(R.id.checkBox2)
        checkBox3= findViewById(R.id.checkBox3)
        checkBox4= findViewById(R.id.checkBox4)
        checkBox5= findViewById(R.id.checkBox5)
        Quantity= findViewById(R.id.quantity)
        IngredientNames= findViewById(R.id.IngredientNames)
        unit= findViewById(R.id.unit)
        image.setOnClickListener(this)
        addIngrediant.setOnClickListener(this)
        publishRecipe.setOnClickListener(this)
        backButton.setOnClickListener(this)
        getRecipeData()
        getcheckBox()
    }

    private fun getcheckBox() {
        var recipeType:ArrayList<String>
        db.collection("Recipes").document(id)
            .get()
            .addOnSuccessListener { document ->
           recipeType= document.get("Type") as ArrayList<String>
           Log.d("sharedRecipeInformaion","size"+recipeType.size)
                if (recipeType.size!=0){

                        if (recipeType[0]!="not")
                            checkBox.isChecked=true
                        if (recipeType[1]!="not")
                            checkBox1.isChecked=true
                        if (recipeType[2]!="not")
                            checkBox2.isChecked=true
                        if (recipeType[3]!="not")
                            checkBox3.isChecked=true
                        if (recipeType[4]!="not")
                            checkBox4.isChecked=true
                        if (recipeType[5]!="not")
                            checkBox5.isChecked=true

                }


    }

    }
    private fun getRecipeData() {
        lateinit var  recipename : String
        lateinit var recipeCal: String
        lateinit var recipeImage : String
        lateinit var recipePrepration: String
        lateinit var recipeId: String
        db.collection("Recipes").document(id)
            .get()
            .addOnSuccessListener { document ->


                recipeId= document.id
                recipename= document.get("name").toString()
                recipeCal=document.get("calories").toString()
                recipeImage= document.get("image").toString()
                currenImage= document.get("image").toString()
                Log.d("nullll","recipeImage="+ recipeImage)
                Log.d("nullll","currenImage="+ currenImage)

                recipePrepration= document.get("prepration").toString()
                Log.d("sharedRecipeInformaion","recipePrepration: "+recipePrepration)

                getIngrediants()
                connectRecipesWithVies(recipename,recipeCal,recipeImage,recipePrepration)
            }


            .addOnFailureListener { exception ->
                Log.w("sharedRecipeInformaion", "Error getting documents: ", exception)
            }
    }
    private fun getIngrediants(){
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        db.collection("Recipes").document(id).collection("ingredients").get()
            .addOnSuccessListener {  documents ->
                for (document in documents){

                    key_list.add(document.id)

                    var rowView: View = inflater.inflate(R.layout.field, null)

                    var quantiti =document.get("ingquantity").toString()
                    var IngNAme=document.get("ingreidentName").toString()
                    var unitNAme=document.get("ingredienunit").toString()
                    rowView.quantity.setText(quantiti)

                    //to select an item
                    var i =findIndex(IngArray,IngNAme)
                    rowView.IngredientNames.setSelection(i)

                    var j =findIndex(quanArray,unitNAme)
                    rowView.unit.setSelection(j)

                    recipeIngLayout.addView(rowView, recipeIngLayout.getChildCount())

                }
            }
            .addOnFailureListener {
                Log.w("sharedRecipeInformaion", "Error getting documents ings: ", it)

            }


    }
    private fun addIngrediants(ingIndex: Int, unitIndex: Int, quantity: String) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.field, null)
        rowView.unit.setSelection(unitIndex)
        rowView.IngredientNames.setSelection(ingIndex)
        rowView.quantity.setText(quantity)

        recipeIngLayout.addView(rowView, recipeIngLayout.getChildCount())
        setAddtoDefault()
    }

    private fun setAddtoDefault() {
        Main_unit.setSelection(0)
        Main_IngredientNames.setSelection(0)
        Main_quantity.setText("")
    }

    private fun updateIngrediants(recipeID:String) {
        var j =key_list.size
        var i = recipeIngLayout.childCount-key_list.size
        var z=recipeIngLayout.childCount
        var count =0
        //to update pre ings
        if (key_list.size==recipeIngLayout.childCount)
        while (j!=0){

            var ingquantity = recipeIngLayout.getChildAt(count)!!.quantity.text.toString()
            Log.d("please","quantity"+ingquantity)
            var ingqunit = recipeIngLayout.getChildAt(count).IngredientNames.selectedItem.toString()
            Log.d("please","unit"+ingqunit)
            var real=recipeIngLayout.getChildAt(count).unit.selectedItem.toString()
            Log.d("please","real"+real)
            val docData = hashMapOf(
                "ingreidentName" to ingqunit,
                "ingredienunit" to real,
                "ingquantity" to ingquantity
            )
            db.collection("Recipes").document(recipeID).collection("ingredients").document(key_list[count]).set(docData).addOnSuccessListener {
                Log.d("please","ing added ")
            }.addOnFailureListener{
                Log.d("please","not added ")

            }
              j--
            count++

        }
        //to add new recipes
        var x = key_list.size
        if(recipeIngLayout.childCount>key_list.size)
        while (i!=0){




            var ingquantity = recipeIngLayout.getChildAt(x)!!.quantity.text.toString()
            var ingqunit = recipeIngLayout.getChildAt(x).IngredientNames.selectedItem.toString()
            var real=recipeIngLayout.getChildAt(x).unit.selectedItem.toString()

            val docData = hashMapOf(
                "ingreidentName" to ingqunit,
                "ingredienunit" to real,
                "ingquantity" to ingquantity
            )
            db.collection("Recipes").document(recipeID).collection("ingredients").document().set(docData).addOnSuccessListener {
                Log.d("please","ing added ")
            }.addOnFailureListener{
                Log.d("please","not added ")

            }

            i--
            x++

        }
       //to delete an old recipes
        if(recipeIngLayout.childCount<key_list.size){

            //remove old document
            var x=0
            var g= key_list.size
            while(g!=0){
            db.collection("Recipes").document(recipeID).collection("ingredients").document(key_list[x]).delete().addOnSuccessListener {
                Log.d("ing","removed ")
            }.addOnFailureListener{
                Log.d("ing","not removed ")

            }
                g--
                x++
            }
            // add new docs
            var a=recipeIngLayout.childCount
            var v=0

            while (a!=0){
                var ingquantity = recipeIngLayout.getChildAt(v)!!.quantity.text.toString()
                var ingqunit = recipeIngLayout.getChildAt(v).IngredientNames.selectedItem.toString()
                var real=recipeIngLayout.getChildAt(v).unit.selectedItem.toString()

                val docData = hashMapOf(
                    "ingreidentName" to ingqunit,
                    "ingredienunit" to real,
                    "ingquantity" to ingquantity
                )
                db.collection("Recipes").document(recipeID).collection("ingredients").document().set(docData).addOnSuccessListener {
                    Log.d("Ing","ing added ")
                }.addOnFailureListener{
                    Log.d("ing","not added ")

                }

                a--
                v++



            }
        }

    }



    fun onDelete(view: View) {
        recipeIngLayout.removeView(view.getParent() as View)
    }
    private fun findIndex(dataArray: Array<String>, item: String): Int {
        for (i in dataArray.indices) {
            if (dataArray[i] == item) {
                return i
            }
        }
        return -1
    }
    private fun connectRecipesWithVies(recipename: String, recipeCal: String, recipeIm: String, recipePrepration: String) {

            recipeName.text=recipename
            recipeDescription.text=recipePrepration
            Glide.with(this).load(recipeIm).into(recipeImage)

        }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.sharedrecipeimage -> {
                launchGallery()
            }
            R.id.addIngredient -> {
                takeIngredients()

            }
            R.id.publishRecipe->{

                db.collection("Recipes").document(id).get().addOnSuccessListener { documentSnapshot ->
                    currenImage= documentSnapshot.get("image").toString()

                }
                Log.d("nulll","currenImage+"+currenImage)

                updateRecipe(currenImage)
            }
            R.id.back_button->{
                finish()
            }

            else -> {

            }
        }
    }

    private fun takeIngredients() {
        var Ing:String=Main_IngredientNames.selectedItem.toString()
        var unit:String=Main_unit.selectedItem.toString()
        var quantity=Main_quantity.text.toString()
        if (quantity.isEmpty())
            Toast.makeText(this, "لا يمكن ترك أي خانة فارغة", Toast.LENGTH_LONG).show()
        else {
            var ingIndex= findIndex(IngArray,Ing)
            var unitIndex= findIndex(quanArray,unit)
            addIngrediants(ingIndex, unitIndex, quantity)
        }

    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            val uploadTask=storageReference!!.putFile(data!!.data!!)
            val task=uploadTask.continueWithTask {
                    task->
                if(!task.isSuccessful)
                    Log.d("","some thing went wrong to uploading to storage")
                storageReference!!.downloadUrl
            }.addOnCompleteListener {task ->

                if(task.isSuccessful){
                    val downloadUri=task.result
                    uri = downloadUri!!.toString().substring(0,downloadUri.toString().indexOf("&token"))
                    Log.d("uriii",""+uri)
                }
            }
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                recipeImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun updateRecipe(imagee:String) {

        var name:String =recipeName!!.text.toString()
        var prepration=recipeDescription!!.text.toString()
        if (uri==null){
                uri=imagee
                Log.d("nullll","uri="+ uri)

}
        else
            Log.d("nullll","outside")






        if (name.isEmpty()||prepration.isEmpty()){
            Toast.makeText(this, "لا يمكن ترك أي خانة فارغة", Toast.LENGTH_LONG).show()

        } else if (recipeIngLayout.getChildCount()==0) {
            Toast.makeText(this, "الرجاء اضافة المقادير", Toast.LENGTH_LONG).show()
        }

        else{
            getTotalCalories(0)

            var type: String? =null
            var type1:String? =null
            var type2:String? =null
            var type3:String? =null
            var type4:String? =null
            var type5:String? =null

            if (checkBox.isChecked==true)
                type = checkBox!!.text.toString()
            else{
                type="not"
            }
            if (checkBox1.isChecked==true)
                type1 = checkBox1!!.text.toString()
            else{
                type1="not"
            }
            if (checkBox2.isChecked==true)
                type2 =checkBox2!!.text.toString()
            else{
                type2="not"
            }
            if (checkBox3.isChecked==true)
                type3 =checkBox3!!.text.toString()
            else{
                type3="not"
            }
            if (checkBox4.isChecked==true)
                type4 =checkBox4!!.text.toString()
            else{
                type4="not"
            }
            if (checkBox5.isChecked==true)
                type5 =checkBox5!!.text.toString()
            else{
                type5="not"

            }

            val docData = hashMapOf(
                "UID" to currentuser!!.toString(),
                "image" to uri,
                "name" to name,
                "prepration" to prepration,
                "Type" to arrayListOf(type,type1,type2,type3,type4,type5),
                "calories" to TotalCalories,
                "date"    to  getCurrentDate()

            )


                db.collection("Recipes").document(id).set(docData, SetOptions.merge()).addOnSuccessListener {
                    Log.d("please","added")
                    updateIngrediants(id)

                    Toast.makeText(this, "تمت اضافة الوصفة", Toast.LENGTH_LONG).show()
                    finish()


                }.addOnFailureListener{
                    Log.d("please","not added"+it.toString())

                }

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
    private fun getTotalCalories(sd:Int): Int {
        var i = main.childCount
        Log.d("TotalCalories","i: "+i)

        var count =0
        while (i!=0){

            var quantity = main.getChildAt(count)!!.quantity.text.toString()
            var name = main.getChildAt(count).IngredientNames.selectedItem.toString()
            var unit=main.getChildAt(count).unit.selectedItem.toString()
            TotalCalories+=CaloriCalculater.calculateCalories(name,unit,quantity)
            i--
            count++

        }
        Log.d("TotalCalories","TotalCalories: "+TotalCalories)


        return TotalCalories
    }

}

