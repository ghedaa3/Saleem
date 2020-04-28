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

        var exercie1="يتم تطبيقه بالتمدد على الأرض والوجه إلى أسفل ثم رفع الجسم بدفع اليدين عن طريق ضغط الأرض. يركز تمرين الضغط على تطوير العضلة الصدرية الكبيرة والعضلة ثلاثية الرؤوس العضدية، و على تنمية وتطوير بعض العضلات الدالية، العضلة المنشارية الأمامية، والعضلة الغرابية العضدية."
        var exercise2="الخطوة 1\n" +
                "نستلقي على الظهر مع وضع الركبتين بزاوية مقدارها ٩٠ درجة ووضع القدمين مسطحتين على الأرض. نحافظ على مد الذراعين على الجانبين وتوجيه راحتي اليدين للأسفل مع أرجحة الجسم قليلاً فوق الأرض. \nالخطوة 2 \n" +
                "نشد عضلات البطن ونرفع الجذع حتى وضع الجلوس بزاوية مقدارها ٤٥ درجة مع التوقف للحظة قبل العودة إلى الأرض."
        var exercise3="قف مستقيمًا وقدميك متجهة للأمام . استنشق الهواء وارفع يدك اليمنى في الهواء. قم بالزفير ، وحافظ على جسمك مستقيمًا ولا ينحني إلى الأمام ، حرك يدك اليسرى لأسفل ساقك اليسرى. حافظ على استقامة ذراعك الأيمن واستمر في التمدد الأقصى لمدة خمسة عدات. استنشق وعد ببطء إلى الوقوف مستقيماً ثم الزفير ، وخفض ذراعك والاسترخاء. كرر على الجانب الآخر"
        var exercise4="الخطوة 1\n" +
                "نمسك دمبل في يد واحدة ونرفعه إلى ارتفاع الكتف والكف مواجه ناحية الصدر والذراع مثني. نقف بثبات، نبقي عضلات الجذع مشدودة، ونفتح القدمين بعرض الكتف.\n" +
                "الخطوة 2 \n نعكس الاتجاه مع العودة إلى وضع البداية ونكرر الحركة على الجانب الآخر."

        if(pagetitle=="مستوى مبتدى") {
            exerciseList.add(
                ExerciseModel(
                    "exercise1",
                    "ضغط",
                    R.drawable.push_up,
                    "10.60",
                    "x10",
                    exercie1
                )
                //https://www.livestrong.com/article/314752-how-many-calories-do-100-push-ups-a-day-burn/
            //https://ar.wikipedia.org/wiki/%D8%B6%D8%BA%D8%B7_(%D8%AA%D9%85%D8%B1%D9%8A%D9%86)
            )
            exerciseList.add(
                ExerciseModel(
                    "exercise2",
                    " معدة",
                    R.drawable.set_up,
                    "16.7",
                    "x10ْ",
                    exercise2
                    //https://www.livestrong.com/article/318729-how-many-calories-do-you-burn-doing-100-sit-ups/
                    //https://www.msn.com/ar-ae/health/exercise/strength/%D8%AA%D9%85%D8%B1%D9%8A%D9%86-%D8%A7%D9%84%D8%AC%D9%84%D9%88%D8%B3-%D8%A7%D9%84%D9%85%D8%B9%D8%AF%D9%8E%D9%84/ss-BBtSgB8
                )
            )
            exerciseList.add(
                ExerciseModel(
                    "exercise3",
                    " استقامة",
                    R.drawable.warm_up,
                    "1 ",
                    "x10",
                    exercise3
                )
            //https://inews.co.uk/news/health/coronavirus-fitness-working-from-home-step-by-step-yoga-barbara-currie-2505109
            )
            exerciseList.add(
                ExerciseModel(
                    "exercise4",
                    "رفع الاثقال",
                    R.drawable.man_liftting_dumbells,
                    "11.1",//11.1
                    "x10",
                    exercise4+"\n [1 كغم]"
                )
            //https://www.msn.com/ar-ae/health/exercise/strength/%d8%aa%d9%85%d8%b1%d9%8a%d9%86-%d8%b6%d8%ba%d8%b7-%d8%a7%d9%84%d9%83%d8%aa%d9%81%d9%8a%d9%86-%d8%a8%d8%a7%d8%b3%d8%aa%d8%ae%d8%af%d8%a7%d9%85-%d8%af%d9%85%d8%a8%d9%84-%d9%81%d9%8a-%d8%b0%d8%b1%d8%a7%d8%b9-%d9%88%d8%a7%d8%ad%d8%af%d8%a9/ss-BBtSsGb
            )
            exerciseAdapter.notifyDataSetChanged()
        }

        if(pagetitle=="مستوى متوسط") {

            exerciseList.add(
                ExerciseModel(
                    "exercise1",
                    "ضغط",
                    R.drawable.push_up,
                    "21.21",
                    "x20",
                        exercie1)
            )
            exerciseList.add(
                ExerciseModel(
                    "exercise2",
                    " معدة",
                    R.drawable.set_up,
                    "33.4",
                    "x20",
              exercise2  )
            )
            exerciseList.add(
                ExerciseModel(
                    "exercise3",
                    " استقامة",
                    R.drawable.warm_up,
                    "4",
                    "x20",
                        exercise3 )
            )
            exerciseList.add(
                ExerciseModel(
                    "exercise4",
                    "رفع الاثقال",
                    R.drawable.man_liftting_dumbells,
                    "22.2",
                    "x20",
                    exercise4+"\n [1 كغم] ")
            )
            exerciseAdapter.notifyDataSetChanged()
        }

        if(pagetitle=="مستوى متقدم") {

            exerciseList.add(
                ExerciseModel(
                    "exercise1",
                    " ضغط",
                    R.drawable.push_up,
                    "31.81",
                    "x30",
              exercie1)
            )
            exerciseList.add(
                ExerciseModel(
                    "exercise2",
                    " معدة",
                    R.drawable.set_up,
                    "50.1",
                    "x30",
            exercise2 )
            )
            exerciseList.add(
                ExerciseModel(
                    "exercise3",
                    " استقامة",
                    R.drawable.warm_up,
                    "3",
                    "x30",
               exercise3 )
            )
            exerciseList.add(
                ExerciseModel(
                    "exercise4",
                    "رفع الاثقال",
                    R.drawable.man_liftting_dumbells,
                    "40",
                    "x30",
              exercise4+"\n[2 كغم] " )
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