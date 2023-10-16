package com.webnexahindicartoon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.webnexahindicartoon.databinding.ActivityEarnHomeBinding
import com.webnexahindicartoon.databinding.ActivityPlayerBinding

class EarnHomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityEarnHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEarnHomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.Earn);

        binding.daily.setOnClickListener {

            Toast.makeText(this, "Daily Bonus claim !", Toast.LENGTH_SHORT).show()
        }
        binding.with.setOnClickListener {

            val intent = Intent(this, WalletActivity::class.java)
            startActivity(intent)
        }

    }
}