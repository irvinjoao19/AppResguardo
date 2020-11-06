package com.dsige.dominion.appresguardo.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dsige.dominion.appresguardo.R
import com.dsige.dominion.appresguardo.data.local.model.ParteDiarioPhoto
import com.dsige.dominion.appresguardo.helper.Util
import com.dsige.dominion.appresguardo.ui.listeners.OnItemClickListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cardview_photo.view.*
import java.io.File

class PhotoAdapter(private val listener: OnItemClickListener.PhotoListener) :
    RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    private var photos = emptyList<ParteDiarioPhoto>()

    fun addItems(list: List<ParteDiarioPhoto>) {
        photos = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview_photo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(photos[position], listener)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(o: ParteDiarioPhoto, listener: OnItemClickListener.PhotoListener) =
            with(itemView) {
                val url = Util.UrlFoto + o.fotoUrl
                Picasso.get()
                    .load(url)
                    .into(imageViewPhoto, object : Callback {
                        override fun onSuccess() {
                            progress.visibility = View.GONE
                        }

                        override fun onError(e: Exception) {
                            val f = File(Util.getFolder(itemView.context), o.fotoUrl)
                            Picasso.get()
                                .load(f)
                                .into(imageViewPhoto, object : Callback {
                                    override fun onSuccess() {
                                        progress.visibility = View.GONE
                                    }

                                    override fun onError(e: Exception) {

                                    }
                                })
                        }
                    })
                textViewName.text = o.fotoUrl
                itemView.setOnClickListener { v -> listener.onItemClick(o, v, adapterPosition) }
            }
    }
}