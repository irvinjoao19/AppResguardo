package com.dsige.dominion.appresguardo.ui.fragments

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.dsige.dominion.appresguardo.R
import com.dsige.dominion.appresguardo.data.local.model.Area
import com.dsige.dominion.appresguardo.data.local.model.ParteDiario
import com.dsige.dominion.appresguardo.data.local.model.Personal
import com.dsige.dominion.appresguardo.data.local.model.TipoDocumento
import com.dsige.dominion.appresguardo.data.viewModel.OtViewModel
import com.dsige.dominion.appresguardo.data.viewModel.ViewModelFactory
import com.dsige.dominion.appresguardo.helper.Gps
import com.dsige.dominion.appresguardo.helper.Util
import com.dsige.dominion.appresguardo.ui.adapters.AreaAdapter
import com.dsige.dominion.appresguardo.ui.adapters.PersonalAdapter
import com.dsige.dominion.appresguardo.ui.adapters.TipoDocumentoAdapter
import com.dsige.dominion.appresguardo.ui.listeners.OnItemClickListener
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_general.*
import java.util.*
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class GeneralFragment : DaggerFragment(), View.OnClickListener {

    override fun onClick(v: View) {
        when (v.id) {
            R.id.editTextFecha -> Util.getDateDialog(context!!, editTextFecha)
            R.id.editTextHInicio -> Util.getHourDialog(context!!, editTextHInicio)
            R.id.editTextHTermino -> Util.getHourDialog(context!!, editTextHTermino)
            R.id.editTextArea -> spinnerDialog(1, "Area")
            R.id.editTextCoordinador -> spinnerDialog(5, "Coordinador")
            R.id.editTextCuadrilla -> spinnerDialog(6, "Jefe de Cuadrilla")
            R.id.imgCoordinador -> dialogPersonal(5, otId, "Coordinador")
            R.id.imgCuadrilla -> dialogPersonal(6, otId, "Jefe de Cuadrilla")
            R.id.imgLugar -> getAddress()
            R.id.imgObs -> microPhone("Observaciones", 2)
            R.id.fabGenerate -> formOt()
        }
    }

    private fun getAddress() {
        val gps = Gps(context!!)
        if (gps.isLocationEnabled()) {
            progressBarLugar.visibility = View.VISIBLE
            Util.getLocationName(
                context!!, editTextLugar, gps.getLatitude(), gps.getLongitude(), progressBarLugar
            )
        } else {
            gps.showSettingsAlert(context!!)
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var otViewModel: OtViewModel
    lateinit var areaAdapter: AreaAdapter
    lateinit var personalAdapter: PersonalAdapter
    private var viewPager: ViewPager? = null
    lateinit var t: ParteDiario

    private var otId: Int = 0
    private var usuarioId: Int = 0
    private var empresaId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        t = ParteDiario()

        arguments?.let {
            otId = it.getInt(ARG_PARAM1)
            usuarioId = it.getInt(ARG_PARAM2)
            empresaId = it.getInt(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_general, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI() {
        viewPager = activity!!.findViewById(R.id.viewPager)
        otViewModel =
            ViewModelProvider(this, viewModelFactory).get(OtViewModel::class.java)

        editTextFecha.setText(Util.getFecha())
        editTextHInicio.setText(Util.getHoraActual())
        editTextHTermino.setText(Util.getHoraActual())

        t.parteDiarioId = otId

        otViewModel.getOtById(otId).observe(viewLifecycleOwner, {
            if (it != null) {
                t = it
                editTextArea.setText(it.nombreServicio)
                editTextFecha.setText(it.fechaAsignacion)
                editTextHInicio.setText(it.horaInicio)
                editTextHTermino.setText(it.horaTermino)
                editTextHTotal.setText(it.totalHoras)
                editTextCoordinador.setText(it.nombreCoordinador)
                editTextCuadrilla.setText(it.nombreJefeCuadrilla)
                editTextLugar.setText(it.lugarTrabajoPD)
                editTextNro.setText(it.nroObraTD)
                editTextObs.setText(it.observacionesPD)
                if (it.estadoId == 6) {
                    fabGenerate.visibility = View.GONE
                }
            } else {
                otViewModel.getFirstArea().observe(viewLifecycleOwner, { a ->
                    if (a != null) {
                        t.servicioId = a.areaId
                        editTextArea.setText(a.nombreArea)
                    }
                })
            }
        })

        otViewModel.getPersonalOtById(otId).observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                for (p: Personal in it) {
                    if (p.cargoId == 5) {
                        imgCoordinador.visibility = View.GONE
                        editTextCoordinador.setText(String.format("%s %s", p.nombre, p.apellidos))
                    } else {
                        imgCuadrilla.visibility = View.GONE
                        editTextCuadrilla.setText(String.format("%s %s", p.nombre, p.apellidos))
                    }
                }
            }
        })

        editTextFecha.setOnClickListener(this)
        editTextHInicio.setOnClickListener(this)
        editTextHTermino.setOnClickListener(this)

        editTextArea.setOnClickListener(this)
        editTextCoordinador.setOnClickListener(this)
        editTextCuadrilla.setOnClickListener(this)
        editTextArea.setOnClickListener(this)
        imgCoordinador.setOnClickListener(this)
        imgCuadrilla.setOnClickListener(this)
        imgLugar.setOnClickListener(this)
        imgObs.setOnClickListener(this)
        fabGenerate.setOnClickListener(this)

        otViewModel.mensajeError.observe(viewLifecycleOwner, {
            //closeLoad()
            Util.toastMensaje(context!!, it)
        })

        otViewModel.mensajeSuccess.observe(viewLifecycleOwner, {
            viewPager?.currentItem = 1
            Util.toastMensaje(context!!, it)
        })
    }

    private fun spinnerDialog(tipo: Int, title: String) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_combo, null)

        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val layoutSearch: TextInputLayout = v.findViewById(R.id.layoutSearch)
        val editTextSearch: TextInputEditText = v.findViewById(R.id.editTextSearch)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)

        progressBar.visibility = View.GONE
        layoutSearch.visibility = View.VISIBLE

        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
        textViewTitulo.text = title

        when (tipo) {
            1 -> {
                areaAdapter = AreaAdapter(object : OnItemClickListener.AreaListener {
                    override fun onItemClick(a: Area, view: View, position: Int) {
                        t.servicioId = a.areaId
                        editTextArea.setText(a.nombreArea)
                        dialog.dismiss()
                    }
                })
                recyclerView.adapter = areaAdapter
                otViewModel.getAreas().observe(this, {
                    areaAdapter.addItems(it)
                })
            }
            else -> {
                personalAdapter =
                    PersonalAdapter(object : OnItemClickListener.PersonalListener {
                        override fun onItemClick(p: Personal, view: View, position: Int) {
                            if (tipo == 5) {
                                t.personalCoordinarId = p.personalId
                                editTextCoordinador.setText(
                                    String.format("%s %s", p.nombre, p.apellidos)
                                )
                            } else {
                                t.personalJefeCuadrillaId = p.personalId
                                editTextCuadrilla.setText(
                                    String.format("%s %s", p.nombre, p.apellidos)
                                )
                            }
                            dialog.dismiss()
                        }
                    })
                recyclerView.adapter = personalAdapter
                otViewModel.getPersonalById(tipo).observe(this, {
                    personalAdapter.addItems(it)
                })
            }
        }

        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(c: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                when (tipo) {
                    1 -> areaAdapter.getFilter().filter(editTextSearch.text.toString())
                    else -> personalAdapter.getFilter().filter(editTextSearch.text.toString())
                }
            }
        })
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
            Util.toastMensaje(context!!, "Dispositivo no compatible para esta opción")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            val result: ArrayList<String>? =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val y = result?.get(0)!!

            if (requestCode == 1) {
                editTextLugar.setText(y)
            } else {
                editTextObs.setText(y)
            }
        }
    }

    private fun formOt() {
        val gps = Gps(context!!)
        if (gps.isLocationEnabled()) {
            if (gps.latitude.toString() != "0.0" || gps.longitude.toString() != "0.0") {
                t.usuarioId = usuarioId
                t.usuarioEfectivoPolicialId = usuarioId

                t.nombreServicio = editTextArea.text.toString()
                t.fechaAsignacion = editTextFecha.text.toString()
                t.horaInicio = editTextHInicio.text.toString()
                t.horaTermino = editTextHTermino.text.toString()

                t.nombreCoordinador = editTextCoordinador.text.toString()
                t.nombreJefeCuadrilla = editTextCuadrilla.text.toString()
                t.lugarTrabajoPD = editTextLugar.text.toString()
                t.nroObraTD = editTextNro.text.toString()
                t.observacionesPD = editTextObs.text.toString()

                t.latitud = gps.latitude.toString()
                t.longitud = gps.longitude.toString()
                t.fechaRegistro = Util.getFecha()

                t.empresaId = empresaId
                t.estadoId = 5
                t.estado = 2
                otViewModel.validateOt(t)
            }
        } else {
            gps.showSettingsAlert(context!!)
        }
    }

    private fun dialogPersonal(i: Int, id: Int, cargo: String) {
        otViewModel.setErrorP(null)
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_personal, null)
        val editTextTipoDoc: TextInputEditText = v.findViewById(R.id.editTextTipoDoc)
        val editTextDoc: TextInputEditText = v.findViewById(R.id.editTextDoc)
        val editTextApellido: TextInputEditText = v.findViewById(R.id.editTextApellido)
        val editTextNombre: TextInputEditText = v.findViewById(R.id.editTextNombre)
        val editTextCargo: TextInputEditText = v.findViewById(R.id.editTextCargo)
        val imageViewClose: ImageView = v.findViewById(R.id.imageViewClose)
        val fab: ExtendedFloatingActionButton = v.findViewById(R.id.fab)
        builder.setView(v)
        val dialog = builder.create()
        dialog.show()
        val p = Personal()
        editTextTipoDoc.setOnClickListener {
            spinnerTipoDoc(editTextTipoDoc, p)
        }
        editTextCargo.setText(cargo)
        fab.setOnClickListener {
            p.nroDocumento = editTextDoc.text.toString()
            p.apellidos = editTextApellido.text.toString()
            p.nombre = editTextNombre.text.toString()
            p.cargoId = i
            p.otId = id
            otViewModel.validatePersonal(p)
        }
        imageViewClose.setOnClickListener {
            dialog.dismiss()
        }
        otViewModel.mensajePersonal.observe(viewLifecycleOwner, {
            if (it != null) {
                if (it == "ok") {
                    dialog.dismiss()
                } else {
                    Util.toastMensaje(context!!, it)
                }
            }
        })
    }

    private fun spinnerTipoDoc(text: TextInputEditText, p: Personal) {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AppTheme))
        @SuppressLint("InflateParams") val v =
            LayoutInflater.from(context).inflate(R.layout.dialog_combo, null)

        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
        val textViewTitulo: TextView = v.findViewById(R.id.textViewTitulo)
        val recyclerView: RecyclerView = v.findViewById(R.id.recyclerView)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        progressBar.visibility = View.GONE

        builder.setView(v)
        val dialog = builder.create()
        dialog.show()

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL)
        )
        textViewTitulo.text = String.format("Tipo Documento")

        val tipoAdapter = TipoDocumentoAdapter(object : OnItemClickListener.TipoDocumentoListener {
            override fun onItemClick(t: TipoDocumento, view: View, position: Int) {
                p.tipoDocId = t.tipoId
                text.setText(t.descripcion)
                dialog.dismiss()
            }
        })
        recyclerView.adapter = tipoAdapter
        otViewModel.getTipoDocumento().observe(this, {
            tipoAdapter.addItems(it)
        })
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: Int, param2: Int, param3: Int) =
            GeneralFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                    putInt(ARG_PARAM3, param3)
                }
            }
    }
}