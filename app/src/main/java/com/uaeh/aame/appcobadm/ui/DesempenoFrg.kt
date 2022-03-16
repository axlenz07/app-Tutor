package com.uaeh.aame.appcobadm.ui

import adapter.Asistencia
import adapter.Calficacion
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import app.AppConfig
import app.AppController
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.uaeh.aame.appcobadm.R
import helper.SessionManager
import kotlinx.android.synthetic.main.fragment_desempeno.*
import org.json.JSONException
import org.json.JSONObject


class DesempenoFrg : Fragment() {

    // Tag Value
    private val TAG = DesempenoFrg::class.simpleName
    private val appConfig = AppConfig()
    private lateinit var session: SessionManager

    // Text View Botones
    private lateinit var btnAsis: Button
    private lateinit var btnCalif: Button
    private lateinit var btnBack: ImageButton
    private lateinit var tvCalif1: TextView
    private lateinit var tvCalif2: TextView
    private lateinit var tvCalif3: TextView

    // Asistencia
    private val Fecha = ArrayList<String>()
    private val Asist = ArrayList<Int>()
    private lateinit var Asistencias: ListView
    private lateinit var asisAdapter: Asistencia

    // Calificaciones 1
    private val Aspecto1 = ArrayList<String>()
    private val Porcentaje1 = ArrayList<String>()
    private val Calif1 = ArrayList<String>()
    private lateinit var parcial1: ListView
    private lateinit var adapter1: Calficacion

    // Calificaciones 2
    private val Aspecto2 = ArrayList<String>()
    private val Porcentaje2 = ArrayList<String>()
    private val Calif2 = ArrayList<String>()
    private lateinit var parcial2: ListView
    private lateinit var adapter2: Calficacion

    // Calificaciones 3
    private val Aspecto3 = ArrayList<String>()
    private val Porcentaje3 = ArrayList<String>()
    private val Calif3 = ArrayList<String>()
    private lateinit var parcial3: ListView
    private lateinit var adapter3: Calficacion

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = layoutInflater.inflate(R.layout.fragment_desempeno, container, false)
        session = SessionManager(requireActivity().applicationContext)
        tvCalif1 = root.findViewById(R.id.tvP1)
        tvCalif2 = root.findViewById(R.id.tvP2)
        tvCalif3 = root.findViewById(R.id.tvP3)

        // Calificaciones
        parcial1 = root.findViewById(R.id.lista_calif_1)
        adapter1 = Calficacion(activity as Activity, Aspecto1, Porcentaje1, Calif1)
        parcial1.adapter = adapter1

        parcial2 = root.findViewById(R.id.lista_calif_2)
        adapter2 = Calficacion(activity as Activity, Aspecto2, Porcentaje2, Calif2)
        parcial2.adapter = adapter2

        parcial3 = root.findViewById(R.id.lista_calif_3)
        adapter3 = Calficacion(activity as Activity, Aspecto3, Porcentaje3, Calif3)
        parcial3.adapter = adapter3

        parcial1.visibility = View.INVISIBLE
        parcial2.visibility = View.INVISIBLE
        parcial3.visibility = View.INVISIBLE
        tvCalif1.visibility = View.INVISIBLE
        tvCalif2.visibility = View.INVISIBLE
        tvCalif3.visibility = View.INVISIBLE

        // Asistencias
        Asistencias = root.findViewById(R.id.lista_asistencia)
        asisAdapter = Asistencia(activity as Activity, Fecha, Asist)
        Asistencias.adapter = asisAdapter

        Asistencias.visibility = View.INVISIBLE

        btnAsis = root.findViewById(R.id.btn_sh_asis)
        btnBack = root.findViewById(R.id.btn_back_des)
        btnCalif = root.findViewById(R.id.btn_sh_calif)

        btnBack.visibility = View.INVISIBLE

        btnAsis.setOnClickListener{
            val id_maestro = arguments?.getString("maestro")
            val id_alumno = session.getAlumno()
            getAsistencias(id_alumno, id_maestro.toString())
            btnBack.visibility = View.VISIBLE
            btnAsis.visibility = View.INVISIBLE
            btnCalif.visibility = View.INVISIBLE
        }

        btnBack.setOnClickListener{
            Asistencias.visibility = View.INVISIBLE
            btnAsis.visibility = View.VISIBLE
            btnCalif.visibility = View.VISIBLE
            btnBack.visibility = View.INVISIBLE

            // Array calificaciones
            Aspecto1.clear()
            Calif1.clear()
            Porcentaje1.clear()
            adapter1.notifyDataSetChanged()

            Aspecto2.clear()
            Calif2.clear()
            Porcentaje2.clear()
            adapter2.notifyDataSetChanged()

            Aspecto3.clear()
            Calif3.clear()
            Porcentaje3.clear()
            adapter3.notifyDataSetChanged()

            tvP1.visibility = View.INVISIBLE
            parcial1.visibility = View.INVISIBLE
            tvP2.visibility = View.INVISIBLE
            parcial2.visibility = View.INVISIBLE
            tvP3.visibility = View.INVISIBLE
            parcial3.visibility = View.INVISIBLE

            // Array Asistencias
            Fecha.clear()
            Asist.clear()
            asisAdapter.notifyDataSetChanged()
        }

