package com.huamani.modaapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.huamani.modaapp.databinding.ItemCartBinding

class CartAdapter(
    private val cartItems: List<CartItem>,
    private val onItemLongClick: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        with(holder.binding) {
            tvCartItemName.text = item.productName
            tvCartItemDetails.text = item.details
            tvCartItemPrice.text = item.priceText
            root.setOnLongClickListener {
                onItemLongClick(item)
                true
            }
        }
    }

    override fun getItemCount(): Int = cartItems.size
}
