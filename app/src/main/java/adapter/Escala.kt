package adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.uaeh.aame.appcobadm.R

class Escala(private val _context: Activity, private val aspecto: ArrayList<String>, private val porcentaje: ArrayList<String>):
    ArrayAdapter<String>(_context, R.layout.list_escala, aspecto) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = _context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_escala, null, true)

        val tvAspecto = rowView.findViewById<TextView>(R.id.aspecto)
        val tvPorcentaje = rowView.findViewById<TextView>(R.id.porcentaje)

        tvAspecto.text = aspecto[position]
        tvPorcentaje.text = "${porcentaje[position]}%"

        return rowView
    }
}