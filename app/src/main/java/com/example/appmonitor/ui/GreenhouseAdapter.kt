package com.example.appmonitor.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appmonitor.R
import com.example.appmonitor.model.Greenhouse
import kotlinx.android.synthetic.main.row_item_greenhouse.view.*

class GreenhouseAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<GreenhouseAdapter.ViewHolder>() {

    var greenhouses = ArrayList<Greenhouse>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        fun bind(greenhouse: Greenhouse) =
            with(itemView) {
                tvGreenhouseId.text = greenhouse.id.toString()
                tvGreenhouseName.text = greenhouse.nameGreenhouse
            }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            val id: String = itemView.tvGreenhouseId.text.toString()
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_item_greenhouse, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val greenhouse = greenhouses[position]
        holder.bind(greenhouse)
    }

    override fun getItemCount(): Int {
        return greenhouses.size
    }

    interface OnItemClickListener {
        fun onItemClick(id: String)
    }
}