package sa.ksu.gpa.saleem.exercise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import sa.ksu.gpa.saleem.R

class SettingsActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        //setSupportActionBar(toolbar1)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "إعدادات ضبط مدة المؤقت"


    }

}
