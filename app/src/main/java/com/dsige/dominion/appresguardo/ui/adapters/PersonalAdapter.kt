package com.dsige.dominion.appresguardo.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dominion.appresguardo.R
import com.dsige.dominion.appresguardo.data.local.model.Personal
import com.dsige.dominion.appresguardo.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo.view.*
import java.util.*
import kotlin.collections.ArrayList

class PersonalAdapter(private val listener: OnItemClickListener.PersonalListener) :
    RecyclerView.Adapter<PersonalAdapter.ViewHolder>() {

    private var d = emptyList<Personal>()
    private var dList: ArrayList<Personal> = ArrayList()

    fun addItems(list: List<Personal>) {
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
        fun bind(m: Personal, listener: OnItemClickListener.PersonalListener) = with(itemView) {
            textViewTitulo.text = String.format("%s %s", m.nombre, m.apellidos)
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
                    val filteredList = ArrayList<Personal>()
                    for (s: Personal in d) {
                        if (s.apellidos.toLowerCase(Locale.getDefault()).contains(keyword)
                            || s.nombre.toLowerCase(Locale.getDefault()).contains(keyword)
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