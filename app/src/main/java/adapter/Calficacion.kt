package adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.uaeh.aame.appcobadm.R

class Calficacion(private val _context: Activity, private val aspecto: ArrayList<String>, private val valor: ArrayList<String>, private val calif: ArrayList<String>):
    ArrayAdapter<String>(_context, R.layout.list_calificacion, aspecto){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = _context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_calificacion, null, true)

        val tvNum = rowView.findViewById<TextView>(R.id.num_aspecto)
        val tvAspec = rowView.findViewById<TextView>(R.id.tv_aspec)
        val tvValor = rowView.findViewById<TextView>(R.id.tv_valor)
        val tvCalif = rowView.findViewById<TextView>(R.id.tv_calif)

        val temp = "${position + 1}.-"

        tvNum.text = temp
        tvAspec.text = aspecto[position]
        tvValor.text = valor[position]
        tvCalif.text = calif[position]

        return rowView
    }
}