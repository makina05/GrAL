package com.example.androidgral

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_drills.*
import kotlinx.android.synthetic.main.activity_home.*
class DrillsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var drillsArrayList: ArrayList<Drill>
    private lateinit var myAdapter: DrillAdapter
    private lateinit var db: FirebaseFirestore


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

    fun initRecycler(){
        recyclerView = findViewById(R.id.recyclerViewId)
        recyclerView.layoutManager = LinearLayoutManager(this)

        drillsArrayList = arrayListOf()
        myAdapter = DrillAdapter(drillsArrayList)

        recyclerViewId.adapter = myAdapter

        EventChangeListener()



    }
    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("drills").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        drillsArrayList.add(dc.document.toObject(Drill::class.java))
                    }
                }
                myAdapter.notifyDataSetChanged()
            }
        })
    }
}







//class DrillsActivity : AppCompatActivity() {
//
////    var drills: List<Drill> = listOf(
////        Drill("Australia","Beroketa ariketa","irudia.com"),
////        Drill("Hamaika","Kontraeraso ariketa", "https://cursokotlin.com/wp-content/uploads/2017/07/thor.jpg")
////    )
////    var drills: MutableList<Drill> = mutableListOf(
////        Drill("Australia","Beroketa ariketa","irudia.com"),
////        Drill("Hamaika","Kontraeraso ariketa", "https://cursokotlin.com/wp-content/uploads/2017/07/thor.jpg")
////    )
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_drills)
//        initRecycler()
//
//        val fab = findViewById<View>(R.id.fabId) as FloatingActionButton
//        title = "Ariketa bilduma"
//        //Adding click listener for FAB
//        fab.setOnClickListener { view ->
//            //Show Dialog here to add new Item
//            addNewItemDialog()
//        }
//    }
//
//    /**
//     * This method will show a dialog box where user can enter new item
//     * to be added
//     */
//    private fun addNewItemDialog() {
//        val alert = AlertDialog.Builder(this)
//
//        val itemEditText = EditText(this)
//        alert.setMessage("Gehitu drill berria")
//        alert.setTitle("Drill izena")
//        alert.setView(itemEditText)
//        alert.setPositiveButton("Ados") { dialog, positiveButton ->
//            val newDrillIntent = Intent(this, NewDrillActivity::class.java)
//            val result = itemEditText.text.toString()
//            newDrillIntent.putExtra("izena", result)
//            startActivity(newDrillIntent)
//            alert.show()
//        }
//        alert.setNegativeButton("Desegin", null)
//        alert.show()
//    }
//
////    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
////        super.onActivityResult(requestCode, resultCode, data)
////        setContentView(R.layout.activity_drills)
////
////        val bundle = intent.extras
////        val izena = bundle?.getString("izena")
////        val desk = bundle?.getString("desk")
////        val irudia = bundle?.getString("irudia")
////        val drilBerria: Drill = Drill(izena, desk, irudia)
////        //drills.add(drilBerria)
////    }
//
//    fun initRecycler() {
//        recyclerViewId.layoutManager = LinearLayoutManager(this)
//        var adapter = DrillAdapter(drills)
//        recyclerViewId.adapter = adapter
//    }
//
//    private lateinit var drills: ArrayList<Drill>
//
//
//    fun getFirebaseData() {
////        val ariketaZerrenda = mutableListOf<Drill>()
////        FirebaseFirestore.getInstance().collection("drills").get().addOnSuccessListener { result ->
////            for (document in result) {
////                val drill:Drill = document.toObject(Drill::class.java)
////                ariketaZerrenda.add(drill)
////            }
////            Log.d("drills","$ariketaZerrenda")
////            drills = ariketaZerrenda as ArrayList<Drill>
////        }
////            .addOnFailureListener { exception ->
////                Log.d(TAG, "Error getting documents: ", exception)
////            }
////    }
//        FirebaseFirestore.getInstance().collection("drills").get().addOnSuccessListener { result ->
//            for (document in result) {
//                val izena = document.get("izena") as String?
//                val desk = document.get("desk") as String?
//                val irudia = document.get("irudia") as String?
//                val ariketa = Drill(izena, desk, irudia)
//                drills.add(ariketa)
//
//            }
//        }
//    }
//}

