package com.huamani.modaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.huamani.modaapp.databinding.ActivityAdminMenuBinding

class AdminMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardRopa.setOnClickListener {
            val intent = Intent(this, NuevaPrendaActivity::class.java)
            startActivity(intent)
        }

        binding.cardPedidos.setOnClickListener {
            Toast.makeText(this, "Sección Pedidos (Próximamente)", Toast.LENGTH_SHORT).show()
        }

        binding.cardClientes.setOnClickListener {
            Toast.makeText(this, "Sección Clientes (Próximamente)", Toast.LENGTH_SHORT).show()
        }

        binding.cardReportes.setOnClickListener {
            Toast.makeText(this, "Sección Reportes (Próximamente)", Toast.LENGTH_SHORT).show()
        }

        binding.btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
