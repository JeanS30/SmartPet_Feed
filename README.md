# SmartPet Feed

Dispensador inteligente de alimento para mascotas compuesto por una aplicación Android (Kotlin + Jetpack Compose) y un prototipo de hardware en Arduino.

## App Android (`/app`)
- **Arquitectura:** Jetpack Compose, Room (persistencia local) y WorkManager (horarios automáticos).
- **Pantallas:** Inicio (nivel de depósito y dispensado manual), Horarios (programación diaria) e Historial (registro de dispensaciones).
- **Integración desacoplada:** Interfaz `FeederDevice` preparada para alternar entre `FakeFeederDevice` (simulación) y conectividad remota por Wi-Fi (`WifiFeederDevice`).

## Hardware y Simulación (`/hardware`)
- **Simulación interactiva en Tinkercad:** [Abrir circuito SmartPet Feed](https://www.tinkercad.com/things/2XJ93AFh1qv/editel?returnTo=%2Fdashboard&sharecode=V1ueI7SDPG1rWctThpu8e5228ur7zfO1sK5HwQkKO-4)
- **Componentes:** Arduino Uno, servomotor (compuerta), sensor ultrasónico HC-SR04 (nivel del depósito) y LEDs indicadores (verde/rojo).

### Protocolo de Comandos (Monitor Serie)
| Comando | Respuesta | Descripción |
| :--- | :--- | :--- |
| `STATUS` | `nivel=80,estado=OK` / `VACIO` | Consulta el porcentaje actual y estado del depósito. |
| `FEED:30` | `OK:30` / `ERR:VACIO` | Dispensa la cantidad indicada en gramos si hay alimento disponible. |
