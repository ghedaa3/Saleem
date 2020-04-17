package sa.ksu.gpa.saleem.exercise

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.glide.slider.library.SliderLayout
import com.google.firebase.firestore.FirebaseFirestore
import sa.ksu.gpa.saleem.R
import sa.ksu.gpa.saleem.recipe.SharedRecipe.viewSharedRecipeActivity

class ExerciseActivity : AppCompatActivity() ,View.OnClickListener {


    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var exerciseList: ArrayList<ExerciseModel>
    private var gridLayout: StaggeredGridLayoutManager? = null
    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var slider: SliderLayout
    private lateinit var button:Button




    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_exercise_body)

        var pagetitle=intent.getStringExtra("title")


        Log.d("exerActtitle", "" + pagetitle)

        var title=findViewById<View>(R.id.text) as TextView

        var buttin=findViewById<View>(R.id.start_btn)

        Log.d("exerActtitle", "" + pagetitle)


        title.setText(pagetitle)

        Log.d("exerAct", "" + title.text.toString())

        db = FirebaseFirestore.getInstance()

        var backImg = findViewById<View>(sa.ksu.gpa.saleem.R.id.back_button)

        backImg.setOnClickListener(this)


        exerciseList = ArrayList()

        initView()




    }


    private fun initView() {
        recyclerView=findViewById(R.id.recyclerViewRecipes)
       /* exerciseAdapter= context?.let { ExerciseAdapter(it,exerciseList!!) }!!
        getRecipes()*/

        exerciseAdapter= ExerciseAdapter(applicationContext,exerciseList)
        getRecipes()


        gridLayout= StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager=gridLayout
        recyclerView.addItemDecoration(
            viewSharedRecipeActivity.GridSpacingItemDecoration(
                1,
                dpToPx(6),
                true
            )
        )
        recyclerView.setItemAnimator(DefaultItemAnimator())
        recyclerView.adapter=exerciseAdapter
        recyclerView.setHasFixedSize(true)


    }
    class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view) // item position
            val column = position % spanCount // item column
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        }

    } //end inner class GridSpacingItemDecoration

    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics))
    } //end dpToPx

    private fun getRecipes() {

       var pagetitle=intent.getStringExtra("title")


                 //   var recipeId= document.id
                   // val dataList = ArrayList<ExerciseModel>()
                    if(pagetitle=="مستوى مبتدى") {
                        exerciseList.add(
                            ExerciseModel(
                                "Phone",
                                "تمرين الضغط",
                                R.drawable.push_up,
                                "4.75",
                                "التكرار: 10 "
                            )
                        )
                        exerciseList.add(
                            ExerciseModel(
                                "Watch",
                                "الجلوس المعدل",
                                R.drawable.set_up,
                                "2 ",
                                "التكرار: 10 "
                            )
                        )
                        exerciseList.add(
                            ExerciseModel(
                                "1",
                                "تمرين استقامة",
                                R.drawable.warm_up,
                                "1 ",
                                "التكرار: 10 "
                            )
                        )
                        exerciseList.add(
                            ExerciseModel(
                                "2",
                                "رفع الاثقال",
                                R.drawable.man_liftting_dumbells,
                                "2 ",
                                "التكرار: 10 "
                            )
                        )
                        exerciseList.add(
                            ExerciseModel(
                                "3",
                                "تمرين هرولة",
                                R.drawable.man_jogging,
                                "2.33 ",
                                "20.0 ثانية"
                            )
                        )


                    exerciseAdapter.notifyDataSetChanged()
                        }

                    if(pagetitle=="مستوى متوسط") {

                        exerciseList.add(
                            ExerciseModel(
                                "Phone",
                                "تمرين الضغط",
                                R.drawable.push_up,
                                "9.5 ",
                                "التكرار: 20 "
                            )
                        )
                        exerciseList.add(
                            ExerciseModel(
                                "Watch",
                                "الجلوس المعدل",
                                R.drawable.set_up,
                                "4 ",
                                "التكرار: 20 "
                            )
                        )
                        exerciseList.add(
                            ExerciseModel(
                                "1",
                                "تمرين استقامة",
                                R.drawable.warm_up,
                                "2 ",
                                "التكرار: 20 "
                            )
                        )
                        exerciseList.add(
                            ExerciseModel(
                                "2",
                                "رفع الاثقال",
                                R.drawable.man_liftting_dumbells,
                                "4 ",
                                "التكرار: 20 "
                            )
                        )
                        exerciseList.add(
                            ExerciseModel(
                                "3",
                                "تمرين هرولة",
                                R.drawable.man_jogging,
                                "3.33 ",
                                "30.0 ثانية"
                            )
                        )

                        exerciseAdapter.notifyDataSetChanged()
                        }

                    if(pagetitle=="مستوى متقدم") {

                        exerciseList.add(
                            ExerciseModel(
                                "Phone",
                                "تمرين الضغط",
                                R.drawable.push_up,
                                "14.16",
                                "التكرار: 30 "
                            )
                        )
                        exerciseList.add(
                            ExerciseModel(
                                "Watch",
                                "الجلوس المعدل",
                                R.drawable.set_up,
                                "6 ",
                                "التكرار: 30 "
                            )
                        )
                        exerciseList.add(
                            ExerciseModel(
                                "1",
                                "تمرين استقامة",
                                R.drawable.warm_up,
                                "3 ",
                                "التكرار: 30 "
                            )
                        )
                        exerciseList.add(
                            ExerciseModel(
                                "2",
                                "رفع الاثقال",
                                R.drawable.man_liftting_dumbells,
                                "6 ",
                                "التكرار: 30"
                            )
                        )
                        exerciseList.add(
                            ExerciseModel(
                                "3",
                                "تمرين هرولة",
                                R.drawable.man_jogging,
                                "4.33 ",
                                "40 ثانية"
                            )
                        )

                        exerciseAdapter.notifyDataSetChanged()
                    }


                }


    override fun onClick(v: View?) {
        when (v?.id) {
            sa.ksu.gpa.saleem.R.id.back_button -> {
                finish()
            }
            /*   sa.ksu.gpa.saleem.R.id.start ->{





               }*/

            else -> {
            }

        }
    }

}