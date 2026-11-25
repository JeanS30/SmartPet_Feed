package com.smartpet.feed.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HistoryScreen(vm: FeederViewModel) {
    val logs by vm.logs.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PetColors.Terracotta)
    ) {
        // Encabezado
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Historial",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Registro de dispensaciones",
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
            if (logs.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Aún no hay alimentaciones registradas",
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
                    items(logs, key = { it.id }) { l ->
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
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        text = "${l.grams} g · ${l.source.replaceFirstChar { it.uppercase() }}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PetColors.DarkText
                                    )
                                    Text(
                                        text = formatDate(l.timestamp),
                                        fontSize = 13.sp,
                                        color = PetColors.MutedText
                                    )
                                }
                                Surface(
                                    color = if (l.success) PetColors.TealStatus else PetColors.RingRed,
                                    shape = RoundedCornerShape(50)
                                ) {
                                    Text(
                                        text = if (l.success) "OK" else "VACÍO",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}