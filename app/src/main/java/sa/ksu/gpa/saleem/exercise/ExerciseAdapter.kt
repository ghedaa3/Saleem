package sa.ksu.gpa.saleem.exercise

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.util.*
import kotlin.collections.ArrayList
import android.R



class ExerciseAdapter(var context:Context, var arrayList:ArrayList<ExerciseModel>):
        RecyclerView.Adapter<ExerciseAdapter.ItemHolder>(), Filterable {

    var filteringList =ArrayList<ExerciseModel>()


    init {
        filteringList= arrayList
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
                .inflate(sa.ksu.gpa.saleem.R.layout.exercise_cardview, parent, false)
        return ItemHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return filteringList.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val exercise: ExerciseModel =filteringList.get(position)
        Glide.with(context).load(exercise.exercisePicture).into(holder.exerciseImage1)
        holder.exerciseTitle2.text=exercise.exerciseTitle
        holder.exerciseCalories3.text=exercise.exerciseCalories
        holder.exerciseId=exercise.exerciseId
        holder.exerciseDuration1.text=exercise.exerciseDuration
        holder.getAdapterPosition();


        holder.itemView.setOnClickListener {


               var intent = Intent(context, InnerExercise::class.java)

               intent.putExtra("ExerciseId",holder.exerciseId)
               intent.putExtra("ExerciseCal",holder.exerciseCalories3.text.toString())
               intent.putExtra("ExerciseTitle",holder.exerciseTitle2.text.toString())
               intent.putExtra("pic",holder.exerciseImage1.toString())
               intent.putExtra("ExerciseDuration",holder.exerciseDuration1.text.toString())
               intent.putExtra("ExerciseDesc",exercise.exerciseDescription.toString())

               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

               context.startActivity(intent)


        }
    }

    interface RecyclerViewClickListener {

        fun onClick(view: View, position: Int)
    }
    class ItemHolder(itemView: View):RecyclerView.ViewHolder(itemView){


        var exerciseImage1 = itemView.findViewById<ImageView>(sa.ksu.gpa.saleem.R.id.recipe_image)
        var exerciseTitle2= itemView.findViewById<TextView>(sa.ksu.gpa.saleem.R.id.recipe_title) as TextView
        var exerciseCalories3= itemView.findViewById<TextView>(sa.ksu.gpa.saleem.R.id.recipe_calories) as TextView
        var exerciseDuration1= itemView.findViewById<TextView>(sa.ksu.gpa.saleem.R.id.exercise_duration) as TextView
        lateinit var  exerciseId:String


    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filteringList = arrayList
                } else {
                    val resultList = ArrayList<ExerciseModel>()
                    for (row in arrayList) {
                        if (row.exerciseTitle.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    filteringList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteringList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteringList = results?.values as ArrayList<ExerciseModel>
                notifyDataSetChanged()
            }

        }
    }
}

