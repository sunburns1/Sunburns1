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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.CityTariff
import com.example.data.model.DriverProfile
import com.example.data.model.LocalHubs
import com.example.data.model.MenuActivityConfig
import com.example.data.model.PaymentGatewayConfig
import com.example.data.model.VehicleType
import com.example.ui.theme.AutoAmber
import com.example.ui.theme.EsewaGreen
import com.example.ui.theme.FonepayRed
import com.example.ui.theme.KhaltiPurple
import com.example.ui.theme.NepalGold
import com.example.ui.theme.SafaaGreen
import com.example.ui.viewmodel.RideViewModel

@Composable
fun VendorDeveloperConsoleScreen(
    viewModel: RideViewModel,
    modifier: Modifier = Modifier,
    onBackToApp: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val activeHub by viewModel.activeHub.collectAsState()
    val drivers by viewModel.drivers.collectAsState()
    val riderProfile by viewModel.riderProfile.collectAsState()
    val isNepali = uiState.isNepaliLanguage

    var activeAdminTab by remember { mutableIntStateOf(0) }
    // 0: Menu & Activities, 1: City Tariffs, 2: Payment Gateways, 3: Drivers Fleet, 4: Vendor Profile

    // Local mutable state for editing city tariffs
    var editableNgBase by remember { mutableStateOf(activeHub.cityTariff.eRickshawBaseNpr.toString()) }
    var editableNgPerKm by remember { mutableStateOf(activeHub.cityTariff.eRickshawPerKmNpr.toString()) }
    var editableAutoBase by remember { mutableStateOf(activeHub.cityTariff.autoRickshawBaseNpr.toString()) }
    var editableAutoPerKm by remember { mutableStateOf(activeHub.cityTariff.autoRickshawPerKmNpr.toString()) }
    var saveStatusMessage by remember { mutableStateOf<String?>(null) }

    // Menu activities toggles
    var isRideEnabled by remember { mutableStateOf(true) }
    var isCargoEnabled by remember { mutableStateOf(true) }
    var isPaymentEnabled by remember { mutableStateOf(true) }
    var isDriversEnabled by remember { mutableStateOf(true) }
    var isActivityEnabled by remember { mutableStateOf(true) }

    // Payment Gateway Merchant Credentials
    var esewaMerchantId by remember { mutableStateOf("ESEWA_MERCHANT_SURAJ_9845") }
    var khaltiMerchantCode by remember { mutableStateOf("KHALTI_VENDOR_SURAJ_01") }
    var fonepayMerchantAcc by remember { mutableStateOf("019283746501 (Nabil Bank)") }
    var fonepayMerchantTitle by remember { mutableStateOf("Safaa Rickshaw Transport Nepal") }
    var commissionRate by remember { mutableStateOf("5.0") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
            // Header with Back button and Master Vendor / Dev badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBackToApp,
                        modifier = Modifier
                            .size(36.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                            .testTag("admin_back_btn")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Vendor & Dev Console",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = SafaaGreen
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = NepalGold
                            ) {
                                Text(
                                    text = "ROOT",
                                    color = Color.Black,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, fontSize = 9.sp),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = if (isNepali) "मुख्य विकासकर्ता तथा भेन्डर नियन्त्रण कक्ष" else "Master App Administrator & Service Vendor Hub",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                        )
                    }
                }
            }
        }

        // Master Credentials Card (Gmail & Verified Role)
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, SafaaGreen),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth().testTag("master_dev_credential_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .background(SafaaGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Suraj Karki",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.Verified, contentDescription = "Verified", tint = SafaaGreen, modifier = Modifier.size(16.dp))
                            }
                            Text(
                                text = "surajkarki2.sk@gmail.com",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF1976D2))
                            )
                            Text(
                                text = "Safaa Rickshaw & Cargo Nepal Pvt. Ltd.",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color.Gray)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Text(
                                text = "ACTIVE",
                                color = SafaaGreen,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isNepali)
                            "तपाईं यस एपको प्रमाणित विकासकर्ता तथा आधिकारिक भेन्डर हुनुहुन्छ। तलका ट्याबहरूबाट मेनु, भाडादर, चालक र भुक्तानी परिवर्तन गर्नुहोस्।"
                        else
                            "You are authenticated as the Master Developer & Vendor. You have full privileges to modify all menu activities, transportation charges, driver fleets, and payment credentials.",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color.DarkGray)
                    )
                }
            }
        }

        // Horizontal Category Tabs for Console
        item {
            val tabs = listOf(
                "Menus & Activities" to Icons.Default.Menu,
                "City Tariffs" to Icons.Default.Speed,
                "Payment Gateways" to Icons.Default.Paid,
                "Drivers Fleet" to Icons.Default.TwoWheeler,
                "Vendor Business" to Icons.Default.Business
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(tabs.indices.toList()) { index ->
                    val isSelected = activeAdminTab == index
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) SafaaGreen else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .clickable { activeAdminTab = index }
                            .testTag("admin_subtab_$index")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = tabs[index].second,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else Color.DarkGray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tabs[index].first,
                                color = if (isSelected) Color.White else Color.DarkGray,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        // Active Tab Content
        item {
            when (activeAdminTab) {
                0 -> MenuActivityControllerSection(
                    isRideEnabled = isRideEnabled,
                    onToggleRide = { isRideEnabled = it },
                    isCargoEnabled = isCargoEnabled,
                    onToggleCargo = { isCargoEnabled = it },
                    isPaymentEnabled = isPaymentEnabled,
                    onTogglePayment = { isPaymentEnabled = it },
                    isDriversEnabled = isDriversEnabled,
                    onToggleDrivers = { isDriversEnabled = it },
                    isActivityEnabled = isActivityEnabled,
                    onToggleActivity = { isActivityEnabled = it },
                    isNepali = isNepali
                )
                1 -> CityTariffControllerSection(
                    activeCity = activeHub.cityTariff.cityName,
                    eRickshawBase = editableNgBase,
                    onERickshawBaseChange = { editableNgBase = it },
                    eRickshawPerKm = editableNgPerKm,
                    onERickshawPerKmChange = { editableNgPerKm = it },
                    autoBase = editableAutoBase,
                    onAutoBaseChange = { editableAutoBase = it },
                    autoPerKm = editableAutoPerKm,
                    onAutoPerKmChange = { editableAutoPerKm = it },
                    isNepali = isNepali,
                    onSave = {
                        val base = editableNgBase.toIntOrNull() ?: 30
                        val perKm = editableNgPerKm.toIntOrNull() ?: 14
                        saveStatusMessage = "✅ Successfully updated ${activeHub.cityTariff.cityName} tariff! New Base: Rs $base, Rate: Rs $perKm/km."
                    },
                    saveMessage = saveStatusMessage
                )
                2 -> PaymentGatewayVendorSection(
                    esewaId = esewaMerchantId,
                    onEsewaIdChange = { esewaMerchantId = it },
                    khaltiCode = khaltiMerchantCode,
                    onKhaltiCodeChange = { khaltiMerchantCode = it },
                    fonepayAccount = fonepayMerchantAcc,
                    onFonepayAccChange = { fonepayMerchantAcc = it },
                    fonepayTitle = fonepayMerchantTitle,
                    onFonepayTitleChange = { fonepayMerchantTitle = it },
                    commission = commissionRate,
                    onCommissionChange = { commissionRate = it },
                    isNepali = isNepali
                )
                3 -> DriverFleetManagementSection(
                    drivers = drivers,
                    isNepali = isNepali
                )
                4 -> VendorBusinessProfileSection(
                    isNepali = isNepali
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun MenuActivityControllerSection(
    isRideEnabled: Boolean,
    onToggleRide: (Boolean) -> Unit,
    isCargoEnabled: Boolean,
    onToggleCargo: (Boolean) -> Unit,
    isPaymentEnabled: Boolean,
    onTogglePayment: (Boolean) -> Unit,
    isDriversEnabled: Boolean,
    onToggleDrivers: (Boolean) -> Unit,
    isActivityEnabled: Boolean,
    onToggleActivity: (Boolean) -> Unit,
    isNepali: Boolean
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("menu_activity_controller_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (isNepali) "मेनु तथा एप गतिविधि नियन्त्रक" else "App Menu & Activity Controller",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = if (isNepali) "एपका स्क्रिन र मेनुहरू चालू वा बन्द गर्नुहोस्:" else "Toggle visibility of app features and navigation screens:",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )

            Spacer(modifier = Modifier.height(14.dp))

            MenuToggleRow(
                title = "Passenger Ride (सवारी यात्रा)",
                desc = "Auto rickshaw & E-rickshaw instant customer booking",
                enabled = isRideEnabled,
                onToggle = onToggleRide
            )

            HorizontalDivider(color = Color(0xFFF0F0F0))

            MenuToggleRow(
                title = "Cargo Delivery (सामान ढुवानी)",
                desc = "Heavy safari parcels, sacks and goods logistics",
                enabled = isCargoEnabled,
                onToggle = onToggleCargo
            )

            HorizontalDivider(color = Color(0xFFF0F0F0))

            MenuToggleRow(
                title = "Payment Gateways (भुक्तानी विधि)",
                desc = "eSewa, Khalti, Nepalese Bank QR, and Cash on Delivery",
                enabled = isPaymentEnabled,
                onToggle = onTogglePayment
            )

            HorizontalDivider(color = Color(0xFFF0F0F0))

            MenuToggleRow(
                title = "Drivers Directory (चालक सूची)",
                desc = "Verified local rickshaw drivers with ratings and call action",
                enabled = isDriversEnabled,
                onToggle = onToggleDrivers
            )

            HorizontalDivider(color = Color(0xFFF0F0F0))

            MenuToggleRow(
                title = "Trip History & Receipts (इतिहास र रसिद)",
                desc = "Past completed trips with Room database digital vouchers",
                enabled = isActivityEnabled,
                onToggle = onToggleActivity
            )
        }
    }
}

@Composable
fun MenuToggleRow(
    title: String,
    desc: String,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
            Text(text = desc, style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray, fontSize = 11.sp))
        }
        Switch(
            checked = enabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = SafaaGreen)
        )
    }
}

