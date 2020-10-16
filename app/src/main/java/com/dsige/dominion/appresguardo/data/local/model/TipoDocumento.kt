package com.dsige.dominion.appresguardo.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class TipoDocumento {

    @PrimaryKey
    var tipoId: Int = 0
    var descripcion: String = ""
}