package com.dsige.dominion.appresguardo.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.dsige.dominion.appresguardo.R
import com.dsige.dominion.appresguardo.data.local.model.ParteDiarioPhoto
import com.dsige.dominion.appresguardo.data.viewModel.OtViewModel
import com.dsige.dominion.appresguardo.data.viewModel.ViewModelFactory
import com.dsige.dominion.appresguardo.helper.Util
import com.dsige.dominion.appresguardo.ui.adapters.OtMultiPhotoAdapter
import com.dsige.dominion.appresguardo.ui.listeners.OnItemClickListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_preview_camera.*
import java.io.File
import javax.inject.Inject

class PreviewCameraActivity : DaggerAppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabOk -> if (galery) {
                formMultiPhoto()
            } else
                formPhoto()
            R.id.imgClose -> if (galery) {
                finish()
            } else {
                startActivity(
                    Intent(this, CameraActivity::class.java)
                        .putExtra("usuarioId", usuarioId)
                        .putExtra("id", id)
                )
                finish()
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var otViewModel: OtViewModel

    private var nameImg: String = ""
    private var usuarioId: Int = 0
    private var id: Int = 0
    private var galery: Boolean = false

    lateinit var lista: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_camera)
        val b = intent.extras
        if (b != null) {
            usuarioId = b.getInt("usuarioId")
            galery = b.getBoolean("galery")
            id = b.getInt("id")
            if (galery) {
                lista = Gson().fromJson(
                    b.getString("nameImg")!!,
                    object : TypeToken<List<String?>?>() {}.type
                )
            } else {
                nameImg = b.getString("nameImg")!!
            }
            bindUI()
        }
    }

    private fun bindUI() {
        otViewModel =
            ViewModelProvider(this, viewModelFactory).get(OtViewModel::class.java)

        fabOk.setOnClickListener(this)
        imgClose.setOnClickListener(this)
        if (galery) {
            Looper.myLooper()?.let {
                Handler(it).postDelayed({
                    val file = File(Util.getFolder(this), lista[0])
                    Picasso.get().load(file)
                        .into(imageView, object : Callback {
                            override fun onSuccess() {
                                progressBar.visibility = View.GONE
                            }

                            override fun onError(e: Exception?) {
                            }
                        })

                    val otPhotoAdapter =
                        OtMultiPhotoAdapter(object : OnItemClickListener.OtMultiPhotoListener {
                            override fun onItemClick(s: String, view: View, position: Int) {
                                val f = File(Util.getFolder(this@PreviewCameraActivity), s)
                                Picasso.get().load(f)
                                    .into(imageView, object : Callback {
                                        override fun onSuccess() {
                                            progressBar.visibility = View.GONE
                                        }

                                        override fun onError(e: Exception?) {
                                        }
                                    })
                            }
                        })
                    recyclerView.itemAnimator = DefaultItemAnimator()
                    recyclerView.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    recyclerView.setHasFixedSize(true)
                    recyclerView.adapter = otPhotoAdapter
                    otPhotoAdapter.addItems(lista)
                    recyclerView.visibility = View.VISIBLE
                }, 1000)
            }
        } else {
            Looper.myLooper()?.let {
                Handler(it).postDelayed({
                    val f = File(Util.getFolder(this), nameImg)
                    Picasso.get().load(f)
                        .into(imageView, object : Callback {
                            override fun onSuccess() {
                                progressBar.visibility = View.GONE
                            }

                            override fun onError(e: Exception?) {
                            }
                        })
                }, 200)
            }
        }

        otViewModel.mensajeError.observe(this, {
            Util.toastMensaje(this, it,false)
        })

        otViewModel.mensajeSuccess.observe(this, {
            finish()
        })
    }

    private fun formPhoto() {
        val f = ParteDiarioPhoto()
        f.parteDiarioId = id
        f.fotoUrl = nameImg
        f.estado = 1
        f.active = 1
        otViewModel.insertPhoto(f)
    }

    private fun formMultiPhoto() {
        val f: ArrayList<ParteDiarioPhoto> = ArrayList()
        if (lista.size > 0)
            for (d: String in lista) {
                val p = ParteDiarioPhoto()
                p.parteDiarioId = id
                p.fotoUrl = d
                p.estado = 1
                p.active = 1
                f.add(p)
            }
        otViewModel.insertMultiPhoto(f)
    }
}