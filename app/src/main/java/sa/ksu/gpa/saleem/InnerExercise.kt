package sa.ksu.gpa.saleem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_edit_prof.view.*
import kotlinx.android.synthetic.main.activity_edit_prof.view.title
import kotlinx.android.synthetic.main.activity_inner_exercise.*
import kotlinx.android.synthetic.main.activity_inner_exercise.view.*
import kotlinx.android.synthetic.main.testdesign.view.*
import kotlinx.android.synthetic.main.testdesign.view.calories
import org.w3c.dom.Text
import pl.droidsonroids.gif.GifImageView

class InnerExercise : AppCompatActivity(),View.OnClickListener  {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inner_exercise)


        var Exercisetitle = findViewById<View>(R.id.title) as TextView
        var calTitle = findViewById<View>(R.id.caloriesTitle) as TextView

        var cal = findViewById<View>(R.id.calories) as TextView
        var DTitle = findViewById<View>(R.id.durationTitle)

        var duration = findViewById<View>(R.id.duration) as TextView
        var backImg = findViewById<View>(R.id.back_button)

        var giff=findViewById<View>(R.id.gif) as GifImageView

        backImg.setOnClickListener(this)

        var id = getIntent().getStringExtra("ExerciseId")
        var calories = getIntent().getStringExtra("ExerciseCal")
        var title = getIntent().getStringExtra("ExerciseTitle")

        Exercisetitle.setText(title)
        cal.setText(calories)

        if (id=="Watch"){


            //giff.setBytes(bitmapData);

            Glide.with(this)
                .load(R.drawable.warm_up)
                .into(giff);


            duration.setText("10 دقائق")

        }
        if (id=="Phone"){

            Glide.with(this)
                .load(R.drawable.push_up)
                .into(giff);


            duration.setText("20 دقائق")



        }

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



