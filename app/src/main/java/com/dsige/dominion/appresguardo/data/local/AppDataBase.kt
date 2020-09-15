package com.dsige.dominion.appresguardo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dsige.dominion.appresguardo.data.local.dao.*
import com.dsige.dominion.appresguardo.data.local.model.*

@Database(
    entities = [
        Usuario::class,
        Accesos::class
    ],
    version = 1, // version 1 en play store
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun accesosDao(): AccesosDao

    companion object {
        @Volatile
        var INSTANCE: AppDataBase? = null
        val DB_NAME = "utf_db"
    }

    fun getDatabase(context: Context): AppDataBase {
        if (INSTANCE == null) {
            synchronized(AppDataBase::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java, "utf_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
        }
        return INSTANCE!!
    }
}