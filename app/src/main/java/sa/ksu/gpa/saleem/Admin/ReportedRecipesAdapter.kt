package sa.ksu.gpa.saleem.Admin

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import sa.ksu.gpa.saleem.R
import sa.ksu.gpa.saleem.recipe.sharedRecipeInformaion


class ReportedRecipesAdapter (private val list: List<ReportedRecipes>, var onActionClick: OnActionClick ,var context: Context): RecyclerView.Adapter<ReportedRecipesAdapter.MyViewHolder>(){


    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.admin_card, parent, false)) {
        private var title: TextView? = null
        private var delete: ImageView? = null



        init {
            title = itemView.findViewById(R.id.title)

            delete = itemView.findViewById(R.id.delete)

        }

        fun bind(ReportedRecipes: ReportedRecipes, onActionClick: OnActionClick) {
            title?.text = ReportedRecipes.report

            delete?.setOnClickListener(View.OnClickListener {
                onActionClick.onDelete(ReportedRecipes,adapterPosition)
            })


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return MyViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list.get(position), onActionClick)
        val recipe: ReportedRecipes =list.get(position)
        var recipeId= recipe.recipeID
        Log.d("flag1", "isReporting is ="+recipeId)

        holder.getAdapterPosition();
           holder.itemView.setOnClickListener {

                var intent = Intent(context, sharedRecipeInformaion::class.java)
               intent.putExtra("RecipeId",recipeId)
                context.startActivity(intent)


            }
    }

    interface OnActionClick{
        fun onClick(item: ReportedRecipes, position:Int)
        fun onDelete(item: ReportedRecipes, position:Int)
    }
}