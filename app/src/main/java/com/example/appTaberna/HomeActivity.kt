package com.example.appTaberna

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType{
    BASIC,GOOGLE
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

            //Borrar preferencias
            this.getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE).edit(){
                this.clear()
                this.apply()
            }

            FirebaseAuth.getInstance().signOut()
            this.onBackPressed()
        }

        //guardado de datos

        val prefs = this.getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE)

        with (prefs.edit()){
            putString("email", email)
            putString("provider", provedor)
                .apply()
        }

    }

}
