package com.huamani.modaapp

data class CartItem(
    val productName: String,
    val details: String,
    val price: Double,
    var quantity: Int,
    val foto: String
) {
    val subtotal: Double
        get() = price * quantity
}
