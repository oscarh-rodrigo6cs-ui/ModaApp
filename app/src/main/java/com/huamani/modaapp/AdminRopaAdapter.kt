package com.huamani.modaapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huamani.modaapp.databinding.ItemAdminRopaBinding

class AdminRopaAdapter(
    private var list: List<DbHelper.AdminProduct>,
    private val onDeleteClick: (DbHelper.AdminProduct) -> Unit
) : RecyclerView.Adapter<AdminRopaAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemAdminRopaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdminRopaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        with(holder.binding) {
            tvAdminModelo.text = item.modelo
            tvAdminDetails.text = "${item.categoria} · Talla ${item.talla} · ${item.color} · Stock: ${item.cantidad}"
            tvAdminPrecio.text = "S/ %.2f".format(item.precio)

            btnDeleteRopa.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<DbHelper.AdminProduct>) {
        list = newList
        notifyDataSetChanged()
    }
}
