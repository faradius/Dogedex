package com.alex.dogedex.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import com.alex.dogedex.R
import com.alex.dogedex.auth.LoginActivity
import com.alex.dogedex.databinding.ActivitySettingsBinding
import com.alex.dogedex.model.User

@ExperimentalMaterialApi
@ExperimentalFoundationApi
class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        User.logout(this)
        val intent = Intent(this, LoginActivity::class.java)
        //Hace que todas las tareas que esten debajo de esta activity sean borradas
        // y que empiece la nueva activity como nueva tarea
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}