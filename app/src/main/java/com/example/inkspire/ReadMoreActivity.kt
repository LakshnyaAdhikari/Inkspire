package com.example.inkspire

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.inkspire.Model.BlogItemModel
import com.example.inkspire.databinding.ActivityReadMoreBinding

class ReadMoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReadMoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityReadMoreBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
//        binding.backButton.setOnClickListener{
//            finish()
//        }
        val blogs = intent.getParcelableExtra<BlogItemModel>("blogItem")
        if(blogs!=null){
            //Retrive user related data here e. x blog title etc.
            binding.titletext.text=blogs.heading
            binding.username.text=blogs.userName
            binding.date.text=blogs.date
            
        }else{
            Toast.makeText(this, "failed to load blog", Toast.LENGTH_SHORT).show()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}