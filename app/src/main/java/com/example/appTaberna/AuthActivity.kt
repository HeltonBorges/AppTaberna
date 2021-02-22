package com.example.appTaberna

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 100
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
                        if (it.isSuccessful) {
                            goHome(it.result?.user?.email?:"", ProviderType.BASIC)
                        }
                        else {
                            showAlert()
                        }
                    }
            }
        }
        findViewById<Button>(R.id.bt_Login).setOnClickListener {
            if(email.text.isNotEmpty() && password.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.text.toString(),
                    password.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            goHome(it.result?.user?.email?:"", ProviderType.BASIC)
                        }
                        else {
                            showAlert()
                        }
                    }
            }
        }
        findViewById<Button>(R.id.btGoogleSignin).setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }

        session()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            goHome(account.email ?: " ", ProviderType.GOOGLE)
                        } else {
                            showAlert()
                        }
                    }
                }
            } catch (e:ApiException){
                showAlert()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        findViewById<ConstraintLayout>(R.id.authLayout).visibility = View.VISIBLE
    }

    private fun showAlert() {
        Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show()
    }

    private fun goHome(email:String, provedor: ProviderType) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provedor", provedor.name)
            startActivity(this)
        }
    }

    private fun session() {
        val prefs = this.getSharedPreferences(getString(R.string.prefs_file), MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provedor = prefs.getString("provider", null)
        if(email != null && provedor != null){
            findViewById<ConstraintLayout>(R.id.authLayout).visibility = View.INVISIBLE
            goHome(email, ProviderType.valueOf(provedor))
        }

    }
}