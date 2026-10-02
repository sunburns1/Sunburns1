package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

enum class VehicleType(
    val titleEn: String,
    val titleNp: String,
    val descriptionEn: String,
    val descriptionNp: String,
    val baseFareNpr: Int,
    val perKmRateNpr: Int,
    val capacityEn: String,
    val isElectric: Boolean,
    val maxSeats: Int,
    val engineTypeEn: String,
    val engineTypeNp: String
) {
    E_RICKSHAW(
        titleEn = "E-Rickshaw (Electronic 7-Seater)",
        titleNp = "सफा ई-रिक्सा (७ सिट ब्याट्री)",
        descriptionEn = "Silent electronic battery powered with 7 passenger seats",
        descriptionNp = "प्रदूषणरहित विद्युतीय ब्याट्री, ७ जना यात्रु सिट क्षमता",
        baseFareNpr = 30,
        perKmRateNpr = 14,
        capacityEn = "7 Passengers (७ सिट)",
        isElectric = true,
        maxSeats = 7,
        engineTypeEn = "Electronic Battery Motor",
        engineTypeNp = "विद्युतीय ब्याट्री मोटर"
    ),
    AUTO_RICKSHAW(
        titleEn = "Auto Rickshaw (Petrol/Diesel 5-Seater)",
        titleNp = "अटो टेम्पो (५ सिट पेट्रोल/डिजेल)",
        descriptionEn = "Agile petrol and diesel engine with 5 passenger seats",
        descriptionNp = "पेट्रोल तथा डिजेल इन्जिनयुक्त, ५ जना यात्रु सिट क्षमता",
        baseFareNpr = 45,
        perKmRateNpr = 18,
        capacityEn = "5 Passengers (५ सिट)",
        isElectric = false,
        maxSeats = 5,
        engineTypeEn = "Petrol & Diesel Engine",
        engineTypeNp = "पेट्रोल तथा डिजेल इन्जिन"
    ),
    CARGO_RICKSHAW(
        titleEn = "Cargo Safari / Delivery",
        titleNp = "डेलिभरी तथा सामान ढुवानी",
        descriptionEn = "Spacious carrier for parcels, groceries & cartons up to 150 kg",
        descriptionNp = "पार्सल, किराना र भारी सामान ढुवानीको लागि (१५० कि.ग्रा.)",
        baseFareNpr = 50,
        perKmRateNpr = 22,
        capacityEn = "Up to 150 kg",
        isElectric = true,
        maxSeats = 1,
        engineTypeEn = "Electronic Cargo Loader",
        engineTypeNp = "विद्युतीय ढुवानी सफारी"
    )
}

enum class ServiceType(val titleEn: String, val titleNp: String) {
    PASSENGER_RIDE("Passenger Ride", "यात्रु यात्रा"),
    PARCEL_DELIVERY("Parcel & Cargo Delivery", "पार्सल तथा सामान डेलिभरी")
}

enum class PaymentMethod(val titleEn: String, val titleNp: String) {
    ESEWA("eSewa Digital Wallet", "ई-सेवा वालेट"),
    KHALTI("Khalti Digital Wallet", "खल्ती वालेट"),
    NEPALESE_BANK("Nepalese Bank / Fonepay", "नेपाली बैंक / फोनपे"),
    CASH("Cash to Rickshaw Driver", "चालकलाई नगद भुक्तानी")
}

enum class RideStatus(val titleEn: String, val titleNp: String) {
    IDLE("Idle", "सुरुवात"),
    SEARCHING("Connecting with Nearby Rickshaws...", "नजिकको रिक्सा खोज्दै..."),
    ACCEPTED("Driver Accepted", "चालकले स्वीकार गर्नुभयो"),
    ARRIVING("Rickshaw is Arriving", "रिक्सा आउँदैछ"),
    IN_TRANSIT("Trip in Progress", "यात्रा सुरु भयो"),
    COMPLETED("Trip Completed", "यात्रा सम्पन्न"),
    CANCELLED("Trip Cancelled", "रद्द गरियो")
}

