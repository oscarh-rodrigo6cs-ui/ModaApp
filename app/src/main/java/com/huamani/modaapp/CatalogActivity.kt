package com.huamani.modaapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.huamani.modaapp.databinding.ActivityCatalogBinding

class CatalogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogBinding
    private lateinit var dbHelper: DbHelper
    private var currentCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DbHelper(this)

        setupCategoryFilters()
        loadProducts(null)

        binding.btnCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload products whenever returning to catalog (e.g. after adding new prenda)
        loadProducts(currentCategory)
    }

    private fun loadProducts(categoria: String?) {
        val products = dbHelper.obtenerRopa(categoria)
        val adapter = CatalogAdapter(products) { product ->
            Toast.makeText(this, "${product.name} agregado al carrito", Toast.LENGTH_SHORT).show()
        }
        binding.rvCatalog.layoutManager = GridLayoutManager(this, 2)
        binding.rvCatalog.adapter = adapter
    }

    private fun setupCategoryFilters() {
        val categoryViews = listOf(
            Pair(binding.tvCatTodas, null),
            Pair(binding.tvCatPolos, "Polos"),
            Pair(binding.tvCatPantalones, "Pantalones"),
            Pair(binding.tvCatVestidos, "Vestidos")
        )

        for ((textView, catName) in categoryViews) {
            textView.setOnClickListener {
                currentCategory = catName
                loadProducts(catName)
                updateCategoryUI(textView, categoryViews.map { it.first })
            }
        }
    }

    private fun updateCategoryUI(selectedView: TextView, allViews: List<TextView>) {
        for (view in allViews) {
            if (view == selectedView) {
                view.setBackgroundResource(R.drawable.bg_button_primary)
                view.setTextColor(getColor(R.color.white))
                view.setTypeface(null, android.graphics.Typeface.BOLD)
            } else {
                view.setBackgroundResource(R.drawable.bg_card)
                view.setTextColor(getColor(R.color.text_primary))
                view.setTypeface(null, android.graphics.Typeface.NORMAL)
            }
        }
    }
}
