package com.huamani.modaapp

data class Product(
    val name: String,
    val details: String,
    val price: Double,
    val category: String,
    val foto: String = ""
)
