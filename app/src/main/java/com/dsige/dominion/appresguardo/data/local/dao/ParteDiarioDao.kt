package com.dsige.dominion.appresguardo.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.dsige.dominion.appresguardo.data.local.model.ParteDiario

@Dao
interface ParteDiarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParteDiarioTask(c: ParteDiario)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParteDiarioListTask(c: List<ParteDiario>)

    @Update
    fun updateParteDiarioTask(vararg c: ParteDiario)

    @Delete
    fun deleteParteDiarioTask(c: ParteDiario)

    @Query("SELECT * FROM ParteDiario")
    fun getParteDiario(): LiveData<ParteDiario>

    @Query("SELECT * FROM ParteDiario WHERE parteDiarioId =:id")
    fun getParteDiarioById(id: Int): LiveData<ParteDiario>

    @Query("SELECT parteDiarioId FROM ParteDiario ORDER BY parteDiarioId DESC LIMIT 1")
    fun getMaxIdOt(): LiveData<Int>

    @Query("SELECT * FROM ParteDiario")
    fun getOts(): DataSource.Factory<Int, ParteDiario>

    @Query("SELECT * FROM ParteDiario WHERE nroObraTD LIKE :s")
    fun getOts(s: String): DataSource.Factory<Int, ParteDiario>

    @Query("SELECT * FROM ParteDiario WHERE estadoId=:e")
    fun getOts(e: Int): DataSource.Factory<Int, ParteDiario>

    @Query("SELECT * FROM ParteDiario WHERE estadoId=:e AND nroObraTD LIKE :s")
    fun getOts(e: Int, s: String): DataSource.Factory<Int, ParteDiario>

    @Query("DELETE FROM ParteDiario")
    fun deleteAll()

    @Query("SELECT * FROM ParteDiario WHERE estado =:i")
    fun getAllRegistroTask(i: Int): List<ParteDiario>


    @Query("UPDATE ParteDiario SET identity =:codigoRetorno , estado = 0 , estadoId = 5 WHERE parteDiarioId=:codigoBase")
    fun updateEnabledOt(codigoBase: Int, codigoRetorno: Int)

    @Query("SELECT * FROM ParteDiario WHERE parteDiarioId =:id")
    fun getOtIdTask(id: Int): ParteDiario
}