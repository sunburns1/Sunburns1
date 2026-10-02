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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.RiderProfileDialog
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.SafaaGreen
import com.example.ui.viewmodel.RideViewModel

@Composable
fun CustomerProfileScreen(
    viewModel: RideViewModel,
    modifier: Modifier = Modifier
) {
    val riderProfile by viewModel.riderProfile.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val isNepali = uiState.isNepaliLanguage

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (isNepali) "ग्राहक तथा यात्रु प्रोफाइल" else "Customer & Rider Profile",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = SafaaGreen
                )
            )
        }

        // Rider Profile Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().testTag("customer_profile_main_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(SafaaGreen.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = SafaaGreen,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = riderProfile.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = riderProfile.phone,
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                )
                                Text(
                                    text = "${allOrders.size} ${if (isNepali) "सवारी सम्पन्न" else "Completed Trips"}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = SafaaGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        IconButton(
                            onClick = { viewModel.openRiderProfile(true) },
                            modifier = Modifier
                                .size(36.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = SafaaGreen, modifier = Modifier.size(18.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Contact & Emergency Details
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = riderProfile.email, style = MaterialTheme.typography.bodySmall)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.WarningAmber, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${if (isNepali) "आपतकालीन (SOS): " else "Emergency Contact: "}${riderProfile.emergencyContact}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Live Location Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = AutoAmber)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isNepali) "ग्राहकको हालको लोकेसन" else "Customer Current Location",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = riderProfile.currentAddress,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "GPS Coordinates: ${riderProfile.currentLat}° N, ${riderProfile.currentLng}° E",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp)
                    )
                }
            }
        }

        // Saved Places
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isNepali) "सुरक्षित ठाउँहरू (Saved Places)" else "Saved Places & Favorite Toles",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    riderProfile.savedPlaces.forEach { place ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    viewModel.setPickupCustom(place.address, place.lat, place.lng)
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = when (place.iconType) {
                                            "school" -> Icons.Default.School
                                            "store" -> Icons.Default.Store
                                            else -> Icons.Default.Home
                                        },
                                        contentDescription = null,
                                        tint = SafaaGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(text = place.label, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                        Text(text = place.address, style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp))
                                    }
                                }

                                Text(
                                    text = if (isNepali) "सवारी बोलाउनुहोस्" else "Set as Pickup",
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
        }

        // App Information & Radius Notice
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFE8F5E9),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isNepali) "सफा रिक्सा नेटवर्क (१०-२० कि.मी.)" else "Safaa Rickshaw Network (10-20 km)",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = SafaaGreen)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isNepali)
                            "यो एप स्थानीय क्षेत्रको १० देखि २० किलोमिटर दायराभित्र ई-रिक्सा, अटो टेम्पो र कार्गो डेलिभरीका लागि समर्पित छ। भुक्तानी ई-सेवा, खल्ती र नेपाली बैंकबाट सुरक्षित रूपमा गर्न सकिन्छ।"
                        else
                            "This app operates exclusively within a 10 to 20 km local radius for safe electric safari rickshaws, auto tempos and parcel logistics in Nepal with eSewa, Khalti, and bank QR payments.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF2E7D32), fontSize = 11.sp)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
