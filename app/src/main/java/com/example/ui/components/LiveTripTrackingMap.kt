package com.example.ui.components

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
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DriverProfile
import com.example.data.model.RideStatus
import com.example.data.model.VehicleType
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.SafaaGreen
import kotlin.math.atan2

@Composable
fun LiveTripTrackingMap(
    rideStatus: RideStatus,
    selectedVehicle: VehicleType,
    driver: DriverProfile?,
    pickupName: String,
    dropoffName: String,
    progress: Float,
    currentSpeedKmh: Int,
    etaMinutes: Int,
    remainingDistKm: Double,
    batteryPercentage: Int,
    isNepali: Boolean,
    modifier: Modifier = Modifier,
    onEmergencySosClick: (() -> Unit)? = null
) {
    // Pulsing radar animation around the moving vehicle
    val infiniteTransition = rememberInfiniteTransition(label = "vehicleRadar")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 12f,
        targetValue = 38f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarPulse"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarAlpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp)
            .clip(RoundedCornerShape(22.dp))
            .testTag("live_trip_tracking_map_card"),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEBF2EC)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val widthPx = constraints.maxWidth.toFloat()
            val heightPx = constraints.maxHeight.toFloat()

            // Define route coordinates on canvas
            // If ARRIVING: route is driver start (top left) to pickup (center)
            // If IN_TRANSIT: route is pickup (left) to dropoff (right)
            val isArriving = rideStatus == RideStatus.ARRIVING

            val startPoint = if (isArriving) {
                Offset(widthPx * 0.15f, heightPx * 0.25f)
            } else {
                Offset(widthPx * 0.18f, heightPx * 0.65f)
            }

            val endPoint = if (isArriving) {
                Offset(widthPx * 0.50f, heightPx * 0.55f)
            } else {
                Offset(widthPx * 0.82f, heightPx * 0.32f)
            }

            // Current animated vehicle position
            val vehicleX = startPoint.x + (endPoint.x - startPoint.x) * progress.coerceIn(0f, 1f)
            val vehicleY = startPoint.y + (endPoint.y - startPoint.y) * progress.coerceIn(0f, 1f)
            val vehicleOffset = Offset(vehicleX, vehicleY)

            // Canvas drawing road network, route lines, vehicle position and markers
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Secondary road network lines
                val roadColor = Color(0xFFD6E3D8)
                drawLine(roadColor, Offset(0f, heightPx * 0.45f), Offset(widthPx, heightPx * 0.45f), 12f)
                drawLine(roadColor, Offset(widthPx * 0.35f, 0f), Offset(widthPx * 0.35f, heightPx), 12f)
                drawLine(roadColor, Offset(widthPx * 0.68f, 0f), Offset(widthPx * 0.68f, heightPx), 10f)

                // Planned Route Background (Gray road track)
                drawLine(
                    color = Color(0xFFB0BEC5),
                    start = startPoint,
                    end = endPoint,
                    strokeWidth = 14f
                )

                // Remaining Route (Dashed amber/blue)
                drawLine(
                    color = Color.White,
                    start = vehicleOffset,
                    end = endPoint,
                    strokeWidth = 6f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)
                )

                // Completed Route (Solid Safaa Green)
                drawLine(
                    color = SafaaGreen,
                    start = startPoint,
                    end = vehicleOffset,
                    strokeWidth = 7f
                )

                // Start Marker (Pickup point)
                drawCircle(color = Color.White, radius = 11f, center = startPoint)
                drawCircle(color = SafaaGreen, radius = 8f, center = startPoint)

                // End Marker (Dropoff destination)
                drawCircle(color = Color.White, radius = 12f, center = endPoint)
                drawCircle(color = AutoAmber, radius = 9f, center = endPoint)

                // Live moving vehicle marker with concentric pulsing radar waves
                drawCircle(
                    color = if (selectedVehicle.isElectric) SafaaGreen.copy(alpha = pulseAlpha)
                    else AutoAmber.copy(alpha = pulseAlpha),
                    radius = pulseRadius,
                    center = vehicleOffset,
                    style = Stroke(width = 3.5f)
                )

                // Vehicle Outer Glow & Badge
                drawCircle(
                    color = Color.White,
                    radius = 16f,
                    center = vehicleOffset
                )
                drawCircle(
                    color = if (selectedVehicle.isElectric) SafaaGreen else AutoAmber,
                    radius = 13f,
                    center = vehicleOffset
                )
            }

            // Top Telemetry Header HUD
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Trip Status Banner
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (selectedVehicle.isElectric) SafaaGreen else AutoAmber,
                    shadowElevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (selectedVehicle.isElectric) Icons.Default.ElectricBolt else Icons.Default.Navigation,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isArriving) {
                                if (isNepali) "रिक्सा आउँदैछ (Arriving)" else "Driver Arriving • $etaMinutes mins"
                            } else {
                                if (isNepali) "यात्रा सुरु भयो (In Transit)" else "In Transit • ETA $etaMinutes mins"
                            },
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                // Speedometer & Battery / Fuel Telemetry Pill
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
                            imageVector = Icons.Default.Speed,
                            contentDescription = "Speed",
                            tint = SafaaGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$currentSpeedKmh km/h",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (selectedVehicle.isElectric) "⚡ $batteryPercentage%" else "⛽ OK",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (selectedVehicle.isElectric) SafaaGreen else AutoAmber,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            // Bottom Floating Route Navigation Card
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(10.dp)
                    .fillMaxWidth(0.95f),
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.96f),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(SafaaGreen.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NearMe,
                                contentDescription = null,
                                tint = SafaaGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = if (isArriving) "Heading to $pickupName" else "Destination: $dropoffName",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                maxLines = 1
                            )
                            Text(
                                text = "${driver?.name ?: "Driver"} (${driver?.licensePlate ?: ""}) • ${remainingDistKm} km left",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.Gray,
                                    fontSize = 11.sp
                                ),
                                maxLines = 1
                            )
                        }
                    }

                    // Emergency SOS Icon Button
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFFFEBEE),
                        modifier = Modifier
                            .clickable { onEmergencySosClick?.invoke() }
                            .testTag("map_emergency_sos_btn")
                    ) {
                        Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.WarningAmber,
                                contentDescription = "Emergency SOS",
                                tint = Color.Red,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
