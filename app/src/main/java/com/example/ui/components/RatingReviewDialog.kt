package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.example.data.model.DriverProfile
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.NepalGold
import com.example.ui.theme.SafaaGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RatingReviewDialog(
    driver: DriverProfile?,
    driverNameFallback: String? = null,
    driverPlateFallback: String? = null,
    driverVehicleFallback: String? = null,
    isNepali: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (rating: Float, feedback: String) -> Unit
) {
    var selectedStars by remember { mutableFloatStateOf(5.0f) }
    var reviewText by remember { mutableStateOf("") }
    val selectedTags = remember { mutableStateListOf<String>() }

    val driverName = driver?.name ?: driverNameFallback ?: if (isNepali) "सफा रिक्सा चालक" else "Safaa Driver"
    val vehicleModel = driver?.vehicleModel ?: driverVehicleFallback ?: "Mayuri Pro Deluxe Electric Safari"
    val licensePlate = driver?.licensePlate ?: driverPlateFallback ?: "बा २ ह ४५८९"

    val starDescription = when (selectedStars.toInt()) {
        5 -> if (isNepali) "उत्कृष्ट तथा सुरक्षित (Outstanding & Safe) 🌟" else "Outstanding & Safe Experience 🌟"
        4 -> if (isNepali) "धेरै राम्रो यात्रा (Very Good Ride) 👍" else "Very Good & Pleasant 👍"
        3 -> if (isNepali) "सामान्य सन्तोषजनक (Satisfactory) ⚖️" else "Average / Satisfactory ⚖️"
        2 -> if (isNepali) "सुधार गर्नुपर्ने (Needs Improvement) ⚠️" else "Needs Improvement ⚠️"
        else -> if (isNepali) "खराब अनुभव (Poor Service) ❌" else "Poor Experience ❌"
    }

    val availableTags = if (isNepali) listOf(
        "सफा ई-रिक्सा",
        "समयमै आइपुग्यो",
        "शान्त र सुरक्षित",
        "मित्रवत चालक",
        "उचित संघ भाडा",
        "डिजिटल भुक्तानी"
    ) else listOf(
        "Clean E-Rickshaw",
        "Arrived On Time",
        "Smooth & Safe Driving",
        "Polite & Friendly",
        "Fair Union Rate",
        "Seamless Digital Pay"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("rating_review_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = SafaaGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isNepali) "विश्वास र मूल्याङ्कन" else "Driver Trust & Review",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = SafaaGreen
                            )
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp).testTag("dismiss_rating_dialog_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (isNepali) "यात्रा सम्पन्न भयो! 🎉" else "Trip Completed! 🎉",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = SafaaGreen
                    )
                )

                Text(
                    text = if (isNepali) "चालकको सेवा कस्तो लाग्यो? तारा छान्नुहोस्:" else "How was your ride experience? Tap to rate:",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Compact Driver Info Card
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(SafaaGreen.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = SafaaGreen)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = driverName,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = SafaaGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = vehicleModel,
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp),
                                maxLines = 1
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF0D47A1)
                        ) {
                            Text(
                                text = licensePlate.take(14),
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 5-Star Interactive Rating Bar
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..5) {
                        val isFilled = i <= selectedStars
                        Icon(
                            imageVector = if (isFilled) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Star $i",
                            tint = if (isFilled) NepalGold else Color.LightGray,
                            modifier = Modifier
                                .size(44.dp)
                                .clickable { selectedStars = i.toFloat() }
                                .padding(2.dp)
                                .testTag("star_rate_$i")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Live Star Description Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when {
                        selectedStars >= 4f -> SafaaGreen.copy(alpha = 0.12f)
                        selectedStars >= 3f -> AutoAmber.copy(alpha = 0.15f)
                        else -> Color(0xFFFFEBEE)
                    }
                ) {
                    Text(
                        text = starDescription,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = when {
                                selectedStars >= 4f -> SafaaGreen
                                selectedStars >= 3f -> AutoAmber
                                else -> Color(0xFFC62828)
                            }
                        ),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Compliment / Feedback Chips
                Text(
                    text = if (isNepali) "सवारीका राम्रा पक्षहरू छान्नुहोस्:" else "What went well? (Select compliments):",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.DarkGray),
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    availableTags.forEach { tag ->
                        val isSelected = selectedTags.contains(tag)
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) SafaaGreen else MaterialTheme.colorScheme.surfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) SafaaGreen else Color.Transparent
                            ),
                            modifier = Modifier
                                .clickable {
                                    if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                                }
                                .testTag("tag_chip_${tag.replace(" ", "_")}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.ThumbUp,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(
                                    text = tag,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Written review text input
                OutlinedTextField(
                    value = reviewText,
                    onValueChange = { if (it.length <= 160) reviewText = it },
                    label = { Text(if (isNepali) "थप प्रतिक्रिया लेख्नुहोस् (वैकल्पिक)" else "Write a review (optional)") },
                    placeholder = {
                        Text(
                            text = if (isNepali) "चालकको व्यवहार, समय र सवारीको अवस्था बारे लेख्नुहोस्..." else "Share details about safety, comfort, or local landmarks...",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    },
                    modifier = Modifier.fillMaxWidth().testTag("review_comment_input"),
                    minLines = 2,
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    supportingText = {
                        Text(
                            text = "${reviewText.length}/160",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.End
                        )
                    }
                )

                // Community Trust Note
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF1F8E9),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = SafaaGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isNepali)
                                "तपाईंको मूल्याङ्कनले नेपालमा स्थानीय चालकहरूको विश्वसनीयता बढाउँछ।"
                            else
                                "Your review builds community trust for verified local drivers in Nepal.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = SafaaGreen)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons: Submit & Maybe Later
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(48.dp).testTag("skip_rating_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isNepali) "पछि गर्ने" else "Skip",
                            color = Color.Gray,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = {
                            val combinedFeedback = buildString {
                                if (selectedTags.isNotEmpty()) {
                                    append(selectedTags.joinToString(", "))
                                }
                                if (reviewText.isNotBlank()) {
                                    if (isNotEmpty()) append(". ")
                                    append(reviewText.trim())
                                }
                            }
                            onSubmit(
                                selectedStars,
                                if (combinedFeedback.isBlank()) {
                                    if (isNepali) "राम्रो र सुरक्षित यात्रा!" else "Great and safe ride!"
                                } else combinedFeedback
                            )
                        },
                        modifier = Modifier.weight(2f).height(48.dp).testTag("submit_rating_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = SafaaGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (isNepali) "मूल्याङ्कन पेश गर्नुहोस्" else "Submit Rating",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
