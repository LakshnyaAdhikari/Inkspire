package com.example.inkspire

import android.os.Bundle
import android.widget.EditText
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.inkspire.Model.UserData
import com.example.inkspire.databinding.ActivityAddArticleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date

class AddArticleActivity : AppCompatActivity() {
    private val binding:ActivityAddArticleBinding by lazy {
        ActivityAddArticleBinding.inflate(layoutInflater)
    }
    private val databaseReference: DatabaseReference=FirebaseDatabase.getInstance().getReference("blogs")
    private val userReference: DatabaseReference=FirebaseDatabase.getInstance().getReference("users")
    private val auth=FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

            binding.addBlogButton.setOnClickListener{
             val title:String= binding.blogTitle.editText?.text.toString().trim()
             val description:String= binding.blogDescription.editText?.text.toString().trim()

              if(title.isEmpty() || description.isEmpty())  {
                  Toast.makeText(context:this, text:"please fill all the fields",Toast.LENGTH_SHORT).show()
              }
               val user:FirebaseUser?:auth.currentUser
               if(user!=null){
                   val userId:String = user.uid
                   val userName:String= user.displayName?"Anonymous"
                   val userImageUrl:comparable<{String& uri}>=user.photoUrl?:""

                   userReference.child(userId).addListenerForSingleValueEvent(object: ValueEventListener{
                       override fun onDatachange(snapshot:DataSnapshot){

                       }
                       override fun onCancelled(error:DatabaseError){
                           val userData:Any? = snapshot.getValue(UserData::class.java)
                           if(UserData!=null){
                               val UserNameFromDB:String= userData.name
                               val userImageUrlFromDB=UserData.profileImage

                               val currentDate = SimpleDateFormat(pattern:"yyyy-mm-dd").format(Date())

                               val blogItem = blogItemModel(
                                   title,
                                   userImageUrlFromDB,
                                   currentDate,
                                   likecount:0,
                                   description,
                                   userImageUrl,


                               )
                           }
                       }
                   })
               }
            }
        }

    }
}
