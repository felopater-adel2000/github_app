package com.felo.github_app.ui.auth

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.felo.github_app.R
import com.felo.github_app.base.BaseActivity
import com.felo.github_app.databinding.ActivityAuthBinding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class AuthActivity : BaseActivity() {
    private val TAG = "AuthActivity"
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        printKeyHash()
    }

    private fun printKeyHash()
    {
        try {
            val info = packageManager.getPackageInfo("com.felo.github_app", PackageManager.GET_SIGNATURES)

            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }


    override fun showProgressBar(show: Boolean) {

    }

    override fun showErrorUI(
        show: Boolean,
        image: Int?,
        title: String?,
        desc: String?,
        isAuthenticated: Boolean?,
        showButton: Boolean?
    ) {

    }
}