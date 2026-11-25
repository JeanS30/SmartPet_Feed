package com.smartpet.feed.device

data class DeviceStatus(val level: Int, val state: String) // ej. 65, "OK"

interface FeederDevice {
    suspend fun feed(grams: Int): Result<Int>   // éxito = "OK:30"
    suspend fun status(): DeviceStatus
}