package com.dsige.dominion.appresguardo.data.local.repository

import androidx.lifecycle.LiveData
import com.dsige.dominion.appresguardo.data.local.model.*
import com.dsige.dominion.appresguardo.helper.Mensaje
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Call

interface AppRepository {

    fun getUsuarioIdTask(): Int

    fun getUsuario(): LiveData<Usuario>

    fun getUsuarioService(
        usuario: String, password: String, imei: String, version: String
    ): Observable<Usuario>

    fun getLogout(login: String): Observable<Mensaje>

    fun insertUsuario(u: Usuario): Completable

    fun deleteSesion(): Completable

    fun deleteSync(): Completable

    fun getSync(u: Int, e: Int, p: Int): Observable<Sync>

    fun saveSync(s: Sync): Completable

    fun saveGps(body: RequestBody): Call<Mensaje>

    fun saveMovil(body: RequestBody): Call<Mensaje>

    fun getAccesos(usuarioId: Int): LiveData<List<Accesos>>
}