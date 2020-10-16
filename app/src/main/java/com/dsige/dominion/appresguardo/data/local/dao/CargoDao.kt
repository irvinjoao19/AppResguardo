package com.dsige.dominion.appresguardo.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dominion.appresguardo.data.local.model.Accesos
import com.dsige.dominion.appresguardo.data.local.model.Cargo

@Dao
interface CargoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCargoTask(c: Cargo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCargoListTask(c: List<Cargo>)

    @Update
    fun updateCargoTask(vararg c: Cargo)

    @Delete
    fun deleteCargoTask(c: Cargo)

    @Query("SELECT * FROM Cargo")
    fun getCargos(): LiveData<List<Cargo>>

    @Query("DELETE FROM Cargo")
    fun deleteAll()
}