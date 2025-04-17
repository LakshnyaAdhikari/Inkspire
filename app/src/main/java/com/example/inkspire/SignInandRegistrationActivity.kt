package com.example.inkspire

import android.content.Intent
import android.os.Bundle
import com.example.inkspire.Model.UserData
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.inkspire.databinding.ActivitySignInandRegistrationBinding
import com.example.inkspire.databinding.ActivityWelcomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import register.WelcomeActivity
import java.net.Authenticator
import javax.security.auth.login.LoginException

class SignInandRegistrationActivity : AppCompatActivity() {
    private val binding:ActivitySignInandRegistrationBinding by lazy {
        ActivitySignInandRegistrationBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FirebaseAuth
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        //initialize firebase authentication

        auth= FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance()

        val action:String? = intent.getStringExtra("action")
        //adjust visibility for login

        if (action=="Login")  {
            binding.Loginbutton.visibility= View.VISIBLE
            binding.signupname.visibility= View.VISIBLE
            binding.signuppassword.visibility= View.VISIBLE

            binding.logintext.isEnabled=false
            binding.logintext.alpha=0.5f
            binding.Loginbutton.setOnClickListener {
                val login_email=binding.loginemail.text.toString()
                val login_password=binding.loginpassword.text.toString()
                if(login_email.isEmpty()||login_password.isEmpty()){
                    Toast.makeText(this,"Please fill all the details",Toast.LENGTH_SHORT).show()
                }else{
                    auth.signInWithEmailAndPassword(login_email,login_password)
                        .addOnCompleteListener{ task ->
                            if(task.isSuccessful){
                                Toast.makeText(this, "login successful", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this,MainActivity::class.java))
                            }else{
                                Toast.makeText(this, "login failed, enter details again", Toast.LENGTH_SHORT).show()
                            }

                        }
                }
            }

            binding.signupname.visibility=View.GONE
            binding.loginemail.visibility=View.GONE
            binding.loginpassword.visibility=View.GONE
            binding.cardview.visibility=View.GONE


            binding.Signupbutton.isEnabled=false
            binding.Signupbutton.alpha=0.5f
        }

        else if (action=="Signup"){
            binding.Loginbutton.isEnabled=false
            binding.Loginbutton.alpha=0.5f
            binding.Signupbutton.setOnClickListener{
                //get data from edit text field
                val registerName=binding.signupname.text.toString()
                val register_email=binding.signupmail.text.toString()
                val register_password=binding.signuppassword.text.toString()
                if( registerName.isEmpty() || register_email.isEmpty() ||register_password.isEmpty()){
                    Toast.makeText(this, "please fill all the details", Toast.LENGTH_SHORT).show()
                }
                else{
                    auth.createUserWithEmailAndPassword(register_email,register_password)
                        .addOnCompleteListener { task->
                            if(task.isSuccessful){
                                val user = auth.currentUser
                                user?.let {
                                    val userReference = database.getReference("users")
                                    val userId: String = user.uid
                                    val userData = UserData(
                                        name = registerName,
                                        email = register_email,
                                        profileImage = "", // or a real image URL if available
                                        likecount = 0,
                                        imageUrl = ""
                                    )

                                    userReference.child(userId).setValue(userData)
                                        .addOnSuccessListener {
                                            Log.d("TAG", "onCreate: data saved")



                                        }

                                        .addOnFailureListener{ e ->
                                            Log.e("TAG", "onCreate: error saving data ${e.message}")


                                        }
                                    Toast.makeText(this,"User Sign Up Successful",Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, WelcomeActivity::class.java))
                                    finish()
                                }
                            }else{
                                Toast.makeText(this,"User Sign Up Failed",Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}