package com.dsige.dominion.appresguardo.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dominion.appresguardo.R
import com.dsige.dominion.appresguardo.data.local.model.Area
import com.dsige.dominion.appresguardo.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo.view.*
import java.util.*
import kotlin.collections.ArrayList

class AreaAdapter(private val listener: OnItemClickListener.AreaListener) :
    RecyclerView.Adapter<AreaAdapter.ViewHolder>() {

    private var d = emptyList<Area>()
    private var dList: ArrayList<Area> = ArrayList()

    fun addItems(list: List<Area>) {
        d = list
        dList = ArrayList(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_combo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dList[position], listener)
    }

    override fun getItemCount(): Int {
        return dList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(m: Area, listener: OnItemClickListener.AreaListener) = with(itemView) {
            textViewTitulo.text = m.nombreArea
            itemView.setOnClickListener { v -> listener.onItemClick(m, v, adapterPosition) }
        }
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                return FilterResults()
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                dList.clear()
                val keyword = charSequence.toString()
                if (keyword.isEmpty()) {
                    dList.addAll(d)
                } else {
                    val filteredList = ArrayList<Area>()
                    for (s: Area in d) {
                        if (s.nombreArea.toLowerCase(
                                Locale.getDefault()
                            ).contains(
                                keyword
                            )
                        ) {
                            filteredList.add(s)
                        }
                    }
                    dList = filteredList
                }
                notifyDataSetChanged()
            }
        }
    }
}