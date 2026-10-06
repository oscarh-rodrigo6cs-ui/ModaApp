package com.huamani.modaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.huamani.modaapp.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCartList()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnCheckout.setOnClickListener {
            if (CartManager.getItems().isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, DatosPedidoActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshCart()
    }

    private fun setupCartList() {
        cartAdapter = CartAdapter(CartManager.getItems()) { item ->
            CartManager.removeItem(item)
            refreshCart()
            Toast.makeText(this, "${item.productName} eliminado", Toast.LENGTH_SHORT).show()
        }
        binding.rvCartItems.layoutManager = LinearLayoutManager(this)
        binding.rvCartItems.adapter = cartAdapter
    }

    private fun refreshCart() {
        cartAdapter.notifyDataSetChanged()
        binding.tvTotalPrice.text = "S/ %.2f".format(CartManager.getTotal())
    }
}
