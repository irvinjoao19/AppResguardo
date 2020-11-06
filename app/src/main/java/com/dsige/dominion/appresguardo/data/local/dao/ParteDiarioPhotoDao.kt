package com.dsige.dominion.appresguardo.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dominion.appresguardo.data.local.model.ParteDiarioPhoto

@Dao
interface ParteDiarioPhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParteDiarioPhotoTask(c: ParteDiarioPhoto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertParteDiarioPhotoListTask(c: List<ParteDiarioPhoto>)

    @Update
    fun updateParteDiarioPhotoTask(vararg c: ParteDiarioPhoto)

    @Delete
    fun deleteParteDiarioPhotoTask(c: ParteDiarioPhoto)

    @Query("SELECT * FROM ParteDiarioPhoto")
    fun getParteDiarioPhoto(): LiveData<ParteDiarioPhoto>

    @Query("SELECT photoId FROM ParteDiarioPhoto")
    fun getParteDiarioPhotoId(): Int

    @Query("SELECT * FROM ParteDiarioPhoto WHERE parteDiarioId =:id")
    fun getParteDiarioPhotoById(id: Int): LiveData<List<ParteDiarioPhoto>>

    @Query("DELETE FROM ParteDiarioPhoto")
    fun deleteAll()

    @Query("SELECT photoId FROM ParteDiarioPhoto")
    fun getParteDiarioPhotoIdTask(): Int

    @Query("SELECt * FROM ParteDiarioPhoto WHERE fotoUrl =:img")
    fun getOtPhotoName(img: String): ParteDiarioPhoto

    @Query("SELECT * FROM ParteDiarioPhoto WHERE parteDiarioId =:id AND active = 1")
    fun getParteDiarioPhotoByOt(id: Int): List<ParteDiarioPhoto>

    @Query("UPDATE ParteDiarioPhoto SET identity =:detalleRetornoId , active = 0 WHERE photoId =:detalleBaseId")
    fun updateEnabledPhoto(detalleBaseId: Int, detalleRetornoId: Int)
}