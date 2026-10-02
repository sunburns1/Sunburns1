package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DriverProfile
import com.example.data.model.VehicleType
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.NepalGold
import com.example.ui.theme.SafaaGreen
import com.example.ui.viewmodel.RideViewModel

@Composable
fun DriversDirectoryScreen(
    viewModel: RideViewModel,
    modifier: Modifier = Modifier
) {
    val drivers by viewModel.drivers.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val isNepali = uiState.isNepaliLanguage
    val context = LocalContext.current

    var selectedFilter by remember { mutableStateOf<VehicleType?>(null) }
    var filterTopRatedOnly by remember { mutableStateOf(false) }

    val filteredDrivers = drivers
        .filter { selectedFilter == null || it.vehicleType == selectedFilter }
        .filter { !filterTopRatedOnly || it.rating >= 4.85f }

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
                    text = if (isNepali) "स्थानीय रिक्सा चालक निर्देशिका" else "Local Rickshaw Drivers Directory",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = SafaaGreen
                    )
                )
                Text(
                    text = if (isNepali) "प्रमाणित चालक, गाडी मोडल, नम्बर प्लेट र मूल्याङ्कन" else "Verified drivers, vehicle models, Nepali plates & customer ratings",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
        }

        // Category Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    val isAll = selectedFilter == null
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isAll) SafaaGreen else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { selectedFilter = null }.testTag("filter_all_drivers")
                    ) {
                        Text(
                            text = if (isNepali) "सबै (${drivers.size})" else "All Drivers (${drivers.size})",
                            color = if (isAll) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }

                items(VehicleType.values()) { vType ->
                    val isSelected = selectedFilter == vType
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) (if (vType.isElectric) SafaaGreen else AutoAmber) else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { selectedFilter = vType }.testTag("filter_${vType.name.lowercase()}")
                    ) {
                        Text(
                            text = if (isNepali) vType.titleNp else vType.titleEn,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }

                item {
                    val isTopRatedSelected = filterTopRatedOnly
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isTopRatedSelected) NepalGold else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { filterTopRatedOnly = !filterTopRatedOnly }.testTag("filter_top_rated")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (isTopRatedSelected) Color.White else NepalGold,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isNepali) "उत्कृष्ट (४.८+ ★)" else "Top Rated (4.8+ ★)",
                                color = if (isTopRatedSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        items(filteredDrivers) { driver ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.openDriverDetail(driver) }
                    .testTag("driver_card_${driver.id}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .background(
                                        if (driver.vehicleType == VehicleType.E_RICKSHAW) SafaaGreen.copy(alpha = 0.15f)
                                        else AutoAmber.copy(alpha = 0.15f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = if (driver.vehicleType == VehicleType.E_RICKSHAW) SafaaGreen else AutoAmber,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = driver.name,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Verified",
                                        tint = SafaaGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Text(
                                    text = driver.nameNp,
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = NepalGold,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "${driver.rating}",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = " (${driver.reviews.size} ${if (isNepali) "समीक्षा" else "reviews"}) • ${driver.totalTrips} ${if (isNepali) "सवारी" else "trips"}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp)
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${driver.phone}")
                                }
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .background(SafaaGreen, CircleShape)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Call", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }

                    if (driver.reviews.isNotEmpty()) {
                        val topRev = driver.reviews.first()
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = NepalGold,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${topRev.reviewerName}: \"${topRev.comment}\"",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        color = Color.DarkGray,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                    ),
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Vehicle & Plate Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = driver.vehicleModel,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF0D47A1),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = driver.licensePlate,
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { viewModel.openDriverDetail(driver) }
                        ) {
                            Text(
                                text = if (isNepali) "विवरण र समीक्षा" else "View Reviews",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = SafaaGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = SafaaGreen,
                                modifier = Modifier.size(16.dp)
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
}
