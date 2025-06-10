package com.example.lab12

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lab12.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*

@Composable
fun MapScreen() {
    // Posiciones
    val arequipa = LatLng(-16.4040102, -71.559611)
    val ciudadCercana = LatLng(-16.389, -71.530)
    val otraPosicion = LatLng(-16.370, -71.580)
    val yura = LatLng(-16.2520984, -71.6836503)
    val routePoints = listOf(arequipa, ciudadCercana, otraPosicion, yura)

    // Estado de la cámara
    val cameraPositionState = rememberCameraPositionState()

    // Estado para tipo de mapa
    var mapType by remember { mutableStateOf(MapType.NORMAL) }

    // Opciones de tipo de mapa
    val mapTypes = listOf("Normal", "Hybrid", "Terrain", "Satellite")
    var expanded by remember { mutableStateOf(false) }
    var selectedMapType by remember { mutableStateOf("Normal") }

    // Animar la cámara hacia Yura al iniciar
    LaunchedEffect(Unit) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(yura, 12f),
            durationMs = 3000
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // 🔽 Menú desplegable para elegir el tipo de mapa
        Box(modifier = Modifier.padding(16.dp)) {
            OutlinedButton(onClick = { expanded = true }) {
                Text("Tipo de mapa: $selectedMapType")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                mapTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            selectedMapType = type
                            mapType = when (type) {
                                "Normal" -> MapType.NORMAL
                                "Hybrid" -> MapType.HYBRID
                                "Terrain" -> MapType.TERRAIN
                                "Satellite" -> MapType.SATELLITE
                                else -> MapType.NORMAL
                            }
                            expanded = false
                        }
                    )
                }
            }
        }

        // 🗺️ Mapa con marcadores, rutas, polígonos y tipo de mapa dinámico
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapType = mapType)
        ) {
            // Marcadores
            Marker(
                state = rememberMarkerState(position = arequipa),
                icon = BitmapDescriptorFactory.fromResource(R.drawable.mountain_icon),
                title = "Arequipa centro"
            )
            Marker(state = rememberMarkerState(position = ciudadCercana), title = "Ciudad cercana")
            Marker(state = rememberMarkerState(position = otraPosicion), title = "Otra posición")
            Marker(state = rememberMarkerState(position = yura), title = "Yura")

            // Polilínea directa (verde)
            Polyline(
                points = listOf(arequipa, yura),
                color = Color.Green,
                width = 8f
            )

            // Ruta con múltiples paradas (azul)
            Polyline(
                points = routePoints,
                color = Color.Blue,
                width = 6f,
                jointType = JointType.ROUND,
                startCap = RoundCap(),
                endCap = RoundCap()
            )

            // Polígonos turísticos
            val plazaDeArmas = listOf(
                LatLng(-16.398866, -71.536961),
                LatLng(-16.398744, -71.536529),
                LatLng(-16.399178, -71.536289),
                LatLng(-16.399299, -71.536721)
            )
            val parqueLambramani = listOf(
                LatLng(-16.422704, -71.530830),
                LatLng(-16.422920, -71.531340),
                LatLng(-16.423264, -71.531110),
                LatLng(-16.423050, -71.530600)
            )
            val mallAventura = listOf(
                LatLng(-16.432292, -71.509145),
                LatLng(-16.432757, -71.509626),
                LatLng(-16.433013, -71.509310),
                LatLng(-16.432566, -71.508853)
            )

            Polygon(
                points = plazaDeArmas,
                strokeColor = Color.Red,
                fillColor = Color.Cyan.copy(alpha = 0.3f),
                strokeWidth = 5f
            )
            Polygon(
                points = parqueLambramani,
                strokeColor = Color.Magenta,
                fillColor = Color.Green.copy(alpha = 0.3f),
                strokeWidth = 5f
            )
            Polygon(
                points = mallAventura,
                strokeColor = Color.Yellow,
                fillColor = Color.Blue.copy(alpha = 0.3f),
                strokeWidth = 5f
            )
        }
    }
}
