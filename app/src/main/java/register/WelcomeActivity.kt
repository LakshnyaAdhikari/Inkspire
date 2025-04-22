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
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.auth.FirebaseUser


    class WelcomeActivity : AppCompatActivity() {
        private val binding: ActivityWelcomeBinding by lazy {
            ActivityWelcomeBinding.inflate(layoutInflater)
        }

        private lateinit var auth:FirebaseAuth


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            auth=FirebaseAuth.getInstance()
            enableEdgeToEdge()
            setContentView(binding.root)


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
         override fun onStart() {
            super.onStart()
            val currentUser: FirebaseUser? = auth.currentUser

            if(currentUser != null){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
