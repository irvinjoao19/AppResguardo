package com.dsige.dominion.appresguardo.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.dsige.dominion.appresguardo.R
import com.dsige.dominion.appresguardo.data.local.model.ParteDiario
import com.dsige.dominion.appresguardo.data.viewModel.OtViewModel
import com.dsige.dominion.appresguardo.data.viewModel.ViewModelFactory
import com.dsige.dominion.appresguardo.helper.Util
import com.dsige.dominion.appresguardo.ui.activities.FirmActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_firm.*
import java.io.File
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FirmFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        if (t.nroObraTD.isNotEmpty()) {
            startActivity(
                Intent(context, FirmActivity::class.java)
                    .putExtra("otId", otId)
                    .putExtra("tipo", tipo)
            )
        } else
            otViewModel.setError("Completar primer formulario")

    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var otViewModel: OtViewModel
    lateinit var t: ParteDiario
    private var otId: Int = 0
    private var tipo: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        t = ParteDiario()
        arguments?.let {
            otId = it.getInt(ARG_PARAM1)
            tipo = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_firm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        otViewModel =
            ViewModelProvider(this, viewModelFactory).get(OtViewModel::class.java)
        otViewModel.getOtById(otId).observe(viewLifecycleOwner, {
            if (it != null) {
                t = it
                if (it.estado == 0) {
                    fabFirma.visibility = View.GONE
                }
                when (tipo) {
                    1 -> if (it.firmaEfectivoPolicial.isNotEmpty()) {
                        getFirma(it.firmaEfectivoPolicial)
                        return@observe
                    }
                    2 -> if (it.firmaJefeCuadrilla.isNotEmpty()) {
                        getFirma(it.firmaJefeCuadrilla)
                        return@observe
                    }
                }
            }
        })

        fabFirma.setOnClickListener(this)

        otViewModel.mensajeError.observe(viewLifecycleOwner, { m ->
            Util.snackBarMensaje(view!!, m)
        })
    }

    private fun getFirma(s: String) {
        progressBar.visibility = View.VISIBLE
        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                val f = File(Util.getFolder(context!!), s)
                Picasso.get().load(f)
                    .into(imgFirma, object : Callback {
                        override fun onSuccess() {
                            progressBar.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                            Log.i("TAG", e.toString())
                        }
                    })
            }, 1000)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int) =
            FirmFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}