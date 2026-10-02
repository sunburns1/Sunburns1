package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.AppDatabase
import com.example.data.repository.RideRepository
import com.example.ui.components.GoogleSignInDialog
import com.example.ui.screens.ActivityHistoryScreen
import com.example.ui.screens.BookingHomeScreen
import com.example.ui.screens.CargoDeliveryScreen
import com.example.ui.screens.DriversDirectoryScreen
import com.example.ui.screens.PaymentSelectionScreen
import com.example.ui.screens.VendorDeveloperConsoleScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SafaaGreen
import com.example.ui.viewmodel.RideViewModel

data class NavTabItem(
    val titleEn: String,
    val titleNp: String,
    val icon: ImageVector,
    val testTag: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = RideRepository(database.rideDao())

        setContent {
            MyApplicationTheme {
                val viewModel: RideViewModel = viewModel(
                    factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                            return RideViewModel(repository) as T
                        }
                    }
                )

                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: RideViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val isNepali = uiState.isNepaliLanguage

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val navTabs = listOf(
        NavTabItem("Ride", "सवारी", Icons.Default.DirectionsCar, "nav_tab_ride"),
        NavTabItem("Cargo", "डेलिभरी", Icons.Default.Inventory2, "nav_tab_cargo"),
        NavTabItem("Payment", "भुक्तानी", Icons.Default.AccountBalanceWallet, "nav_tab_payment"),
        NavTabItem("Drivers", "चालकहरू", Icons.Default.TwoWheeler, "nav_tab_drivers"),
        NavTabItem("Activity", "इतिहास", Icons.Default.History, "nav_tab_history")
    )

    BackHandler(enabled = uiState.showVendorConsoleScreen || selectedTabIndex != 0) {
        if (uiState.showVendorConsoleScreen) {
            viewModel.openVendorConsole(false)
        } else {
            selectedTabIndex = 0
        }
    }

    if (uiState.showVendorConsoleScreen) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            VendorDeveloperConsoleScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding),
                onBackToApp = { viewModel.openVendorConsole(false) }
            )
        }
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    navTabs.forEachIndexed { index, tab ->
                        val isSelected = selectedTabIndex == index
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { selectedTabIndex = index },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.titleEn
                                )
                            },
                            label = {
                                Text(
                                    text = if (isNepali) tab.titleNp else tab.titleEn,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SafaaGreen,
                                selectedTextColor = SafaaGreen,
                                indicatorColor = SafaaGreen.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier.testTag(tab.testTag)
                        )
                    }
                }
            }
        ) { innerPadding ->
            Crossfade(
                targetState = selectedTabIndex,
                label = "ScreenTransition",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) { tabIndex ->
                when (tabIndex) {
                    0 -> BookingHomeScreen(
                        viewModel = viewModel,
                        onNavigateToPayment = { selectedTabIndex = 2 }
                    )
                    1 -> CargoDeliveryScreen(viewModel = viewModel)
                    2 -> PaymentSelectionScreen(
                        viewModel = viewModel,
                        onPaymentConfirmed = { selectedTabIndex = 0 }
                    )
                    3 -> DriversDirectoryScreen(viewModel = viewModel)
                    4 -> ActivityHistoryScreen(viewModel = viewModel)
                    else -> BookingHomeScreen(
                        viewModel = viewModel,
                        onNavigateToPayment = { selectedTabIndex = 2 }
                    )
                }
            }
        }
    }

    if (uiState.showGoogleSignInDialog) {
        GoogleSignInDialog(
            currentEmail = uiState.userEmail,
            isNepali = isNepali,
            onDismiss = { viewModel.openGoogleSignIn(false) },
            onSignInSuccess = { email, name ->
                viewModel.signInWithGoogle(email, name)
            }
        )
    }
}
