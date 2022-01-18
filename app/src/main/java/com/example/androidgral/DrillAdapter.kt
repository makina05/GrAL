package com.example.androidgral

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_drill.view.*

import android.content.Intent
import android.net.Uri
import android.text.Editable
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore


class DrillAdapter(val drills: ArrayList<Drill>):RecyclerView.Adapter<DrillAdapter.DrillHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrillHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DrillHolder(layoutInflater.inflate(R.layout.item_drill,parent,false))
    }

    override fun getItemCount(): Int {
        return drills.size
    }

    override fun onBindViewHolder(holder: DrillHolder, position: Int) {
        holder.render(drills[position])
    }

    class DrillHolder(val view: View):RecyclerView.ViewHolder(view){
        fun render(drill:Drill) {
            view.izenaTvId.text = drill.izena
            view.deskTvId.text = drill.desk
            Picasso.get().load(drill.irudia).into(view.irudiaIvId)

            itemView.setOnClickListener {

                Toast.makeText(view.context,drill.izena,Toast.LENGTH_SHORT).show()
                getUrlFromIntent(view)
            }
        }
        fun getUrlFromIntent(view: View) {
            val url = "http://www.google.com"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            view.context.startActivity(intent)


        }
    }
}