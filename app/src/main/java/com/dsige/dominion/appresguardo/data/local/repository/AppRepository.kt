package com.dsige.dominion.appresguardo.data.local.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.dsige.dominion.appresguardo.data.local.model.*
import com.dsige.dominion.appresguardo.helper.Mensaje
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Call

interface AppRepository {
    //usuario , sync , envio gps,movil
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

    fun getEstados(): LiveData<List<Estado>>
    fun getFirstArea(): LiveData<Area>
    fun getAreas(): LiveData<List<Area>>
    fun getPersonalById(id: Int): LiveData<List<Personal>>
    fun getMaxIdOt(): LiveData<Int>
    fun insertOrUpdateOt(t: ParteDiario): Completable
    fun getParteDiarioById(id: Int): LiveData<ParteDiario>
    fun getOts(): LiveData<PagedList<ParteDiario>>
    fun getOts(s: String): LiveData<PagedList<ParteDiario>>
    fun getOts(e: Int): LiveData<PagedList<ParteDiario>>
    fun getOts(e: Int, s: String): LiveData<PagedList<ParteDiario>>

    fun getSendOt(i: Int): Observable<List<ParteDiario>>
    fun sendRegistroOt(body: RequestBody): Observable<Mensaje>
    fun updateOt(t: Mensaje): Completable
    fun getTipoDocumento(): LiveData<List<TipoDocumento>>
    fun getPersonalOtById(otId: Int): LiveData<List<Personal>>
    fun insertPersonal(p: Personal): Completable

    fun deletePhoto(o: ParteDiarioPhoto, context: Context): Completable
    fun getPhotoById(otId: Int): LiveData<List<ParteDiarioPhoto>>
    fun insertPhoto(f: ParteDiarioPhoto): Completable
    fun insertMultiPhoto(f: ArrayList<ParteDiarioPhoto>): Completable
    fun updateRegistro(t: ParteDiario): Completable

}