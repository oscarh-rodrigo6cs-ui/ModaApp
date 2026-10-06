package com.huamani.modaapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.huamani.modaapp.databinding.ActivityAdminPedidosBinding

class AdminPedidosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminPedidosBinding
    private lateinit var dbHelper: DbHelper
    private lateinit var adapter: AdminPedidosAdapter
    private var currentFilter = "PENDIENTE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DbHelper(this)

        binding.toolbarAdminPedidos.setNavigationOnClickListener {
            finish()
        }

        setupRecyclerView()
        setupTabs()
        loadPedidos()
    }

    private fun setupRecyclerView() {
        adapter = AdminPedidosAdapter(emptyList()) { pedido ->
            dbHelper.actualizarEstadoPedido(pedido.id, "ATENDIDO")
            Toast.makeText(this, "Pedido #${pedido.id} marcado como atendido", Toast.LENGTH_SHORT).show()
            loadPedidos()
        }
        binding.rvPedidos.layoutManager = LinearLayoutManager(this)
        binding.rvPedidos.adapter = adapter
    }

    private fun setupTabs() {
        binding.btnPendientes.setOnClickListener {
            currentFilter = "PENDIENTE"
            binding.btnPendientes.backgroundTintList = getColorStateList(R.color.primary)
            binding.btnPendientes.setTextColor(getColor(R.color.white))
            binding.btnAtendidos.backgroundTintList = getColorStateList(R.color.gray_light)
            binding.btnAtendidos.setTextColor(getColor(R.color.text_primary))
            loadPedidos()
        }

        binding.btnAtendidos.setOnClickListener {
            currentFilter = "ATENDIDO"
            binding.btnAtendidos.backgroundTintList = getColorStateList(R.color.primary)
            binding.btnAtendidos.setTextColor(getColor(R.color.white))
            binding.btnPendientes.backgroundTintList = getColorStateList(R.color.gray_light)
            binding.btnPendientes.setTextColor(getColor(R.color.text_primary))
            loadPedidos()
        }
    }

    private fun loadPedidos() {
        val list = dbHelper.obtenerPedidos(currentFilter)
        adapter.updateList(list)
    }
}
