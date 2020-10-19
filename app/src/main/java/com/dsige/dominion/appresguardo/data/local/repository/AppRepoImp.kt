package com.dsige.dominion.appresguardo.data.local.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.dsige.dominion.appresguardo.data.local.model.*
import com.dsige.dominion.appresguardo.helper.Mensaje
import com.google.gson.Gson
import com.dsige.dominion.appresguardo.data.local.AppDataBase
import com.dsige.dominion.appresguardo.helper.Util
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call

class AppRepoImp(private val apiService: ApiService, private val dataBase: AppDataBase) :
    AppRepository {

    override fun getUsuarioIdTask(): Int {
        return dataBase.usuarioDao().getUsuarioIdTask()
    }

    override fun getUsuario(): LiveData<Usuario> {
        return dataBase.usuarioDao().getUsuario()
    }

    override fun getUsuarioService(
        usuario: String, password: String, imei: String, version: String
    ): Observable<Usuario> {
        val u = Filtro(usuario, password, imei, version)
        val json = Gson().toJson(u)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.getLogin(body)
    }

    override fun getLogout(login: String): Observable<Mensaje> {
        val u = Filtro(login)
        val json = Gson().toJson(u)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.getLogout(body)
    }

    override fun insertUsuario(u: Usuario): Completable {
        return Completable.fromAction {
            dataBase.usuarioDao().insertUsuarioTask(u)
            val a: List<Accesos>? = u.accesos
            if (a != null) {
                dataBase.accesosDao().insertAccesosListTask(a)
            }
        }
    }

    override fun deleteSesion(): Completable {
        return Completable.fromAction {
            dataBase.accesosDao().deleteAll()
            dataBase.areaDao().deleteAll()
            dataBase.cargoDao().deleteAll()
            dataBase.estadoDao().deleteAll()
            dataBase.parteDiarioDao().deleteAll()
            dataBase.personalDao().deleteAll()
            dataBase.tipoDocumentoDao().deleteAll()
            dataBase.usuarioDao().deleteAll()
        }
    }

    override fun deleteSync(): Completable {
        return Completable.fromAction {

        }
    }

    override fun getSync(u: Int, e: Int, p: Int): Observable<Sync> {
        val f = Filtro(u, e, p)
        val json = Gson().toJson(f)
        Log.i("TAG", json)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return apiService.getSync(body)
    }

    override fun saveSync(s: Sync): Completable {
        return Completable.fromAction {
            val pds: List<ParteDiario>? = s.pds
            if (pds != null) {
                dataBase.parteDiarioDao().insertParteDiarioListTask(pds)
            }
            val personals: List<Personal>? = s.personals
            if (personals != null) {
                dataBase.personalDao().insertPersonalListTask(personals)
            }
            val cargos: List<Cargo>? = s.cargos
            if (cargos != null) {
                dataBase.cargoDao().insertCargoListTask(cargos)
            }
            val areas: List<Area>? = s.areas
            if (areas != null) {
                dataBase.areaDao().insertAreaListTask(areas)
            }
            val estados: List<Estado>? = s.estados
            if (estados != null) {
                dataBase.estadoDao().insertEstadoListTask(estados)
            }
            val tipoDocuments: List<TipoDocumento>? = s.tipoDocuments
            if (tipoDocuments != null) {
                dataBase.tipoDocumentoDao().insertTipoDocumentoListTask(tipoDocuments)
            }
        }
    }

    override fun saveGps(body: RequestBody): Call<Mensaje> {
        return apiService.saveGps(body)
    }

    override fun saveMovil(body: RequestBody): Call<Mensaje> {
        return apiService.saveMovil(body)
    }

    override fun getAccesos(usuarioId: Int): LiveData<List<Accesos>> {
        return dataBase.accesosDao().getAccesosById(usuarioId)
    }

    override fun getEstados(): LiveData<List<Estado>> {
        return dataBase.estadoDao().getEstados()
    }

    override fun getFirstArea(): LiveData<Area> {
        return dataBase.areaDao().getFirstArea()
    }

    override fun getAreas(): LiveData<List<Area>> {
        return dataBase.areaDao().getAreas()
    }

    override fun getPersonalById(id: Int): LiveData<List<Personal>> {
        return dataBase.personalDao().getPersonalById(id)
    }

    override fun getMaxIdOt(): LiveData<Int> {
        return dataBase.parteDiarioDao().getMaxIdOt()
    }

    override fun insertOrUpdateOt(t: ParteDiario): Completable {
        return Completable.fromAction {
            t.totalHoras = Util.getDifferenceBetwenDates(t.horaInicio, t.horaTermino)
            val p: ParteDiario? = dataBase.parteDiarioDao().getOtIdTask(t.parteDiarioId)
            if (p == null) {
                dataBase.parteDiarioDao().insertParteDiarioTask(t)
            } else
                dataBase.parteDiarioDao().updateParteDiarioTask(t)
        }
    }

    override fun getParteDiarioById(id: Int): LiveData<ParteDiario> {
        return dataBase.parteDiarioDao().getParteDiarioById(id)
    }

    override fun getOts(): LiveData<PagedList<ParteDiario>> {
        return dataBase.parteDiarioDao().getOts().toLiveData(
            Config(pageSize = 20, enablePlaceholders = true)
        )
    }

    override fun getOts(s: String): LiveData<PagedList<ParteDiario>> {
        return dataBase.parteDiarioDao().getOts(s).toLiveData(
            Config(pageSize = 20, enablePlaceholders = true)
        )
    }

    override fun getOts(e: Int): LiveData<PagedList<ParteDiario>> {
        return dataBase.parteDiarioDao().getOts(e).toLiveData(
            Config(pageSize = 20, enablePlaceholders = true)
        )
    }

    override fun getOts(e: Int, s: String): LiveData<PagedList<ParteDiario>> {
        return dataBase.parteDiarioDao().getOts(e, s).toLiveData(
            Config(pageSize = 20, enablePlaceholders = true)
        )
    }

    override fun getSendOt(i: Int): Observable<List<ParteDiario>> {
        return Observable.create { e ->
            val v: List<ParteDiario> = dataBase.parteDiarioDao().getAllRegistroTask(i)
            if (v.isNotEmpty()) {
                val list: ArrayList<ParteDiario> = ArrayList()
                for (r: ParteDiario in v) {
                    r.personals =
                        dataBase.personalDao().getPersonalByOt(r.parteDiarioId)
                    list.add(r)
                }
                e.onNext(list)
            } else
                e.onError(Throwable("No hay datos disponibles por enviar"))

            e.onComplete()
        }
    }

    override fun sendRegistroOt(body: RequestBody): Observable<Mensaje> {
        return apiService.saveRegistro(body)
    }

    override fun updateOt(t: Mensaje): Completable {
        return Completable.fromAction {
            dataBase.parteDiarioDao().updateEnabledOt(t.codigoBase, t.codigoRetorno)
        }
    }

    override fun getTipoDocumento(): LiveData<List<TipoDocumento>> {
        return dataBase.tipoDocumentoDao().getTipoDocumentos()
    }

    override fun getPersonalOtById(otId: Int): LiveData<List<Personal>> {
        return dataBase.personalDao().getPersonalOtById(otId)
    }

    override fun insertPersonal(p: Personal): Completable {
        return Completable.fromAction {
            val d: Personal? = dataBase.personalDao().getPersonalDoc(p.nroDocumento)
            if (d == null) {
                dataBase.personalDao().insertPersonalTask(p)
            } else {
                Throwable("Nro Documento existe volver a ingresar")
            }
        }
    }
}