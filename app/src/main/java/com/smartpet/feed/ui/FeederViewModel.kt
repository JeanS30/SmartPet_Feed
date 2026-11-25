package com.smartpet.feed.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smartpet.feed.data.AppDatabase
import com.smartpet.feed.data.FeedingLog
import com.smartpet.feed.data.Schedule
import com.smartpet.feed.device.DeviceStatus
import com.smartpet.feed.device.ServiceLocator
import com.smartpet.feed.work.cancelFeed
import com.smartpet.feed.work.scheduleFeed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FeederViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.get(app).dao()
    private val device = ServiceLocator.device

    val schedules = dao.schedules()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val logs = dao.logs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _status = MutableStateFlow(DeviceStatus(100, "OK"))
    val status: StateFlow<DeviceStatus> = _status

    init { refresh() }

    fun refresh() {
        viewModelScope.launch { _status.value = device.status() }
    }

    fun feedNow(grams: Int = 30, source: String = "manual") {
        viewModelScope.launch {
            val r = device.feed(grams)
            dao.insertLog(
                FeedingLog(
                    timestamp = System.currentTimeMillis(),
                    grams = grams,
                    success = r.isSuccess,
                    source = source
                )
            )
            _status.value = device.status()
        }
    }

    fun addSchedule(hour: Int, minute: Int, grams: Int) {
        viewModelScope.launch {
            val id = dao.insert(Schedule(hour = hour, minute = minute, grams = grams)).toInt()
            scheduleFeed(getApplication(), Schedule(id, hour, minute, grams, true))
        }
    }

    fun toggle(s: Schedule) {
        viewModelScope.launch {
            val n = s.copy(enabled = !s.enabled)
            dao.update(n)
            if (n.enabled) scheduleFeed(getApplication(), n) else cancelFeed(getApplication(), n.id)
        }
    }

    fun delete(s: Schedule) {
        viewModelScope.launch {
            cancelFeed(getApplication(), s.id)
            dao.delete(s)
        }
    }
}
