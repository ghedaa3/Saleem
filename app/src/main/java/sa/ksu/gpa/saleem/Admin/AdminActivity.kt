package sa.ksu.gpa.saleem.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_admin2.*
import sa.ksu.gpa.saleem.R
import sa.ksu.gpa.saleem.loginn

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin2)
        var title=findViewById<View>(R.id.users) as TextView
        var title1=findViewById<View>(R.id.recipe_title) as TextView
        var title2=findViewById<View>(R.id.adviceTitle) as TextView
        var img=findViewById<View>(R.id.logout)

        img.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("تسجيل الخروج")
            //set message for alert dialog
            builder.setMessage("هل متاكد من تسجيل الخروج ؟ ")


            builder.setPositiveButton("نعم"){ dialogInterface, which ->

                FirebaseAuth.getInstance().signOut();
                val intent = Intent(this, loginn::class.java)
                startActivity(intent)
            }

            builder.setNegativeButton("لا"){dialogInterface, which ->
            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        user_list.setOnClickListener {
            var intent = Intent(this, Admin::class.java)
            intent.putExtra("pageTitle",title.text.toString())
            startActivity(intent)
        }

        reported_advices.setOnClickListener {
            var intent = Intent(this, AdminAdvices::class.java)
            intent.putExtra("pageTitle",title2.text.toString())
            startActivity(intent)
        }

        reported_recipes.setOnClickListener {
            var intent = Intent(this, AdminRecipes::class.java)
            intent.putExtra("pageTitle",title1.text.toString())
            startActivity(intent)
        }

    }
}
