package com.uaeh.aame.appcobadm.ui

import adapter.Materias
import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import app.AppConfig
import app.AppController
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.uaeh.aame.appcobadm.R
import helper.SessionManager
import org.json.JSONException
import org.json.JSONObject

class MateriasDesFrg : Fragment() {

    // session appconfig tag
    private lateinit var session: SessionManager
    private val TAG = MateriasDesFrg::class.simpleName
    private val appConfig = AppConfig()

    // list view arreglos materia
    private var IDMaestro: ArrayList<String> = ArrayList()
    private var Materia: ArrayList<String> = ArrayList()
    private lateinit var listView: ListView
    private lateinit var adaptador: Materias


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = layoutInflater.inflate(R.layout.fragment_materias_des, container, false)

        session = SessionManager(requireActivity().applicationContext)
        listView = root.findViewById(R.id.list_DesMat)

        adaptador = Materias(activity as Activity, IDMaestro, Materia)
        listView.adapter = adaptador

        listView.setOnItemClickListener{
                adapterView, view, position, id ->
            val idi = adapterView.getItemAtPosition(position)
            val bundle = bundleOf("maestro" to idi)
            findNavController().navigate(R.id.action_materiaDes_to_desempeno, bundle)
            //getAvisoMateria(idi.toString())
            IDMaestro.clear()
            Materia.clear()
            adaptador.notifyDataSetChanged()
        }

        getMaterias()

        return root
    }

    private fun getMaterias(){
        val tag_string_req = "req_get_materia"
        val strReq: StringRequest = object : StringRequest(Method.POST, appConfig.URL_GET_MATERIAS, Response.Listener { response ->
            Log.d(TAG, "Materias Respones: $response")
            try {
                val jObj = JSONObject(response.substring(response.indexOf("{"), response.length))
                val error = jObj.getBoolean("error")
                Log.d(TAG, "Error response: $error")
                if (!error){
                    val tam = jObj.length()-1
                    for (indice in 0 until tam) {
                        val materia = jObj.getJSONObject(indice.toString())
                        IDMaestro.add(materia.getString("maestro"))
                        Materia.add(materia.getString("materia"))
                    }
                    listView.adapter = adaptador

                } else {
                    val errorMsg = jObj.getString("error_msg")
                    Toast.makeText(requireActivity().applicationContext, errorMsg, Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException){
                e.printStackTrace()
                Toast.makeText(requireActivity().applicationContext, "Json error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error ->
            Log.d(TAG, "Materias Get Detalles Error: ${error.message}")
            Toast.makeText(requireActivity().applicationContext, "", Toast.LENGTH_SHORT).show()
        }) {
        }
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req)
    }
}