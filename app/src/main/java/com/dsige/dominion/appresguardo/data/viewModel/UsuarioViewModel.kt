package com.dsige.dominion.appresguardo.data.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dsige.dominion.appresguardo.data.local.model.*
import com.dsige.dominion.appresguardo.data.local.repository.*
import com.dsige.dominion.appresguardo.helper.Mensaje
import com.dsige.dominion.appresguardo.helper.Util
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UsuarioViewModel @Inject
internal constructor(private val roomRepository: AppRepository, private val retrofit: ApiError) :
    ViewModel() {

    val mensajeError = MutableLiveData<String>()
    val mensajeSuccess = MutableLiveData<String>()

    val user: LiveData<Usuario>
        get() = roomRepository.getUsuario()

    fun setError(s: String) {
        mensajeError.value = s
    }

    fun getLogin(usuario: String, pass: String, imei: String, version: String) {
        roomRepository.getUsuarioService(usuario, pass, imei, version)
            .delay(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Usuario> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(usuario: Usuario) {
                    insertUsuario(usuario, version)
                }

                override fun onError(t: Throwable) {
                    if (t is HttpException) {
                        val body = t.response().errorBody()
                        try {
                            val error = retrofit.errorConverter.convert(body!!)
                            mensajeError.postValue(error.Message)
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                        }
                    } else {
                        mensajeError.postValue(t.message)
                    }
                }

                override fun onComplete() {
                }
            })
    }

    fun insertUsuario(u: Usuario, v: String) {
        roomRepository.insertUsuario(u)
            .delay(3, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onComplete() {
//                    getSync(u, v)
                    sync(u.usuarioId, u.empresaId, u.personalId)
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    fun logout(login: String) {
        roomRepository.deleteSesion()
            .delay(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onComplete() {
                    mensajeSuccess.value = "Close"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }


    fun getSync(u: Int, e: Int, p: Int) {
        roomRepository.deleteSync()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onComplete() {
                    sync(u, e, p)
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    fun sync(u: Int, e: Int, p: Int) {
        roomRepository.getSync(u, e, p)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Sync> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Sync) {
                    insertSync(t)
                }

                override fun onError(e: Throwable) {
                    if (e is HttpException) {
                        val body = e.response().errorBody()
                        try {
                            val error = retrofit.errorConverter.convert(body!!)
                            mensajeError.postValue(error.Message)
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                            Log.i("TAG", e1.toString())
                        }
                    } else {
                        mensajeError.postValue(e.toString())
                    }
                }
            })
    }

    fun insertSync(s: Sync) {
        roomRepository.saveSync(s)
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onComplete() {
                    mensajeSuccess.value = "Sincronización Completa"
                }

                override fun onError(e: Throwable) {
                    mensajeError.value = e.toString()
                }
            })
    }

    fun getAccesos(usuarioId: Int): LiveData<List<Accesos>> {
        return roomRepository.getAccesos(usuarioId)
    }

    fun sendData(context: Context) {
        roomRepository.getOtFile(1).flatMap { observable ->
            Observable.fromIterable(observable).flatMap { a ->
                val b = MultipartBody.Builder()
                val file = File(Util.getFolder(context), a)
                if (file.exists()) {
                    b.addFormDataPart(
                        "files", file.name,
                        RequestBody.create(
                            MediaType.parse("multipart/form-data"), file
                        )
                    )
                }
                b.setType(MultipartBody.FORM)
                val body = b.build()
                Observable.zip(
                    Observable.just(a), roomRepository.sendFile(body), { _, mensaje ->
                        mensaje
                    })
            }
        }.subscribeOn(Schedulers.io())
            .delay(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: String) {
                    Log.i("tag", t)
                }

                override fun onError(t: Throwable) {
                    if (t is HttpException) {
                        val body = t.response().errorBody()
                        try {
                            val error = retrofit.errorConverter.convert(body!!)
                            mensajeError.postValue(error.Message)
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                        }
                    } else {
                        mensajeError.postValue(t.message)
                    }
                }

                override fun onComplete() {
                    sendParteDiario()
                }
            })
    }

    private fun sendParteDiario() {
        val ots: Observable<List<ParteDiario>> = roomRepository.getSendOt(1)
        ots.flatMap { observable ->
            Observable.fromIterable(observable).flatMap { a ->
                val json = Gson().toJson(a)
                Log.i("TAG", json)
                val body =
                    RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
                Observable.zip(
                    Observable.just(a), roomRepository.saveParteDiario(body), { _, mensaje ->
                        mensaje
                    })
            }
        }.subscribeOn(Schedulers.io())
            .delay(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Mensaje> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: Mensaje) {
                    updateOt(t)
                }

                override fun onError(t: Throwable) {
                    if (t is HttpException) {
                        val body = t.response().errorBody()
                        try {
                            val error = retrofit.errorConverter.convert(body!!)
                            mensajeError.postValue(error.Message)
                        } catch (e1: IOException) {
                            e1.printStackTrace()
                        }
                    } else {
                        mensajeError.postValue(t.message)
                    }
                }

                override fun onComplete() {
                    mensajeSuccess.value = "Enviado"
                }
            })
    }

    private fun updateOt(t: Mensaje) {
        roomRepository.updateOt(t)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {}
                override fun onSubscribe(d: Disposable) {}
                override fun onError(e: Throwable) {
//                    mensajeError.value = e.message
                }
            })
    }
}