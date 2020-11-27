package com.example.ordering

data class UserRequest(val uid: String,
                       val username: String,
                       val originLat: Double,
                       val originLong: Double,
                       val destLat: Double,
                       val destLong: Double,
                       val originAddress: String,
                       val destAddress: String)
