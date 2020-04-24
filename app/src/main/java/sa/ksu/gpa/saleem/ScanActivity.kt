package sa.ksu.gpa.saleem

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_scan.*
import kotlinx.android.synthetic.main.scanner_dialog_add_snack.*
import kotlinx.android.synthetic.main.scanner_dialog_add_snack.view.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class ScanActivity : AppCompatActivity() ,ZXingScannerView.ResultHandler{
    private lateinit var mScannerView: ZXingScannerView
    lateinit var db:FirebaseFirestore
    var TAG="ScanActivity"
    lateinit var currentuser:String
    private lateinit var SnackName: String
    private lateinit var SnackCalories: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        db= FirebaseFirestore.getInstance()
        currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()

        initScannerView()
    }

    private fun initScannerView() {
        mScannerView = ZXingScannerView(this)
        mScannerView.setAutoFocus(true)
        mScannerView.setResultHandler(this)
        frame_layout_camera.addView(mScannerView)
    }
    override fun onStart() {
        mScannerView.startCamera()
        super.onStart()
    }

    override fun onPause() {
        mScannerView.stopCamera()
        super.onPause()
    }
    override fun handleResult(result: Result?) {
        var r:String
        r=result!!.toString()
        Log.d("testing",""+r)

        val docRef = db.collection("Products").document("6281022120349")
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.get("ID") == null) {
                    Log.d(TAG, "No such document")
                    Log.d("testing",""+r)
                    productNotFoundedDialog()
                } else {
                    var name =document.get("name").toString()
                    var company=document.get("company").toString()
                    var servingSize=document.get("servingSize").toString()
                    var calories= document.get("calories").toString()
                    var fat=document.get("fat").toString()
                    var protein=document.get("protein").toString()
                    var carb=document.get("carb").toString()
                    productFoundedDialog(name,company,servingSize,calories,fat,protein,carb)
                    Log.d(TAG, "DocumentSnapshot data: ${document.get("name")}")

                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ")
            }
    }

    private fun productFoundedDialog(name:String,company:String,servingSize:String,calories:String,fat:String,protein:String,carb:String) {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.scanner_dialog_add_snack, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        val  mAlertDialog = mBuilder.show()
        mAlertDialog.text.setOnClickListener { mAlertDialog.dismiss() }
        mAlertDialog.productCompany1.setText(company)
        mAlertDialog.productName1.setText(name)
        mAlertDialog.productServingSizeNumber.setText(servingSize)
        mAlertDialog.calories.setText(calories)
        mAlertDialog.protein.setText(protein)
        mAlertDialog.carb.setText(carb)
        mAlertDialog.fat.setText(fat)
        SnackName=name
        SnackCalories=calories
        mAlertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        //cancel button click of custom layout
        mDialogView.cancel.setOnClickListener{
            mAlertDialog.dismiss()
            Toast.makeText(this,"",Toast.LENGTH_LONG)
            finish()

        }
        mDialogView.add.setOnClickListener{
            addasAmealDialog()
            mAlertDialog.dismiss()
            finish()

        }
    }


    private fun productNotFoundedDialog() {
        SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
            .setTitleText("نعتذر لك")
            .setContentText("هذا المنتج غير متوفر")
            .setConfirmButton("حسنًا") { sDialog -> sDialog.dismissWithAnimation()
                finish()

            }
            .show()

    }
    private fun addasAmealDialog(){


        val data1 = hashMapOf(
            "food_name" to SnackName,
            "type" to "fromScanner",
            "date" to getCurrentDate(),
            "user_id" to currentuser,
            "cal_of_food" to SnackCalories.toDouble()
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