@Composable
fun CityTariffControllerSection(
    activeCity: String,
    eRickshawBase: String,
    onERickshawBaseChange: (String) -> Unit,
    eRickshawPerKm: String,
    onERickshawPerKmChange: (String) -> Unit,
    autoBase: String,
    onAutoBaseChange: (String) -> Unit,
    autoPerKm: String,
    onAutoPerKmChange: (String) -> Unit,
    isNepali: Boolean,
    onSave: () -> Unit,
    saveMessage: String?
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("tariff_controller_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isNepali) "सहर अनुसार भाडादर मास्टर" else "City Transportation Tariff Master",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Current City: $activeCity",
                        style = MaterialTheme.typography.bodySmall.copy(color = SafaaGreen, fontWeight = FontWeight.Bold)
                    )
                }

                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "20 km Radius",
                        color = SafaaGreen,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "⚡ E-Rickshaw (सफा ई-रिक्सा - ब्याट्री)",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = eRickshawBase,
                    onValueChange = onERickshawBaseChange,
                    label = { Text("Base Fare (रू)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f).testTag("admin_erick_base_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = eRickshawPerKm,
                    onValueChange = onERickshawPerKmChange,
                    label = { Text("Per KM (रू)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f).testTag("admin_erick_km_input"),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "🛺 Auto Rickshaw (अटो टेम्पो - CNG/LPG)",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = autoBase,
                    onValueChange = onAutoBaseChange,
                    label = { Text("Base Fare (रू)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f).testTag("admin_auto_base_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = autoPerKm,
                    onValueChange = onAutoPerKmChange,
                    label = { Text("Per KM (रू)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f).testTag("admin_auto_km_input"),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("save_tariffs_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = SafaaGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isNepali) "नयाँ भाडादर सुरक्षित गर्नुहोस्" else "Save & Broadcast New Rates",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            saveMessage?.let { msg ->
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = msg,
                        color = SafaaGreen,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PaymentGatewayVendorSection(
    esewaId: String,
    onEsewaIdChange: (String) -> Unit,
    khaltiCode: String,
    onKhaltiCodeChange: (String) -> Unit,
    fonepayAccount: String,
    onFonepayAccChange: (String) -> Unit,
    fonepayTitle: String,
    onFonepayTitleChange: (String) -> Unit,
    commission: String,
    onCommissionChange: (String) -> Unit,
    isNepali: Boolean
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("payment_vendor_setup_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (isNepali) "भेन्डर पेमेन्ट गेटवे खाता व्यवस्थापन" else "Vendor Payment Gateway & Payout Accounts",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Suraj Karki • Payout & Merchant Account Credentials",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = esewaId,
                onValueChange = onEsewaIdChange,
                label = { Text("eSewa Merchant Code") },
                leadingIcon = { Icon(Icons.Default.Security, contentDescription = null, tint = EsewaGreen) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = khaltiCode,
                onValueChange = onKhaltiCodeChange,
                label = { Text("Khalti Public Key / Vendor ID") },
                leadingIcon = { Icon(Icons.Default.Security, contentDescription = null, tint = KhaltiPurple) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = fonepayAccount,
                onValueChange = onFonepayAccChange,
                label = { Text("Fonepay Settlement Account No.") },
                leadingIcon = { Icon(Icons.Default.AccountBalance, contentDescription = null, tint = FonepayRed) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = fonepayTitle,
                onValueChange = onFonepayTitleChange,
                label = { Text("Fonepay Merchant Display Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = commission,
                onValueChange = onCommissionChange,
                label = { Text("Vendor Platform Commission (%)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

@Composable
fun DriverFleetManagementSection(
    drivers: List<DriverProfile>,
    isNepali: Boolean
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("fleet_management_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${if (isNepali) "दर्ता भएका चालकहरू" else "Registered Drivers"} (${drivers.size})",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = SafaaGreen
                ) {
                    Text(
                        text = "+ Add Rickshaw",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            drivers.forEach { drv ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = drv.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Text(text = drv.vehicleModel, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color.Gray))
                            Text(text = drv.licensePlate, style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF0D47A1), fontWeight = FontWeight.Bold))
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Text(
                                text = "Verified",
                                color = SafaaGreen,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VendorBusinessProfileSection(
    isNepali: Boolean
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("vendor_business_profile_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (isNepali) "भेन्डर कम्पनी तथा कानुनी विवरण" else "Vendor Company & Legal Registration",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(12.dp))

            VendorInfoRow("Company Name", "Safaa Rickshaw & Cargo Nepal Pvt. Ltd.")
            VendorInfoRow("Master Gmail", "surajkarki2.sk@gmail.com")
            VendorInfoRow("Vendor PAN/VAT", "609823145")
            VendorInfoRow("Head Office", "Bharatpur-1, Narayanghat, Chitwan, Nepal")
            VendorInfoRow("Registration No.", "291840/079/080")
            VendorInfoRow("App Developer Role", "Master Author & System Administrator")
            VendorInfoRow("Version", "v2.4.0-vendor-release")
        }
    }
}

@Composable
fun VendorInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
        Text(text = value, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
    }
}
