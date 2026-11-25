package com.smartpet.feed.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScheduleScreen(vm: FeederViewModel) {
    val schedules by vm.schedules.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PetColors.Terracotta)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Encabezado
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Horarios",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Alimentación automática diaria",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
            }

            // Cuerpo crema redondeado
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = PetColors.CreamBg,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                if (schedules.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "Toca + para programar un horario",
                            color = PetColors.MutedText,
                            fontSize = 15.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(schedules, key = { it.id }) { s ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 18.dp, vertical = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(Modifier.weight(1f)) {
                                        Text(
                                            text = "%02d:%02d".format(s.hour, s.minute),
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = PetColors.DarkText
                                        )
                                        Text(
                                            text = "Porción: ${s.grams} g",
                                            color = PetColors.MutedText,
                                            fontSize = 14.sp
                                        )
                                    }
                                    Switch(
                                        checked = s.enabled,
                                        onCheckedChange = { vm.toggle(s) },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = PetColors.TealStatus
                                        )
                                    )
                                    IconButton(onClick = { vm.delete(s) }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Eliminar",
                                            tint = PetColors.Terracotta
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            containerColor = PetColors.Terracotta,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Agregar horario")
        }
    }

    if (showDialog) {
        AddScheduleDialog(
            onDismiss = { showDialog = false },
            onAdd = { h, m, g ->
                vm.addSchedule(h, m, g)
                showDialog = false
            }
        )
    }
}

@Composable
private fun AddScheduleDialog(onDismiss: () -> Unit, onAdd: (Int, Int, Int) -> Unit) {
    var h by remember { mutableStateOf("8") }
    var m by remember { mutableStateOf("0") }
    var g by remember { mutableStateOf("30") }
    val hi = h.toIntOrNull()
    val mi = m.toIntOrNull()
    val gi = g.toIntOrNull()
    val valid = hi != null && hi in 0..23 && mi != null && mi in 0..59 && gi != null && gi in 1..500
    val num = KeyboardOptions(keyboardType = KeyboardType.Number)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PetColors.CreamBg,
        title = { Text("Nuevo horario", fontWeight = FontWeight.Bold, color = PetColors.DarkText) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(h, { h = it }, label = { Text("Hora (0-23)") }, singleLine = true, keyboardOptions = num)
                OutlinedTextField(m, { m = it }, label = { Text("Minuto (0-59)") }, singleLine = true, keyboardOptions = num)
                OutlinedTextField(g, { g = it }, label = { Text("Gramos") }, singleLine = true, keyboardOptions = num)
            }
        },
        confirmButton = {
            Button(
                onClick = { onAdd(hi!!, mi!!, gi!!) },
                enabled = valid,
                colors = ButtonDefaults.buttonColors(containerColor = PetColors.Terracotta)
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = PetColors.MutedText) }
        }
    )
}