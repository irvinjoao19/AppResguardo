package com.dsige.dominion.appresguardo.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Personal {
    @PrimaryKey(autoGenerate = true)
    var personalId: Int = 0
    var empresaId: Int = 0
    var tipoDocId: Int = 0
    var nroDocumento: String = ""
    var apellidos: String = ""
    var nombre: String = ""
    var cargoId: Int = 0
    var direccion: String = ""
    var distritoId: Int = 0
    var estado: Int = 0
    var usuarioId: Int = 0
    var otId: Int = 0
}