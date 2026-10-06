package com.huamani.modaapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.huamani.modaapp.databinding.ActivityDatosPedidoBinding

class DatosPedidoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDatosPedidoBinding
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDatosPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DbHelper(this)

        setupSummary()
        setupPhoneWatcher()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnConfirmarPedido.setOnClickListener {
            confirmarPedido()
        }
    }

    private fun setupSummary() {
        val totalItems = CartManager.getItems().sumOf { it.quantity }
        val totalAmount = CartManager.getTotal()
        binding.tvResumenCantidad.text = "$totalItems prendas"
        binding.tvResumenTotal.text = "S/ %.2f".format(totalAmount)
    }

    private fun setupPhoneWatcher() {
        binding.etTelefono.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val telefono = s.toString().trim()
                if (telefono.length == 9) {
                    val cliente = dbHelper.buscarClientePorTelefono(telefono)
                    if (cliente != null) {
                        binding.etNombres.setText(cliente["nombres"])
                        binding.etApellidos.setText(cliente["apellidos"])
                        binding.etNombres.isEnabled = false
                        binding.etApellidos.isEnabled = false
                        binding.tvEstadoCliente.text = "Cliente registrado (Encontrado)"
                    } else {
                        binding.etNombres.setText("")
                        binding.etApellidos.setText("")
                        binding.etNombres.isEnabled = true
                        binding.etApellidos.isEnabled = true
                        binding.tvEstadoCliente.text = "Número nuevo: completa tus datos"
                    }
                } else {
                    binding.tvEstadoCliente.text = "Ingrese teléfono de 9 dígitos"
                }
            }
        })
    }

    private fun confirmarPedido() {
        val telefono = binding.etTelefono.text.toString().trim()
        val nombres = binding.etNombres.text.toString().trim()
        val apellidos = binding.etApellidos.text.toString().trim()

        if (telefono.length != 9 || nombres.isEmpty() || apellidos.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos del cliente", Toast.LENGTH_SHORT).show()
            return
        }

        if (CartManager.getItems().isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            return
        }

        val total = CartManager.getTotal()
        val pedidoId = dbHelper.registrarPedidoCompleto(telefono, nombres, apellidos, total, CartManager.getItems())

        if (pedidoId > 0) {
            Toast.makeText(this, "¡Pedido #$pedidoId registrado con éxito!", Toast.LENGTH_LONG).show()

            // Construct WhatsApp Message
            val sb = java.lang.StringBuilder()
            sb.append("Hola, he realizado un nuevo pedido en ModaApp.\n")
            sb.append("Número de pedido: *$pedidoId*\n")
            sb.append("Cliente: $nombres $apellidos\n")
            sb.append("Teléfono: $telefono\n")
            sb.append("Estado: *PENDIENTE*\n\n")
            sb.append("Detalle:\n")
            for (item in CartManager.getItems()) {
                sb.append("- ${item.quantity}x ${item.productName} (${item.details}) = S/ ${item.subtotal}\n")
            }
            sb.append("\n*Total: S/ $total*")

            CartManager.clear()

            // Open WhatsApp Intent
            val adminNumber = "+51987654321" // Default admin number for Peru
            val intentWsp = Intent(Intent.ACTION_VIEW)
            intentWsp.data = android.net.Uri.parse("https://api.whatsapp.com/send?phone=$adminNumber&text=" + java.net.URLEncoder.encode(sb.toString(), "UTF-8"))
            
            try {
                startActivity(intentWsp)
            } catch (e: Exception) {
                Toast.makeText(this, "No tienes WhatsApp instalado o no se puede abrir", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, CatalogActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            finish()
        } else {
            Toast.makeText(this, "Error al registrar el pedido", Toast.LENGTH_SHORT).show()
        }
    }
}
