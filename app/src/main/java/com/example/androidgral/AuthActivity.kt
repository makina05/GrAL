package com.example.androidgral

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        setTheme(R.style.Theme_AndroidGral)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        //Analytics event
        val analytics:FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message","Firebase integrazioa lortuta")
        analytics.logEvent("InitScreen",bundle)

        //Setup
        setup()
    }
    private fun setup(){
        title = "Kautoketa"

        signUpButton.setOnClickListener{
            if (emailEditText.text.isNotEmpty() && passwdEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailEditText.text.toString(),passwdEditText.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        showHome(it.result?.user?.email?:"",ProviderType.BASIC) // ? -> emaila hutsik jartzen bada string huts batekin betetzen du
                    } else{
                        showAlert()
                    }
                }
            }
        }
        logInBtn.setOnClickListener{
            if (emailEditText.text.isNotEmpty() && passwdEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEditText.text.toString(),passwdEditText.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful){
                        showHome(it.result?.user?.email?:"",ProviderType.BASIC) // ? -> emaila hutsik jartzen bada string huts batekin betetzen du
                    } else{
                        showAlert()
                    }
                }
            }
        }
    }
    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Errore bat egon da koutoketa prozesuan")
        builder.setPositiveButton("Ados",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType){
        val homeIntent = Intent(this,HomeActivity::class.java).apply{
            putExtra("email",email)
            putExtra("provider",provider.name)
        }
        startActivity(homeIntent)

    }
}