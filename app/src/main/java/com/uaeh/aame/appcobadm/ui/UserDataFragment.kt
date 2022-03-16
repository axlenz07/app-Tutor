package com.uaeh.aame.appcobadm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.uaeh.aame.appcobadm.R
import helper.SessionManager


class UserDataFragment : Fragment() {

    private lateinit var session : SessionManager

    // Text Views y Boton
    private lateinit var tvNombre: TextView
    private lateinit var tvApellidos: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvTelefono: TextView
    private lateinit var btEditar: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val myApp = activity
        session = SessionManager(myApp!!.applicationContext)
        val root = inflater.inflate(R.layout.fragment_user_data, container, false)

        tvNombre = root.findViewById(R.id.nombres)
        tvApellidos = root.findViewById(R.id.apellidos)
        tvEmail = root.findViewById(R.id.email)
        tvTelefono = root.findViewById(R.id.telefono)

        btEditar = root.findViewById(R.id.Edit_data)


        tvNombre.text = session.getNombre()
        tvApellidos.text = session.getApellidos()
        tvTelefono.text = session.getTel()
        tvEmail.text = session.getMail()

        btEditar.setOnClickListener() {
            val amount = session.getMail()
            val bundle = bundleOf(
                "email_papa" to amount)
            findNavController().navigate(R.id.action_nav_user_data_to_UpdateData, bundle)
        }

        return root
    }


}