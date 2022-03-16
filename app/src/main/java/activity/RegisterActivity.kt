package activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import app.AppConfig
import app.AppController
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest

import com.uaeh.aame.appcobadm.MainActivity
import com.uaeh.aame.appcobadm.R
import helper.SessionManager

import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    private val TAG = RegisterActivity::class.simpleName
    private lateinit var btnRegister: Button
    private lateinit var btnLinkToLogin: Button
    private lateinit var inputName: EditText
    private lateinit var inputLsName: EditText
    private lateinit var inputPhone: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var inputConfPassw: EditText
    private lateinit var pDialog: ProgressDialog
    private lateinit var session: SessionManager

    // Trial to get fun from app package
    private val appConfig = AppConfig()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Find View By Id
        inputName = findViewById(R.id.name)
        inputLsName = findViewById(R.id.lt_names)
        inputPhone = findViewById(R.id.phone)
        inputEmail = findViewById(R.id.email)
        inputPassword = findViewById(R.id.password)
        inputConfPassw = findViewById(R.id.confirm_password)
        btnRegister = findViewById(R.id.btnRegister)
        btnLinkToLogin = findViewById(R.id.btnLinkToLoginScreen)

        btnLinkToLogin.isInvisible = true

        // Progress dialog
        pDialog = ProgressDialog(this)
        pDialog.setCancelable(false)

        // Progress Bar

        // Session Manager
        session = SessionManager(applicationContext)

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already loggde in. Take him to main activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Register Button Click Event
        btnRegister.setOnClickListener {
            val name = inputName.text.toString()
            val lsname = inputLsName.text.toString()
            val cel = inputPhone.text.toString()
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()
            val confpass = inputConfPassw.text.toString()

            if (name.isNotEmpty() && lsname.isNotEmpty() && cel.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                if (password == confpass)
                    registerUser(name, lsname, cel, email, password)
                else
                    Toast.makeText(applicationContext, "Tu contraseña no coincide, Por favor revisa!!", Toast.LENGTH_LONG).show()
            }
            else
                Toast.makeText(applicationContext, "Por favor, ingresa tus datos!", Toast.LENGTH_LONG).show()
        }

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener {
            val i = Intent(applicationContext, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    /**
     * Function to store user in MySQL database will post params(tag, name, email,
     * password) to register url
     */
    private fun registerUser(name: String, lsname: String, celul: String, email: String, password: String) {
        // Tag used to cancel the request
        val tag_string_req = "req_register"

        pDialog.setMessage("Registrando usuario...")
        showDialog()

        val strReq: StringRequest = object : StringRequest(Method.POST, appConfig.URL_REGISTER, Response.Listener { response ->
            Log.d(TAG, "Register Response: $response")
            hideDialog()
            try {
                val jObj = JSONObject(response.substring(response.indexOf("{"), response.length))
                val error = jObj.getBoolean("error")
                if (!error) {
                    // User succesfully stored in MySQL
                    // Now store the user in sqlite
                   /* val id_parent = jObj.getString("id_parent")
                    val user = jObj.getJSONObject("user")
                    val name = user.getString("nombres")
                    val lsname = user.getString("apellidos")
                    val cel = user.getString("telefono")
                    val email = user.getString("email")
                    val created_at = user.getString("created_at")

                    // Inserting row in users table
                    db.addUser(id_parent, name, lsname, cel, email, created_at)*/

                    btnLinkToLogin.isVisible = true
                    Toast.makeText(applicationContext, "Usuario registrado exitosamente. Inicia Sesion Ahora!", Toast.LENGTH_LONG).show()

                    /*// Launch login activity
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()*/
                } else {
                    // Error occured in registration. Get the error message
                    val errorMsg = jObj.getString("error_msg")
                    Toast.makeText(applicationContext, errorMsg, Toast.LENGTH_LONG).show()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }


        }, Response.ErrorListener { error ->
            Log.e(TAG, "Registration Error: ${error.message}")
            Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
            hideDialog()
        }) {
            override fun getParams(): Map<String, String> {
                // Posting params to register url
                val params = HashMap<String, String>()
                params["nombres"] = name
                params["apellidos"] = lsname
                params["telefono"] = celul
                params["email"] = email
                params["password"] = password

                return params
            }
        }
        // Adding request to reques queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req)
    }

    private fun showDialog() {
        if (!pDialog.isShowing)
            pDialog.show()
    }

    private fun hideDialog() {
        if (pDialog.isShowing)
            pDialog.dismiss()
    }
}