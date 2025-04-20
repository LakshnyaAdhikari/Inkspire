package com.example.inkspire.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.inkspire.Model.BlogItemModel
import com.example.inkspire.ReadMoreActivity
import com.example.inkspire.databinding.BlogItemBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.collection.R

class BlogAdapter (private val items: List<BlogItemModel>):RecyclerView.Adapter<BlogAdapter.blogViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): blogViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding:BlogItemBinding=BlogItemBinding.inflate(inflater, parent, false)
        return blogViewHolder(binding)
    }



    override fun onBindViewHolder(holder: blogViewHolder, position: Int) {
        val blogItem:BlogItemModel=items[position]
        holder.bind(blogItem)
    }

    override fun getItemCount(): Int {
     return items.size    }

    private fun handleLikeButtonClicked(postId: Any, blogItemModel: Any) {
        val userReference:DatabaseReference= databaseReference.child("user").child(currentUser!!.uid)
        val postLikeRefernce= databaseReference.child("blogs").child(postId).child("likes")

        postLikeRefernce.child(currentUser.uid).addEventListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    userReference.child("likes").child(postId).removeValue()
                    .addOnSuccessListener{
                        postLikeRefernce.child(currentUser.uid).removeValue()
                        blogItemModel.likedBy?.remove(currentUser.uid)
                        updateLikeButtonImage(binding , liked = :fixed)

                        val newLikeCount:Int=blogItemModel.likCount-1
                        blogItemModel.likeCount=newLikeCount
                        databaseReference.child("blogs").child(postId).child(newLikeCount).setValue(newLikeCount)
                        notifyDataSetChanged()
                    }
                        .addOnFailureListener{e->Log.e("LikedClicked","onDatachnage:Failed to unlike the blog $e") }
                }
                else{
                    userReference.child("likes").child(postId).setValue(true)
                        .addOnSuccessListener {
                            postLikeRefernce.child(currentUser.uid).setValue(true)
                            blogItemModel.likedBy?.add(currenUser.uid)
                            updateLikeButtonImage(true)

                            val newLikeCount=blogItemModel.likeCount=+1
                            blogItemModel.likeCount=newLikeCount
                            databaseReference.child("blogs").child(postId).child("likescount").setValues()
                            notifyDataSetChanged()
                        }
                        .addOnFailureListener() {e->Log.e("LikedClicked","onDatachnage:Failed to unlike the blog $e")  }
                }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }}

    inner class blogViewHolder (private val binding: BlogItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(blogItemModel: BlogItemModel) {
          binding.heading.text=blogItemModel.heading

            binding.username.text=blogItemModel.userName
            binding.date.text=blogItemModel.date
            binding.post.text=blogItemModel.post
            binding.likecount.text=blogItemModel.likeCount.toString()

            //set on click listener
            binding.root.setOnClickListener{
                val context= binding.root.context
                val intent= Intent(context,ReadMoreActivity::class.java)
                intent.putExtra("blogItem",blogItemModel)
                context.startActivity(intent)
            }
        }
        val postLikReference:DatabaseReference=databaseReference.child(pathString:"blogs").child(pathString:"likes")
        val currentUserLiked = currentUser?.uid?. let{ uid->postLikReference.child(uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    binding.likebutton.setImageResource(R.drawable.heart3icon)
                }else{
                    binding.likebutton.setImageResource(R.drawable.heart2icon)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
            binding.likebutton.setOnClickListener(){
                if(currentUserLiked!=null){
                    handleLikeButtonClicked(postId,blogItemModel)
                }
                else{
                    Toast.makeText(this@BlogAdapter,"YOU HAVE TO LOGIN FIRST",Toast.LENGTH_SHORT).short()
                }
            }
        }

    }
    private fun updateLikeButtonImage(binding: BlogItemBinding ,liked:Boolean){
        if(liked){
            binding.likebutton.setImageResource(R.drawable.heart2icon)
        }
        else{
            binding.likebutton.setImageResource(R.drawable.heart3icon)
        }
    }
}