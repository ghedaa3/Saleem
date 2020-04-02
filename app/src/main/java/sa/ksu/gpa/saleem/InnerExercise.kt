package sa.ksu.gpa.saleem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_inner_exercise.*

class InnerExercise : AppCompatActivity(),View.OnClickListener  {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inner_exercise)


        var Exercisetitle = findViewById<View>(R.id.title1)
        var calTitle = findViewById<View>(R.id.caloriesTitle)

        var cal = findViewById<View>(R.id.calories)
        var DTitle = findViewById<View>(R.id.durationTitle)

        var duration = findViewById<View>(R.id.duration)
        var backImg = findViewById<View>(R.id.back_button)

        backImg.setOnClickListener(this)


        var gif = findViewById<View>(R.id.recipe_image)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_button -> {
                finish()
            }
            else -> {

            }
        }
    }
}



