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

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}