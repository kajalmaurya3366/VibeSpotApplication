package com.example.vibespotapplication.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vibespotapplication.Adapter.PostAdapter
import com.example.vibespotapplication.Model.Post
import com.example.vibespotapplication.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var postRecyclerView: RecyclerView
    private lateinit var adapter: PostAdapter
    private lateinit var postList: ArrayList<Post>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        db = Firebase.firestore

        //click on add btn
        binding.fbAddBtn.setOnClickListener {
            startActivity(Intent(this, AddpostActivity::class.java))
        }

        binding.rvPosts.layoutManager = LinearLayoutManager(this)
        postList = ArrayList()
        adapter = PostAdapter(this,postList)
        binding.rvPosts.adapter=adapter

        fetchPosts()


        //logout
        binding.tvLogout.setOnClickListener {
            mAuth.signOut()
            val intent= Intent(this, LoginActivity::class.java)
            finish()
            startActivity(intent)
        }
    }



    private fun fetchPosts() {
        postList.clear()
        db.collection("posts")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.e("MainActivity", "Listen failed for posts.", e)
//                    Toast.makeText(this, "Failed to load posts: ${e.message}", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    for (document in snapshots.documents) {
                        val post = document.toObject(Post::class.java)
                        post?.let {
                            postList.add(it)
                        }
                    }
                    adapter.notifyDataSetChanged()
                    Log.d("MainActivity", "Posts fetched and updated: ${postList.size}")
                } else {
                    Log.d("MainActivity", "Current posts data: null")
                }
            }
    }
}