data class LandmarkLocation(
    val id: String,
    val nameEn: String,
    val nameNp: String,
    val lat: Double,
    val lng: Double,
    val category: String = "Chowk"
)

data class CityTariff(
    val cityName: String,
    val eRickshawBaseNpr: Int,
    val eRickshawPerKmNpr: Int,
    val autoRickshawBaseNpr: Int,
    val autoRickshawPerKmNpr: Int,
    val cargoBaseNpr: Int,
    val cargoPerKmNpr: Int,
    val tariffSource: String
)

data class LocationHub(
    val id: String,
    val nameEn: String,
    val nameNp: String,
    val centerLat: Double,
    val centerLng: Double,
    val defaultRadiusKm: Double = 20.0,
    val landmarks: List<LandmarkLocation>,
    val cityTariff: CityTariff
)

data class DriverReview(
    val id: String = UUID.randomUUID().toString(),
    val reviewerName: String,
    val rating: Float,
    val comment: String,
    val dateText: String
)

data class DriverProfile(
    val id: String,
    val name: String,
    val nameNp: String,
    val phone: String,
    val vehicleType: VehicleType,
    val vehicleModel: String,
    val licensePlate: String,
    val rating: Float,
    val totalTrips: Int,
    val experienceYears: Int,
    val currentLat: Double,
    val currentLng: Double,
    val isAvailable: Boolean = true,
    val reviews: List<DriverReview> = emptyList()
)

data class SavedPlace(
    val id: String = UUID.randomUUID().toString(),
    val label: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val iconType: String = "home"
)

data class RiderProfile(
    val id: String = "rider_default",
    val name: String = "Suraj Karki",
    val phone: String = "+977 9845123456",
    val email: String = "surajkarki2.sk@gmail.com",
    val emergencyContact: String = "+977 9812000000",
    val currentAddress: String = "Lions Chowk, Narayangarh",
    val currentLat: Double = 27.7018,
    val currentLng: Double = 84.4312,
    val savedPlaces: List<SavedPlace> = listOf(
        SavedPlace(
            label = "Home (घर)",
            address = "Lions Chowk, Narayangarh",
            lat = 27.7018,
            lng = 84.4312,
            iconType = "home"
        ),
        SavedPlace(
            label = "College (कलेज)",
            address = "Birendra Multiple Campus, Bharatpur",
            lat = 27.6890,
            lng = 84.4380,
            iconType = "school"
        ),
        SavedPlace(
            label = "Main Bazaar (बजार)",
            address = "Sahid Chowk, Narayangarh",
            lat = 27.6985,
            lng = 84.4265,
            iconType = "store"
        )
    )
)

data class ParcelDeliveryInfo(
    val receiverName: String = "",
    val receiverPhone: String = "",
    val parcelCategory: String = "Standard Package",
    val weightKg: Double = 5.0,
    val instructions: String = "",
    val isFragile: Boolean = false
)

@Entity(tableName = "ride_orders")
data class RideOrderEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val serviceType: String, // PASSENGER_RIDE or PARCEL_DELIVERY
    val vehicleType: String, // E_RICKSHAW, AUTO_RICKSHAW, CARGO_RICKSHAW
    val pickupAddress: String,
    val pickupLat: Double,
    val pickupLng: Double,
    val dropoffAddress: String,
    val dropoffLat: Double,
    val dropoffLng: Double,
    val distanceKm: Double,
    val fareNpr: Int,
    val passengerCount: Int = 1,
    val paymentMethod: String,
    val paymentStatus: String, // PAID, UNPAID, CASH_COLLECTED
    val transactionRef: String? = null,
    val driverId: String? = null,
    val driverName: String? = null,
    val driverPhone: String? = null,
    val driverVehicle: String? = null,
    val driverPlate: String? = null,
    val receiverName: String? = null,
    val receiverPhone: String? = null,
    val parcelNote: String? = null,
    val status: String, // PENDING, ACCEPTED, ARRIVING, IN_TRANSIT, COMPLETED, CANCELLED
    val rating: Float? = null,
    val reviewFeedback: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
