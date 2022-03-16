package adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.uaeh.aame.appcobadm.R

class Materias(private val _context: Activity, private val id: ArrayList<String>, private val nombre: ArrayList<String>):
    ArrayAdapter<String>(_context, R.layout.list_materias, id){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = _context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_materias, null, true)

        val tvNum = rowView.findViewById<TextView>(R.id.numMat)
        val tvMateria = rowView.findViewById<TextView>(R.id.nomMateria)

        tvNum.text = "${position + 1}.-"
        tvMateria.text = nombre[position]

        return rowView
    }
}