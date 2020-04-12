package sa.ksu.gpa.saleem.exercise

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.request.RequestOptions
import com.glide.slider.library.SliderLayout
import com.glide.slider.library.slidertypes.TextSliderView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_viewsharedrecipe.*
import sa.ksu.gpa.saleem.HomeFragment
import sa.ksu.gpa.saleem.R
import sa.ksu.gpa.saleem.SettingFragment
import sa.ksu.gpa.saleem.recipe.SharedRecipe.viewSharedRecipeActivity

class ExerciseListActivity : AppCompatActivity() {


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list)

        var pageTitle=findViewById<View>(R.id.titleb) as TextView
        var pageTitle1=pageTitle.toString()

        Log.d("exerAct", "" + pageTitle?.text.toString())

        Log.d("exerAct", "" + pageTitle1?.toString())

        bottomNavigation.selectedItemId=R.id.exercise
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home-> {
                    title="الرئيسية"
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.profile-> {
                    title="الاعدادات"
                    loadFragment(SettingFragment())

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.meals-> {
                    title = "وصفات"

                    val intent = Intent(this, viewSharedRecipeActivity::class.java)
                    startActivity(intent)

                    return@setOnNavigationItemSelectedListener true
                }

                R.id.exercise-> {
                    title = "التمارين"
                    val intent = Intent(this, ExerciseListActivity::class.java)
                    startActivity(intent)

                    return@setOnNavigationItemSelectedListener true
                }

            }
            false

        }

        findViewById<LinearLayout>(R.id.beginner).setOnClickListener {



            val intent = Intent(this, ExerciseActivity::class.java)


            intent.putExtra("title",pageTitle.text)

            Log.d("listact", "" + pageTitle?.toString())

            startActivity(intent)


        }

    }


    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
