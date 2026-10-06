package com.huamani.modaapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.huamani.modaapp.databinding.ActivityAdminRopaBinding

class AdminRopaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminRopaBinding
    private lateinit var dbHelper: DbHelper
    private lateinit var adapter: AdminRopaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminRopaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DbHelper(this)

        setupList()
        setupSearch()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnNuevaPrendaAdmin.setOnClickListener {
            val intent = Intent(this, NuevaPrendaActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadRopa("")
    }

    private fun setupList() {
        val lista = dbHelper.obtenerTodasLasPrendas("")
        adapter = AdminRopaAdapter(lista) { prenda ->
            // Delete action
            dbHelper.eliminarPrenda(prenda.id)
            Toast.makeText(this, "${prenda.modelo} eliminada", Toast.LENGTH_SHORT).show()
            loadRopa(binding.etSearchRopa.text.toString().trim())
        }
        binding.rvAdminRopa.layoutManager = LinearLayoutManager(this)
        binding.rvAdminRopa.adapter = adapter
    }

    private fun loadRopa(query: String) {
        val lista = dbHelper.obtenerTodasLasPrendas(query)
        adapter.updateList(lista)
    }

    private fun setupSearch() {
        binding.etSearchRopa.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                loadRopa(s.toString().trim())
            }
        })
    }
}
