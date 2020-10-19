package com.dsige.dominion.appresguardo.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dsige.dominion.appresguardo.data.local.model.Accesos
import com.dsige.dominion.appresguardo.data.local.model.Area

@Dao
interface AreaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAreaTask(c: Area)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAreaListTask(c: List<Area>)

    @Update
    fun updateAreaTask(vararg c: Area)

    @Delete
    fun deleteAreaTask(c: Area)

    @Query("SELECT * FROM Area")
    fun getAreas(): LiveData<List<Area>>

    @Query("DELETE FROM Area")
    fun deleteAll()

    @Query("SELECT * FROM Area")
    fun getFirstArea(): LiveData<Area>
}