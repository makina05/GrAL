package com.example.androidgral

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_drills.*
import kotlinx.android.synthetic.main.activity_new_drill.*

class DrillsActivity : AppCompatActivity() {

//    var drills: List<Drill> = listOf(
//        Drill("Australia","Beroketa ariketa","irudia.com"),
//        Drill("Hamaika","Kontraeraso ariketa", "https://cursokotlin.com/wp-content/uploads/2017/07/thor.jpg")
//    )
    var drills: MutableList<Drill> = mutableListOf(
        Drill("Australia","Beroketa ariketa","irudia.com"),
        Drill("Hamaika","Kontraeraso ariketa", "https://cursokotlin.com/wp-content/uploads/2017/07/thor.jpg")
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drills)

        initRecycler()

        val fab = findViewById<View>(R.id.fabId) as FloatingActionButton
        title = "Ariketa bilduma"
        //Adding click listener for FAB
        fab.setOnClickListener { view ->
            //Show Dialog here to add new Item
            addNewItemDialog()
        }
    }
    /**
     * This method will show a dialog box where user can enter new item
     * to be added
    */
    private fun addNewItemDialog() {
        val alert = AlertDialog.Builder(this)

        val itemEditText = EditText(this)
        alert.setMessage("Gehitu drill berria")
        alert.setTitle("Drill izena")
        alert.setView(itemEditText)
        alert.setPositiveButton("Ados") { dialog, positiveButton ->
            val newDrillIntent = Intent(this, NewDrillActivity::class.java)
            val result = itemEditText.text.toString()
            newDrillIntent.putExtra("izena",result)
            startActivity(newDrillIntent)
            alert.show()
        }
        alert.setNegativeButton("Desegin",null)
        alert.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        setContentView(R.layout.activity_drills)

        val bundle = intent.extras
        val izena = bundle?.getString("izena")
        val desk = bundle?.getString("desk")
        val irudia = bundle?.getString("irudia")
        val drilBerria: Drill = Drill(izena,desk,irudia)
        drills.add(drilBerria)
    }

    fun initRecycler(){
        recyclerViewId.layoutManager = LinearLayoutManager(this)
        val adapter = DrillAdapter(drills)
        recyclerViewId.adapter = adapter
    }
}