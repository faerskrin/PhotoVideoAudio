package com.example.wsapp1.model

data class DataX(
        val id: Int,
        val name: String,
        val photos: List<IMG>,
        val status: String,
        val vin: String
)

data class IMG (
        val auto_id: Int,
        val id: Int,
        val url : String

)