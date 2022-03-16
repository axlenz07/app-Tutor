package com.uaeh.aame.appcobadm.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import app.AppConfig
import app.AppController
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.uaeh.aame.appcobadm.R
import helper.SessionManager
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONException
import org.json.JSONObject

class HomeFragment : Fragment() {

    // Edit text y Button
    private lateinit var etNombre: EditText
    private lateinit var tvNombre: TextView
    private lateinit var btnAddAlum: Button
    private lateinit var btnEditAlum: Button
    private lateinit var btnUpdateAlum: Button

    private lateinit var session: SessionManager
    private val appConfig = AppConfig()
    private val TAG = HomeFragment::class.simpleName

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        // Find view by id
        etNombre = root.findViewById(R.id.nom_alumno)
        tvNombre = root.findViewById(R.id.nombre_alumno)
        btnAddAlum = root.findViewById(R.id.toAddAlumno)
        btnEditAlum = root.findViewById(R.id.toEditAlumno)
        btnUpdateAlum = root.findViewById(R.id.toUpdateAlumno)

        btnUpdateAlum.visibility = View.INVISIBLE
        btnEditAlum.visibility = View.INVISIBLE
        btnAddAlum.visibility = View.INVISIBLE
        etNombre.visibility = View.INVISIBLE
        tvNombre.visibility = View.INVISIBLE

        btnAddAlum.setOnClickListener{
            val nombre = etNombre.text.toString()
            val id_papa = session.getId()
            addAlumno(nombre, id_papa)
        }

        btnEditAlum.setOnClickListener{
            tvNombre.visibility = View.INVISIBLE
            etNombre.visibility = View.VISIBLE
            etNombre.setHint(R.string.hint_edit_alumno)
            btnEditAlum.visibility = View.INVISIBLE
            btnUpdateAlum.visibility = View.VISIBLE
            etNombre.setText("")
        }

        btnUpdateAlum.setOnClickListener{
            val nombre = etNombre.text.toString()
            val id_alum = session.getAlumno()
            editAlumno(id_alum, nombre)
        }

        session = SessionManager(requireActivity().applicationContext)

        getAlumno(session.getId())
        return root
    }

    private fun getAlumno(id_papa: String){
        val tag_string_req = "req_get_alumno"
        val strReq: StringRequest = object : StringRequest(Method.POST, appConfig.URL_GET_ALUMNO , Response.Listener { response ->
            Log.d(TAG, "Home Respones: $response")
            try {
                val jObj = JSONObject(response.substring(response.indexOf("{"), response.length))
                val error = jObj.getBoolean("error")
                Log.d(TAG, "Error response: $error")
                if (!error){
                    val id = jObj.getString("id_alumno")
                    val user = jObj.getJSONObject("alumno")
                    val nombre = user.getString("nombre")
                    tvNombre.text = nombre
                    btnEditAlum.visibility = View.VISIBLE
                    tvNombre.visibility = View.VISIBLE
                    session.saveAlumno(id)
                } else {
                    btnAddAlum.visibility = View.VISIBLE
                    etNombre.visibility = View.VISIBLE
                    etNombre.setHint(R.string.hint_add_alumno)
                    val errorMsg = jObj.getString("error_msg")
                    Toast.makeText(requireActivity().applicationContext, errorMsg, Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException){
                e.printStackTrace()
                Toast.makeText(requireActivity().applicationContext, "Json error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error ->
            Log.d(TAG, "Home Agregar Error: ${error.message}")
            Toast.makeText(requireActivity().applicationContext, "", Toast.LENGTH_SHORT).show()
        }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id_papa"] = id_papa
                return params
            }
        }
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req)
    }

    private fun addAlumno(nombre: String, id_papa: String){
        val tag_string_req = "req_add_alumno"
        val strReq: StringRequest = object : StringRequest(Method.POST, appConfig.URL_ADD_ALUMNO , Response.Listener { response ->
            Log.d(TAG, "Home Respones: $response")
            try {
                val jObj = JSONObject(response.substring(response.indexOf("{"), response.length))
                val error = jObj.getBoolean("error")
                Log.d(TAG, "Error response: $error")
                if (!error){
                    val id = jObj.getString("id_alumno")
                    val user = jObj.getJSONObject("alumno")
                    val nom = user.getString("nombre")
                    btnEditAlum.visibility = View.VISIBLE
                    btnAddAlum.visibility = View.INVISIBLE
                    etNombre.setText("")
                    etNombre.visibility = View.INVISIBLE
                    tvNombre.visibility = View.VISIBLE
                    tvNombre.text = nom
                    session.saveAlumno(id)
                    Toast.makeText(requireActivity(), "Se agrego el alumno correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    val errorMsg = jObj.getString("error_msg")
                    Toast.makeText(requireActivity().applicationContext, errorMsg, Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException){
                e.printStackTrace()
                Toast.makeText(requireActivity().applicationContext, "Json error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error ->
            Log.d(TAG, "Home Agregar Error: ${error.message}")
            Toast.makeText(requireActivity().applicationContext, "", Toast.LENGTH_SHORT).show()
        }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id_papa"] = id_papa
                params["nombre"] = nombre
                return params
            }
        }
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req)
    }

    private fun editAlumno(id_alum: String, nombre: String){
        val tag_string_req = "req_edit_alumno"
        val strReq: StringRequest = object : StringRequest(Method.POST, appConfig.URL_UPDATE_ALUMNO , Response.Listener { response ->
            Log.d(TAG, "Home Respones: $response")
            try {
                val jObj = JSONObject(response.substring(response.indexOf("{"), response.length))
                val error = jObj.getBoolean("error")
                Log.d(TAG, "Error response: $error")
                if (!error){
                    val user = jObj.getJSONObject("alumno")
                    val nom = user.getString("nombre")
                    tvNombre.text = nom
                    tvNombre.visibility = View.VISIBLE
                    etNombre.visibility = View.INVISIBLE
                    btnEditAlum.visibility = View.VISIBLE
                    btnUpdateAlum.visibility = View.INVISIBLE
                    Toast.makeText(requireActivity(), "Se actualizo la informacion correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    val errorMsg = jObj.getString("error_msg")
                    Toast.makeText(requireActivity().applicationContext, errorMsg, Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException){
                e.printStackTrace()
                Toast.makeText(requireActivity().applicationContext, "Json error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error ->
            Log.d(TAG, "Home Agregar Error: ${error.message}")
            Toast.makeText(requireActivity().applicationContext, "", Toast.LENGTH_SHORT).show()
        }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id_alumno"] = id_alum
                params["nombre"] = nombre
                return params
            }
        }
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req)
    }

}