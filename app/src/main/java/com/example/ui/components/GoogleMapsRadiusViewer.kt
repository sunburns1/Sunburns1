package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Traffic
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DriverProfile
import com.example.data.model.LandmarkLocation
import com.example.data.model.LocationHub
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.NepalGold
import com.example.ui.theme.SafaaGreen
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GoogleMapsRadiusViewer(
    hub: LocationHub,
    serviceRadiusKm: Double = 20.0,
    pickupName: String,
    pickupLat: Double,
    pickupLng: Double,
    dropoffName: String,
    dropoffLat: Double,
    dropoffLng: Double,
    tripDistKm: Double,
    isWithinRadius: Boolean,
    activeDrivers: List<DriverProfile>,
    mapLayerType: String = "streets", // "streets", "satellite", "terrain"
    showTraffic: Boolean = true,
    zoomLevel: Float = 1.0f,
    isNepali: Boolean,
    modifier: Modifier = Modifier,
    onLayerSelected: (String) -> Unit = {},
    onTrafficToggle: () -> Unit = {},
    onZoomIn: () -> Unit = {},
    onZoomOut: () -> Unit = {},
    onRecenter: () -> Unit = {},
    onLandmarkSelected: ((LandmarkLocation) -> Unit)? = null
) {
    val context = LocalContext.current
    var showLayersMenu by remember { mutableStateOf(false) }

    // Pulsing 20km boundary radar wave
    val infiniteTransition = rememberInfiniteTransition(label = "googleMapsPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "radiusPulse"
    )

    // Palette per map layer type (Google Maps styling)
    val (mapBgColor, roadColor, waterColor, textContColor) = when (mapLayerType) {
        "satellite" -> Quad(Color(0xFF1E262C), Color(0xFF4A5560), Color(0xFF101B24), Color.White)
        "terrain" -> Quad(Color(0xFFE8ECE5), Color(0xFFC7D3C0), Color(0xFFA5C4D4), Color(0xFF263238))
        else -> Quad(Color(0xFFF2F4F2), Color(0xFFE3E8E3), Color(0xFFCADDE8), Color(0xFF37474F))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
            .clip(RoundedCornerShape(22.dp))
            .testTag("google_maps_radius_viewer_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = mapBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val widthPx = constraints.maxWidth.toFloat()
            val heightPx = constraints.maxHeight.toFloat()
            val center = Offset(widthPx / 2f, heightPx / 2f)

            // Canvas drawing Google Maps layers: 20 km boundary, roads, traffic, landmarks, pins
            Canvas(modifier = Modifier.fillMaxSize()) {
                val base20KmRadius = (minOf(widthPx, heightPx) * 0.44f) * zoomLevel
                val currentRadius = base20KmRadius * pulseScale

                // 1. Waterway / River (e.g. Narayani River in Chitwan, Bagmati in Kathmandu)
                val riverPath = Path().apply {
                    moveTo(0f, heightPx * 0.18f)
                    cubicTo(
                        widthPx * 0.3f, heightPx * 0.22f,
                        widthPx * 0.5f, heightPx * 0.12f,
                        widthPx, heightPx * 0.28f
                    )
                    lineTo(widthPx, heightPx * 0.38f)
                    cubicTo(
                        widthPx * 0.5f, heightPx * 0.22f,
                        widthPx * 0.3f, heightPx * 0.32f,
                        0f, heightPx * 0.26f
                    )
                    close()
                }
                drawPath(riverPath, color = waterColor)

                // 2. Road Network Grid (Google Maps arterial highway & streets)
                val highways = listOf(0.40f, 0.65f)
                highways.forEach { frac ->
                    drawLine(
                        color = roadColor,
                        start = Offset(0f, heightPx * frac),
                        end = Offset(widthPx, heightPx * frac),
                        strokeWidth = 10f
                    )
                }
                val verticals = listOf(0.28f, 0.52f, 0.76f)
                verticals.forEach { frac ->
                    drawLine(
                        color = roadColor,
                        start = Offset(widthPx * frac, 0f),
                        end = Offset(widthPx * frac, heightPx),
                        strokeWidth = 9f
                    )
                }

                // 3. Live Traffic Layer (Simulated Google Traffic: Green / Orange / Red)
                if (showTraffic) {
                    // Smooth green arterial
                    drawLine(
                        color = Color(0xFF4CAF50),
                        start = Offset(0f, heightPx * 0.40f),
                        end = Offset(widthPx * 0.52f, heightPx * 0.40f),
                        strokeWidth = 5f
                    )
                    // Moderate orange flow
                    drawLine(
                        color = Color(0xFFFF9800),
                        start = Offset(widthPx * 0.52f, heightPx * 0.40f),
                        end = Offset(widthPx * 0.76f, heightPx * 0.40f),
                        strokeWidth = 5f
                    )
                    // Red traffic at busy central chowk
                    drawLine(
                        color = Color(0xFFE53935),
                        start = Offset(widthPx * 0.52f, heightPx * 0.30f),
                        end = Offset(widthPx * 0.52f, heightPx * 0.52f),
                        strokeWidth = 5f
                    )
                }

                // 4. Google Maps 20-Kilometer Service Radius Circle
                // Shaded zone fill
                drawCircle(
                    color = if (isWithinRadius) Color(0x1F137547) else Color(0x1FE65100),
                    radius = currentRadius,
                    center = center
                )
                // 20 km Outer Dotted Border
                drawCircle(
                    color = if (isWithinRadius) SafaaGreen else AutoAmber,
                    radius = currentRadius,
                    center = center,
                    style = Stroke(
                        width = 3.2f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 10f), 0f)
                    )
                )

                // 5. Central Hub Marker (e.g. Narayanghat / Kathmandu / Biratnagar)
                drawCircle(color = Color.White, radius = 9f, center = center)
                drawCircle(color = SafaaGreen, radius = 6f, center = center)

                // 6. Pickup & Dropoff Coordinate Projections
                val pickupPos = Offset(
                    center.x - base20KmRadius * 0.42f,
                    center.y + base20KmRadius * 0.28f
                )
                val dropoffPos = Offset(
                    center.x + base20KmRadius * 0.48f,
                    center.y - base20KmRadius * 0.32f
                )

                // Polyline Route (Google Maps directions blue road track)
                drawLine(
                    color = Color(0xFF1976D2),
                    start = pickupPos,
                    end = dropoffPos,
                    strokeWidth = 6.5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 8f), 0f)
                )

                // 7. Authentic Google Maps Teardrop Markers:
                // Green Pickup Teardrop Pin
                drawCircle(color = Color(0xFF2E7D32), radius = 10f, center = pickupPos)
                drawCircle(color = Color.White, radius = 4f, center = pickupPos)

                // Red Dropoff Teardrop Pin
                drawCircle(color = Color(0xFFD32F2F), radius = 11f, center = dropoffPos)
                drawCircle(color = Color.White, radius = 4f, center = dropoffPos)

                // 8. Simulated Active Rickshaws near customer
                val angles = listOf(45.0, 140.0, 215.0, 310.0)
                angles.forEachIndexed { i, deg ->
                    val rad = Math.toRadians(deg)
                    val r = base20KmRadius * (0.3f + i * 0.15f)
                    val rx = center.x + (r * cos(rad)).toFloat()
                    val ry = center.y + (r * sin(rad)).toFloat()
                    val isElec = i % 2 == 0

                    drawCircle(color = Color.White, radius = 7f, center = Offset(rx, ry))
                    drawCircle(
                        color = if (isElec) SafaaGreen else AutoAmber,
                        radius = 5f,
                        center = Offset(rx, ry)
                    )
                }
            }

            // Top Status Bar: Hub & 20 KM Tag
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White.copy(alpha = 0.95f),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = null,
                            tint = SafaaGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isNepali) hub.nameNp.take(18) else hub.nameEn.take(18),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isWithinRadius) SafaaGreen else AutoAmber,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isWithinRadius) Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "20 km ${if (isNepali) "स्थानीय दायरा" else "Service Area"}",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            // Right Google Maps Control Panel (+, -, Layers, Traffic, Open in Maps)
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Zoom In Button (+)
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 3.dp,
                    modifier = Modifier.clickable { onZoomIn() }.testTag("gmap_zoom_in_btn")
                ) {
                    Box(modifier = Modifier.padding(6.dp), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Add, contentDescription = "Zoom In", tint = Color.DarkGray, modifier = Modifier.size(18.dp))
                    }
                }

                // Zoom Out Button (-)
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 3.dp,
                    modifier = Modifier.clickable { onZoomOut() }.testTag("gmap_zoom_out_btn")
                ) {
                    Box(modifier = Modifier.padding(6.dp), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Remove, contentDescription = "Zoom Out", tint = Color.DarkGray, modifier = Modifier.size(18.dp))
                    }
                }

                // Map Layers Switcher (Streets / Satellite / Terrain)
                Surface(
                    shape = CircleShape,
                    color = if (showLayersMenu) SafaaGreen else Color.White,
                    shadowElevation = 3.dp,
                    modifier = Modifier.clickable { showLayersMenu = !showLayersMenu }.testTag("gmap_layers_btn")
                ) {
                    Box(modifier = Modifier.padding(6.dp), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Layers,
                            contentDescription = "Layers",
                            tint = if (showLayersMenu) Color.White else Color.DarkGray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Traffic Layer Toggle
                Surface(
                    shape = CircleShape,
                    color = if (showTraffic) Color(0xFFE8F5E9) else Color.White,
                    shadowElevation = 3.dp,
                    modifier = Modifier.clickable { onTrafficToggle() }.testTag("gmap_traffic_toggle_btn")
                ) {
                    Box(modifier = Modifier.padding(6.dp), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Traffic,
                            contentDescription = "Traffic",
                            tint = if (showTraffic) SafaaGreen else Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Open in External Google Maps App
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 3.dp,
                    modifier = Modifier
                        .clickable {
                            val uri = Uri.parse("geo:${pickupLat},${pickupLng}?q=${dropoffLat},${dropoffLng}(Destination)")
                            val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
                                setPackage("com.google.android.apps.maps")
                            }
                            try {
                                context.startActivity(mapIntent)
                            } catch (_: Exception) {
                                val browserIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://www.google.com/maps/dir/?api=1&origin=${pickupLat},${pickupLng}&destination=${dropoffLat},${dropoffLng}")
                                )
                                context.startActivity(browserIntent)
                            }
                        }
                        .testTag("gmap_open_external_btn")
                ) {
                    Box(modifier = Modifier.padding(6.dp), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Open in Google Maps",
                            tint = Color(0xFF1976D2),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Layer Selector Popover (Streets / Satellite / Terrain)
            if (showLayersMenu) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 50.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    shadowElevation = 6.dp
                ) {
                    Column(modifier = Modifier.padding(6.dp)) {
                        listOf("streets" to "Streets", "satellite" to "Satellite", "terrain" to "Terrain").forEach { (type, name) ->
                            val isSel = mapLayerType == type
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) SafaaGreen.copy(alpha = 0.15f) else Color.Transparent,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onLayerSelected(type)
                                        showLayersMenu = false
                                    }
                            ) {
                                Text(
                                    text = name,
                                    color = if (isSel) SafaaGreen else Color.DarkGray,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Navigation & Scale HUD
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(8.dp)
                    .fillMaxWidth(0.94f),
                shape = RoundedCornerShape(14.dp),
                color = Color.White.copy(alpha = 0.96f),
                shadowElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(SafaaGreen.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ElectricBolt, contentDescription = null, tint = SafaaGreen, modifier = Modifier.size(15.dp))
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${activeDrivers.size} ${if (isNepali) "रिक्सा नजिक उपलब्ध" else "Rickshaws nearby"}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    // Map scale bar indicator (e.g. 5 km scale)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Distance: $tripDistKm km",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.DarkGray)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isWithinRadius) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                        ) {
                            Text(
                                text = if (isWithinRadius) "✅ 20km OK" else "⚠️ Outside",
                                color = if (isWithinRadius) SafaaGreen else AutoAmber,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class Quad<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)
