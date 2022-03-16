package adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.uaeh.aame.appcobadm.R

class DetalleMateria(private val _context: Activity, private val id: ArrayList<String>, private val materia: ArrayList<String>, private val nombre: ArrayList<String>,
    private val apellido: ArrayList<String>, private val tel: ArrayList<String>, private val mail: ArrayList<String>): ArrayAdapter<String>(_context, R.layout.list_detalle_materia, id){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = _context.layoutInflater
        val rowView = inflater.inflate(R.layout.list_detalle_materia, null, true)

        val tvNum = rowView.findViewById<TextView>(R.id.num_Mat)
        val tvMateria = rowView.findViewById<TextView>(R.id.nom_Materia)
        val tvNombCom = rowView.findViewById<TextView>(R.id.nomb_Maest)
        val tvTel = rowView.findViewById<TextView>(R.id.numTel)
        val tvMail = rowView.findViewById<TextView>(R.id.numMail)

        tvNum.text = "${position + 1}.-"
        tvMateria.text = materia[position]
        tvNombCom.text = "${nombre[position]} ${apellido[position]}"
        tvTel.text = tel[position]
        tvMail.text = mail[position]

        return rowView
    }
}