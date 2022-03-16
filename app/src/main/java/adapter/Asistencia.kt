package adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.uaeh.aame.appcobadm.R

class Asistencia(private val _context: Activity, private val fecha: ArrayList<String>, private val asistencia: ArrayList<Int>):
    ArrayAdapter<String>(_context, R.layout.list_asistencia, fecha){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = _context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_asistencia, null, true)

        val num = rowView.findViewById<TextView>(R.id.num_asis)
        val date = rowView.findViewById<TextView>(R.id.tvfecha)
        val imagen = rowView.findViewById<ImageView>(R.id.imAsis)

        val temp = "${position + 1}.- "
        num.text = temp
        date.text = fecha[position]
        if (asistencia[position] == 1)
            imagen.setImageResource(R.drawable.ic_positive)
        else
            imagen.setImageResource(R.drawable.ic_negative)

        return rowView
    }
}