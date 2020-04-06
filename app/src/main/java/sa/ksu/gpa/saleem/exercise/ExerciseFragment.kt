package sa.ksu.gpa.saleem.exercise

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import sa.ksu.gpa.saleem.R
import sa.ksu.gpa.saleem.recipe.SharedRecipe.viewSharedRecipeActivity

class ExerciseFragment :Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var recipeList: ArrayList<ExerciseModel>
    private var gridLayout: StaggeredGridLayoutManager? = null
    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var slider: SliderLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_exercise_fragment, container, false)
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = FirebaseFirestore.getInstance()



        recipeList = ArrayList()

        initView()




        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                exerciseAdapter.filter.filter(newText)
                return false
            }

        })

    }

    private fun initSlider() {
        val requestOptions = RequestOptions()
        requestOptions.centerCrop()

        db.collection("Recipes").limit(4).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val textSliderView = TextSliderView(this.context)

                var name =document.get("name").toString()
                var image =document.get("image").toString()
                textSliderView
                    .description(name)
                    .image(image)
                    .setRequestOption(requestOptions)
                    .setProgressBarVisible(true);


                slider.addSlider(textSliderView)
            }


        }.addOnFailureListener{
            Log.d("Exception",""+it.toString())
        }

    }

    private fun initView() {
        recyclerView=view!!.findViewById(R.id.recyclerViewRecipes)
        exerciseAdapter= context?.let {
            ExerciseAdapter(
                it,
                recipeList!!
            )
        }!!
        getRecipes()


        gridLayout= StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager=gridLayout
        recyclerView?.addItemDecoration(
            viewSharedRecipeActivity.GridSpacingItemDecoration(
                2,
                dpToPx(6),
                true
            )
        )
        recyclerView?.setItemAnimator(DefaultItemAnimator())
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


                 //   var recipeId= document.id
                   // val dataList = ArrayList<ExerciseModel>()
                     recipeList.add(
                         ExerciseModel(
                             "Phone",
                             "تمرين استقامة",
                             R.drawable.ic_fitness_center_black_24dp,
                             "400"
                         )
                     )
                    recipeList.add(
                        ExerciseModel(
                            "Watch",
                            "تمرين شد العضلات",
                            R.drawable.ic_fitness_center_black_24dp,
                            "1240"
                        )
                    )


                    exerciseAdapter!!.notifyDataSetChanged()

                }




}