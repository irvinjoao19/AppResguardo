package com.dsige.dominion.appresguardo.ui.listeners

import android.view.View
import com.dsige.dominion.appresguardo.data.local.model.*

interface OnItemClickListener {
    interface AreaListener {
        fun onItemClick(a: Area, view: View, position: Int)
    }

    interface PersonalListener {
        fun onItemClick(p: Personal, view: View, position: Int)
    }

    interface ParteDiarioListener {
        fun onItemClick(p: ParteDiario, view: View, position: Int)
    }

    interface EstadoListener {
        fun onItemClick(e: Estado, view: View, position: Int)
    }

    interface CargoListener {
        fun onItemClick(c: Cargo, view: View, position: Int)
    }

    interface TipoDocumentoListener {
        fun onItemClick(t: TipoDocumento, view: View, position: Int)
    }
}