package com.dsige.dominion.appresguardo.data.module

import android.app.Application
import androidx.room.Room
import com.dsige.dominion.appresguardo.data.local.dao.*
import com.dsige.dominion.appresguardo.data.local.AppDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule {

    @Provides
    @Singleton
    internal fun provideRoomDatabase(application: Application): AppDataBase {
        if (AppDataBase.INSTANCE == null) {
            synchronized(AppDataBase::class.java) {
                if (AppDataBase.INSTANCE == null) {
                    AppDataBase.INSTANCE = Room.databaseBuilder(
                        application.applicationContext,
                        AppDataBase::class.java, AppDataBase.DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
        }
        return AppDataBase.INSTANCE!!
    }

    @Provides
    internal fun provideUsuarioDao(appDataBase: AppDataBase): UsuarioDao {
        return appDataBase.usuarioDao()
    }

    @Provides
    internal fun provideAccesoDao(appDataBase: AppDataBase): AccesosDao {
        return appDataBase.accesosDao()
    }
}