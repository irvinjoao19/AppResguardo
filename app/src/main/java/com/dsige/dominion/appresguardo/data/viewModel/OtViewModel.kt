package com.dsige.dominion.appresguardo.data.viewModel

import android.content.Context
import android.content.Intent
import android.util.Log
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

    fun setErrorP(s: String?) {
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
        if (t.horaInicio.isEmpty()) {
            mensajeError.value = "Ingresar Hora Inicio"
            return
        }
        if (t.horaTermino.isEmpty()) {
            mensajeError.value = "Ingresar Hora Termino"
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

    fun getFirstArea(): LiveData<Area> {
        return roomRepository.getFirstArea()
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

    fun deletePhoto(o: ParteDiarioPhoto, context: Context) {
        roomRepository.deletePhoto(o, context)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    mensajeError.value = "Foto Eliminada"
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {

                }
            })
    }

    fun getPhotoById(otId: Int): LiveData<List<ParteDiarioPhoto>> {
        return roomRepository.getPhotoById(otId)
    }

    fun generarArchivo(
        size: Int, usuarioId: Int, context: Context,
        data: Intent, direccion: String, distrito: String
    ) {
        Util.getFolderAdjunto(size, usuarioId, context, data, direccion, distrito)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ArrayList<String>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: ArrayList<String>) {
                    mensajeSuccess.value = t.toString()
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.message
                }

                override fun onComplete() {}
            })
    }

    fun insertPhoto(f: ParteDiarioPhoto) {
        roomRepository.insertPhoto(f)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    mensajeSuccess.value = "Ok"
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.i("TAG", e.toString())
                }
            })
    }

    fun insertMultiPhoto(f: ArrayList<ParteDiarioPhoto>) {
        roomRepository.insertMultiPhoto(f)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    mensajeSuccess.value = "Ok"
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    Log.i("TAG", e.toString())
                }
            })
    }

    fun validateRegistro(t: ParteDiario, tipo: String) {
        if (t.incidencia.isEmpty()) {
            mensajeError.value = "Ingrese Incidencia"
            return
        }
        updateRegistro(t, tipo)
    }

    private fun updateRegistro(t: ParteDiario, tipo: String) {
        roomRepository.updateRegistro(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    mensajeSuccess.value = tipo
                }

                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {
                    Log.i("TAG", e.toString())
                }
            })
    }

}