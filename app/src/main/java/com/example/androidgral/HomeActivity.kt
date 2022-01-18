package com.example.androidgral

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*

enum class ProviderType{
    BASIC,
    GOOGLE
}

class HomeActivity : AppCompatActivity() {
    fun showHide(btn:ImageButton) {
        btn.isVisible = FirebaseAuth.getInstance().currentUser?.email.toString()=="admin@admin.com"}
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        showHide(firebaseBtnId)
        //setup
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setup(email?:"",provider?:"")


        //baloreak gorde
        val prefs: SharedPreferences.Editor? = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs?.putString("email",email)
        prefs?.putString("provider",provider)
        prefs?.apply()

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
        drillsButton.setOnClickListener{
            val drillsIntent = Intent(this,DrillsActivity::class.java).apply{
                putExtra("email",email)
                putExtra("twitter",sareTextView.text)
            }
            startActivity(drillsIntent)
        }
        logOutButton.setOnClickListener {
            //datuak ezabatu
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs?.clear()
            prefs?.apply()

            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        firebaseBtnId.setOnClickListener {
            val url = "https://console.firebase.google.com/u/1/project/androidgral/firestore/data/~2Ferabiltzaileak~2Fadmin@admin.com"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
        }
    }
