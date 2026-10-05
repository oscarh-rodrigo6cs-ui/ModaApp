package com.huamani.modaapp

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huamani.modaapp.databinding.ItemCatalogBinding

class CatalogAdapter(
    private val products: List<Product>,
    private val onAddClick: (Product) -> Unit
) : RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder>() {

    class CatalogViewHolder(val binding: ItemCatalogBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        val binding = ItemCatalogBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CatalogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        val product = products[position]
        with(holder.binding) {
            tvProductName.text = product.name
            tvProductDetails.text = product.details
            tvProductPrice.text = "S/ %.2f".format(product.price)

            if (product.foto.isNotEmpty()) {
                try {
                    ivProductPhoto.setImageURI(Uri.parse(product.foto))
                } catch (e: Exception) {
                    ivProductPhoto.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            } else {
                ivProductPhoto.setImageResource(android.R.drawable.ic_menu_gallery)
            }

            btnAdd.setOnClickListener {
                onAddClick(product)
            }
        }
    }

    override fun getItemCount(): Int = products.size
}
