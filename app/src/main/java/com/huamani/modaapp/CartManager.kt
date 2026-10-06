package com.huamani.modaapp

object CartManager {
    private val items = mutableListOf<CartItem>()

    fun addItem(product: Product) {
        val existing = items.find { it.productName == product.name && it.details == product.details }
        if (existing != null) {
            existing.quantity++
        } else {
            items.add(CartItem(product.name, product.details, product.price, 1, product.foto))
        }
    }

    fun removeItem(item: CartItem) {
        items.remove(item)
    }

    fun getItems(): List<CartItem> = items

    fun getTotal(): Double = items.sumOf { it.subtotal }

    fun clear() {
        items.clear()
    }
}
