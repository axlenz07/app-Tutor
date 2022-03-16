package helper

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.util.Log
import java.nio.file.FileSystemAlreadyExistsException

class SessionManager {
    // LogCat tag
    private val TAG = SessionManager::class.simpleName

    // Shared Preferences
    var pref: SharedPreferences
    var editor: Editor
    var _context: Context

    // Shared pref mode
    var PRIVATE_MODE = 0

    // Shared preferences file name
    private val PREF_NAME = "LoginSQL"
    private val KEY_IS_LOGGEDIN = "isLoggedIn"

    constructor(context: Context) {
        this._context = context
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
        editor.apply()
    }

    fun setLogin(isLoggedIn: Boolean) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn)

        // commit changes
        editor.commit()

        Log.d(TAG, "User login session modified")
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false)
    }

    fun saveUserData(id: String, nombre: String, apellidos: String, telefono: String, email: String, contraseña: String) {
        editor.putString("Id_Padre", id)
        editor.putString("Nombre", nombre)
        editor.putString("Apellidos", apellidos)
        editor.putString("Telefono", telefono)
        editor.putString("Correo", email)
        editor.putString("Contraseña", contraseña)
        editor.commit()
        Log.d(TAG, "Correo Guardado")
    }

    fun saveAlumno(id: String){
        editor.putString("Alumno", id)
        editor.commit()
        Log.d(TAG, "Alumno Guardado")
    }

    fun getId(): String {
        return pref.getString("Id_Padre", "sin-id").toString()
    }

    fun getMail(): String {
        return pref.getString("Correo", "sincorreo@null").toString()
    }

    fun getNombre(): String {
        return pref.getString("Nombre", "null").toString()
    }

    fun getApellidos(): String {
        return pref.getString("Apellidos", "null").toString()
    }

    fun getTel(): String {
        return pref.getString("Telefono", "null").toString()
    }

    fun getCont(): String {
        return pref.getString("Contraseña", "null").toString()
    }

    fun getAlumno(): String {
        return pref.getString("Alumno", "null").toString()
    }
}