package com.example.inkspire

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.inkspire.Model.BlogItemModel
import com.example.inkspire.Model.UserData
import com.example.inkspire.databinding.ActivityAddArticleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class AddArticleActivity : AppCompatActivity() {

    private val binding: ActivityAddArticleBinding by lazy {
        ActivityAddArticleBinding.inflate(layoutInflater)
    }

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("blogs")
    private val userReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("users")
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
//    binding.imageButton.setOnClickListener{
//        finish()
//    }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.addBlogButton.setOnClickListener {
            val title: String = binding.blogTitle.editText?.text.toString().trim()
            val description: String = binding.blogDescription.editText?.text.toString().trim()

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user: FirebaseUser? = auth.currentUser
            if (user != null) {
                val userId: String = user.uid
                val userName: String = user.displayName ?: "Anonymous"

                userReference.child(userId)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val userData = snapshot.getValue(UserData::class.java)
                            if (userData != null) {
                                val userNameFromDB: String = userData.name
                                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                                val blogItem = BlogItemModel(
                                    title,
                                    userNameFromDB,
                                    currentDate,
                                    description,
                                    0
                                )

                                val key = databaseReference.push().key
                                if (key != null) {
                                    val blogRef = databaseReference.child(key)
                                    blogRef.setValue(blogItem).addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this@AddArticleActivity,
                                                "Failed to add Blog",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@AddArticleActivity,
                                "Database error: ${error.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }
    }
}
