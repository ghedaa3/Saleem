package sa.ksu.gpa.saleem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
class MyExcersieAdapter (private val list: List<MyExcersie>, var onActionClick: OnActionClick): RecyclerView.Adapter<MyExcersieAdapter.MyViewHolder>(){

    class MyViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_my_food, parent, false)) {
        private var title: TextView? = null
        private var date: TextView? = null
        private var delete: ImageView? = null
        private var edit: ImageView? = null


        init {
            title = itemView.findViewById(R.id.title)
            date = itemView.findViewById(R.id.date)
            delete = itemView.findViewById(R.id.delete)
            edit = itemView.findViewById(R.id.edit)
        }

        fun bind(MyExcersie: MyExcersie, onActionClick: OnActionClick) {
            title?.text = MyExcersie.Title
            date?.text = MyExcersie.Date
            delete?.setOnClickListener(View.OnClickListener {
                onActionClick.onDelete(MyExcersie,adapterPosition)
            })

            if(MyExcersie.Type=="FromExercise")
                edit!!.visibility= View.GONE
            else
                edit!!.visibility= View.VISIBLE

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
        fun onClick(item:MyExcersie , position:Int)
        fun onEdit(item:MyExcersie , position:Int)
        fun onDelete(item:MyExcersie , position:Int)
    }
}