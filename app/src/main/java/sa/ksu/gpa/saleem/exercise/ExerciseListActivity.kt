package sa.ksu.gpa.saleem.exercise

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_exercise_list.*
import kotlinx.android.synthetic.main.activity_viewsharedrecipe.bottomNavigation
import sa.ksu.gpa.saleem.HomeFragment
import sa.ksu.gpa.saleem.R
import sa.ksu.gpa.saleem.SettingFragment
import sa.ksu.gpa.saleem.recipe.SharedRecipe.viewSharedRecipeActivity

class ExerciseListActivity : AppCompatActivity() {


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list)

        var pageTitle = findViewById<View>(R.id.titleb) as TextView
        var pageTitle1 = findViewById<View>(R.id.title1) as TextView
        var pageTitle2 = findViewById<View>(R.id.title2) as TextView

        /* pageTitle.setOnClickListener(this)
        pageTitle1.setOnClickListener(this)
        pageTitle2.setOnClickListener(this)*/


        /*    var title=pageTitle.toString()*/

        Log.d("exerActList1", "" + pageTitle.text.toString())

        Log.d("exerActList2", "" + pageTitle1.toString())




        bottomNavigation.selectedItemId = R.id.exercise
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    title = "الرئيسية"
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.profile -> {
                    title = "الاعدادات"
                    loadFragment(SettingFragment())

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.meals -> {
                    title = "وصفات"

                    val intent = Intent(this, viewSharedRecipeActivity::class.java)
                    startActivity(intent)

                    return@setOnNavigationItemSelectedListener true
                }

                R.id.exercise -> {
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


            intent.putExtra("title", titleb.text)

            // Log.d("listact", "" + pageTitle?.toString())

            startActivity(intent)


        }

        findViewById<LinearLayout>(R.id.intermediate).setOnClickListener {


            val intent = Intent(this, ExerciseActivity::class.java)


            intent.putExtra("title", title1.text)

            //Log.d("listact", "" + pageTitle?.toString())

            startActivity(intent)


        }

        findViewById<LinearLayout>(R.id.advance).setOnClickListener {


            val intent = Intent(this, ExerciseActivity::class.java)


            intent.putExtra("title", title2.text)

            //  Log.d("listact", "" + pageTitle?.toString())

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


    /*   override fun onClick(v: View?) {
        when (v?.id) {
            R.id.beginner -> {


                val intent = Intent(this, ExerciseActivity::class.java)


                intent.putExtra("title",titleb.text)

            }
            R.id.intermediate -> {


                val intent = Intent(this, ExerciseActivity::class.java)


                intent.putExtra("title",title1.text)


            }
            R.id.advance->{

                val intent = Intent(this, ExerciseActivity::class.java)


                intent.putExtra("title",title2.text)

            }

            else -> {

            }
        }
    }*/


}


