package com.example.newsapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {

    lateinit var dbref: DatabaseReference
    lateinit var db: FirebaseFirestore
    lateinit var binding: ActivityMainBinding
    lateinit var uri: Uri
    lateinit var storageRef: StorageReference
    var imagecode = 2
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbref = FirebaseDatabase.getInstance().reference
        db = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance().reference


        binding.btnadd.setOnClickListener {
            var title = binding.edttitel.text.toString()
            var text = binding.edttext.text.toString()


            val ref = storageRef.child("images/${uri.lastPathSegment}.jpg")
            var uploadTask = ref.putFile(uri)

            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    var key = dbref.root.push().key!!

                    var data = newslistmodel(key, title, text, downloadUri.toString())

                    db.collection("user").document().set(data).addOnCompleteListener {
                        if (task.isSuccessful) {
                            Toast.makeText(this, "123456", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "e", Toast.LENGTH_SHORT).show()
                        }
                    }

                    dbref.root.child("img").child(key).setValue(data).addOnCompleteListener {
                        binding.edttitel.text.clear()
                        binding.edttext.text.clear()
                        Log.e(TAG, "onCreate: 11111111111111111111 $")
                    }
                        .addOnFailureListener {
                            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                            Log.e(TAG, "onCreate: ================================= ${it.message}")
                        }
                } else {
                    // Handle failures
                    // ...
                }
            }
        }

        binding.imguplodeimg.setOnClickListener {

            var intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            startActivityForResult(intent, imagecode)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == imagecode) {
                uri = data?.data!!
                binding.imguplodeimg.setImageURI(uri)
            }
        }
    }
}