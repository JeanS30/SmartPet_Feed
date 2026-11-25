package com.smartpet.feed.ui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(ts: Long): String =
    SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(Date(ts))

