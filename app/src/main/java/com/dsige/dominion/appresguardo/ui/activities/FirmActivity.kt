package com.dsige.dominion.appresguardo.ui.activities

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.lifecycle.ViewModelProvider
import com.dsige.dominion.appresguardo.R
import com.dsige.dominion.appresguardo.data.local.model.ParteDiario
import com.dsige.dominion.appresguardo.data.viewModel.OtViewModel
import com.dsige.dominion.appresguardo.data.viewModel.ViewModelFactory
import com.dsige.dominion.appresguardo.helper.Util
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_firm.*
import javax.inject.Inject


class FirmActivity : DaggerAppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fabFirma -> {
                if (paintView.validDraw()) {
                    val name = paintView.save(this@FirmActivity, otId)
                    if (tipo == 1) {
                        p.firmaEfectivoPolicial = name
                    } else {
                        p.estado = 1
                        p.firmaJefeCuadrilla = name
                    }
                    otViewModel.validateOt(p)
                } else {
                    Util.toastMensaje(this, "Debes de Firmar",true)
                }
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var otViewModel: OtViewModel

    lateinit var p: ParteDiario
    private var otId: Int = 0
    private var tipo: Int = 0

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.eraser -> {
                paintView.clear()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firm)
        p = ParteDiario()
        val b = intent.extras
        if (b != null) {
            bindUI(b.getInt("otId"), b.getInt("tipo"))
        }
    }

    private fun bindUI(id: Int, t: Int) {
        otId = id
        tipo = t
        otViewModel =
            ViewModelProvider(this, viewModelFactory).get(OtViewModel::class.java)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Firma"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }

//         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
//            val insets: Insets = windowMetrics.windowInsets
//                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
//            windowMetrics.bounds.width() - insets.left - insets.right
//             paintView.init(windowMetrics!!)
//        } else {}

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        paintView.init(displayMetrics)
        fabFirma.setOnClickListener(this)

        otViewModel.getOtById(otId).observe(this, {
            p = it
        })

        otViewModel.mensajeError.observe(this, { m ->
            Util.toastMensaje(this, m,false)
        })

        otViewModel.mensajeSuccess.observe(this, { m ->
            Util.toastMensaje(this, m,false)
            finish()
        })
    }
}