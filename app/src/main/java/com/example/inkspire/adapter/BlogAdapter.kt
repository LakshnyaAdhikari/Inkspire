package com.example.inkspire.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.inkspire.Model.BlogItemModel
import com.example.inkspire.ReadMoreActivity
import com.example.inkspire.databinding.BlogItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class BlogAdapter(private val blogList: MutableList<BlogItemModel>) :
    RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {

    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
    private val database = FirebaseDatabase.getInstance("https://inkspire-78f16-default-rtdb.asia-southeast1.firebasedatabase.app")
        .reference.child("blogs")

    inner class BlogViewHolder(private val binding: BlogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(blog: BlogItemModel) {
            val context: Context = binding.root.context

            // Set data to views
            binding.heading.text = blog.heading
            binding.username.text = blog.userName
            binding.date.text = blog.date
            binding.post.text = blog.post
            binding.likecount.text = blog.likeCount.toString()

            // Load profile image using Glide if available
           // Glide.with(context)
               // .load(blog.ProfileImage)
               // .into(binding.profileImage)

            // Update like button image based on liked status
            val isLiked = blog.likedBy.contains(currentUserUid)
            updateLikeButtonImage(isLiked)

            // Like button click listener
            binding.likebutton.setOnClickListener {
                if (currentUserUid != null && blog.postId != null) {
                    handleLike(blog)
                } else {
                    Toast.makeText(context, "You have to login first", Toast.LENGTH_SHORT).show()
                }
            }

            // Read More button click listener
            binding.readmorebutton.setOnClickListener {
                val intent = Intent(context, ReadMoreActivity::class.java)
                intent.putExtra("blogItem", blog)
                context.startActivity(intent)
            }
        }

        // Update heart icon based on like status
        private fun updateLikeButtonImage(isLiked: Boolean) {
            val drawableId = if (isLiked) com.example.inkspire.R.drawable.heart3icon
            else com.example.inkspire.R.drawable.heart2icon
            binding.likebutton.setImageResource(drawableId)
        }

        // Like/Unlike handler
        private fun handleLike(blog: BlogItemModel) {
            val postRef = database.child(blog.postId!!).child("likedBy")

            postRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val likedByList = snapshot.getValue(object :
                        GenericTypeIndicator<MutableList<String>>() {}) ?: mutableListOf()

                    if (likedByList.contains(currentUserUid)) {
                        likedByList.remove(currentUserUid)
                    } else {
                        likedByList.add(currentUserUid!!)
                    }

                    // Update likeCount and likedBy in the database
                    val updates = mapOf(
                        "likedBy" to likedByList,
                        "likeCount" to likedByList.size
                    )
                    database.child(blog.postId!!).updateChildren(updates)

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(binding.root.context, "Like failed: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val binding = BlogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BlogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        holder.bind(blogList[position])
    }

    override fun getItemCount(): Int = blogList.size
}
