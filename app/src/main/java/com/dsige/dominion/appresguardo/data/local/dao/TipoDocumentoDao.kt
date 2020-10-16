package com.dsige.dominion.appresguardo.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dominion.appresguardo.data.local.model.TipoDocumento

@Dao
interface TipoDocumentoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTipoDocumentoTask(c: TipoDocumento)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTipoDocumentoListTask(c: List<TipoDocumento>)

    @Update
    fun updateTipoDocumentoTask(vararg c: TipoDocumento)

    @Delete
    fun deleteTipoDocumentoTask(c: TipoDocumento)

    @Query("SELECT * FROM TipoDocumento")
    fun getTipoDocumentos(): LiveData<List<TipoDocumento>>

    @Query("DELETE FROM TipoDocumento")
    fun deleteAll()
}