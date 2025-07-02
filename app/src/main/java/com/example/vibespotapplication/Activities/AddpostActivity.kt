package com.example.vibespotapplication.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.vibespotapplication.Activities.MainActivity
import com.example.vibespotapplication.R
import com.example.vibespotapplication.databinding.ActivityAddpostBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class AddpostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddpostBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddpostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        db = Firebase.firestore

        // on back click
        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // click on post btn
        binding.btnPost.setOnClickListener {

            val name = binding.etPlaceName.text.toString()
            val location = binding.etLocation.text.toString()
            val desc = binding.etDescPost.text.toString()

            val currentUser = mAuth.currentUser
            if (currentUser != null) {
                val userId = currentUser.uid
                db.collection("users").document(userId).get()
                    .addOnSuccessListener { documentsnapShot->
                        if(documentsnapShot.exists())
                        {
                            val authorName= documentsnapShot.getString("name")
                            if(authorName !=null){
                                post(name, location, desc,userId,authorName)
                            }
                        }
                    }
            }

        }
    }

    private fun post(name: String, location: String, desc: String,userId:String,authorName:String) {

        val postId = db.collection("posts").document().id

        val post = hashMapOf(
            "postId" to postId,
            "placeName" to name,
            "location" to location,
            "description" to desc,
            "userId" to userId,
            "authorName" to authorName
        )
        db.collection("posts")
            .document(postId)
            .set(post)
            .addOnSuccessListener {
                binding.etPlaceName.text.clear()
                binding.etLocation.text.clear()
                binding.etDescPost.text.clear()
                Toast.makeText(this, "Successfully posted", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                finish()
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to post", Toast.LENGTH_SHORT).show()
            }
    }
}