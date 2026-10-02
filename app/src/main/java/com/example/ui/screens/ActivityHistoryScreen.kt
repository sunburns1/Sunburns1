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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.PaymentMethod
import com.example.data.model.RideOrderEntity
import com.example.data.model.ServiceType
import com.example.data.model.VehicleType
import com.example.ui.components.RatingReviewDialog
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.EsewaGreen
import com.example.ui.theme.FonepayRed
import com.example.ui.theme.KhaltiPurple
import com.example.ui.theme.NepalGold
import com.example.ui.theme.SafaaGreen
import com.example.ui.viewmodel.RideViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ActivityHistoryScreen(
    viewModel: RideViewModel,
    modifier: Modifier = Modifier
) {
    val orders by viewModel.allOrders.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val isNepali = uiState.isNepaliLanguage

    var selectedOrderForReceipt by remember { mutableStateOf<RideOrderEntity?>(null) }
    var selectedOrderForRating by remember { mutableStateOf<RideOrderEntity?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Column {
                Text(
                    text = if (isNepali) "सवारी तथा डेलिभरी इतिहास" else "Rides & Delivery Activity",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = SafaaGreen
                    )
                )
                Text(
                    text = if (isNepali) "तपाईंको विगतका सबै ई-रिक्सा र अटो यात्रा विवरण" else "All your completed auto rickshaw trips & e-rickshaw deliveries",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
        }

        if (orders.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isNepali) "हालसम्म कुनै यात्रा इतिहास छैन" else "No Ride History Yet",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isNepali) "गृहपृष्ठबाट रिक्सा वा डेलिभरी बुक गर्नुहोस्।" else "Book an Auto or E-Rickshaw from the Home tab to see receipts here.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(orders) { order ->
                val isElectric = order.vehicleType == VehicleType.E_RICKSHAW.name || order.vehicleType == VehicleType.CARGO_RICKSHAW.name
                val isCargo = order.serviceType == ServiceType.PARCEL_DELIVERY.name

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedOrderForReceipt = order }
                        .testTag("history_order_card_${order.id}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(
                                            if (isCargo) AutoAmber.copy(alpha = 0.15f)
                                            else SafaaGreen.copy(alpha = 0.15f),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isCargo) Icons.Default.Inventory2 else Icons.Default.ElectricBolt,
                                        contentDescription = null,
                                        tint = if (isCargo) AutoAmber else SafaaGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isCargo) "Cargo Delivery" else "Passenger Ride",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(order.timestamp)),
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color.Gray)
                                    )
                                }
                            }

                            Text(
                                text = "रू ${order.fareNpr}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isCargo) AutoAmber else SafaaGreen
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Route details
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).background(SafaaGreen, CircleShape))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = order.pickupAddress,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                maxLines = 1
                            )
                        }

                        Box(modifier = Modifier.padding(start = 3.dp).width(2.dp).height(8.dp).background(Color.LightGray))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).background(AutoAmber, CircleShape))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = order.dropoffAddress,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                maxLines = 1
                            )
                        }

                        // Driver & Rating Review Row
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${if (isNepali) "चालक" else "Driver"}: ${order.driverName ?: (if (isNepali) "प्रमाणित चालक" else "Verified Driver")}",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray, fontSize = 11.sp),
                                maxLines = 1
                            )

                            if (order.rating != null) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = NepalGold,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "${order.rating} ★",
                                        color = Color(0xFFB78103),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = NepalGold.copy(alpha = 0.15f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, NepalGold),
                                    modifier = Modifier.clickable { selectedOrderForRating = order }.testTag("rate_trip_btn_${order.id}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = NepalGold, modifier = Modifier.size(13.dp))
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = if (isNepali) "मूल्याङ्कन दिनुहोस्" else "Rate Driver",
                                            color = Color(0xFFB78103),
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                }
                            }
                        }

                        if (!order.reviewFeedback.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "\"${order.reviewFeedback}\"",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        color = Color.DarkGray,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    maxLines = 1
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Bottom badges: Distance, Payment method, and Receipt button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFF1F8E9)
                                ) {
                                    Text(
                                        text = "${order.distanceKm} km",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = when (order.paymentMethod) {
                                        PaymentMethod.ESEWA.name -> EsewaGreen.copy(alpha = 0.15f)
                                        PaymentMethod.KHALTI.name -> KhaltiPurple.copy(alpha = 0.15f)
                                        PaymentMethod.NEPALESE_BANK.name -> FonepayRed.copy(alpha = 0.15f)
                                        else -> SafaaGreen.copy(alpha = 0.15f)
                                    }
                                ) {
                                    Text(
                                        text = when (order.paymentMethod) {
                                            PaymentMethod.ESEWA.name -> "eSewa"
                                            PaymentMethod.KHALTI.name -> "Khalti"
                                            PaymentMethod.NEPALESE_BANK.name -> "Bank"
                                            else -> "Cash"
                                        },
                                        color = when (order.paymentMethod) {
                                            PaymentMethod.ESEWA.name -> EsewaGreen
                                            PaymentMethod.KHALTI.name -> KhaltiPurple
                                            PaymentMethod.NEPALESE_BANK.name -> FonepayRed
                                            else -> SafaaGreen
                                        },
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Text(
                                text = if (isNepali) "रसिद हेर्नुहोस्" else "View Receipt",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = SafaaGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    selectedOrderForReceipt?.let { order ->
        DigitalReceiptDialog(
            order = order,
            isNepali = isNepali,
            onDismiss = { selectedOrderForReceipt = null },
            onRateOrder = { targetOrder ->
                selectedOrderForRating = targetOrder
            }
        )
    }

    selectedOrderForRating?.let { order ->
        RatingReviewDialog(
            driver = null,
            driverNameFallback = order.driverName,
            driverPlateFallback = order.driverPlate,
            driverVehicleFallback = order.driverVehicle,
            isNepali = isNepali,
            onDismiss = { selectedOrderForRating = null },
            onSubmit = { rating, feedback ->
                viewModel.ratePastTrip(order.id, order.driverId, rating, feedback)
                selectedOrderForRating = null
            }
        )
    }
}

@Composable
fun DigitalReceiptDialog(
    order: RideOrderEntity,
    isNepali: Boolean,
    onDismiss: () -> Unit,
    onRateOrder: (RideOrderEntity) -> Unit = {}
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(8.dp).testTag("digital_receipt_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isNepali) "डिजिटल भुक्तानी रसिद" else "Digital Payment Receipt",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = SafaaGreen,
                    modifier = Modifier.size(48.dp)
                )

                Text(
                    text = "रू ${order.fareNpr}",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = SafaaGreen
                    )
                )

                Text(
                    text = "${if (isNepali) "भुक्तानी सम्पन्न" else "Payment Confirmed"} • ${order.paymentMethod}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                ReceiptRow(label = "TXN Ref:", value = order.transactionRef ?: "N/A")
                ReceiptRow(label = "Driver:", value = "${order.driverName ?: "Verified Driver"} (${order.driverPlate ?: ""})")
                ReceiptRow(label = "Pickup:", value = order.pickupAddress)
                ReceiptRow(label = "Destination:", value = order.dropoffAddress)
                ReceiptRow(label = "Distance:", value = "${order.distanceKm} km")
                ReceiptRow(label = "Date:", value = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(order.timestamp)))

                // Rating & Review section inside receipt
                Spacer(modifier = Modifier.height(10.dp))
                if (order.rating != null) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = NepalGold.copy(alpha = 0.12f),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isNepali) "तपाईंको मूल्याङ्कन" else "Driver Review & Rating",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFB78103)
                                    )
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    for (i in 1..5) {
                                        val isFilled = i <= (order.rating ?: 0f)
                                        Icon(
                                            imageVector = if (isFilled) Icons.Default.Star else Icons.Default.StarBorder,
                                            contentDescription = null,
                                            tint = if (isFilled) NepalGold else Color.Gray,
                                            modifier = Modifier.size(15.dp)
                                        )
                                    }
                                }
                            }
                            if (!order.reviewFeedback.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "\"${order.reviewFeedback}\"",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                        color = Color.DarkGray
                                    )
                                )
                            }
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (isNepali) "चालकलाई मूल्याङ्कन दिनुहोस्" else "Rate this Driver",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = if (isNepali) "नेपालमा विश्वासिलो सेवा बनाउनुहोस्" else "Help build community trust",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray, fontSize = 10.sp)
                                )
                            }
                            Button(
                                onClick = {
                                    onRateOrder(order)
                                    onDismiss()
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NepalGold),
                                modifier = Modifier.testTag("rate_from_receipt_btn")
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isNepali) "तारा दिनुहोस्" else "Rate",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(46.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SafaaGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (isNepali) "बन्द गर्नुहोस्" else "Done", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun ReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 1
        )
    }
}