        btnCalif.setOnClickListener{
            val id_maest = arguments?.getString("maestro")
            val id_alumno = session.getAlumno()
            getCalif(id_maest.toString(), id_alumno)
            btnBack.visibility = View.VISIBLE
            btnAsis.visibility = View.INVISIBLE
            btnCalif.visibility = View.INVISIBLE
        }

        return root
    }

    private fun getAsistencias(id_alumno: String, maestro: String) {
        val tag_string_req = "req_get_materia_asistencias"
        val strReq: StringRequest = object : StringRequest(Method.POST, appConfig.URL_GET_ASISTENCIAS, Response.Listener { response ->
            Log.d(TAG, "Materias Asistencias Response: $response")
            try {
                val jObj = JSONObject(response.substring(response.indexOf("{"), response.length))
                val error = jObj.getBoolean("error")
                Log.d(TAG, "Error response: $error")
                if (!error){
                    if (jObj.length() == 1 ) {
                        Fecha.clear()
                        Asist.clear()
                        asisAdapter.notifyDataSetChanged()
                        Toast.makeText(
                            requireActivity().applicationContext,
                            "No hay fechas registradas por parte del maestro",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val tam = jObj.length() - 1
                        for (indice in 0 until tam) {
                            val aviso = jObj.getJSONObject(indice.toString())
                            Fecha.add(aviso.getString("fecha"))
                            Asist.add(aviso.getInt("asistencia"))
                        }
                        asisAdapter.notifyDataSetChanged()
                        Asistencias.visibility = View.VISIBLE
                    }
                } else {
                    val errorMsg = jObj.getString("error_msg")
                    Toast.makeText(requireActivity().applicationContext, errorMsg, Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException){
                e.printStackTrace()
                Toast.makeText(requireActivity().applicationContext, "Json error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error ->
            Log.d(TAG, "Materias Get Asistencias Error: ${error.message}")
            Toast.makeText(requireActivity().applicationContext, "", Toast.LENGTH_SHORT).show()
        }) {
            override fun getParams(): MutableMap<String, String> {
                val param = HashMap<String, String>()
                param["id_alumno"] = id_alumno
                param["id_maes"] = maestro
                return param
            }
        }
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req)
    }

    private fun getCalif(maestro: String, alumno: String){
        val tag_string_req = "req_get_aspectos"
        val strReq: StringRequest = object : StringRequest(Method.POST, appConfig.URL_GET_CALIF, Response.Listener { response ->
            Log.d(TAG, "Escala Response: $response")
            try {
                val jObj = JSONObject(response.substring(response.indexOf("{"), response.length))
                val error = jObj.getBoolean("error")
                Log.d(TAG, "Error response: $error")

                // Checar si es de a debis
                if (!error) {
                    val tam = jObj.length()-1
                    for (indice in 0 until tam){
                        val aspecto = jObj.getJSONObject(indice.toString())
                        val asp = aspecto.getString("aspecto")
                        val por = aspecto.getString("porcentaje")
                        val calif = aspecto.getString("calificacion")
                        val parc = aspecto.getString("parcial")
                        addItemLista(asp, por, calif, parc)
                    }
                    tvP1.visibility = View.VISIBLE
                    parcial1.visibility = View.VISIBLE
                    tvP2.visibility = View.VISIBLE
                    parcial2.visibility = View.VISIBLE
                    tvP3.visibility = View.VISIBLE
                    parcial3.visibility = View.VISIBLE
                } else {
                    val errorMsg = jObj.getString("error_msg")
                    Toast.makeText(requireActivity().applicationContext, errorMsg, Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: JSONException){
                e.printStackTrace()
                Toast.makeText(requireActivity().applicationContext, "Json error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error ->
            Log.d(TAG, "Escala anadir aspecto error: ${error.message}")
            Toast.makeText(requireActivity().applicationContext, error.message, Toast.LENGTH_SHORT).show()
        }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["maestro"] = maestro
                params["alumno"] = alumno
                return params
            }
        }
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req)
    }

    private fun addItemLista(asp:String, por:String, calif:String , parcial:String){
        if (parcial == "1"){
            Aspecto1.add(asp)
            Porcentaje1.add(por)
            Calif1.add(calif)
            parcial1.adapter = adapter1
        } else if (parcial == "2"){
            Aspecto2.add(asp)
            Porcentaje2.add(por)
            Calif2.add(calif)
            parcial2.adapter = adapter2
        } else if (parcial == "3"){
            Aspecto3.add(asp)
            Porcentaje3.add(por)
            Calif3.add(calif)
            parcial3.adapter = adapter3
        }
    }
}