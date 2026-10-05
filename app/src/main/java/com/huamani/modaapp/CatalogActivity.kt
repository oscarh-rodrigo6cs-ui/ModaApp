package com.huamani.modaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.huamani.modaapp.databinding.ActivityCatalogBinding

class CatalogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val products = listOf(
            Product("Polo oversize", "Talla M - Azul", 39.90, "Polos"),
            Product("Polo básico", "Talla S - Negro", 29.90, "Polos"),
            Product("Polo estampado", "Talla L - Rojo", 45.00, "Polos"),
            Product("Polo cuello V", "Talla M - Blanco", 32.00, "Polos"),
            Product("Jean slim", "Talla 32 - Gris", 89.90, "Pantalones"),
            Product("Vestido midi", "Talla S - Morado", 119.00, "Vestidos")
        )

        val adapter = CatalogAdapter(products) { product ->
            Toast.makeText(this, "${product.name} agregado al carrito", Toast.LENGTH_SHORT).show()
        }

        binding.rvCatalog.layoutManager = GridLayoutManager(this, 2)
        binding.rvCatalog.adapter = adapter

        binding.btnCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }
}
