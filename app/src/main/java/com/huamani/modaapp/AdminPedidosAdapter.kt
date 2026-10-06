package com.huamani.modaapp

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huamani.modaapp.databinding.ItemAdminPedidoBinding

class AdminPedidosAdapter(
    private var pedidos: List<DbHelper.Pedido>,
    private val onAtenderClick: (DbHelper.Pedido) -> Unit
) : RecyclerView.Adapter<AdminPedidosAdapter.PedidoViewHolder>() {

    inner class PedidoViewHolder(val binding: ItemAdminPedidoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val binding = ItemAdminPedidoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PedidoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.binding.tvPedidoId.text = "#${pedido.id}"
        holder.binding.tvPedidoFecha.text = pedido.fecha
        holder.binding.tvClienteNombres.text = pedido.clienteNombres
        holder.binding.tvClienteTelefono.text = pedido.clienteTelefono
        holder.binding.tvTotal.text = "Total: S/ %.2f".format(pedido.total)

        if (pedido.estado == "PENDIENTE") {
            holder.binding.btnAtender.visibility = View.VISIBLE
            holder.binding.btnAtender.setOnClickListener {
                onAtenderClick(pedido)
            }
        } else {
            holder.binding.btnAtender.visibility = View.GONE
        }

        holder.binding.btnLlamar.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${pedido.clienteTelefono}")
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = pedidos.size

    fun updateList(newList: List<DbHelper.Pedido>) {
        pedidos = newList
        notifyDataSetChanged()
    }
}
