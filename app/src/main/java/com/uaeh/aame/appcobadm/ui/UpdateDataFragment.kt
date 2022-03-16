package com.uaeh.aame.appcobadm.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import app.AppConfig
import app.AppController
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.uaeh.aame.appcobadm.R
import helper.SessionManager
import kotlinx.android.synthetic.main.fragment_update_data.*
import org.json.JSONException
import org.json.JSONObject

class UpdateDataFragment : Fragment() {
    // TAG
    private val TAG = UpdateDataFragment::class.simpleName

    // Clase para guardar variables
    private lateinit var session: SessionManager
    private val appConfig = AppConfig()

    // Edit Text y Botones
    private lateinit var etxNombre: EditText
    private lateinit var etxApellido: EditText
    private lateinit var etxTelefono: EditText
    private lateinit var etxEmail: EditText
    private lateinit var etxContrasena: EditText
    private lateinit var etxConfirmPassw: EditText
    private lateinit var btnActualizar: Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_update_data, container, false)

        // Inicializar clase session
        session = SessionManager(requireActivity().applicationContext)

        // Inicializar Edit text y Boton
        etxNombre = root.findViewById(R.id.etNombre)
        etxApellido = root.findViewById(R.id.etApellidos)
        etxTelefono = root.findViewById(R.id.etTelefono)
        etxEmail = root.findViewById(R.id.etEmail)
        etxContrasena = root.findViewById(R.id.etContraseña)
        etxConfirmPassw = root.findViewById(R.id.etConfirmarConstraseña)
        btnActualizar = root.findViewById(R.id.btnUpdateData)

        Toast.makeText(requireActivity().applicationContext, arguments?.getString("email_papa"), Toast.LENGTH_SHORT).show()
        btnActualizar.setOnClickListener {
            updateUser()
        }
        return root
    }

    private fun updateUser() {
        // Tag used to cancel the request
        val tag_string_req = "req_update"

        // Arreglo con los datos a actualizar
        val padre = obtenerDatos()

        val strReq: StringRequest = object : StringRequest(Method.POST, appConfig.URL_UPDATE, Response.Listener { response ->
            Log.d(TAG, "Update Response: $response")
            try {
                val jObj = JSONObject(response.substring(response.indexOf("{"), response.length))
                val error = jObj.getBoolean("error")
                Log.d(TAG, "Error Response: $error")

                // Checar por error en nodo de json
                if (!error) {
                    // Usuario actualizado
                    // Volver a guardar los valores
                    val id = jObj.getString("id_parent")
                    val user = jObj.getJSONObject("user")
                    val nombre = user.getString("nombres")
                    val apellidos = user.getString("apellidos")
                    val telefono = user.getString("telefono")
                    val email = user.getString("email")
                    val cont = user.getString("contraseña")
                    session.saveUserData(id, nombre, apellidos, telefono, email, cont)
                    Toast.makeText(requireActivity().applicationContext, "Se actualizo la informacion correctamente.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    // Error al actualizar informacion. Obtener el mensaje de error.
                    val errorMsg = jObj.getString("error_msg")
                    Toast.makeText(requireActivity().applicationContext, errorMsg, Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException) {
                // JSON error
                e.printStackTrace()
                Toast.makeText(requireActivity().applicationContext, "Json error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }, Response.ErrorListener { error ->
            Log.e(TAG, "Update error: ${error.message}")
            Toast.makeText(requireActivity().applicationContext, error.message, Toast.LENGTH_SHORT).show()
        }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id"] = session.getId()
                params["nombres"] = padre[0]
                params["apellidos"] = padre[1]
                params["telefono"] = padre[2]
                params["email"] = padre[3]
                params["contraseña"] = padre[4]
                params["encrip"] = padre[5]

                return params
            }
        }
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req)
    }

    private fun obtenerDatos(): Array<String>{
        val nom: String
        val ape: String
        val tel: String
        val mail: String

        // Guardar el dato de nombre
        if (etxNombre.text.toString().isNotEmpty())
            nom = etxNombre.text.toString()
        else
            nom = session.getNombre()
        if (etxApellido.text.toString().isNotEmpty())
            ape = etxApellido.text.toString()
        else
            ape = session.getApellidos()
        if (etxTelefono.text.toString().isNotEmpty())
            tel = etxTelefono.text.toString()
        else
            tel = session.getTel()
        if (etxEmail.text.toString().isNotEmpty())
            mail = etxEmail.text.toString()
        else
            mail = session.getMail()

        lateinit var cont: String
        lateinit var cambiar: String
        if (etxContrasena.text.isNotEmpty() && etxConfirmPassw.text.isNotEmpty()){
            if (etxContrasena.text.toString() == etxConfirmPassw.text.toString()) {
                cont = etxConfirmPassw.text.toString()
                cambiar = "si"
            } else
                Toast.makeText(requireActivity().applicationContext, "No coinciden las contraseñas", Toast.LENGTH_SHORT).show()
        } else {
            cont = session.getCont()
            cambiar = "no"
        }

        val datosUsuario: Array<String> = arrayOf(nom, ape, tel, mail, cont, cambiar)
        return datosUsuario
    }
}