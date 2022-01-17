package com.example.androidgral

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_new_drill.view.*
import kotlinx.android.synthetic.main.item_drill.view.*

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
        }
    }
}