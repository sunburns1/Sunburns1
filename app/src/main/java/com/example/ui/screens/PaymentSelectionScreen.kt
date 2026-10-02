package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LocalHubs
import com.example.data.model.PaymentMethod
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.EsewaGreen
import com.example.ui.theme.FonepayRed
import com.example.ui.theme.KhaltiPurple
import com.example.ui.theme.NepalGold
import com.example.ui.theme.SafaaGreen
import com.example.ui.viewmodel.RideViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PaymentSelectionScreen(
    viewModel: RideViewModel,
    modifier: Modifier = Modifier,
    onPaymentConfirmed: (() -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val activeHub by viewModel.activeHub.collectAsState()
    val isNepali = uiState.isNepaliLanguage
    val payableFare = viewModel.calculateFareNpr()
    val tripDistKm = viewModel.getTripDistanceKm()

    var showTariffComparison by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Column {
                Text(
                    text = if (isNepali) "भुक्तानी विधि छनोट (Payment Selection)" else "Payment Method Selection",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = SafaaGreen
                    )
                )
                Text(
                    text = if (isNepali)
                        "ई-सेवा, खल्ती, नेपाली बैंक (फोनपे) वा नगद भुक्तानी गर्नुहोस्"
                    else
                        "Integrates simulated eSewa & Khalti APIs, Nepalese Banks & Cash",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
        }

        // Active Trip Fare Summary Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().testTag("payment_fare_summary_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isNepali) "तिर्नुपर्ने कुल भाडा" else "Payable Trip Fare",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                            )
                            Text(
                                text = "रू $payableFare",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = SafaaGreen
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = if (isNepali) uiState.selectedVehicle.titleNp else uiState.selectedVehicle.titleEn,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = SafaaGreen
                                    )
                                )
                                Text(
                                    text = "$tripDistKm km • ${activeHub.cityTariff.cityName}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color.Gray)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${uiState.pickupName.take(18)} → ${uiState.dropoffName.take(18)}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            maxLines = 1
                        )
                        Text(
                            text = "${activeHub.cityTariff.tariffSource.take(24)}...",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp, color = Color.Gray)
                        )
                    }
                }
            }
        }

        // Payment Methods Grid Selector (eSewa, Khalti, Bank, Cash)
        item {
            Text(
                text = if (isNepali) "भुक्तानी सेवा छान्नुहोस् (Select Gateway):" else "Select Payment Gateway:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) brandColor else MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) brandColor else Color(0xFFE0E0E0)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.setPaymentMethod(method) }
                            .testTag("select_gateway_${method.name.lowercase()}")
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
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
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = when (method) {
                                    PaymentMethod.ESEWA -> "eSewa"
                                    PaymentMethod.KHALTI -> "Khalti"
                                    PaymentMethod.NEPALESE_BANK -> "Bank/QR"
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
        }

        // Active Payment Method Simulated API Element Box
        item {
            when (uiState.selectedPaymentMethod) {
                PaymentMethod.ESEWA -> {
                    EsewaApiSimulatedCard(
                        fareNpr = payableFare,
                        isNepali = isNepali,
                        onPaymentSuccess = { ref ->
                            viewModel.confirmPayment(ref)
                            onPaymentConfirmed?.invoke()
                        }
                    )
                }
                PaymentMethod.KHALTI -> {
                    KhaltiApiSimulatedCard(
                        fareNpr = payableFare,
                        isNepali = isNepali,
                        onPaymentSuccess = { ref ->
                            viewModel.confirmPayment(ref)
                            onPaymentConfirmed?.invoke()
                        }
                    )
                }
                PaymentMethod.NEPALESE_BANK -> {
                    NepaleseBankFonepayCard(
                        fareNpr = payableFare,
                        isNepali = isNepali,
                        onPaymentSuccess = { ref ->
                            viewModel.confirmPayment(ref)
                            onPaymentConfirmed?.invoke()
                        }
                    )
                }
                PaymentMethod.CASH -> {
                    CashPaymentCard(
                        fareNpr = payableFare,
                        isNepali = isNepali,
                        onPaymentSuccess = { ref ->
                            viewModel.confirmPayment(ref)
                            onPaymentConfirmed?.invoke()
                        }
                    )
                }
            }
        }

        // Cities & Transportation Charge Tariff Guide (Narayanghat, Biratnagar, Kathmandu, etc.)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showTariffComparison = !showTariffComparison }
                    .testTag("tariff_comparison_toggle_card")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = null,
                                tint = SafaaGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isNepali) "सहर अनुसारको आधिकारिक भाडादर (City Tariffs)" else "Transportation Charges by City",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        Icon(
                            imageVector = if (showTariffComparison) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null
                        )
                    }

                    if (showTariffComparison) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (isNepali)
                                "नारायणगढ, विराटनगर, काठमाडौँ र अन्य सहरका अटो तथा ई-रिक्सा दरहरू:"
                            else
                                "Transportation charges & rates across Nepal's auto & e-rickshaw cities:",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        LocalHubs.ALL_HUBS.forEach { hub ->
                            val isCurrent = hub.id == activeHub.id
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isCurrent) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surface,
                                border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.5.dp, SafaaGreen) else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { viewModel.selectActiveHub(hub) }
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = if (isNepali) hub.nameNp else hub.nameEn,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        if (isCurrent) {
                                            Surface(color = SafaaGreen, shape = RoundedCornerShape(6.dp)) {
                                                Text(
                                                    text = "Active",
                                                    color = Color.White,
                                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "• E-Rickshaw: Rs ${hub.cityTariff.eRickshawBaseNpr} base + ${hub.cityTariff.eRickshawPerKmNpr}/km\n" +
                                                "• Auto Rickshaw: Rs ${hub.cityTariff.autoRickshawBaseNpr} base + ${hub.cityTariff.autoRickshawPerKmNpr}/km\n" +
                                                "• Cargo Delivery: Rs ${hub.cityTariff.cargoBaseNpr} base + ${hub.cityTariff.cargoPerKmNpr}/km",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color.DarkGray)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun EsewaApiSimulatedCard(
    fareNpr: Int,
    isNepali: Boolean,
    onPaymentSuccess: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var esewaPhone by remember { mutableStateOf("9845123456") }
    var mpin by remember { mutableStateOf("1234") }
    var isProcessing by remember { mutableStateOf(false) }
    var transactionResult by remember { mutableStateOf<String?>(null) }
    var otpSent by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }

    val simulatedWalletBalance = 5420.50
    val cashbackNpr = (fareNpr * 0.05).toInt()

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, EsewaGreen.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth().testTag("esewa_api_simulated_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // eSewa Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = EsewaGreen,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "eSewa Direct API",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = EsewaGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "NPR $simulatedWalletBalance",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = EsewaGreen)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Eco Cashback banner
            Surface(
                color = Color(0xFFF1F8E9),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = EsewaGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isNepali)
                            "सफा ई-रिक्सा छुट: ५% क्यासब्याक (रू $cashbackNpr बचत)"
                        else
                            "Eco E-Rickshaw Offer: 5% Cashback (Save Rs $cashbackNpr)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = esewaPhone,
                onValueChange = { esewaPhone = it },
                label = { Text(if (isNepali) "ई-सेवा दर्ता मोबाइल नं." else "eSewa Mobile ID (+977)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth().testTag("screen_esewa_phone_input"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = mpin,
                onValueChange = { mpin = it },
                label = { Text(if (isNepali) "४ अंकको गोप्य MPIN" else "4-digit Secure MPIN") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().testTag("screen_esewa_mpin_input"),
                singleLine = true
            )

            if (otpSent) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = otpCode,
                    onValueChange = { otpCode = it },
                    label = { Text(if (isNepali) "६ अंकको SMS OTP (प्रविष्ट: 841029)" else "6-digit SMS OTP (Enter: 841029)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("screen_esewa_otp_input"),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (!otpSent) {
                OutlinedButton(
                    onClick = {
                        otpSent = true
                        otpCode = "841029"
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("esewa_request_otp_btn")
                ) {
                    Text(if (isNepali) "SMS OTP प्राप्त गर्नुहोस्" else "Request eSewa SMS OTP")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    isProcessing = true
                    coroutineScope.launch {
                        delay(1200)
                        val ref = "ESW-NEP-${(100000..999999).random()}"
                        transactionResult = ref
                        isProcessing = false
                        onPaymentSuccess(ref)
                    }
                },
                enabled = !isProcessing,
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("pay_esewa_api_submit_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = EsewaGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Authorizing eSewa API...", color = Color.White)
                } else {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${if (isNepali) "ई-सेवाबाट भुक्तानी गर्नुहोस्" else "Authorize & Pay via eSewa"} (रू $fareNpr)",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            transactionResult?.let { ref ->
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "✅ Payment Approved! Ref: $ref",
                        color = EsewaGreen,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun KhaltiApiSimulatedCard(
    fareNpr: Int,
    isNepali: Boolean,
    onPaymentSuccess: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var khaltiPhone by remember { mutableStateOf("9813245678") }
    var mpin by remember { mutableStateOf("5678") }
    var isProcessing by remember { mutableStateOf(false) }
    var otpSent by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }
    var transactionResult by remember { mutableStateOf<String?>(null) }

    val simulatedWalletBalance = 3850.00
    val khaltiPoints = 1250

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, KhaltiPurple.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth().testTag("khalti_api_simulated_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = KhaltiPurple,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Khalti Smart API",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = KhaltiPurple, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "NPR $simulatedWalletBalance",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = KhaltiPurple)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = Color(0xFFF3E5F5),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💎 $khaltiPoints Khalti Points • Earn +${(fareNpr * 0.1).toInt()} KP this ride",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = KhaltiPurple,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = khaltiPhone,
                onValueChange = { khaltiPhone = it },
                label = { Text(if (isNepali) "खल्ती दर्ता नम्बर" else "Khalti Mobile ID (+977)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth().testTag("screen_khalti_phone_input"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = mpin,
                onValueChange = { mpin = it },
                label = { Text(if (isNepali) "४ अंकको खल्ती MPIN" else "4-digit Khalti MPIN") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().testTag("screen_khalti_mpin_input"),
                singleLine = true
            )

            if (otpSent) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = otpCode,
                    onValueChange = { otpCode = it },
                    label = { Text(if (isNepali) "खल्ती OTP (प्रविष्ट: 681920)" else "Khalti OTP (Enter: 681920)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth().testTag("screen_khalti_otp_input"),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (!otpSent) {
                OutlinedButton(
                    onClick = {
                        otpSent = true
                        otpCode = "681920"
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().testTag("khalti_request_otp_btn")
                ) {
                    Text(if (isNepali) "खल्ती OTP कोड पठाउनुहोस्" else "Request Khalti OTP SMS")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    isProcessing = true
                    coroutineScope.launch {
                        delay(1200)
                        val ref = "KHL-API-${(100000..999999).random()}"
                        transactionResult = ref
                        isProcessing = false
                        onPaymentSuccess(ref)
                    }
                },
                enabled = !isProcessing,
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("pay_khalti_api_submit_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = KhaltiPurple),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Verifying Khalti...", color = Color.White)
                } else {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${if (isNepali) "खल्तीबाट भुक्तानी पक्का गर्नुहोस्" else "Confirm Khalti Payment"} (रू $fareNpr)",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            transactionResult?.let { ref ->
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = Color(0xFFF3E5F5),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "✅ Khalti Authorized! Token: $ref",
                        color = KhaltiPurple,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NepaleseBankFonepayCard(
    fareNpr: Int,
    isNepali: Boolean,
    onPaymentSuccess: (String) -> Unit
) {
    val nepaliBanks = listOf(
        "Nabil Bank",
        "Global IME Bank",
        "NIC Asia Bank",
        "Nepal Bank Ltd",
        "Rastriya Banijya Bank",
        "Siddhartha Bank"
    )
    var selectedBank by remember { mutableStateOf(nepaliBanks[0]) }
    var txnVoucher by remember { mutableStateOf("FONE-849102") }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, FonepayRed.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth().testTag("nepalese_bank_card")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = FonepayRed,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Fonepay QR & Nepalese Bank Direct",
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(nepaliBanks) { bank ->
                    val isSelected = bank == selectedBank
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) FonepayRed else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.clickable { selectedBank = bank }
                    ) {
                        Text(
                            text = bank,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dynamic Fonepay QR Simulation Frame
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFFAFAFA),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE0E0E0)),
                modifier = Modifier.size(130.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode2,
                        contentDescription = "Fonepay QR",
                        tint = FonepayRed,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "Fonepay Scan & Pay",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = FonepayRed,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Merchant: Safaa Rickshaw Transport Union",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    )
                    Text(
                        text = "A/C: 019283746501 ($selectedBank)",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color.Gray)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = txnVoucher,
                onValueChange = { txnVoucher = it },
                label = { Text(if (isNepali) "बैंक सन्दर्भ / भाउचर नं." else "Bank Transaction Reference Number") },
                modifier = Modifier.fillMaxWidth().testTag("screen_bank_txn_input"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    val ref = "BNK-${selectedBank.take(3).uppercase()}-${(10000..99999).random()}"
                    onPaymentSuccess(ref)
                },
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("screen_confirm_bank_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = FonepayRed),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.AccountBalance, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${if (isNepali) "बैंक भुक्तानी पक्का गर्नुहोस्" else "Confirm Bank Transfer"} (रू $fareNpr)",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun CashPaymentCard(
    fareNpr: Int,
    isNepali: Boolean,
    onPaymentSuccess: (String) -> Unit
) {
    val commonNotes = listOf(50, 100, 200, 500, 1000)
    var cashGiven by remember { mutableIntStateOf(if (fareNpr <= 100) 100 else 500) }
    val changeToReturn = (cashGiven - fareNpr).coerceAtLeast(0)

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, SafaaGreen.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth().testTag("cash_payment_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = SafaaGreen,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (isNepali) "चालकलाई नगद भुक्तानी" else "Cash on Ride (COD)",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = "Total: रू $fareNpr",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = SafaaGreen)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (isNepali) "चालकलाई दिइने रुपैयाँ (Cash Note Given):" else "Select Cash Note Handed to Driver:",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                commonNotes.forEach { note ->
                    val isSelected = note == cashGiven
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) SafaaGreen else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { cashGiven = note }
                    ) {
                        Text(
                            text = "रू $note",
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(vertical = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                color = Color(0xFFF1F8E9),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (isNepali) "चालकले फिर्ता गर्नुपर्ने रकम:" else "Change to Receive from Driver:",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF33691E))
                        )
                        Text(
                            text = "रू $changeToReturn",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = SafaaGreen
                            )
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Money,
                        contentDescription = null,
                        tint = SafaaGreen,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onPaymentSuccess("CASH-ON-DELIVERY")
                },
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("select_cash_confirm_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = SafaaGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isNepali) "नगद भुक्तानी छान्नुहोस्" else "Confirm Cash on Ride",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
