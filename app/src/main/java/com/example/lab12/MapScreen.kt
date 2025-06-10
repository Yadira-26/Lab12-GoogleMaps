package com.example.lab12

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.lab12.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*

@Composable
fun MapScreen() {
    // Ubicaciones
    val arequipa = LatLng(-16.4040102, -71.559611)
    val ciudadCercana = LatLng(-16.389, -71.530)
    val otraPosicion = LatLng(-16.370, -71.580)
    val yura = LatLng(-16.2520984, -71.6836503)

    val cameraPositionState = rememberCameraPositionState()

    // Ruta con múltiples paradas
    val routePoints = listOf(arequipa, ciudadCercana, otraPosicion, yura)

    // Animar cámara hacia Yura
    LaunchedEffect(Unit) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(yura, 12f),
            durationMs = 3000
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
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

            //Ruta directa Arequipa - Yura (verde)
            Polyline(
                points = listOf(arequipa, yura),
                color = Color.Green,
                width = 8f
            )

            //Ruta con múltiples paradas (azul)
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
