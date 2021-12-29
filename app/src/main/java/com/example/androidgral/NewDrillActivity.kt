package com.example.androidgral

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_drills.*
import kotlinx.android.synthetic.main.activity_new_drill.*
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso
import java.util.HashMap

class NewDrillActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    private var PICK_IMAGE_REQUEST = 12
    private var imagePath: Uri?=null
    private var firebaseStorage: FirebaseStorage?=null
    private var storageReference: StorageReference?=null
    private var firebaseAuth: FirebaseAuth?=null
    private var prg: ProgressDialog?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_drill)

        //alert dialogeko izena zuzenean esleitu ariketari
        val bundle = intent.extras
        val izena = bundle?.getString("izena")
        textViewId.text = izena

        //irudia
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        imageViewId.setOnClickListener {
            fileChooser()
        }

    }

    private fun fileChooser() {
        val intent = Intent()
        intent.type="image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,PICK_IMAGE_REQUEST)
    }

    private fun sendData(){
        val db = FirebaseFirestore.getInstance()

        val imageRef: StorageReference = storageReference!!.child(firebaseAuth!!.uid!!).child("image").child("Drill Pic")
        val uploadImage:UploadTask = imageRef.putFile(imagePath!!)
        uploadImage.addOnFailureListener{
            Toast.makeText(this,"Errore bat jazo da", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==Activity.RESULT_OK && data!=null && data.data!=null){
            imagePath = data.data
            Picasso.get().load(imagePath).into(imageViewId)
            sendData()
        }
    }
}

