package sa.ksu.gpa.saleem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyRecipesApapter (private val list: List<MyRecipe>, var onActionClick: OnActionClick): RecyclerView.Adapter<MyRecipesApapter.MyViewHolder>(){

    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_my_food, parent, false)) {
        private var title: TextView? = null
        private var delete: ImageView? = null
        private var edit: ImageView? = null


        init {
            title = itemView.findViewById(R.id.title)
            delete = itemView.findViewById(R.id.delete)
            edit = itemView.findViewById(R.id.edit)
        }

        fun bind(MyExcersie: MyRecipe, onActionClick: OnActionClick) {
            title?.text = MyExcersie.Title
            delete?.setOnClickListener(View.OnClickListener {
                onActionClick.onDelete(MyExcersie,adapterPosition)
            })
            edit?.setOnClickListener(View.OnClickListener {
                onActionClick.onEdit(MyExcersie,adapterPosition)
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
        holder.bind(list.get(position),onActionClick)
    }

    interface OnActionClick{
        fun onClick(item:MyRecipe , position:Int)
        fun onEdit(item:MyRecipe , position:Int)
        fun onDelete(item:MyRecipe , position:Int)
    }
}