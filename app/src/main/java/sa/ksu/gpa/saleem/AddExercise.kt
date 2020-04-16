package sa.ksu.gpa.saleem

import android.app.Activity
import android.content.Intent
import android.net.Uri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_exercise_fragment.view.*
import kotlinx.android.synthetic.main.testdesign.*
import pl.utkala.searchablespinner.SearchableSpinner
import java.io.IOException

class AddExercise : AppCompatActivity(), View.OnClickListener{

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private lateinit var db: FirebaseFirestore
    private var storageReference: StorageReference? = null
    private lateinit var image : TextView
    private lateinit var Recipeimage : ImageView
    private lateinit var publishRecipe: Button
    private lateinit var nameRecipe: EditText


    private lateinit var calories: EditText

    private lateinit var duration: EditText

    private lateinit var uri:String
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exercise)


        db = FirebaseFirestore.getInstance()
        //to-do
        storageReference = FirebaseStorage.getInstance().getReference().child("12").child("image")

        image=findViewById(R.id.sharedrecipeimage)
        Recipeimage=findViewById(R.id.sharedrecipeimageReal)

        publishRecipe = findViewById(R.id.publishRecipe)
        nameRecipe = findViewById(R.id.Recipename)
        calories=findViewById(R.id.calories)
        duration=findViewById(R.id.duration)


        backButton= findViewById(R.id.back_button)

        image.setOnClickListener(this)

        publishRecipe.setOnClickListener(this)
        backButton.setOnClickListener(this)

        val userUid = FirebaseAuth.getInstance().currentUser!!.uid

    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.sharedrecipeimage -> {
                launchGallery()
            }

            R.id.publishRecipe->{
                addRecipe()
                finish()
            }
            R.id.back_button->{
                finish()
            }

            else -> {

            }
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
                    Log.d("uriiiex",""+uri)
                }
            }
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                Recipeimage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun addRecipe(){


        var exerciseID:String=""
        var name:String =nameRecipe!!.text.toString()
        var cal:Double=calories!!.text.toString().toDouble()
        var dur:Double=duration!!.text.toString().toDouble()

        val userUid = FirebaseAuth.getInstance().currentUser!!.uid
        var urii:String=uri!!.toString()

        val currentuser =userUid
        val docData = hashMapOf(
            // "UID" to currentuser!!.toString(),
            "UID" to currentuser,
            "image" to uri,
            "name" to name,
            "calories" to cal,
            "duraion" to dur

        )
 /*       val NumberOfCaloriesDoc = db.collection("Users").document(currentuser)
        NumberOfCaloriesDoc.get().addOnSuccessListener {
                documentSnapshot ->*/

 /*           NumberOfRecipes= documentSnapshot.get("NumberOfRecipes").toString().toInt()
            recipeID= currentuser+NumberOfRecipes
*/
            db.collection("exercise").document(exerciseID).set(docData).addOnSuccessListener {
                Log.d("please","added")
                //NumberOfCaloriesDoc.update("NumberOfRecipes", FieldValue.increment(1))
            }.addOnFailureListener{
                Log.d("please","not added"+it.toString())

            }

       /* }.addOnFailureListener {
            Log.d("Here",""+it)
        }
*/


    }



}
