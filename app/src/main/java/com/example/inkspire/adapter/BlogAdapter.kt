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
import com.google.firebase.database.FirebaseDatabase

class BlogAdapter(private val blogList: MutableList<BlogItemModel>) : RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {

    private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
    private val database = FirebaseDatabase.getInstance("https://inkspire-78f16-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("blogs")

    inner class BlogViewHolder(private val binding: BlogItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(blog: BlogItemModel) {
            val context: Context = binding.root.context

            binding.heading.text = blog.heading
            binding.username.text = blog.userName
            binding.date.text = blog.date
            binding.post.text = blog.post
            binding.likecount.text = blog.likeCount.toString()

            val isLiked = blog.likedBy?.contains(currentUserUid) == true
            updateLikeButtonImage(isLiked)

            binding.likebutton.setOnClickListener {
                if (currentUserUid != null && blog.postId != null) {
                    handleLike(blog)
                } else {
                    Toast.makeText(context, "YOU HAVE TO LOGIN FIRST", Toast.LENGTH_SHORT).show()
                }
            }

            binding.readmorebutton.setOnClickListener {
                val intent = Intent(context, ReadMoreActivity::class.java)
                intent.putExtra("blogItem", blog)
                context.startActivity(intent)
            }
        }

        private fun updateLikeButtonImage(isLiked: Boolean) {
            val drawableId = if (isLiked) com.example.inkspire.R.drawable.heart3icon else com.example.inkspire.R.drawable.heart2icon
            binding.likebutton.setImageResource(drawableId)
        }

        private fun handleLike(blog: BlogItemModel) {
            val postRef = database.child(blog.postId!!)

            val likedByList = blog.likedBy ?: mutableListOf()

            if (likedByList.contains(currentUserUid)) {
                likedByList.remove(currentUserUid)
                blog.likeCount--
            } else {
                likedByList.add(currentUserUid!!)
                blog.likeCount++
            }

            blog.likedBy = likedByList
            postRef.child("likeCount").setValue(blog.likeCount)
            postRef.child("likedBy").setValue(likedByList)

            binding.likecount.text = blog.likeCount.toString()
            updateLikeButtonImage(likedByList.contains(currentUserUid))
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