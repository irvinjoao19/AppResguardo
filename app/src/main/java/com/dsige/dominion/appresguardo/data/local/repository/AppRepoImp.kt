package com.dsige.dominion.appresguardo.data.local.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.dsige.dominion.appresguardo.data.local.model.*
import com.dsige.dominion.appresguardo.helper.Mensaje
import com.google.gson.Gson
import com.dsige.dominion.appresguardo.data.local.AppDataBase
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
//            val o: List<Ot>? = s.ots
//            if (o != null) {
//                dataBase.otDao().insertOtListTask(o)
//            }
//            val g: List<Grupo>? = s.groups
//            if (g != null) {
//                dataBase.grupoDao().insertGrupoListTask(g)
//            }
//            val e: List<Estado>? = s.estados
//            if (e != null) {
//                dataBase.estadoDao().insertEstadoListTask(e)
//            }
//            val d: List<Distrito>? = s.distritos
//            if (d != null) {
//                dataBase.distritoDao().insertDistritoListTask(d)
//            }
//            val m: List<Material>? = s.materials
//            if (m != null) {
//                dataBase.materialDao().insertMaterialListTask(m)
//            }
//            val se: List<Servicio>? = s.servicios
//            if (se != null) {
//                dataBase.servicioDao().insertServicioListTask(se)
//                if (se.isNotEmpty()) {
//                    dataBase.usuarioDao().updateServicio(se[0].nombreServicio, se[0].servicioId)
//                }
//            }
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
}