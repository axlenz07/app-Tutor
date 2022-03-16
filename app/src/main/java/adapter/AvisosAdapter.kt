package adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.uaeh.aame.appcobadm.R

class AvisosAdapter(private val _context: Activity, private val title: ArrayList<String>, private val descripcion: ArrayList<String>, private val fecha: ArrayList<String>):
    ArrayAdapter<String>(_context, R.layout.list_avisos, title){

    private lateinit var tvNumero: TextView
    private lateinit var tvTitulo: TextView
    private lateinit var tvDescrip: TextView
    private lateinit var tvFecha: TextView

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = _context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_avisos, null, true)

        tvNumero = rowView.findViewById(R.id.num)
        tvTitulo = rowView.findViewById(R.id.titulo)
        tvDescrip = rowView.findViewById(R.id.descripcion)
        tvFecha = rowView.findViewById(R.id.fecha)

        tvNumero.text = (position + 1).toString()
        tvTitulo.text = title[position]
        tvDescrip.text = descripcion[position]
        tvFecha.text = fecha[position]

        return rowView
    }
}