package com.example.inkspire

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.inkspire.databinding.ActivitySignInandRegistrationBinding
import com.example.inkspire.databinding.ActivityWelcomeBinding
import java.net.Authenticator
import javax.security.auth.login.LoginException

class SignInandRegistrationActivity : AppCompatActivity() {
    private val binding: ActivitySignInandRegistrationBinding by lazy {
        ActivitySignInandRegistrationBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FireBaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        //initialize firebase authentication

        auth= FireBaseAuth.getInstance()

        val action:String? = intent.getStringExtra("action")
        //adjust visibility for login

        if (action=="Login")  {
binding.Loginbutton.visibility= View.VISIBLE
            binding.loginname.visibility= View.VISIBLE
            binding.loginpassword.visibility= View.VISIBLE

            binding.logintext.isEnabled=false
            binding.logintext.alpha=0.5f

            binding.loginname.visibility=View.GONE
            binding.registeredemail.visibility=View.GONE
            binding.registeredpassword.visibility=View.GONE
            binding.cardview.visibility=View.GONE


            binding.Signupbutton.isEnabled=false
            binding.Signupbutton.alpha=0.5f








        }

        else if (action=="Signup"){
            binding.Loginbutton.isEnabled=false
            binding.Loginbutton.alpha=0.5f
            binding.registrationbutton.setonClickListener{
                //get data from edit text field
                val registerName=binding.registerName.text.toString()
                val register_email=binding.registerName.text.toString()
                val register_password=binding.registerName.text.toString()
                if( registerName.isEmpty() || register_email.isEmpty() ||register_password.isEmpty()){
                    Toast.makeText(this, text:"please fill all the details", Toast.LENGTH_SHORT).show()
                }
                else{
                    auth.createUserWithEmailAndPassword(register_email,register_password)
                        .addOnCompleteListener { task->
                            if(task.issuccessful){

                            }
                            else{

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