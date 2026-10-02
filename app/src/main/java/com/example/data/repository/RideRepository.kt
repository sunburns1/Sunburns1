package com.example.data.repository

import com.example.data.local.RideDao
import com.example.data.model.DriverProfile
import com.example.data.model.DriverReview
import com.example.data.model.LocalHubs
import com.example.data.model.LocationHub
import com.example.data.model.RideOrderEntity
import com.example.data.model.RiderProfile
import com.example.data.model.SampleDrivers
import com.example.data.model.VehicleType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class RideRepository(private val rideDao: RideDao) {

    val allOrders: Flow<List<RideOrderEntity>> = rideDao.getAllOrders()

    private val _drivers = MutableStateFlow<List<DriverProfile>>(SampleDrivers.DRIVERS)
    val drivers: StateFlow<List<DriverProfile>> = _drivers.asStateFlow()

    private val _riderProfile = MutableStateFlow(RiderProfile())
    val riderProfile: StateFlow<RiderProfile> = _riderProfile.asStateFlow()

    private val _activeHub = MutableStateFlow<LocationHub>(LocalHubs.NARAYANGHAT)
    val activeHub: StateFlow<LocationHub> = _activeHub.asStateFlow()

    // Service radius limit in km (up to 20 km)
    private val _serviceRadiusKm = MutableStateFlow(20.0)
    val serviceRadiusKm: StateFlow<Double> = _serviceRadiusKm.asStateFlow()

    fun setServiceRadius(radiusKm: Double) {
        _serviceRadiusKm.value = radiusKm.coerceIn(5.0, 20.0)
    }

    fun setActiveHub(hub: LocationHub) {
        _activeHub.value = hub
    }

    fun updateRiderProfile(updated: RiderProfile) {
        _riderProfile.value = updated
    }

    fun addDriverReview(driverId: String, reviewerName: String, rating: Float, comment: String) {
        val currentList = _drivers.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == driverId }
        if (index != -1) {
            val driver = currentList[index]
            val newReview = DriverReview(
                reviewerName = reviewerName.ifBlank { "Verified Customer" },
                rating = rating,
                comment = comment.ifBlank { "Great, on-time service!" },
                dateText = "Just now"
            )
            val updatedReviews = listOf(newReview) + driver.reviews
            val newTripCount = driver.totalTrips + 1
            val newRating = ((driver.rating * driver.reviews.size + rating) / (driver.reviews.size + 1))
            currentList[index] = driver.copy(
                reviews = updatedReviews,
                totalTrips = newTripCount,
                rating = (newRating * 10).toInt() / 10f
            )
            _drivers.value = currentList
        }
    }

    suspend fun saveOrder(order: RideOrderEntity) {
        rideDao.insertOrder(order)
    }

    suspend fun updateOrder(order: RideOrderEntity) {
        rideDao.updateOrder(order)
    }

    suspend fun updateOrderRating(orderId: String, rating: Float, feedback: String) {
        rideDao.updateOrderRating(orderId, rating, feedback)
    }

    fun findBestDriver(vehicleType: VehicleType): DriverProfile {
        return _drivers.value.firstOrNull { it.vehicleType == vehicleType && it.isAvailable }
            ?: _drivers.value.firstOrNull { it.vehicleType == vehicleType }
            ?: _drivers.value.first()
    }

    companion object {
        fun calculateDistanceKm(
            lat1: Double,
            lon1: Double,
            lat2: Double,
            lon2: Double
        ): Double {
            val earthRadiusKm = 6371.0
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)
            val a = sin(dLat / 2).pow(2.0) +
                    cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                    sin(dLon / 2).pow(2.0)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            val dist = earthRadiusKm * c
            // Round to 1 decimal place
            return (dist * 10).toInt() / 10.0
        }
    }
}
