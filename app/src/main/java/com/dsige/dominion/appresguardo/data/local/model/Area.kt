package com.dsige.dominion.appresguardo.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
open class Area {
    @PrimaryKey
    var areaId: Int = 0
    var nombreArea: String = ""
    var estado: Int = 0
}