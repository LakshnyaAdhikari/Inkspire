package com.example.inkspire

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.inkspire.databinding.ActivitySignInandRegistrationBinding
import com.example.inkspire.databinding.ActivityWelcomeBinding
import javax.security.auth.login.LoginException

class SignInandRegistrationActivity : AppCompatActivity() {
    private val binding: ActivitySignInandRegistrationBinding by lazy {
        ActivitySignInandRegistrationBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val action:String? = intent.getStringExtra("action")
        //adjust visibility for login

        if (action=="Login")  {
binding.Loginbutton.visibility= View.VISIBLE
            binding.loginname.visibility= View.VISIBLE
            binding.loginpassword.visibility= View.VISIBLE

            binding.logintext.visibility=View.INVISIBLE
            binding.Signupbutton.visibility=View.INVISIBLE





        }

        else if (action=="Signup"){

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}