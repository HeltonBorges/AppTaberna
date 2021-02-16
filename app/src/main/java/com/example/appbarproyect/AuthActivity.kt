package com.example.appbarproyect

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integración de firebase")
        analytics.logEvent("InitScreen", bundle)

        //autentificación
        var email = findViewById<EditText>(R.id.editTextTextEmailAddress)
        var password = findViewById<EditText>(R.id.editTextTextPassword)
        findViewById<Button>(R.id.bt_SingUP).setOnClickListener {
            if(email.text.isNotEmpty() && password.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.text.toString(),
                    password.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) goHome(it.result?.user?.email?:"", ProviderType.BASIC)
                        else showAlert()
                    }
            }
        }
        findViewById<Button>(R.id.bt_Login).setOnClickListener {
            if(email.text.isNotEmpty() && password.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.text.toString(),
                    password.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) goHome(it.result?.user?.email?:"", ProviderType.BASIC)
                        else showAlert()
                    }
            }
        }


    }

    private fun showAlert() {
        Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT)
    }

    private fun goHome(email:String, provedor:ProviderType) {
        val homeIntent = Intent(this,HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provedor", provedor.name)
        }
    }
}