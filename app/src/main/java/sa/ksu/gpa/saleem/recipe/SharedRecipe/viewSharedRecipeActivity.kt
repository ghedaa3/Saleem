package sa.ksu.gpa.saleem.recipe.SharedRecipe

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.request.RequestOptions
import com.glide.slider.library.SliderLayout
import com.glide.slider.library.animations.DescriptionAnimation
import com.glide.slider.library.slidertypes.BaseSliderView
import com.glide.slider.library.slidertypes.TextSliderView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.bottomNavigation
import kotlinx.android.synthetic.main.activity_viewsharedrecipe.*
import sa.ksu.gpa.saleem.HomeFragment
import sa.ksu.gpa.saleem.R
import sa.ksu.gpa.saleem.SettingFragment
import sa.ksu.gpa.saleem.exercise.ExerciseFragment


class viewSharedRecipeActivity : AppCompatActivity(), BaseSliderView.OnSliderClickListener {

    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView:RecyclerView
    private lateinit var recipeList:ArrayList<RecipeModel>
    private var gridLayout:StaggeredGridLayoutManager?=null
    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var slider: SliderLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewsharedrecipe)
        db = FirebaseFirestore.getInstance()
        slider= findViewById(R.id.slider)
        recipeList= ArrayList()

        initView()
        initSlider()
        bottomNavigation.selectedItemId=R.id.meals
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

                    loadFragment(ExerciseFragment())

                    return@setOnNavigationItemSelectedListener true
                }

            }
            false

        }

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                recipesAdapter.filter.filter(newText)
                return false
            }

        })

    }
    private fun initView() {
        recyclerView=findViewById(R.id.recyclerViewRecipes)
        recipesAdapter= RecipesAdapter(applicationContext,recipeList!!)
        getRecipes()

        gridLayout= StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager=gridLayout
        recyclerView?.addItemDecoration(GridSpacingItemDecoration(2, dpToPx(6), true))
        recyclerView?.setItemAnimator(DefaultItemAnimator())
        recyclerView.adapter=recipesAdapter
        recyclerView.setHasFixedSize(true)


    }

    private fun getRecipes() {
        db.collection("Recipes")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {

                        var recipeId= document.id
                        var recipename= document.get("name").toString()
                        var recipeCalproes=document.get("calories").toString()
                        var recipeImage= document.get("image").toString()
                        var recipe = RecipeModel(recipeId , recipename, recipeImage,recipeCalproes)
                        recipeList.add(recipe)
                        recipesAdapter!!.notifyDataSetChanged()

                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("here", "Error getting documents: ", exception)
                }


    }

    private fun initSlider() {
        val requestOptions = RequestOptions()
        requestOptions.centerCrop()

        db.collection("Recipes").limit(4).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val textSliderView = TextSliderView(this)

                var name =document.get("name").toString()
                var image =document.get("image").toString()
                textSliderView
                        .description(name)
                        .image(image)
                        .setRequestOption(requestOptions)
                        .setProgressBarVisible(true)
                        .setOnSliderClickListener(this);

                slider.addSlider(textSliderView)
            }


        }.addOnFailureListener{
            Log.d("Exception",""+it.toString())
        }
        slider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide)
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
        slider.setCustomAnimation(DescriptionAnimation())
        slider.setDuration(3000)
    }


    class GridSpacingItemDecoration(private val spanCount: Int, private val spacing: Int, private val includeEdge: Boolean) : ItemDecoration() {
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

    override fun onSliderClick(slider: BaseSliderView?) {
        //
    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}
