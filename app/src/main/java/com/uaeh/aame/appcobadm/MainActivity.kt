package com.uaeh.aame.appcobadm

import activity.LoginActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import helper.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    // Declaracion de botones y textView
    private lateinit var txtName: TextView
    private lateinit var txtemail: TextView

    // Declaracion de DB y Session
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Encontrar por id Drawer Layout
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        // Obtener nav header view
        val headerView: View = navView.getHeaderView(0)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Encontrar por id textView
        txtName = headerView.findViewById(R.id.nv_Name)
        txtemail = headerView.findViewById(R.id.nv_Email)

        // Session Manager
        session = SessionManager(applicationContext)

        if (!session.isLoggedIn())
            logoutUser()

        /*
        * Passing each menu ID as a set of Ids because each
        * menu should be considered as top level destination
        * */
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_home, R.id.nav_desempeno, R.id.nav_user_data, R.id.nav_materias, R.id.nav_about), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        userDetails()
    }

    /*
     *  Cerrar sesion del usuario. Colocara bandera isLoggedIn a falso en preferencias
     *  compartidas, limpiara la informacion del usuario desde la tabla urpadres de sqlite
     */
    private fun logoutUser(){
        session.setLogin(false)

        // Lanzando activity Login
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu: this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                logoutUser()
            }
        }
        return super.onOptionsItemSelected(item)
    }

     /*
     *  Funcion para obtener datos de inicio de sesion
     * Unique id, nombre etc
     *
     */
    private fun userDetails() {
        val nombre = session.getNombre()
        val email = session.getMail()

        txtName.text = nombre
        txtemail.text = email
    }
}