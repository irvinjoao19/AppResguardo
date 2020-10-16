package com.dsige.dominion.appresguardo.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dominion.appresguardo.data.local.model.Personal

@Dao
interface PersonalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPersonalTask(c: Personal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPersonalListTask(c: List<Personal>)

    @Update
    fun updatePersonalTask(vararg c: Personal)

    @Delete
    fun deletePersonalTask(c: Personal)

    @Query("SELECT * FROM Personal")
    fun getPersonal(): LiveData<Personal>

    @Query("SELECT * FROM Personal WHERE cargoId =:id")
    fun getPersonalById(id: Int): LiveData<List<Personal>>

    @Query("DELETE FROM Personal")
    fun deleteAll()

    @Query("SELECT * FROM Personal WHERE otId =:id")
    fun getPersonalByOt(id: Int): List<Personal>

    @Query("SELECT * FROM Personal WHERE otId =:id")
    fun getPersonalOtById(id: Int): LiveData<List<Personal>>

    @Query("SELECT * FROM Personal WHERE nroDocumento =:doc")
    fun getPersonalDoc(doc: String): Personal

}