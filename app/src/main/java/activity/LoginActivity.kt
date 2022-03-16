package activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.HashMap
import com.android.volley.toolbox.StringRequest

import helper.SessionManager
import app.AppConfig
import app.AppController
import com.android.volley.Response
import com.uaeh.aame.appcobadm.MainActivity
import com.uaeh.aame.appcobadm.R

import org.json.JSONException
import org.json.JSONObject

class   LoginActivity : AppCompatActivity() {
    private val TAG = LoginActivity::class.simpleName
    private lateinit var btnLogin: Button
    private lateinit var btnLinkToRegister: Button
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var pDialog: ProgressDialog
    private lateinit var session: SessionManager


    // trial to get fun from app package
    private val appConfig = AppConfig()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inputEmail = findViewById(R.id.email)
        inputPassword = findViewById(R.id.password)
        btnLogin = findViewById(R.id.btnLogin)
        btnLinkToRegister = findViewById(R.id.btnLinkToRegisterScreen)

        // Progress bar
        pDialog = ProgressDialog(this)
        pDialog.setCancelable(false)

        // Session Manager
        session = SessionManager(applicationContext)

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Login button Clock Event
        btnLogin.setOnClickListener {
            login()
        }

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener {
            toRegister()
        }
    }

    /**
     * Function to call another Activity (Home and Register)
     */
    private fun login() {
        val email = inputEmail.text.toString()
        val password = inputPassword.text.toString()

        // Check for empty data in the form
        if (email.isNotEmpty() && password.isNotEmpty()) {
            // Login user
            checkLogin(email, password)
        } else {
            // Prompt user to enter credentials
            Toast.makeText(applicationContext, "Por favor ingresa tus datos para inicar sesion", Toast.LENGTH_LONG).show()
        }
    }

    private fun toRegister() {
        val i = Intent(applicationContext, RegisterActivity::class.java)
        startActivity(i)
        finish()
    }

    /**
     *  Function to verify login details in mysql db
     */
    private fun checkLogin(email: String, password: String) {
        // Tag used to cancel the request
        val tag_string_req = "req_login"

        pDialog.setMessage("Iniciando Session...")
        showDialog()

        val strReq: StringRequest = object : StringRequest(Method.POST, appConfig.URL_LOGIN, Response.Listener { response ->
            Log.d(TAG, "Login Response: $response")
            hideDialog()
            try {
                val jObj = JSONObject(response.substring(response.indexOf("{"), response.length))
                val error = jObj.getBoolean("error")
                Log.d(TAG, "Error response: $error")

                // Check for error node in json
                if (!error) {
                    // User successfully logged in
                    // Create login screen
                    val id = jObj.getString("id_parent")
                    val user = jObj.getJSONObject("user")
                    val nombre = user.getString("nombres")
                    val apellidos = user.getString("apellidos")
                    val telefono = user.getString("telefono")
                    val mail = user.getString("email")
                    val cont = user.getString("compara")
                    session.setLogin(true)

                    session.saveUserData(id, nombre, apellidos, telefono, mail, cont)

                    // Launch home activity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Error in login. Get the error message
                    val errorMsg = jObj.getString("error_msg")
                    Toast.makeText(applicationContext, errorMsg, Toast.LENGTH_LONG).show()
                }
            } catch (e: JSONException) {
                // JSON error
                e.printStackTrace()
                Toast.makeText(applicationContext, "Json error: ${e.message}", Toast.LENGTH_LONG).show()
            }

        }, Response.ErrorListener { error ->
            Log.e(TAG, "Login Error: ${error.message}")
            Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
            hideDialog()
        }) {
            override fun getParams(): Map<String, String> {
                // Posting parameters to login url
                val params = HashMap<String, String>()
                params["email"] = email
                params["password"] = password
                return params
            }
        }

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req)
    }

    private fun showDialog() {
        if (!pDialog.isShowing) pDialog.show()
    }

    private fun hideDialog() {
        if (pDialog.isShowing) pDialog.dismiss()
    }
}