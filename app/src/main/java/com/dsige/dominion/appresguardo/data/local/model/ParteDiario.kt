package com.dsige.dominion.appresguardo.data.local.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
open class ParteDiario {
    @PrimaryKey(autoGenerate = true)
    var parteDiarioId: Int = 0
    var empresaId: Int = 0
    var servicioId: Int = 0
    var nombreServicio: String = ""
    var fechaAsignacion: String = ""
    var fechaRegistro: String = ""
    var horaInicio: String = ""
    var horaTermino: String = ""
    var totalHoras: String = ""
    var cantidadHoras: Double = 0.0
    var precio: Double = 0.0
    var totalSoles: Double = 0.0
    var usuarioEfectivoPolicialId: Int = 0
    var personalCoordinarId: Int = 0
    var personalJefeCuadrillaId: Int = 0
    var lugarTrabajoPD: String = ""
    var nroObraTD: String = ""
    var observacionesPD: String = ""
    var latitud: String = ""
    var longitud: String = ""
    var firmaEfectivoPolicial: String = ""
    var firmaJefeCuadrilla: String = ""
    var identity: Int = 0
    var usuarioId: Int = 0

    var estadoId: Int = 0 // 4 -> ejecutado  // 5 enviado aprobar
    var estado: Int = 0 // 1 -> para enviar , 2 -> incompleto

    var nombreJefeCuadrilla : String = ""
    var nombreCoordinador : String = ""


    @Ignore
    var personals: List<Personal> = ArrayList()

}