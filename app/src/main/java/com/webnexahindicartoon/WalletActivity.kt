package com.webnexahindicartoon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.webnexahindicartoon.databinding.ActivityEarnHomeBinding
import com.webnexahindicartoon.databinding.ActivityWalletBinding

class WalletActivity : AppCompatActivity() {

lateinit var binding: ActivityWalletBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityWalletBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this,R.color.Earn);

        binding.back.setOnClickListener {
            finish()
        }

    }
}