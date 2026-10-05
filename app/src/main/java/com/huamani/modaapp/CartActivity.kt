package com.huamani.modaapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.huamani.modaapp.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cartItems = listOf(
            CartItem("Polo oversize - M", "Azul - 2 x S/ 39.90", "S/ 79.80", 79.80),
            CartItem("Jean slim - 32", "Gris - 1 x S/ 89.90", "S/ 89.90", 89.90),
            CartItem("Vestido midi - S", "Morado - 1 x S/ 119.00", "S/ 119.00", 119.00)
        )

        val adapter = CartAdapter(cartItems) { item ->
            Toast.makeText(this, "${item.productName} eliminado del carrito", Toast.LENGTH_SHORT).show()
        }

        binding.rvCartItems.layoutManager = LinearLayoutManager(this)
        binding.rvCartItems.adapter = adapter
        binding.tvTotalPrice.text = "S/ 288.70"

        binding.btnCheckout.setOnClickListener {
            Toast.makeText(this, "¡Pedido realizado con éxito!", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
