package com.example.androidgral

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_home.*

class AuthActivity : AppCompatActivity() {
    private val GOOGLE_SIGN_IN=100
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
        session()
    }

    private fun session() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE)
        val email:String? = prefs.getString("email",null)
        val provider:String? = prefs.getString("provider",null)

        if(email != null && provider != null) {
            authLayID.visibility = View.INVISIBLE
            showHome(email,ProviderType.valueOf(provider))
        }
    }
    //inbisible jarri dugunez lehen, leihoa berirekitzen den bakoitzean bisible bihurtzeko:
    override fun onStart() {
        super.onStart()

        authLayID.visibility = View.VISIBLE
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
        googleBtnId.setOnClickListener {
            //config Auth
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
            requestIdToken("4857564904-di78godfkqpmlj7vpmv92cqqkpi0c9a5.apps.googleusercontent.com").requestEmail().build()

            val googleClient:GoogleSignInClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()

            startActivityForResult(googleClient.signInIntent,GOOGLE_SIGN_IN)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken,null)

                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful){
                            showHome(account.email ?: "",ProviderType.GOOGLE)
                        }else{
                            showAlert()
                        }
                    }
                }
            }catch (e: ApiException){
                showAlert()
            }


        }
    }
}