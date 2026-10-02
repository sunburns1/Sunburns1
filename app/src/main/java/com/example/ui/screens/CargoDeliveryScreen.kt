package com.example.ui.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PaymentMethod
import com.example.data.model.ServiceType
import com.example.data.model.VehicleType
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.EsewaGreen
import com.example.ui.theme.FonepayRed
import com.example.ui.theme.KhaltiPurple
import com.example.ui.theme.SafaaGreen
import com.example.ui.viewmodel.RideViewModel

@Composable
fun CargoDeliveryScreen(
    viewModel: RideViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val isNepali = uiState.isNepaliLanguage

    var receiverName by remember { mutableStateOf(uiState.parcelInfo.receiverName) }
    var receiverPhone by remember { mutableStateOf(uiState.parcelInfo.receiverPhone) }
    var instructions by remember { mutableStateOf(uiState.parcelInfo.instructions) }
    var isFragile by remember { mutableStateOf(uiState.parcelInfo.isFragile) }
    var selectedCategory by remember { mutableStateOf("Groceries / Food (किराना/खाद्यान्न)") }
    var weightKg by remember { mutableDoubleStateOf(15.0) }

    val categories = listOf(
        "Groceries / Food (किराना/खाद्यान्न)",
        "Hardware & Sacks (बोरा र हार्डवेयर)",
        "Electronics & Appliances",
        "Documents & Parcels (कागजात)",
        "Clothing & Boxes (कपडा र बक्स)"
    )

    val distKm = viewModel.getTripDistanceKm()
    // Cargo fare: base Rs 55 + Rs 25/km + weight surcharge if > 25kg
    val weightSurcharge = if (weightKg > 25) ((weightKg - 25) * 2).toInt() else 0
    val totalCargoFare = 55 + (distKm * 25).toInt() + weightSurcharge

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(AutoAmber.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = null,
                        tint = AutoAmber,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isNepali) "रिक्सा पार्सल तथा सामान ढुवानी" else "Rickshaw Cargo & Parcel Delivery",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = if (isNepali) "स्थानीय १०-२० कि.मी. भित्र छिटो र भरपर्दो" else "Within local 10-20 km radius • Up to 150 kg",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )
                }
            }
        }

        // Receiver Information Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isNepali) "सामान पाउने व्यक्तिको विवरण (Receiver):" else "Receiver Details:",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = receiverName,
                        onValueChange = {
                            receiverName = it
                            viewModel.updateParcelInfo(uiState.parcelInfo.copy(receiverName = it))
                        },
                        label = { Text(if (isNepali) "पाउने व्यक्तिको नाम" else "Receiver Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth().testTag("receiver_name_input"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = receiverPhone,
                        onValueChange = {
                            receiverPhone = it
                            viewModel.updateParcelInfo(uiState.parcelInfo.copy(receiverPhone = it))
                        },
                        label = { Text(if (isNepali) "पाउने व्यक्तिको फोन नं. (+977)" else "Receiver Phone Number") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth().testTag("receiver_phone_input"),
                        singleLine = true
                    )
                }
            }
        }

        // Parcel Category and Weight
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isNepali) "सामानको प्रकार (Category):" else "Package Category:",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { cat ->
                            val isSelected = cat == selectedCategory
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) AutoAmber else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.clickable { selectedCategory = cat }
                            ) {
                                Text(
                                    text = cat,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "${if (isNepali) "तौल" else "Estimated Weight"}: ${weightKg.toInt()} kg",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(5.0, 15.0, 30.0, 60.0, 100.0).forEach { w ->
                            val isSelected = weightKg == w
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) SafaaGreen else Color(0xFFF1F1F1),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { weightKg = w }
                            ) {
                                Text(
                                    text = "${w.toInt()}kg",
                                    color = if (isSelected) Color.White else Color.Black,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { isFragile = !isFragile }
                    ) {
                        Checkbox(
                            checked = isFragile,
                            onCheckedChange = { isFragile = it }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isNepali) "फुत्किने वा संवेदनशील सामान (Fragile Items)" else "Fragile / Handle With Care",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = instructions,
                        onValueChange = {
                            instructions = it
                            viewModel.updateParcelInfo(uiState.parcelInfo.copy(instructions = it))
                        },
                        label = { Text(if (isNepali) "डेलिभरी निर्देशन / ठेगाना जानकारी" else "Delivery note / landmark instructions") },
                        modifier = Modifier.fillMaxWidth().testTag("delivery_instructions_input"),
                        minLines = 2,
                        maxLines = 3
                    )
                }
            }
        }

        // Delivery Rickshaw Fare & Payment Preview
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isNepali) "सामान ढुवानी भाडा" else "Cargo Delivery Total",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF795548))
                        )
                        Text(
                            text = "रू $totalCargoFare",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = AutoAmber
                            )
                        )
                        Text(
                            text = "$distKm km • Atul Shakti Cargo Safari",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color.Gray)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = when (uiState.selectedPaymentMethod) {
                            PaymentMethod.ESEWA -> EsewaGreen
                            PaymentMethod.KHALTI -> KhaltiPurple
                            PaymentMethod.NEPALESE_BANK -> FonepayRed
                            PaymentMethod.CASH -> SafaaGreen
                        }
                    ) {
                        Text(
                            text = when (uiState.selectedPaymentMethod) {
                                PaymentMethod.ESEWA -> "eSewa"
                                PaymentMethod.KHALTI -> "Khalti"
                                PaymentMethod.NEPALESE_BANK -> "Bank Transfer"
                                PaymentMethod.CASH -> "COD / Cash"
                            },
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Action Button
        item {
            Button(
                onClick = {
                    viewModel.setServiceType(ServiceType.PARCEL_DELIVERY)
                    viewModel.setVehicle(VehicleType.CARGO_RICKSHAW)
                    viewModel.startBookingFlow()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("book_cargo_rickshaw_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = AutoAmber),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.LocalShipping, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isNepali) "डेलिभरी रिक्सा बोलाउनुहोस् • रू $totalCargoFare" else "Book Delivery Rickshaw • Rs $totalCargoFare",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
