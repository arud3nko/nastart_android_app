package com.example.nastartapplication

data class Product(
    val name:       String,
    val quantity:   Int,
    val price:      Int) {
}

data class Customer(
    val name:       String,
    val phone:      String
)

data class Invoice(
    val id:             Int,
    val products:       List<Product>,
    val price:          Int,
    val paymentType:    String? = null,
    val createdTime:    String,
    val timezone:       String? = null,
    val customerNote:   String? = null
)

data class Order(
    val customer: Customer,
    val invoice: Invoice
)


