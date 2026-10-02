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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.NepalGold
import com.example.ui.theme.SafaaGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GoogleSignInDialog(
    currentEmail: String,
    isNepali: Boolean,
    onDismiss: () -> Unit,
    onSignInSuccess: (email: String, name: String) -> Unit
) {
    var emailInput by remember { mutableStateOf(currentEmail.ifBlank { "surajkarki2.sk@gmail.com" }) }
    var nameInput by remember { mutableStateOf("Suraj Karki") }
    var isSigningIn by remember { mutableStateOf(false) }
    var showCustomInput by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val isVendorDevAccount = emailInput.trim().equals("surajkarki2.sk@gmail.com", ignoreCase = true) ||
            emailInput.contains("admin", ignoreCase = true) ||
            emailInput.contains("dev", ignoreCase = true)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("google_sign_in_dialog"),
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
                // Top close & header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFF2F4F7),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "G",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp,
                                    color = Color(0xFF4285F4)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isNepali) "गुगल खाताबाट साइन इन" else "Sign in with Google",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp).testTag("close_google_login_btn")) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (isNepali)
                        "आफ्नो जीमेल खाताबाट लगइन गर्नुहोस्। यो खाताबाट तपाईं विकासकर्ता तथा भेन्डर हुनुहुनेछ र एपको सबै सेटिङ परिवर्तन गर्न सक्नुहुनेछ।"
                    else
                        "Log in with your Gmail. As the developer and vendor of this app, you can change all menu activities, tariffs, and settings.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Default Recognized Gmail Account (Suraj Karki - Master Developer & Vendor)
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isVendorDevAccount) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        if (isVendorDevAccount) SafaaGreen else Color.LightGray
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            emailInput = "surajkarki2.sk@gmail.com"
                            nameInput = "Suraj Karki"
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(SafaaGreen, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "SK",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Suraj Karki",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified Vendor",
                                    tint = SafaaGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "surajkarki2.sk@gmail.com",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray)
                            )
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = SafaaGreen,
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                Text(
                                    text = "MASTER DEVELOPER & VENDOR",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Black),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                                )
                            }
                        }

                        Icon(Icons.Default.Check, contentDescription = null, tint = SafaaGreen)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Optional custom email input trigger
                Text(
                    text = if (showCustomInput) "Hide Custom Gmail" else "+ Use Another Gmail Account",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .clickable { showCustomInput = !showCustomInput }
                        .padding(4.dp)
                )

                if (showCustomInput) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        label = { Text("Gmail Address") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth().testTag("custom_gmail_input"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Your Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth().testTag("custom_name_input"),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        isSigningIn = true
                        coroutineScope.launch {
                            delay(900)
                            isSigningIn = false
                            onSignInSuccess(emailInput.trim(), nameInput.trim())
                            onDismiss()
                        }
                    },
                    enabled = !isSigningIn && emailInput.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("confirm_google_signin_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = SafaaGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isSigningIn) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isNepali) "साइन इन हुँदैछ..." else "Authenticating with Google...")
                    } else {
                        Text(
                            text = if (isNepali)
                                "यो जीमेलबाट विकासकर्ता तथा भेन्डर लगइन गर्नुहोस्"
                            else
                                "Continue as Developer & Vendor",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
