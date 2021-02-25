package com.example.appTaberna

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

enum class ProviderType{
    BASIC,GOOGLE
}

class HomeActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    var listaProduccto: MutableList<Producto> = mutableListOf()
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

        cargarlista()

        findViewById<Button>(R.id.btPrincipalCliente).setOnClickListener {


            val nombre = findViewById<TextView>(R.id.edtxtName).text.toString()
            val address = findViewById<TextView>(R.id.edtxtAddress).text.toString()
            if (email != null) {
                db.collection("users").document(email).set(
                        hashMapOf("nombre" to nombre,
                                "provider" to provedor,
                                "address" to address)
                )
            }
            val homeIntent = Intent(this, ClientMainActivity::class.java).apply {
               putExtra("email", email)
               putExtra("provedor", provedor)
               startActivity(this)
           }
        }

        //guardado de datos

        val prefs = this.getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE)

        with (prefs.edit()){
            putString("email", email)
            putString("provider", provedor)
                .apply()
        }

    }

    fun cargarlista(){
        val TAG = "App"
        db.collection("productos")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        listaProduccto.add(Producto(document.id,
                                document.get("Descripcion") as String,
                                document.get("Precio") as String))
                        Log.d(TAG, "$listaProduccto")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }

    }

}
