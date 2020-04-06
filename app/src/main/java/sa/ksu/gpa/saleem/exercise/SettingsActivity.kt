package sa.ksu.gpa.saleem.exercise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_inner_exercise.*
import sa.ksu.gpa.saleem.R

class SettingsActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        //setSupportActionBar(toolbar1)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "إعدادات ضبط مدة المؤقت"
        back_button.setOnClickListener{
            super.finish()
        }


    }

}
