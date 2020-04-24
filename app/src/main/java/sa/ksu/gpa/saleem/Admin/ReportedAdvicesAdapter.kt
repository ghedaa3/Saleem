package sa.ksu.gpa.saleem.Admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sa.ksu.gpa.saleem.R


class ReportedAdvicesAdapter (private val list: List<ReportedAdvices>, var onActionClick: OnActionClick): RecyclerView.Adapter<ReportedAdvicesAdapter.MyViewHolder>(){

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

        fun bind(ReportedAdvices: ReportedAdvices, onActionClick: OnActionClick) {
            title?.text = ReportedAdvices.report
            delete?.setOnClickListener(View.OnClickListener {
                onActionClick.onDelete(ReportedAdvices,adapterPosition)
            })
            edit?.setOnClickListener(View.OnClickListener {
                onActionClick.onEdit(ReportedAdvices,adapterPosition)
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
        fun onClick(item: ReportedAdvices, position:Int)
        fun onEdit(item: ReportedAdvices, position:Int)
        fun onDelete(item: ReportedAdvices, position:Int)
    }
}