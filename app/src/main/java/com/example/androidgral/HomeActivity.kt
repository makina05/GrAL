package com.example.androidgral

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_home.*

enum class ProviderType{
    BASIC
}

class HomeActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //setup
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setup(email?:"",provider?:"")
    }

    private fun setup(email:String,provider:String){
        title = "Hasiera"

        emailTextView.text = email
        providerTextView.text = provider

        logOutButton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
        saveButton.setOnClickListener{
            db.collection("erabiltzaileak").document(email).set(
                hashMapOf("provider" to provider,
                "klub" to klubTextView.text.toString(),
                "sarea" to sareTextView.text.toString())
            )
        }
        getButton.setOnClickListener{
            db.collection("erabiltzaileak").document(email).get().addOnSuccessListener {
                klubTextView.setText(it.get("klub") as String?)
                sareTextView.setText(it.get("sarea") as String?)
            }
        }
        deleteButton.setOnClickListener{
            db.collection("erabiltzaileak").document(email).delete()
        }
    }
}