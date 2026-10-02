package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.LocalHubs
import com.example.data.model.PaymentMethod
import com.example.data.model.RideStatus
import com.example.data.model.ServiceType
import com.example.data.model.VehicleType
import com.example.ui.components.DriverProfileDialog
import com.example.ui.components.LocalRadiusMapCanvas
import com.example.ui.components.PaymentDialog
import com.example.ui.components.RatingReviewDialog
import com.example.ui.components.RiderProfileDialog
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.EsewaGreen
import com.example.ui.theme.FonepayRed
import com.example.ui.theme.KhaltiPurple
import com.example.ui.theme.NepalGold
import com.example.ui.theme.SafaaGreen
import com.example.ui.viewmodel.RideViewModel

@Composable
fun BookingHomeScreen(
    viewModel: RideViewModel,
    modifier: Modifier = Modifier,
    onNavigateToPayment: (() -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val activeHub by viewModel.activeHub.collectAsState()
    val serviceRadiusKm by viewModel.serviceRadiusKm.collectAsState()
    val drivers by viewModel.drivers.collectAsState()
    val riderProfile by viewModel.riderProfile.collectAsState()

    var showHubMenu by remember { mutableStateOf(false) }
    var showPickupPicker by remember { mutableStateOf(false) }
    var showDropoffPicker by remember { mutableStateOf(false) }

    val isNepali = uiState.isNepaliLanguage
    val distKm = viewModel.getTripDistanceKm()
    val isWithinRadius = viewModel.isPickupWithinRadius() && viewModel.isDropoffWithinRadius()
    val fareNpr = viewModel.calculateFareNpr()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Clean Top Header with App Name, Language Toggle & Rider Profile
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Safaa Ride & Cargo",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = SafaaGreen
                        )
                    )
                    Text(
                        text = if (isNepali) "सफा टेम्पो, अटो र ई-रिक्सा सेवा" else "Local Auto & E-Rickshaw Ride & Delivery",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Nepali / English language quick switch
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clickable { viewModel.toggleLanguage() }
                            .testTag("language_toggle_btn")
                    ) {
                        Text(
                            text = if (isNepali) "EN" else "नेपाली",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Rider Profile Avatar
                    IconButton(
                        onClick = { viewModel.openRiderProfile(true) },
                        modifier = Modifier
                            .size(38.dp)
                            .background(SafaaGreen.copy(alpha = 0.15f), CircleShape)
                            .testTag("rider_profile_icon_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Rider Profile",
                            tint = SafaaGreen
                        )
                    }
                }
            }
        }

        // Active Trip Card (Shown if a ride is in progress)
        if (uiState.rideStatus != RideStatus.IDLE) {
            item {
                ActiveRideTrackingCard(
                    uiState = uiState,
                    isNepali = isNepali,
                    fareNpr = fareNpr,
                    onCallDriver = { viewModel.openDriverDetail(uiState.activeDriver) },
                    onCompleteTrip = { viewModel.openPaymentFlow(true) },
                    onCancelTrip = { viewModel.cancelCurrentTrip() },
                    onViewDriver = { viewModel.openDriverDetail(uiState.activeDriver) }
                )
            }
        }

        // 10-20km Service Area & Radius Selector Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = null,
                                tint = SafaaGreen,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isNepali) "सञ्चालन क्षेत्र (Local Area Hub):" else "Local Service Area:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        // Hub Dropdown Trigger
                        Box {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCFD8DC)),
                                modifier = Modifier
                                    .clickable { showHubMenu = true }
                                    .testTag("hub_selector_dropdown")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isNepali) activeHub.nameNp.take(18) else activeHub.nameEn.take(18),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Icon(Icons.Default.ExpandMore, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            }

                            DropdownMenu(
                                expanded = showHubMenu,
                                onDismissRequest = { showHubMenu = false }
                            ) {
                                LocalHubs.ALL_HUBS.forEach { hub ->
                                    DropdownMenuItem(
                                        text = { Text(if (isNepali) hub.nameNp else hub.nameEn) },
                                        onClick = {
                                            viewModel.selectActiveHub(hub)
                                            showHubMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Up to 20 km Radius Limit Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${if (isNepali) "अधिकतम स्थानीय दायरा" else "Max Local Radius"}: ${serviceRadiusKm.toInt()} km",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(5.0, 10.0, 15.0, 20.0).forEach { r ->
                                val isSelected = serviceRadiusKm == r
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (isSelected) SafaaGreen else Color.White,
                                    modifier = Modifier.clickable { viewModel.setServiceRadius(r) }
                                ) {
                                    Text(
                                        text = "${r.toInt()} km",
                                        color = if (isSelected) Color.White else Color.Black,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Slider(
                        value = serviceRadiusKm.toFloat(),
                        onValueChange = { viewModel.setServiceRadius(it.toDouble()) },
                        valueRange = 5f..20f,
                        steps = 14,
                        colors = SliderDefaults.colors(
                            thumbColor = SafaaGreen,
                            activeTrackColor = SafaaGreen
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("radius_slider")
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFE8F5E9),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "📋 ${activeHub.cityTariff.tariffSource} (Max 20 km)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                color = SafaaGreen,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Service Type Switcher: Passenger Ride vs Cargo Delivery
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE8EDE9), RoundedCornerShape(14.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val isRide = uiState.serviceType == ServiceType.PASSENGER_RIDE
                Surface(
                    shape = RoundedCornerShape(11.dp),
                    color = if (isRide) SafaaGreen else Color.Transparent,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.setServiceType(ServiceType.PASSENGER_RIDE) }
                        .testTag("switch_to_ride_tab")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = if (isRide) Color.White else Color.DarkGray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isNepali) "यात्रु यात्रा (Ride)" else "Passenger Ride",
                            color = if (isRide) Color.White else Color.DarkGray,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(11.dp),
                    color = if (!isRide) AutoAmber else Color.Transparent,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.setServiceType(ServiceType.PARCEL_DELIVERY) }
                        .testTag("switch_to_delivery_tab")
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null,
                            tint = if (!isRide) Color.White else Color.DarkGray,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isNepali) "सामान डेलिभरी (Cargo)" else "Cargo Delivery",
                            color = if (!isRide) Color.White else Color.DarkGray,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        // Animated 10-20km Radar & Route Map Visualizer
        item {
            LocalRadiusMapCanvas(
                hub = activeHub,
                serviceRadiusKm = serviceRadiusKm,
                pickupName = uiState.pickupName,
                pickupLat = uiState.pickupLat,
                pickupLng = uiState.pickupLng,
                dropoffName = uiState.dropoffName,
                dropoffLat = uiState.dropoffLat,
                dropoffLng = uiState.dropoffLng,
                tripDistanceKm = distKm,
                isWithinRadius = isWithinRadius,
                activeDrivers = drivers,
                isNepali = isNepali
            )
        }

        // Simple Pickup and Dropoff Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Pickup Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showPickupPicker = true }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(SafaaGreen, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isNepali) "उठ्ने ठाउँ (Pickup)" else "Pickup Location",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp)
                            )
                            Text(
                                text = uiState.pickupName,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Icon(Icons.Default.ExpandMore, contentDescription = null, tint = Color.Gray)
                    }

                    DropdownMenu(
                        expanded = showPickupPicker,
                        onDismissRequest = { showPickupPicker = false }
                    ) {
                        activeHub.landmarks.forEach { lm ->
                            DropdownMenuItem(
                                text = { Text(if (isNepali) lm.nameNp else lm.nameEn) },
                                onClick = {
                                    viewModel.setPickup(lm)
                                    showPickupPicker = false
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .width(2.dp)
                            .height(14.dp)
                            .background(Color.LightGray)
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    // Dropoff Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDropoffPicker = true }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(AutoAmber, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isNepali) "पुग्ने ठाउँ (Destination)" else "Dropoff Destination",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp)
                            )
                            Text(
                                text = uiState.dropoffName,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Icon(Icons.Default.ExpandMore, contentDescription = null, tint = Color.Gray)
                    }

                    DropdownMenu(
                        expanded = showDropoffPicker,
                        onDismissRequest = { showDropoffPicker = false }
                    ) {
                        activeHub.landmarks.forEach { lm ->
                            DropdownMenuItem(
                                text = { Text(if (isNepali) lm.nameNp else lm.nameEn) },
                                onClick = {
                                    viewModel.setDropoff(lm)
                                    showDropoffPicker = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Vehicle Selection Header
        item {
            Text(
                text = if (isNepali) "सवारी साधन छान्नुहोस् (Select Rickshaw):" else "Select Vehicle:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
        }

        // Vehicle Options Cards (E-Rickshaw, Auto Rickshaw, Cargo)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                VehicleType.values().forEach { vehicle ->
                    val isSelected = uiState.selectedVehicle == vehicle
                    val (cBase, cPerKm) = viewModel.getRatesForCurrentCity(vehicle)
                    val vehicleFare = cBase + (distKm * cPerKm).toInt()

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) {
                            if (vehicle.isElectric) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                        } else MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) {
                                if (vehicle.isElectric) SafaaGreen else AutoAmber
                            } else Color(0xFFE0E0E0)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setVehicle(vehicle) }
                            .testTag("vehicle_card_${vehicle.name.lowercase()}")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .background(
                                            if (vehicle.isElectric) SafaaGreen.copy(alpha = 0.15f)
                                            else AutoAmber.copy(alpha = 0.15f),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (vehicle.isElectric) Icons.Default.ElectricBolt else Icons.Default.DirectionsCar,
                                        contentDescription = null,
                                        tint = if (vehicle.isElectric) SafaaGreen else AutoAmber,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = if (isNepali) vehicle.titleNp else vehicle.titleEn,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        if (vehicle.isElectric) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                color = SafaaGreen,
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = "ECO",
                                                    color = Color.White,
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold
                                                    ),
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "${vehicle.capacityEn} • Rs $cBase base + $cPerKm/km (${activeHub.cityTariff.cityName})",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color.Gray,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "रू $vehicleFare",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (vehicle.isElectric) SafaaGreen else AutoAmber
                                    )
                                )
                                Text(
                                    text = "~${(distKm * 3.5).toInt().coerceAtLeast(4)} mins",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color.Gray,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Payment Method Selector
        item {
            Column {
                Text(
                    text = if (isNepali) "भुक्तानी माध्यम (Payment Method):" else "Payment Method:",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PaymentMethod.values().forEach { method ->
                        val isSelected = uiState.selectedPaymentMethod == method
                        val brandColor = when (method) {
                            PaymentMethod.ESEWA -> EsewaGreen
                            PaymentMethod.KHALTI -> KhaltiPurple
                            PaymentMethod.NEPALESE_BANK -> FonepayRed
                            PaymentMethod.CASH -> SafaaGreen
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) brandColor else MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) brandColor else Color(0xFFDCDCDC)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.setPaymentMethod(method) }
                                .testTag("pay_method_${method.name.lowercase()}")
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = when (method) {
                                        PaymentMethod.ESEWA, PaymentMethod.KHALTI -> Icons.Default.Security
                                        PaymentMethod.NEPALESE_BANK -> Icons.Default.AccountBalance
                                        PaymentMethod.CASH -> Icons.Default.Money
                                    },
                                    contentDescription = null,
                                    tint = if (isSelected) Color.White else brandColor,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = when (method) {
                                        PaymentMethod.ESEWA -> "eSewa"
                                        PaymentMethod.KHALTI -> "Khalti"
                                        PaymentMethod.NEPALESE_BANK -> "Bank"
                                        PaymentMethod.CASH -> if (isNepali) "नगद" else "Cash"
                                    },
                                    color = if (isSelected) Color.White else Color.Black,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = { onNavigateToPayment?.invoke() },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("open_payment_selection_screen_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = EsewaGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isNepali) "ई-सेवा र खल्ती API भुक्तानी स्क्रिन खोल्नुहोस्" else "Configure eSewa & Khalti API Gateway",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        // Main Simple Booking Button
        item {
            Button(
                onClick = {
                    if (isWithinRadius) {
                        viewModel.startBookingFlow()
                    }
                },
                enabled = isWithinRadius && uiState.rideStatus == RideStatus.IDLE,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("main_book_rickshaw_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (uiState.selectedVehicle.isElectric) SafaaGreen else AutoAmber
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                if (isWithinRadius) {
                    Text(
                        text = if (isNepali)
                            "${uiState.selectedVehicle.titleNp} बुक गर्नुहोस् • रू $fareNpr"
                        else
                            "Book ${uiState.selectedVehicle.titleEn} • Rs $fareNpr",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isNepali) "१०-२० कि.मी. भित्रको स्थान छान्नुहोस्" else "Outside 10-20km Local Area",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Interactive Dialogs
    if (uiState.showPaymentFlow) {
        PaymentDialog(
            method = uiState.selectedPaymentMethod,
            amountNpr = fareNpr,
            isNepali = isNepali,
            onDismiss = { viewModel.openPaymentFlow(false) },
            onPaymentSuccess = { ref -> viewModel.confirmPayment(ref) }
        )
    }

    if (uiState.showRatingModal) {
        val order = uiState.orderBeingRated
        RatingReviewDialog(
            driver = uiState.activeDriver,
            driverNameFallback = order?.driverName,
            driverPlateFallback = order?.driverPlate,
            driverVehicleFallback = order?.driverVehicle,
            isNepali = isNepali,
            onDismiss = { viewModel.dismissRatingModal() },
            onSubmit = { rating, text -> viewModel.submitRating(rating, text) }
        )
    }

    uiState.selectedDriverForDetail?.let { driver ->
        DriverProfileDialog(
            driver = driver,
            isNepali = isNepali,
            onDismiss = { viewModel.openDriverDetail(null) }
        )
    }

    if (uiState.showRiderProfileModal) {
        RiderProfileDialog(
            profile = riderProfile,
            isNepali = isNepali,
            onDismiss = { viewModel.openRiderProfile(false) },
            onSave = { updated -> viewModel.updateRider(updated) },
            onSelectSavedPlaceAsPickup = { place ->
                viewModel.setPickupCustom(place.address, place.lat, place.lng)
            }
        )
    }
}

@Composable
fun ActiveRideTrackingCard(
    uiState: com.example.ui.viewmodel.RideBookingUiState,
    isNepali: Boolean,
    fareNpr: Int,
    onCallDriver: () -> Unit,
    onCompleteTrip: () -> Unit,
    onCancelTrip: () -> Unit,
    onViewDriver: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9)),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, SafaaGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth().testTag("active_ride_tracking_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Status Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.5.dp,
                        color = SafaaGreen
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isNepali) uiState.rideStatus.titleNp else uiState.rideStatus.titleEn,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = SafaaGreen
                        )
                    )
                }

                Surface(
                    color = SafaaGreen,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "रू $fareNpr",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Assigned Driver Card
            uiState.activeDriver?.let { driver ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onViewDriver() }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(SafaaGreen.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = SafaaGreen)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = driver.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = driver.vehicleModel,
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp)
                                )
                                Text(
                                    text = driver.licensePlate,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF0D47A1),
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                )
                            }
                        }

                        IconButton(
                            onClick = onCallDriver,
                            modifier = Modifier
                                .size(36.dp)
                                .background(SafaaGreen, CircleShape)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Call", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onCancelTrip,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f).testTag("cancel_active_trip_btn")
                ) {
                    Text(if (isNepali) "रद्द गर्नुहोस्" else "Cancel Ride", color = Color.Red)
                }

                Button(
                    onClick = onCompleteTrip,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SafaaGreen),
                    modifier = Modifier.weight(1f).testTag("complete_pay_trip_btn")
                ) {
                    Text(
                        text = if (isNepali) "सम्पन्न र भुक्तानी" else "Arrived & Pay",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
