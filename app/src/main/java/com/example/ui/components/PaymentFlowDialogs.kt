package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.window.Dialog
import com.example.data.model.PaymentMethod
import com.example.ui.theme.EsewaGreen
import com.example.ui.theme.FonepayRed
import com.example.ui.theme.KhaltiPurple
import com.example.ui.theme.SafaaGreen

@Composable
fun PaymentDialog(
    method: PaymentMethod,
    amountNpr: Int,
    isNepali: Boolean,
    onDismiss: () -> Unit,
    onPaymentSuccess: (transactionRef: String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("payment_modal_card"),
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
                    Text(
                        text = if (isNepali) "भुक्तानी विवरण" else "Payment Details",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp).testTag("close_payment_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Amount Pill
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isNepali) "जम्मा तिर्नुपर्ने रकम" else "Total Payable Fare",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                        Text(
                            text = "रू $amountNpr",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (method) {
                    PaymentMethod.ESEWA -> {
                        EsewaPaymentView(
                            amountNpr = amountNpr,
                            isNepali = isNepali,
                            onSuccess = onPaymentSuccess
                        )
                    }
                    PaymentMethod.KHALTI -> {
                        KhaltiPaymentView(
                            amountNpr = amountNpr,
                            isNepali = isNepali,
                            onSuccess = onPaymentSuccess
                        )
                    }
                    PaymentMethod.NEPALESE_BANK -> {
                        NepaleseBankPaymentView(
                            amountNpr = amountNpr,
                            isNepali = isNepali,
                            onSuccess = onPaymentSuccess
                        )
                    }
                    PaymentMethod.CASH -> {
                        CashPaymentView(
                            amountNpr = amountNpr,
                            isNepali = isNepali,
                            onSuccess = onPaymentSuccess
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EsewaPaymentView(
    amountNpr: Int,
    isNepali: Boolean,
    onSuccess: (String) -> Unit
) {
    var esewaId by remember { mutableStateOf("9845123456") }
    var mpin by remember { mutableStateOf("1234") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // eSewa Brand Badge
        Surface(
            color = EsewaGreen,
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "eSewa",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Digital Wallet",
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = esewaId,
            onValueChange = { esewaId = it },
            label = { Text(if (isNepali) "ई-सेवा आइडी / मोबाइल नं." else "eSewa ID / Mobile Number") },
            modifier = Modifier.fillMaxWidth().testTag("esewa_id_input"),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = mpin,
            onValueChange = { mpin = it },
            label = { Text(if (isNepali) "४ अंकको MPIN" else "4-digit MPIN") },
            modifier = Modifier.fillMaxWidth().testTag("esewa_mpin_input"),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = {
                val ref = "ESW-${(100000..999999).random()}"
                onSuccess(ref)
            },
            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("confirm_esewa_pay_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = EsewaGreen),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${if (isNepali) "ई-सेवाबाट तिर्नुहोस्" else "Pay via eSewa"} (रू $amountNpr)",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun KhaltiPaymentView(
    amountNpr: Int,
    isNepali: Boolean,
    onSuccess: (String) -> Unit
) {
    var khaltiId by remember { mutableStateOf("9813245678") }
    var mpin by remember { mutableStateOf("5678") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = KhaltiPurple,
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Khalti",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Smart Wallet",
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = khaltiId,
            onValueChange = { khaltiId = it },
            label = { Text(if (isNepali) "खल्ती दर्ता नं." else "Khalti Mobile Number") },
            modifier = Modifier.fillMaxWidth().testTag("khalti_id_input"),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = mpin,
            onValueChange = { mpin = it },
            label = { Text(if (isNepali) "खल्ती पिन (MPIN)" else "Khalti MPIN / OTP") },
            modifier = Modifier.fillMaxWidth().testTag("khalti_mpin_input"),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = {
                val ref = "KHL-${(100000..999999).random()}"
                onSuccess(ref)
            },
            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("confirm_khalti_pay_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = KhaltiPurple),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${if (isNepali) "खल्तीबाट तिर्नुहोस्" else "Pay via Khalti"} (रू $amountNpr)",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun NepaleseBankPaymentView(
    amountNpr: Int,
    isNepali: Boolean,
    onSuccess: (String) -> Unit
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
    var txnRef by remember { mutableStateOf("FONE-99824") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isNepali) "बैंक छान्नुहोस् (Select Bank):" else "Select Nepalese Bank:",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(nepaliBanks) { bank ->
                val isSelected = bank == selectedBank
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) FonepayRed else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.clickable { selectedBank = bank }
                ) {
                    Text(
                        text = bank,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Fonepay QR Simulation Frame
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF9F9F9),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE0E0E0)),
            modifier = Modifier.size(140.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.QrCode2,
                    contentDescription = "Fonepay QR",
                    tint = FonepayRed,
                    modifier = Modifier.size(68.dp)
                )
                Text(
                    text = "Fonepay QR Code",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = FonepayRed
                    )
                )
                Text(
                    text = "Safaa Rickshaw Union",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 9.sp,
                        color = Color.Gray
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = txnRef,
            onValueChange = { txnRef = it },
            label = { Text(if (isNepali) "बैंक भाउचर / सन्दर्भ नं." else "Bank Transaction Reference") },
            modifier = Modifier.fillMaxWidth().testTag("bank_txn_input"),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = {
                val ref = "BNK-${selectedBank.take(3).uppercase()}-${(10000..99999).random()}"
                onSuccess(ref)
            },
            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("confirm_bank_pay_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = FonepayRed),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.AccountBalance, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${if (isNepali) "बैंकबाट भुक्तानी पक्का गर्नुहोस्" else "Confirm Bank Transfer"} (रू $amountNpr)",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun CashPaymentView(
    amountNpr: Int,
    isNepali: Boolean,
    onSuccess: (String) -> Unit
) {
    val commonNotes = listOf(100, 200, 500, 1000)
    var cashGiven by remember { mutableIntStateOf(if (amountNpr <= 100) 100 else 500) }
    val changeToReturn = (cashGiven - amountNpr).coerceAtLeast(0)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isNepali) "चालकलाई दिनुहुने नोट छान्नुहोस्:" else "Select Cash Note Given to Driver:",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            commonNotes.forEach { note ->
                val isSelected = note == cashGiven
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) SafaaGreen else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { cashGiven = note }
                ) {
                    Text(
                        text = "रू $note",
                        textAlign = TextAlign.Center,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Change Calculation Box
        Surface(
            color = Color(0xFFF1F8E9),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isNepali) "चालकले फिर्ता दिने खुद्रा रकम:" else "Change Driver will Return:",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF33691E))
                    )
                    Text(
                        text = "रू $changeToReturn",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = SafaaGreen
                        )
                    )
                }
                Icon(
                    imageVector = Icons.Default.Money,
                    contentDescription = null,
                    tint = SafaaGreen,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onSuccess("CASH-PAID-EXACT")
            },
            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("confirm_cash_pay_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = SafaaGreen),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isNepali) "नगद भुक्तानी पुष्टि गर्नुहोस्" else "Confirm Cash to Driver",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.White
            )
        }
    }
}
