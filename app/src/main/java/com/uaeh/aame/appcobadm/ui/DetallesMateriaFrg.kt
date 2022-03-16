package com.uaeh.aame.appcobadm.ui

import adapter.AvisosAdapter
import adapter.Escala
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
import org.json.JSONException
import org.json.JSONObject

class DetallesMateriaFrg : Fragment() {

    //Tag value
    private val TAG = DetallesMateriaFrg::class.simpleName
    private val appConfig = AppConfig()

    // Text View Botones
    private lateinit var btnAviso: Button
    private lateinit var btnEscala: Button
    private lateinit var btnBack: ImageButton
    private lateinit var tvParcial1: TextView
    private lateinit var tvParcial2: TextView
    private lateinit var tvParcial3: TextView

    // Avisos
    private lateinit var Titulo: ArrayList<String>
    private lateinit var Description: ArrayList<String>
    private lateinit var Fecha: ArrayList<String>
    private lateinit var Avisos: ListView
    private lateinit var avisosAdapter: AvisosAdapter

    // arreglo 1
    private lateinit var Aspecto1: ArrayList<String>
    private lateinit var Porcentaje1: ArrayList<String>
    private lateinit var parcial1: ListView
    private lateinit var adapter1: Escala

    // arreglo 2
    private lateinit var Aspecto2: ArrayList<String>
    private lateinit var Porcentaje2: ArrayList<String>
    private lateinit var parcial2: ListView
    private lateinit var adapter2: Escala

    // arreglo 3
    private lateinit var Aspecto3: ArrayList<String>
    private lateinit var Porcentaje3: ArrayList<String>
    private lateinit var parcial3: ListView
    private lateinit var adapter3: Escala

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = layoutInflater.inflate(R.layout.fragment_detalles_materia_frg, container, false)

        // Inicializar botones
        btnAviso = root.findViewById(R.id.btAvisos)
        btnEscala = root.findViewById(R.id.btEscala)
        btnBack = root.findViewById(R.id.btn_back)
        tvParcial1 = root.findViewById(R.id.tv1)
        tvParcial2 = root.findViewById(R.id.tv2)
        tvParcial3 = root.findViewById(R.id.tv3)

        // Array List 1
        Aspecto1 = ArrayList()
        Porcentaje1 = ArrayList()

        // Array List 2
        Aspecto2 = ArrayList()
        Porcentaje2 = ArrayList()

        // Array 3
        Aspecto3 = ArrayList()
        Porcentaje3 = ArrayList()

        // Array Aviso
        Titulo = ArrayList()
        Description = ArrayList()
        Fecha = ArrayList()

        // Inicializar List Views
        parcial1 = root.findViewById(R.id.lista_parcial_1)
        adapter1 = Escala(activity as Activity, Aspecto1, Porcentaje1)
        parcial1.adapter = adapter1

        parcial2 = root.findViewById(R.id.lista_parcial_2)
        adapter2 = Escala(activity as Activity, Aspecto2, Porcentaje2)
        parcial2.adapter = adapter2

        parcial3 = root.findViewById(R.id.lista_parcial_3)
        adapter3 = Escala(activity as Activity, Aspecto3, Porcentaje3)
        parcial3.adapter = adapter3

        Avisos = root.findViewById(R.id.lista_avisos)
        avisosAdapter = AvisosAdapter(activity as Activity, Titulo, Description, Fecha)

        parcial1.visibility = View.INVISIBLE
        parcial2.visibility = View.INVISIBLE
        parcial3.visibility = View.INVISIBLE
        tvParcial1.visibility = View.INVISIBLE
        tvParcial2.visibility = View.INVISIBLE
        tvParcial3.visibility = View.INVISIBLE
        Avisos.visibility = View.INVISIBLE
        btnBack.visibility = View.INVISIBLE

        val idprof = arguments?.getString("maestro")

        btnAviso.setOnClickListener{
            btnAviso.visibility = View.INVISIBLE
            btnEscala.visibility = View.INVISIBLE
            btnBack.visibility = View.VISIBLE
            getAvisoMateria(idprof.toString())

        }

