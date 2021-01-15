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
        Accesos::class,
        Area::class,
        Cargo::class,
        Estado::class,
        ParteDiario::class,
        Personal::class,
        TipoDocumento::class,
        ParteDiarioPhoto::class
    ],
    version = 11, // version 2 en play store
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun accesosDao(): AccesosDao
    abstract fun areaDao(): AreaDao
    abstract fun cargoDao(): CargoDao
    abstract fun estadoDao(): EstadoDao
    abstract fun parteDiarioDao(): ParteDiarioDao
    abstract fun personalDao(): PersonalDao
    abstract fun tipoDocumentoDao(): TipoDocumentoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun parteDiarioPhotoDao(): ParteDiarioPhotoDao

    companion object {
        @Volatile
        var INSTANCE: AppDataBase? = null
        val DB_NAME = "resguar_db"
    }

    fun getDatabase(context: Context): AppDataBase {
        if (INSTANCE == null) {
            synchronized(AppDataBase::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java, "resguardo_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
        }
        return INSTANCE!!
    }
}