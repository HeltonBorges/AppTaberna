package com.example.appbarproyect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType{
    BASIC
}

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bundle:Bundle? = intent.extras
        val email = bundle?.getString("email")
        val provedor = bundle?.getString("provedor")

        findViewById<TextView>(R.id.emailTextView).text = email
        findViewById<TextView>(R.id.ProvedorTextView).text = provedor

        findViewById<Button>(R.id.btLogOut).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            this.onBackPressed()
        }
    }
}