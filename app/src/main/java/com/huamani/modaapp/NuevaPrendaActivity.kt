package com.huamani.modaapp

import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.huamani.modaapp.databinding.ActivityNuevaPrendaBinding

class NuevaPrendaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNuevaPrendaBinding
    private lateinit var dbHelper: DbHelper
    private var selectedImageUri: String = ""

    private val getContentLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it.toString()
            binding.ivPrendaFoto.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuevaPrendaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DbHelper(this)

        setupSpinners()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnCambiarFoto.setOnClickListener {
            getContentLauncher.launch("image/*")
        }

        binding.btnGuardar.setOnClickListener {
            guardarPrenda()
        }
    }

    private fun setupSpinners() {
        val categorias = dbHelper.obtenerCategorias()
        val catAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias)
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategoria.adapter = catAdapter

        val tallas = arrayOf("XS", "S", "M", "L", "XL")
        val tallaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tallas)
        tallaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTalla.adapter = tallaAdapter
    }

    private fun guardarPrenda() {
        val categoria = binding.spinnerCategoria.selectedItem?.toString() ?: "Polos"
        val talla = binding.spinnerTalla.selectedItem?.toString() ?: "M"
        val modelo = binding.etModelo.text.toString().trim()
        val marca = binding.etMarca.text.toString().trim()
        val color = binding.etColor.text.toString().trim()
        val cantidadStr = binding.etCantidad.text.toString().trim()
        val precioStr = binding.etPrecio.text.toString().trim()

        if (modelo.isEmpty() || precioStr.isEmpty() || cantidadStr.isEmpty()) {
            Toast.makeText(this, "Complete los campos obligatorios (Modelo, Precio, Cantidad)", Toast.LENGTH_SHORT).show()
            return
        }

        val cantidad = cantidadStr.toIntOrNull() ?: 0
        val precio = precioStr.toDoubleOrNull() ?: 0.0

        if (precio <= 0) {
            Toast.makeText(this, "El precio debe ser mayor a 0", Toast.LENGTH_SHORT).show()
            return
        }

        val id = dbHelper.insertarRopa(modelo, categoria, talla, marca, color, precio, cantidad, selectedImageUri)
        if (id > -1) {
            Toast.makeText(this, "¡Prenda registrada con éxito!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al registrar la prenda", Toast.LENGTH_SHORT).show()
        }
    }
}