        btnEscala.setOnClickListener{
            btnAviso.visibility = View.INVISIBLE
            btnEscala.visibility = View.INVISIBLE
            btnBack.visibility = View.VISIBLE
            parcial1.visibility = View.VISIBLE
            parcial2.visibility = View.VISIBLE
            parcial3.visibility = View.VISIBLE
            tvParcial1.visibility = View.VISIBLE
            tvParcial2.visibility = View.VISIBLE
            tvParcial3.visibility = View.VISIBLE
            getEscala(idprof.toString())
        }

        btnBack.setOnClickListener{
            btnAviso.visibility = View.VISIBLE
            btnEscala.visibility = View.VISIBLE
            btnBack.visibility = View.INVISIBLE
            Avisos.visibility = View.INVISIBLE
            parcial1.visibility = View.INVISIBLE
            parcial2.visibility = View.INVISIBLE
            parcial3.visibility = View.INVISIBLE
            tvParcial1.visibility = View.INVISIBLE
            tvParcial2.visibility = View.INVISIBLE
            tvParcial3.visibility = View.INVISIBLE

            // Array List 1
            Aspecto1.clear()
            Porcentaje1.clear()

            // Array List 2
            Aspecto2.clear()
            Porcentaje2.clear()

            // Array 3
            Aspecto3.clear()
            Porcentaje3.clear()

        }
        return root
    }

    private fun getAvisoMateria(maestro: String){
        val tag_string_req = "req_get_materia_avisos"
        val strReq: StringRequest = object : StringRequest(Method.POST, appConfig.URL_GET_AVISOS, Response.Listener { response ->
            Log.d(TAG, "Materias Avisos Response: $response")
            try {
                val jObj = JSONObject(response.substring(response.indexOf("{"), response.length))
                val error = jObj.getBoolean("error")
                Log.d(TAG, "Error response: $error")
                if (!error){
                    if (jObj.length() == 1 ) {
                        Titulo.clear()
                        Description.clear()
                        Fecha.clear()
                        avisosAdapter.notifyDataSetChanged()
                        Toast.makeText(
                            requireActivity().applicationContext,
                            "No hay avisos por parte del maestro",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val tam = jObj.length() - 1
                        for (indice in 0 until tam) {
                            val aviso = jObj.getJSONObject(indice.toString())
                            Titulo.add(aviso.getString("titulo"))
                            Description.add(aviso.getString("descripcion"))
                            Fecha.add(aviso.getString("fecha"))
                        }
                        Avisos.adapter = avisosAdapter
                        Avisos.visibility = View.VISIBLE
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
            Log.d(TAG, "Materias Get Avisos Error: ${error.message}")
            Toast.makeText(requireActivity().applicationContext, "", Toast.LENGTH_SHORT).show()
        }) {
            override fun getParams(): MutableMap<String, String> {
                val param = HashMap<String, String>()
                param["id_maes"] = maestro
                return param
            }
        }
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req)
    }

    private fun getEscala(maestro: String){
        val tag_string_req = "req_get_aspectos"
        val strReq: StringRequest = object : StringRequest(Method.POST, appConfig.URL_OBTENER_ESCALA, Response.Listener { response ->
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
                        val id = aspecto.getString("id_aspecto")
                        val asp = aspecto.getString("aspecto")
                        val por = aspecto.getString("porcentaje")
                        val parc = aspecto.getString("parcial")
                        addItemLista(asp, por, parc)
                    }
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
                return params
            }
        }
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req)
    }

    private fun addItemLista(asp:String, por:String, parcial:String){
        if (parcial == "1"){
            Aspecto1.add(asp)
            Porcentaje1.add(por)
            parcial1.adapter = adapter1
        } else if (parcial == "2"){
            Aspecto2.add(asp)
            Porcentaje2.add(por)
            parcial2.adapter = adapter2
        } else if (parcial == "3"){
            Aspecto3.add(asp)
            Porcentaje3.add(por)
            parcial3.adapter = adapter3
        }
    }
}