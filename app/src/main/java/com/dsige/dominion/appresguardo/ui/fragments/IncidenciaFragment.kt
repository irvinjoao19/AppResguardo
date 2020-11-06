package com.dsige.dominion.appresguardo.ui.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.dsige.dominion.appresguardo.R
import com.dsige.dominion.appresguardo.data.local.model.ParteDiario
import com.dsige.dominion.appresguardo.data.local.model.ParteDiarioPhoto
import com.dsige.dominion.appresguardo.data.viewModel.OtViewModel
import com.dsige.dominion.appresguardo.data.viewModel.ViewModelFactory
import com.dsige.dominion.appresguardo.helper.Gps
import com.dsige.dominion.appresguardo.helper.Permission
import com.dsige.dominion.appresguardo.helper.Util
import com.dsige.dominion.appresguardo.ui.activities.CameraActivity
import com.dsige.dominion.appresguardo.ui.activities.PreviewCameraActivity
import com.dsige.dominion.appresguardo.ui.adapters.PhotoAdapter
import com.dsige.dominion.appresguardo.ui.listeners.OnItemClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.DaggerFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_incidencia.*
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class IncidenciaFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        if (t.nroObraTD.isEmpty()) {
            otViewModel.setError("Completar primer formulario")
            return
        }
        when (v.id) {
            R.id.fabSearch -> microPhone("Incidencias", 1)
            R.id.fabCamara -> formRegistro("1")
            R.id.fabGaleria -> formRegistro("2")
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var otViewModel: OtViewModel
    lateinit var t: ParteDiario
    private var otId: Int = 0
    private var size: Int = 0
    private var maxSize: Int = 5
    private var usuarioId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        t = ParteDiario()
        arguments?.let {
            otId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_incidencia, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        otViewModel =
            ViewModelProvider(this, viewModelFactory).get(OtViewModel::class.java)

        val photoAdapter = PhotoAdapter(object : OnItemClickListener.PhotoListener {
            override fun onItemClick(p: ParteDiarioPhoto, view: View, position: Int) {
                if (p.active == 1) {
                    confirmDelete(p)
                }
            }
        })

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = photoAdapter

        otViewModel.getPhotoById(otId).observe(viewLifecycleOwner, {
            size = it.size
            photoAdapter.addItems(it)
//            if (it.isNotEmpty())
//                fabSave.visibility = View.VISIBLE
//            else
//                fabSave.visibility = View.GONE

            if (it.size == maxSize) {
                fabCamara.visibility = View.GONE
                fabGaleria.visibility = View.GONE
            }
        })

        otViewModel.getOtById(otId).observe(viewLifecycleOwner, {
            if (it != null) {
                t = it
                editTextIncidencia.setText(it.incidencia)
            }
        })
        otViewModel.mensajeError.observe(viewLifecycleOwner, {
            Util.toastMensaje(context!!, it, false)
        })

        otViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            when (it) {
                "1" -> {
                    goCamera()
                    return@observe
                }
                "2" -> {
                    goGalery()
                    return@observe
                }
                else -> {
                    startActivity(
                        Intent(context, PreviewCameraActivity::class.java)
                            .putExtra("nameImg", it)
                            .putExtra("usuarioId", usuarioId)
                            .putExtra("id", otId)
                            .putExtra("galery", true)
                    )
                }
            }
        })

        fabSearch.setOnClickListener(this)
        fabCamara.setOnClickListener(this)
        fabGaleria.setOnClickListener(this)
//        fabSave.setOnClickListener(this)
    }

    private fun microPhone(titulo: String, permission: Int) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, titulo)

        try {
            startActivityForResult(intent, permission)
        } catch (a: ActivityNotFoundException) {
            Util.toastMensaje(context!!, "Dispositivo no compatible para esta opción", false)
        }
    }

    private fun confirmDelete(o: ParteDiarioPhoto) {
        val dialog = MaterialAlertDialogBuilder(context!!)
            .setTitle("Mensaje")
            .setMessage("Deseas eliminar esta foto ?")
            .setPositiveButton("SI") { dialog, _ ->
                otViewModel.deletePhoto(o, context!!)
                dialog.dismiss()
            }
            .setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
            }
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == 1) {
                val result: ArrayList<String>? =
                    data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val y = result?.get(0)!!
                editTextIncidencia.setText(y)
            } else {
                if (data != null) {
                    val gps = Gps(context!!)
                    if (gps.isLocationEnabled()) {
                        try {
                            otViewModel.setError("Cargando imagenes seleccionadas...")
                            val addressObservable = Observable.just(
                                Geocoder(context)
                                    .getFromLocation(
                                        gps.getLatitude(), gps.getLongitude(), 1
                                    )[0]
                            )
                            addressObservable.subscribeOn(Schedulers.io())
                                .delay(1000, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : io.reactivex.Observer<Address> {
                                    override fun onSubscribe(d: Disposable) {}
                                    override fun onNext(address: Address) {
                                        otViewModel.generarArchivo(
                                            (maxSize - size),
                                            usuarioId,
                                            context!!,
                                            data,
                                            address.getAddressLine(0).toString(),
                                            address.locality.toString()
                                        )
                                    }

                                    override fun onError(e: Throwable) {}
                                    override fun onComplete() {}
                                })
                        } catch (e: IOException) {
                            otViewModel.setError(e.toString())
                        }
                    } else {
                        gps.showSettingsAlert(context!!)
                    }
                }
            }
        }
    }

    private fun goGalery() {
        otViewModel.setError("Maximo " + (maxSize - size) + " fotos para seleccionar")
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.type = "image/*"
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(i, Permission.GALERY_REQUEST)
    }

    private fun goCamera() {
        startActivity(
            Intent(context, CameraActivity::class.java)
                .putExtra("usuarioId", usuarioId)
                .putExtra("id", otId)
        )
    }

    private fun formRegistro(tipo: String) {
        t.incidencia = editTextIncidencia.text.toString()
        otViewModel.validateRegistro(t, tipo)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            IncidenciaFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}