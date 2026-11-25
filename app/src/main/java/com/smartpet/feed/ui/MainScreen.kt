package com.smartpet.feed.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen(vm: FeederViewModel) {
    val status by vm.status.collectAsState()
    val logs by vm.logs.collectAsState()
    val schedules by vm.schedules.collectAsState()
    var grams by remember { mutableIntStateOf(30) }

    LaunchedEffect(Unit) { vm.refresh() }
    val last = logs.firstOrNull()
    val nextSchedule = schedules.firstOrNull { it.enabled }

    val ringColor = when {
        status.level > 50 -> PetColors.RingGreen
        status.level > 20 -> PetColors.RingOrange
        else -> PetColors.RingRed
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PetColors.Terracotta)
            .verticalScroll(rememberScrollState())
    ) {
        // Franja superior de simulación
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(PetColors.CreamBg)
                .padding(vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "MODO SIMULACIÓN: sin hardware conectado",
                color = PetColors.DarkText,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium
            )
        }

        // Encabezado Terracota
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp, bottom = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SmartPet Feed",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "🐶   🐾   🐱",
                fontSize = 24.sp
            )
        }

        // Contenedor principal crema con bordes redondeados arriba
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = PetColors.CreamBg,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Anillo circular de nivel
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { status.level / 100f },
                        modifier = Modifier.size(132.dp),
                        color = ringColor,
                        trackColor = ringColor.copy(alpha = 0.18f),
                        strokeWidth = 12.dp,
                        strokeCap = StrokeCap.Round
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${status.level}%",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PetColors.DarkText
                        )
                        Text(
                            text = "Depósito",
                            fontSize = 12.sp,
                            color = PetColors.MutedText
                        )
                    }
                }

                // Píldora de Estado (Teal o Rojo si está vacío)
                Surface(
                    color = if (status.state == "VACIO") PetColors.RingRed else PetColors.TealStatus,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = if (status.state == "VACIO") "Estado: VACÍO" else "Estado: ${status.state}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 28.dp, vertical = 10.dp)
                    )
                }

                // Píldora de última comida
                Surface(
                    color = PetColors.SoftPill,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (last == null) "Última comida: sin registros"
                        else "Última comida: ${formatDate(last.timestamp)} ·${last.grams} g",
                        color = PetColors.DarkText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }

                // Selector de porción (- 30 g +)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    FilledTonalIconButton(
                        onClick = { if (grams > 10) grams -= 10 },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = PetColors.SoftPill,
                            contentColor = PetColors.DarkText
                        ),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Text("−", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }

                    Text(
                        text = "$grams g",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PetColors.DarkText
                    )

                    FilledTonalIconButton(
                        onClick = { if (grams < 200) grams += 10 },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = PetColors.SoftPill,
                            contentColor = PetColors.DarkText
                        ),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Text("+", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Botón principal Terracota
                Button(
                    onClick = { vm.feedNow(grams, "manual") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PetColors.Terracotta,
                        contentColor = Color.White
                    )
                ) {
                    Text("Dispensar ahora", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                // Tarjeta inferior de Próxima comida / Simular horario
                Surface(
                    color = PetColors.Terracotta,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Próxima comida:",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 13.sp
                            )
                            Text(
                                text = if (nextSchedule != null)
                                    "Hoy %02d:%02d · %d g".format(
                                        nextSchedule.hour,
                                        nextSchedule.minute,
                                        nextSchedule.grams
                                    )
                                else "Sin horarios activos",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        if (nextSchedule != null) {
                            Switch(
                                checked = nextSchedule.enabled,
                                onCheckedChange = { vm.toggle(nextSchedule) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = PetColors.TealStatus
                                )
                            )
                        } else {
                            TextButton(
                                onClick = { vm.feedNow(grams, "horario") },
                                colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                            ) {
                                Text("Simular", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}