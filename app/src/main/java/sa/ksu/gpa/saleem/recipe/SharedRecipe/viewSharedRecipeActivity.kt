package sa.ksu.gpa.saleem.recipe.SharedRecipe

import android.content.Intent
import android.graphics.Rect
import android.os.Build
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.bottomNavigation
import kotlinx.android.synthetic.main.activity_viewsharedrecipe.*
import kotlinx.android.synthetic.main.fragment_home_body.*
import kotlinx.android.synthetic.main.home_fragment.*
import sa.ksu.gpa.saleem.HomeFragment
import sa.ksu.gpa.saleem.R
import sa.ksu.gpa.saleem.SettingFragment
import sa.ksu.gpa.saleem.exercise.ExerciseListActivity
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class viewSharedRecipeActivity : AppCompatActivity(), BaseSliderView.OnSliderClickListener {

    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView:RecyclerView
    private lateinit var recipeList:ArrayList<RecipeModel>
    private var gridLayout:StaggeredGridLayoutManager?=null
    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var slider: SliderLayout
    private  var  currentuser = FirebaseAuth.getInstance().currentUser?.uid.toString()



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

                    val intent = Intent(this, ExerciseListActivity::class.java)
                    startActivity(intent)

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
                        var recipeDate= document.get("date").toString()
                        var recipe = RecipeModel(recipeId , recipename, recipeImage,recipeCalproes,recipeDate)
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
    fun onDeleteWater(view: View) {
        add_water.removeView(view.getParent() as View)
        db.collection("users").document(currentuser!!).collection("Water").document(getCurrentDate()).update("amountOfWater", FieldValue.increment(-1))
        updateWater()


    }
    private fun  updateWater(){
        var totalWaterAmount = 0
        db.collection("users").document(currentuser!!)
            .collection("Water").document(getCurrentDate()).get().addOnSuccessListener {
                if (it.exists()){
                    totalWaterAmount=it.get("amountOfWater").toString().toInt()

                }
                if(totalWaterAmount!=null)
                    waterAmountTV.text= totalWaterAmount.toString()
                else
                    waterAmountTV.text="0"
            }
    }
    fun getCurrentDate():String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formatted = current.format(formatter)
            return formatted
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val currentDate = sdf.format(Date())
        return "$currentDate"
    }

}
