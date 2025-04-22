package com.example.inkspire

import android.content.Intent
import android.content.res.ColorStateList
import android.database.DatabaseErrorHandler
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.TextSnapshot
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inkspire.Model.BlogItemModel
import com.example.inkspire.adapter.BlogAdapter
import com.example.inkspire.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Error

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference : DatabaseReference
    private val blogItems= mutableListOf<BlogItemModel>()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance("https://inkspire-78f16-default-rtdb.asia-southeast1.firebasedatabase.app").reference.child("blogs")


        val userId:String? = auth.currentUser?.uid

        if(userId!= null){
            loadUserProfileImage(userId)
        }

        val recyclerView=binding.blogRecyclerView
        val blogAdapter=BlogAdapter(blogItems)
        recyclerView.adapter=blogAdapter
        recyclerView.layoutManager=LinearLayoutManager(this)

        databaseReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                blogItems.clear()
                for (snapshot in snapshot.children){
                    val blogItem = snapshot.getValue(BlogItemModel::class.java)
                     blogItem?.postId=snapshot.key
                    if (blogItem!=null){
                        Log.d("FirebaseData", "Blog: ${blogItem.heading}")
                        blogItems.add(blogItem)

                    }else{
                        Log.e("FirebaseData", "Failed to parse blog item")
                    }
                }
                blogItems.reverse()
                blogAdapter.notifyDataSetChanged()

            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity,"Blog loading failed",Toast.LENGTH_SHORT).show()

            }

        })
        //recyclerView.layout()
        binding.floatingAddArticleButton.setOnClickListener {
            startActivity(Intent(this,AddArticleActivity::class.java))
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadUserProfileImage(userId: String) {
        val userReference = FirebaseDatabase.getInstance().reference.child("users").child(userId)

        userReference.child("profileImage").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val profileImageUrl:String? = snapshot.getValue(String::class.java)


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity,"error loading profile image",Toast.LENGTH_SHORT).show()

            }
        })

    }
}