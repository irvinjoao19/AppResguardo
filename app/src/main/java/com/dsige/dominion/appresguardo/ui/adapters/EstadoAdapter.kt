package com.dsige.dominion.appresguardo.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dominion.appresguardo.R
import com.dsige.dominion.appresguardo.data.local.model.Estado
import com.dsige.dominion.appresguardo.ui.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.cardview_combo.view.*

class EstadoAdapter(private val listener: OnItemClickListener.EstadoListener) :
    RecyclerView.Adapter<EstadoAdapter.ViewHolder>() {

    private var menu = emptyList<Estado>()

    fun addItems(list: List<Estado>) {
        menu = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_combo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(menu[position], listener)
    }

    override fun getItemCount(): Int {
        return menu.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(m: Estado, listener: OnItemClickListener.EstadoListener) = with(itemView) {
            textViewTitulo.text = m.abreviatura
            itemView.setOnClickListener { v -> listener.onItemClick(m, v, adapterPosition) }
        }
    }
}