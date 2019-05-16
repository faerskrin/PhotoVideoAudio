package com.example.wsapp1.model

data class GetAutoFromUser(
        val `data`: List<DataX>,
        val method: String,
        val status: String
)