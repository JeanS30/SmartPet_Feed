package com.smartpet.feed.device

class FakeFeederDevice : FeederDevice {
    private var level = 100

    override suspend fun feed(grams: Int): Result<Int> {
        if (level <= 0) return Result.failure(Exception("VACIO"))
        level = (level - grams / 3).coerceAtLeast(0)
        return Result.success(grams)
    }

    override suspend fun status() =
        DeviceStatus(level, if (level > 0) "OK" else "VACIO")
}