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
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class AddArticleActivity : AppCompatActivity() {

    private val binding: ActivityAddArticleBinding by lazy {
        ActivityAddArticleBinding.inflate(layoutInflater)
    }

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance("https://inkspire-78f16-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("blogs")

    private val userReference: DatabaseReference =
        FirebaseDatabase.getInstance("https://inkspire-78f16-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("users")

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.backButton2.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.addBlogButton.setOnClickListener {
            val title = binding.blogTitle.editText?.text.toString().trim()
            val description = binding.blogDescription.editText?.text.toString().trim()

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentUser = auth.currentUser
            val currentEmail = currentUser?.email

            if (currentEmail != null) {
                // Look up user by email
                userReference.orderByChild("email").equalTo(currentEmail)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (childSnapshot in snapshot.children) {
                                    val userData = childSnapshot.getValue(UserData::class.java)
                                    if (userData != null) {
                                        val userName = userData.name
                                        val profileImage = userData.profileImage
                                        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                                        val key = databaseReference.push().key
                                        if (key != null) {
                                            val blogItem = BlogItemModel(
                                                heading = title,
                                                userName = userName,
                                                date = currentDate,
                                                post = description,
                                                likeCount = 0,
                                                ProfileImage = profileImage ?: "",
                                                postId = key,
                                                likedBy = mutableListOf()
                                            )

                                            databaseReference.child(key).setValue(blogItem)
                                                .addOnCompleteListener {
                                                    if (it.isSuccessful) {
                                                        Toast.makeText(
                                                            this@AddArticleActivity,
                                                            "Blog Added Successfully!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
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
                            } else {
                                Toast.makeText(
                                    this@AddArticleActivity,
                                    "User not found in database",
                                    Toast.LENGTH_SHORT
                                ).show()
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
            } else {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
