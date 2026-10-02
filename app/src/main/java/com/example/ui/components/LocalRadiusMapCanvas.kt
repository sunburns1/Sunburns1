package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DriverProfile
import com.example.data.model.LocationHub
import com.example.data.model.VehicleType
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.SafaaGreen
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LocalRadiusMapCanvas(
    hub: LocationHub,
    serviceRadiusKm: Double,
    pickupName: String,
    pickupLat: Double,
    pickupLng: Double,
    dropoffName: String,
    dropoffLat: Double,
    dropoffLng: Double,
    tripDistanceKm: Double,
    isWithinRadius: Boolean,
    activeDrivers: List<DriverProfile>,
    isNepali: Boolean,
    modifier: Modifier = Modifier,
    onMapClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseRadius"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp)
            .clip(RoundedCornerShape(20.dp))
            .testTag("local_radius_map_card")
            .clickable { onMapClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEFF5F0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val widthPx = constraints.maxWidth.toFloat()
            val heightPx = constraints.maxHeight.toFloat()
            val center = Offset(widthPx / 2f, heightPx / 2f)

            // Canvas drawing map grids, 10-20km boundary, and routes
            Canvas(modifier = Modifier.fillMaxSize()) {
                val baseRadius = (minOf(widthPx, heightPx) * 0.42f)
                val animatedRadius = baseRadius * pulseScale

                // Background subtle road grid lines
                val gridColor = Color(0xFFDDE7DF)
                for (i in 1..4) {
                    val y = heightPx * (i / 5f)
                    drawLine(
                        color = gridColor,
                        start = Offset(0f, y),
                        end = Offset(widthPx, y),
                        strokeWidth = 2f
                    )
                }
                for (i in 1..5) {
                    val x = widthPx * (i / 6f)
                    drawLine(
                        color = gridColor,
                        start = Offset(x, 0f),
                        end = Offset(x, heightPx),
                        strokeWidth = 2f
                    )
                }

                // Service radius circle (10-20km zone)
                drawCircle(
                    color = if (isWithinRadius) Color(0x22137547) else Color(0x22E65100),
                    radius = animatedRadius,
                    center = center
                )
                drawCircle(
                    color = if (isWithinRadius) SafaaGreen else AutoAmber,
                    radius = animatedRadius,
                    center = center,
                    style = Stroke(
                        width = 3.5f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 12f), 0f)
                    )
                )

                // Central Local Hub Marker
                drawCircle(
                    color = Color.White,
                    radius = 9f,
                    center = center
                )
                drawCircle(
                    color = SafaaGreen,
                    radius = 6f,
                    center = center
                )

                // Calculate relative positions for pickup and dropoff
                val pickupOffset = Offset(
                    center.x - baseRadius * 0.45f,
                    center.y + baseRadius * 0.25f
                )
                val dropoffOffset = Offset(
                    center.x + baseRadius * 0.50f,
                    center.y - baseRadius * 0.35f
                )

                // Route Polyline (dashed road)
                drawLine(
                    color = Color(0xFF2C3E50),
                    start = pickupOffset,
                    end = dropoffOffset,
                    strokeWidth = 5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)
                )

                // Pickup Marker (Green)
                drawCircle(color = Color.White, radius = 10f, center = pickupOffset)
                drawCircle(color = SafaaGreen, radius = 7f, center = pickupOffset)

                // Dropoff Marker (Amber/Red)
                drawCircle(color = Color.White, radius = 10f, center = dropoffOffset)
                drawCircle(color = AutoAmber, radius = 7f, center = dropoffOffset)

                // Draw 3-4 simulated nearby active rickshaws
                val angles = listOf(45.0, 135.0, 220.0, 310.0)
                angles.forEachIndexed { idx, angleDeg ->
                    val rad = Math.toRadians(angleDeg)
                    val r = baseRadius * (0.35f + (idx * 0.12f))
                    val rx = center.x + (r * cos(rad)).toFloat()
                    val ry = center.y + (r * sin(rad)).toFloat()
                    val isElec = idx % 2 == 0

                    drawCircle(
                        color = Color.White,
                        radius = 8f,
                        center = Offset(rx, ry)
                    )
                    drawCircle(
                        color = if (isElec) SafaaGreen else AutoAmber,
                        radius = 5.5f,
                        center = Offset(rx, ry)
                    )
                }
            }

            // Top Status Overlay: Hub Name & 10-20km Status badge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.95f),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "Hub",
                            tint = SafaaGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isNepali) hub.nameNp else hub.nameEn,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isWithinRadius) SafaaGreen else AutoAmber,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isWithinRadius) Icons.Default.CheckCircle else Icons.Default.Warning,
                            contentDescription = "Zone",
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${serviceRadiusKm.toInt()} km ${if (isNepali) "स्थानीय क्षेत्र" else "Local Zone"}",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            // Bottom Route & Distance pill
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(10.dp)
                    .fillMaxWidth(0.92f),
                shape = RoundedCornerShape(14.dp),
                color = Color.White.copy(alpha = 0.95f),
                shadowElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
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
                            Icon(
                                imageVector = Icons.Default.ElectricBolt,
                                contentDescription = "Active",
                                tint = SafaaGreen,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "${activeDrivers.size} ${if (isNepali) "रिक्सा नजिक उपलब्ध छन्" else "Rickshaws nearby"}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            )
                            Text(
                                text = "${if (isNepali) "यात्रा दुरी" else "Distance"}: $tripDistanceKm km",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF616161),
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Surface(
                        color = Color(0xFFF1F8E9),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isWithinRadius) "✅ Valid 10-20km" else "⚠️ Outside Limit",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isWithinRadius) SafaaGreen else AutoAmber,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}
