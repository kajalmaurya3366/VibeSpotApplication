package com.example.vibespotapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vibespotapplication.Model.Post
import com.example.vibespotapplication.R

class PostAdapter(val context:Context,val postList:ArrayList<Post>): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.post_item_view,parent,false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        val currentPost=postList[position]

        holder.placeName.text=currentPost.placeName
        holder.location.text=currentPost.location
        holder.desc.text=currentPost.description
        holder.authorName.text="~ ${currentPost.authorName}"

    }

    override fun getItemCount(): Int = postList.size

    class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val placeName=itemView.findViewById<TextView>(R.id.tv_place_name)
        val location = itemView.findViewById<TextView>(R.id.tv_location)
        val desc=itemView.findViewById<TextView>(R.id.tv_details)
        val authorName=itemView.findViewById<TextView>(R.id.tv_sender_name)
    }
}