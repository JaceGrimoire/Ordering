package com.example.ordering

data class RideRequest(var requestId: String,
                       var userID: String,
                       var passengers: Int,
                       var pickupLong: Double,
                       var pickupLat: Double,
                       var destinationLong: Double,
                       var destinationLat: Double,
                       var locDesc: String)
