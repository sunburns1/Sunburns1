package com.example.data.model

data class MenuActivityConfig(
    val id: String,
    val titleEn: String,
    val titleNp: String,
    val isEnabled: Boolean = true,
    val iconName: String = "default"
)

data class PaymentGatewayConfig(
    val esewaMerchantId: String = "ESEWA_MERCHANT_SURAJ_9845",
    val esewaEnabled: Boolean = true,
    val khaltiMerchantCode: String = "KHALTI_VENDOR_SURAJ_01",
    val khaltiEnabled: Boolean = true,
    val fonepayMerchantAccount: String = "019283746501 (Nabil Bank)",
    val fonepayMerchantName: String = "Safaa Rickshaw Transport Nepal",
    val fonepayEnabled: Boolean = true,
    val cashEnabled: Boolean = true,
    val platformCommissionPercent: Double = 5.0
)

data class VendorDeveloperProfile(
    val gmail: String = "surajkarki2.sk@gmail.com",
    val fullName: String = "Suraj Karki",
    val roleTitle: String = "Master App Developer & Vendor",
    val roleTitleNp: String = "मुख्य विकासकर्ता तथा भेन्डर",
    val isVerifiedDeveloper: Boolean = true,
    val isVerifiedVendor: Boolean = true,
    val vendorBusinessName: String = "Safaa Rickshaw & Cargo Nepal Pvt. Ltd.",
    val vendorPanVat: String = "609823145",
    val phone: String = "+977 9845123456",
    val appVersion: String = "2.4.0-pro-vendor"
)

data class VendorAppSettings(
    val vendorProfile: VendorDeveloperProfile = VendorDeveloperProfile(),
    val gatewayConfig: PaymentGatewayConfig = PaymentGatewayConfig(),
    val menuActivities: List<MenuActivityConfig> = listOf(
        MenuActivityConfig("ride", "Passenger Ride", "यात्रु यात्रा", true, "directions_car"),
        MenuActivityConfig("cargo", "Cargo Delivery", "सामान डेलिभरी", true, "inventory_2"),
        MenuActivityConfig("payment", "Payment Gateway", "भुक्तानी विधि", true, "account_balance_wallet"),
        MenuActivityConfig("drivers", "Drivers Directory", "चालक निर्देशिका", true, "two_wheeler"),
        MenuActivityConfig("activity", "Past Activity & History", "यात्रा इतिहास", true, "history")
    ),
    val dynamicCityTariffs: Map<String, CityTariff> = mapOf(
        "narayanghat" to LocalHubs.NARAYANGHAT.cityTariff,
        "biratnagar" to LocalHubs.BIRATNAGAR.cityTariff,
        "ktm_valley" to LocalHubs.KATHMANDU.cityTariff,
        "pokhara" to LocalHubs.POKHARA.cityTariff,
        "butwal" to LocalHubs.BUTWAL.cityTariff,
        "nepalgunj" to LocalHubs.NEPALGUNJ.cityTariff
    )
)
