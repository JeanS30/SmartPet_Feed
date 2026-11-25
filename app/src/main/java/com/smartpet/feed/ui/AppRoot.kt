package com.smartpet.feed.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

object PetColors {
    val Terracotta = Color(0xFFC2553A)
    val CreamBg = Color(0xFFFFF8F3)
    val SoftPill = Color(0xFFF0EAE6)
    val TealStatus = Color(0xFF1F7A82)
    val RingGreen = Color(0xFF38B03C)
    val RingOrange = Color(0xFFEF6C00)
    val RingRed = Color(0xFFC62828)
    val DarkText = Color(0xFF2D2424)
    val MutedText = Color(0xFF7A6F6D)
}

@Composable
fun AppRoot() {
    val vm: FeederViewModel = viewModel()
    val nav = rememberNavController()
    val route = nav.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        containerColor = PetColors.CreamBg,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                val itemColors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PetColors.Terracotta,
                    selectedTextColor = PetColors.Terracotta,
                    indicatorColor = PetColors.Terracotta.copy(alpha = 0.12f),
                    unselectedIconColor = PetColors.MutedText,
                    unselectedTextColor = PetColors.MutedText
                )
                NavigationBarItem(
                    selected = route == "main",
                    onClick = { nav.navigate("main") { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Home, null) },
                    label = { Text("Inicio") },
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = route == "schedule",
                    onClick = { nav.navigate("schedule") { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.DateRange, null) },
                    label = { Text("Horarios") },
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = route == "history",
                    onClick = { nav.navigate("history") { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Info, null) },
                    label = { Text("Historial") },
                    colors = itemColors
                )
            }
        }
    ) { padding ->
        NavHost(nav, startDestination = "main", modifier = Modifier.padding(padding)) {
            composable("main") { MainScreen(vm) }
            composable("schedule") { ScheduleScreen(vm) }
            composable("history") { HistoryScreen(vm) }
        }
    }
}
