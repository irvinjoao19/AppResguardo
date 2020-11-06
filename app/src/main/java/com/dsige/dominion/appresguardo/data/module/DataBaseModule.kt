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
    internal fun provideAccesosDao(appDataBase: AppDataBase): AccesosDao {
        return appDataBase.accesosDao()
    }

    @Provides
    internal fun provideAreaDao(appDataBase: AppDataBase): AreaDao {
        return appDataBase.areaDao()
    }

    @Provides
    internal fun provideCargoDao(appDataBase: AppDataBase): CargoDao {
        return appDataBase.cargoDao()
    }

    @Provides
    internal fun provideEstadoDao(appDataBase: AppDataBase): EstadoDao {
        return appDataBase.estadoDao()
    }

    @Provides
    internal fun provideParteDiarioDao(appDataBase: AppDataBase): ParteDiarioDao {
        return appDataBase.parteDiarioDao()
    }

    @Provides
    internal fun providePersonalDao(appDataBase: AppDataBase): PersonalDao {
        return appDataBase.personalDao()
    }

    @Provides
    internal fun provideTipoDocumentoDao(appDataBase: AppDataBase): TipoDocumentoDao {
        return appDataBase.tipoDocumentoDao()
    }

    @Provides
    internal fun provideUsuarioDao(appDataBase: AppDataBase): UsuarioDao {
        return appDataBase.usuarioDao()
    }

    @Provides
    internal fun provideParteDiarioPhotoDao(appDataBase: AppDataBase): ParteDiarioPhotoDao {
        return appDataBase.parteDiarioPhotoDao()
    }
}