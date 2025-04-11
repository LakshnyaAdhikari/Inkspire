package register

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.inkspire.MainActivity
import com.example.inkspire.R
import com.example.inkspire.SignInandRegistrationActivity
import com.example.inkspire.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private val binding: ActivityWelcomeBinding by lazy {
        ActivityWelcomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth=FireBaseAuth.getInstance()
        enableEdgeToEdge()
        setContentView(binding.root)
        private lateinit var auth:FireBaseAuth


        binding.LoginButton.setOnClickListener{
           val intent=Intent(this,SignInandRegistrationActivity::class.java)
            intent.putExtra("action", "Login")

            startActivity(intent)
            finish()

        }

        binding.SignUpButton.setOnClickListener{
            val intent=Intent(this,SignInandRegistrationActivity::class.java)
            intent.putExtra("action", "Signup")

            startActivity(intent)
            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    override fun onstart() {
        super.onStart()
        val currentUser: FirebaseUser? = auth.currentUser

        if(currentUser != null){
            startActivity(Intent(packageContext:this, MainActivity::class.java))
            finish()
        }
    }
}
