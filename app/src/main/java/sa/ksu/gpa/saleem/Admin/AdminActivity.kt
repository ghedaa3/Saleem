package sa.ksu.gpa.saleem.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_admin2.*
import sa.ksu.gpa.saleem.R

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin2)

        user_list.setOnClickListener {
            var intent = Intent(this, Admin::class.java)
            startActivity(intent)
        }

        reported_advices.setOnClickListener {
            var intent = Intent(this, AdminAdvices::class.java)
            startActivity(intent)
        }
        reported_recipes.setOnClickListener {
            var intent = Intent(this, AdminRecipes::class.java)
            startActivity(intent)
        }
    }
}
