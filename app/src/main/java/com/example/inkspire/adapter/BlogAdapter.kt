package com.example.inkspire.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inkspire.Model.BlogItemModel
import com.example.inkspire.ReadMoreActivity
import com.example.inkspire.databinding.BlogItemBinding

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
    }
}