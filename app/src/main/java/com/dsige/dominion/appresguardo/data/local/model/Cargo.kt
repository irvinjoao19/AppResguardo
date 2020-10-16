package com.dsige.dominion.appresguardo.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Cargo {
    @PrimaryKey
    var cargoId: Int = 0
    var nombreCargo: String = ""
    var estado: Int = 0
}