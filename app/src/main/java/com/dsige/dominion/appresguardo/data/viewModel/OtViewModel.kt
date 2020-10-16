package com.dsige.dominion.appresguardo.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.dsige.dominion.appresguardo.data.local.model.*
import com.dsige.dominion.appresguardo.data.local.repository.*
import com.dsige.dominion.appresguardo.helper.Mensaje
import com.dsige.dominion.appresguardo.helper.Util
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class OtViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {


    val mensajePersonal = MutableLiveData<String>()
    val mensajeError = MutableLiveData<String>()
    val mensajeSuccess = MutableLiveData<String>()
    val search: MutableLiveData<String> = MutableLiveData()

    val user: LiveData<Usuario>
        get() = roomRepository.getUsuario()

    fun setError(s: String) {
        mensajeError.value = s
    }

    fun setErrorP(s:String?){
        mensajePersonal.value = s
    }

    fun getEstados(): LiveData<List<Estado>> {
        return roomRepository.getEstados()
    }

    fun getOts(): LiveData<PagedList<ParteDiario>> {
        return Transformations.switchMap(search) { input ->
            if (input == null || input.isEmpty()) {
                roomRepository.getOts()
            } else {
                val f = Gson().fromJson(search.value, Filtro::class.java)
                if (f.estadoId == 0) {
                    if (f.search.isNotEmpty()) {
                        roomRepository.getOts(
                            String.format("%s%s%s", "%", f.search, "%")
                        )
                    } else {
                        roomRepository.getOts()
                    }
                } else {
                    if (f.search.isEmpty()) {
                        roomRepository.getOts(f.estadoId)
                    } else {
                        roomRepository.getOts(
                            f.estadoId, String.format("%s%s%s", "%", f.search, "%")
                        )
                    }
                }
            }
        }
    }

    fun validateOt(t: ParteDiario) {
        if (t.nombreServicio.isEmpty()) {
            mensajeError.value = "Seleccione Area"
            return
        }
        if (t.fechaAsignacion.isEmpty()) {
            mensajeError.value = "Ingresar Fecha"
            return
        }
        if (t.nombreCoordinador.isEmpty()) {
            mensajeError.value = "Seleccione Coordinador"
            return
        }
        if (t.nombreJefeCuadrilla.isEmpty()) {
            mensajeError.value = "Seleccione Jefe de Cuadrilla"
            return
        }
        if (t.nroObraTD.isEmpty()) {
            mensajeError.value = "Ingrese Nro OT/TD"
            return
        }
        insertOrUpdateOt(t)
    }

    private fun insertOrUpdateOt(t: ParteDiario) {
        roomRepository.insertOrUpdateOt(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    mensajeSuccess.value = "Ot Generado"
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }

            })
    }

    fun getOtById(id: Int): LiveData<ParteDiario> {
        return roomRepository.getParteDiarioById(id)
    }

    fun getMaxIdOt(): LiveData<Int> {
        return roomRepository.getMaxIdOt()
    }

    fun getAreas(): LiveData<List<Area>> {
        return roomRepository.getAreas()
    }

    fun getPersonalById(id: Int): LiveData<List<Personal>> {
        return roomRepository.getPersonalById(id)
    }

    fun getTipoDocumento(): LiveData<List<TipoDocumento>> {
        return roomRepository.getTipoDocumento()
    }

    fun validatePersonal(p: Personal) {
        if (p.nroDocumento.isEmpty()) {
            mensajePersonal.value = "Ingrese Nro Documento"
            return
        }

        insertPersonal(p)
    }

    private fun insertPersonal(p: Personal) {
        roomRepository.insertPersonal(p)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    mensajePersonal.value = "ok"
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    mensajePersonal.value = e.message
                }

            })

    }

    fun getPersonalOtById(otId: Int): LiveData<List<Personal>> {
        return roomRepository.getPersonalOtById(otId)
    }

}