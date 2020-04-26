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
        getExcercise()


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


    private fun getExcercise() {

        var pagetitle=intent.getStringExtra("title")


        //   var recipeId= document.id
        // val dataList = ArrayList<ExerciseModel>()
        if(pagetitle=="مستوى مبتدى") {
            exerciseList.add(
                ExerciseModel(
                    "exercise1",
                    "ضغط",
                    R.drawable.push_up,
                    "4.75",
                    "10 مرات ",
                    "يتم تطبيقه بالتمدد على الأرض والوجه إلى أسفل ثم رفع الجسم بدفع اليدين عن طريق ضغط الأرض. يركز تمرين الضغط على تطوير العضلة الصدرية الكبيرة والعضلة ثلاثية الرؤوس العضدية، و على تنمية وتطوير بعض العضلات الدالية، العضلة المنشارية الأمامية، والعضلة الغرابية العضدية."
                )
            )
            exerciseList.add(
                ExerciseModel(
                    "exercise2",
                    " معدة",
                    R.drawable.set_up,
                    "2 ",
                    "10 مرات ",
                    "الخطوة 1\n" +
                            "نستلقي على الظهر مع وضع الركبتين بزاوية مقدارها ٩٠ درجة ووضع القدمين مسطحتين على الأرض. نحافظ على مد الذراعين على الجانبين وتوجيه راحتي اليدين للأسفل مع أرجحة الجسم قليلاً فوق الأرض. \nالخطوة 2 \n" +
                            "نشد عضلات البطن ونرفع الجذع حتى وضع الجلوس بزاوية مقدارها ٤٥ درجة مع التوقف للحظة قبل العودة إلى الأرض."
                //https://www.msn.com/ar-ae/health/exercise/strength/%D8%AA%D9%85%D8%B1%D9%8A%D9%86-%D8%A7%D9%84%D8%AC%D9%84%D9%88%D8%B3-%D8%A7%D9%84%D9%85%D8%B9%D8%AF%D9%8E%D9%84/ss-BBtSgB8
                )
            )
            exerciseList.add(
                ExerciseModel(
                    "exercise3",
                    " استقامة",
                    R.drawable.warm_up,
                    "1 ",
                    "10 مرات ",
                    ""
                )
            )
            exerciseList.add(
                ExerciseModel(
                    "exercise4",
                    "رفع الاثقال",
                    R.drawable.man_liftting_dumbells,
                    "2 ",
                    "10 مرات ",
                    "الخطوة 1\n" +
                            "نمسك دمبل في يد واحدة ونرفعه إلى ارتفاع الكتف والكف مواجه ناحية الصدر والذراع مثني. نقف بثبات، نبقي عضلات الجذع مشدودة، ونفتح القدمين بعرض الكتف.\n" +
                            "الخطوة 2 \n نعكس الاتجاه مع العودة إلى وضع البداية ونكرر الحركة على الجانب الآخر."
                )
            )
       /*     exerciseList.add(
                ExerciseModel(
                    "exercise5",
                    "تمرين هرولة",
                    R.drawable.man_jogging,
                    "2.33 ",
                    "20.0 ثانية",
                    ""
                )
            )*/


            exerciseAdapter.notifyDataSetChanged()
        }

        if(pagetitle=="مستوى متوسط") {

            exerciseList.add(
                ExerciseModel(
                    "exercise1",
                    "ضغط",
                    R.drawable.push_up,
                    "9.5 ",
                    "20 مرات ",
                    "يتم تطبيقه بالتمدد على الأرض والوجه إلى أسفل ثم رفع الجسم بدفع اليدين عن طريق ضغط الأرض. يركز تمرين الضغط على تطوير العضلة الصدرية الكبيرة والعضلة ثلاثية الرؤوس العضدية، و على تنمية وتطوير بعض العضلات الدالية، العضلة المنشارية الأمامية، والعضلة الغرابية العضدية."
                )
            )
            exerciseList.add(
                ExerciseModel(
                    "exercise2",
                    " معدة",
                    R.drawable.set_up,
                    "4 ",
                    "20 مرات  ",
                    "الخطوة 1\n" +
                            "نستلقي على الظهر مع وضع الركبتين بزاوية مقدارها ٩٠ درجة ووضع القدمين مسطحتين على الأرض. نحافظ على مد الذراعين على الجانبين وتوجيه راحتي اليدين للأسفل مع أرجحة الجسم قليلاً فوق الأرض. \nالخطوة 2 \n" +
                            "نشد عضلات البطن ونرفع الجذع حتى وضع الجلوس بزاوية مقدارها ٤٥ درجة مع التوقف للحظة قبل العودة إلى الأرض."
                )
            )
            exerciseList.add(
                ExerciseModel(
                    "exercise3",
                    " استقامة",
                    R.drawable.warm_up,
                    "2 ",
                    "20 مرات ",
                    ""
                )
            )
            exerciseList.add(
                ExerciseModel(
                    "exercise4",
                    "رفع الاثقال",
                    R.drawable.man_liftting_dumbells,
                    "4 ",
                    "20 مرات ",
                    "الخطوة 1\n" +
                            "نمسك دمبل في يد واحدة ونرفعه إلى ارتفاع الكتف والكف مواجه ناحية الصدر والذراع مثني. نقف بثبات، نبقي عضلات الجذع مشدودة، ونفتح القدمين بعرض الكتف.\n" +
                            "الخطوة 2 \n نعكس الاتجاه مع العودة إلى وضع البداية ونكرر الحركة على الجانب الآخر."
                )
            )
   /*         exerciseList.add(
                ExerciseModel(
                    "exercise5",
                    "تمرين هرولة",
                    R.drawable.man_jogging,
                    "3.33 ",
                    "30.0 ثانية",
                    ""
                )
            )*/

            exerciseAdapter.notifyDataSetChanged()
        }

        if(pagetitle=="مستوى متقدم") {

            exerciseList.add(
                ExerciseModel(
                    "exercise1",
                    " ضغط",
                    R.drawable.push_up,
                    "14.16",
                    "30 مرات",
                    "يتم تطبيقه بالتمدد على الأرض والوجه إلى أسفل ثم رفع الجسم بدفع اليدين عن طريق ضغط الأرض. يركز تمرين الضغط على تطوير العضلة الصدرية الكبيرة والعضلة ثلاثية الرؤوس العضدية، و على تنمية وتطوير بعض العضلات الدالية، العضلة المنشارية الأمامية، والعضلة الغرابية العضدية."
                )
            )
            exerciseList.add(
                ExerciseModel(
                    "exercise2",
                    " معدة",
                    R.drawable.set_up,
                    "6 ",
                    "30 مرات",
                    "الخطوة 1\n" +
                            "نستلقي على الظهر مع وضع الركبتين بزاوية مقدارها ٩٠ درجة ووضع القدمين مسطحتين على الأرض. نحافظ على مد الذراعين على الجانبين وتوجيه راحتي اليدين للأسفل مع أرجحة الجسم قليلاً فوق الأرض. \nالخطوة 2 \n" +
                            "نشد عضلات البطن ونرفع الجذع حتى وضع الجلوس بزاوية مقدارها ٤٥ درجة مع التوقف للحظة قبل العودة إلى الأرض."
                )
            )
            exerciseList.add(
                ExerciseModel(
                    "exercise3",
                    " استقامة",
                    R.drawable.warm_up,
                    "3 ",
                    "30 مرات",
                    ""
                )
            )
            exerciseList.add(
                ExerciseModel(
                    "exercise4",
                    "رفع الاثقال",
                    R.drawable.man_liftting_dumbells,
                    "6 ",
                    "30 مرات",
                    "الخطوة 1\n" +
                            "نمسك دمبل في يد واحدة ونرفعه إلى ارتفاع الكتف والكف مواجه ناحية الصدر والذراع مثني. نقف بثبات، نبقي عضلات الجذع مشدودة، ونفتح القدمين بعرض الكتف.\n" +
                            "الخطوة 2 \n نعكس الاتجاه مع العودة إلى وضع البداية ونكرر الحركة على الجانب الآخر."
                )
            )
       /*     exerciseList.add(
                ExerciseModel(
                    "exercise5",
                    "تمرين هرولة",
                    R.drawable.man_jogging,
                    "4.33 ",
                    "40 ثانية",
                    ""
                )
            )*/

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