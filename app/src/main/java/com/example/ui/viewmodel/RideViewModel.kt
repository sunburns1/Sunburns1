package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.DriverProfile
import com.example.data.model.LandmarkLocation
import com.example.data.model.LocalHubs
import com.example.data.model.LocationHub
import com.example.data.model.ParcelDeliveryInfo
import com.example.data.model.PaymentMethod
import com.example.data.model.RideOrderEntity
import com.example.data.model.RideStatus
import com.example.data.model.RiderProfile
import com.example.data.model.ServiceType
import com.example.data.model.VehicleType
import com.example.data.repository.RideRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

data class RideBookingUiState(
    val serviceType: ServiceType = ServiceType.PASSENGER_RIDE,
    val selectedVehicle: VehicleType = VehicleType.E_RICKSHAW,
    val pickupName: String = "Lions Chowk, Narayangarh",
    val pickupLat: Double = 27.7018,
    val pickupLng: Double = 84.4312,
    val dropoffName: String = "Chaubiskothi Hospital Hub",
    val dropoffLat: Double = 27.6812,
    val dropoffLng: Double = 84.4310,
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.ESEWA,
    val parcelInfo: ParcelDeliveryInfo = ParcelDeliveryInfo(),
    val rideStatus: RideStatus = RideStatus.IDLE,
    val activeDriver: DriverProfile? = null,
    val activeOrderId: String? = null,
    val isNepaliLanguage: Boolean = false,
    val showPaymentFlow: Boolean = false,
    val paymentSuccessRef: String? = null,
    val showRatingModal: Boolean = false,
    val orderBeingRated: RideOrderEntity? = null,
    val selectedDriverForDetail: DriverProfile? = null,
    val showRiderProfileModal: Boolean = false,
    val cashGivenByCustomer: Int = 100,
    val showVendorConsoleScreen: Boolean = false,
    val showGoogleSignInDialog: Boolean = false,
    val userEmail: String = "surajkarki2.sk@gmail.com",
    val userName: String = "Suraj Karki"
)

class RideViewModel(private val repository: RideRepository) : ViewModel() {

    val activeHub: StateFlow<LocationHub> = repository.activeHub
    val serviceRadiusKm: StateFlow<Double> = repository.serviceRadiusKm
    val drivers: StateFlow<List<DriverProfile>> = repository.drivers
    val riderProfile: StateFlow<RiderProfile> = repository.riderProfile

    val allOrders: StateFlow<List<RideOrderEntity>> = repository.allOrders
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _uiState = MutableStateFlow(RideBookingUiState())
    val uiState: StateFlow<RideBookingUiState> = _uiState.asStateFlow()

    init {
        // Initialize with default pickup and dropoff from the active hub
        val defaultLandmarks = LocalHubs.NARAYANGHAT.landmarks
        if (defaultLandmarks.size >= 2) {
            val p = defaultLandmarks[0]
            val d = defaultLandmarks[3]
            _uiState.value = _uiState.value.copy(
                pickupName = p.nameEn,
                pickupLat = p.lat,
                pickupLng = p.lng,
                dropoffName = d.nameEn,
                dropoffLat = d.lat,
                dropoffLng = d.lng
            )
        }
    }

    fun setServiceType(type: ServiceType) {
        val defaultVehicle = if (type == ServiceType.PARCEL_DELIVERY) {
            VehicleType.CARGO_RICKSHAW
        } else {
            VehicleType.E_RICKSHAW
        }
        _uiState.value = _uiState.value.copy(
            serviceType = type,
            selectedVehicle = defaultVehicle
        )
    }

    fun setVehicle(vehicle: VehicleType) {
        _uiState.value = _uiState.value.copy(selectedVehicle = vehicle)
    }

    fun setPaymentMethod(method: PaymentMethod) {
        _uiState.value = _uiState.value.copy(selectedPaymentMethod = method)
    }

    fun toggleLanguage() {
        _uiState.value = _uiState.value.copy(isNepaliLanguage = !_uiState.value.isNepaliLanguage)
    }

    fun setServiceRadius(km: Double) {
        repository.setServiceRadius(km)
    }

    fun selectActiveHub(hub: LocationHub) {
        repository.setActiveHub(hub)
        val lms = hub.landmarks
        if (lms.size >= 2) {
            val p = lms[0]
            val d = lms[1]
            _uiState.value = _uiState.value.copy(
                pickupName = p.nameEn,
                pickupLat = p.lat,
                pickupLng = p.lng,
                dropoffName = d.nameEn,
                dropoffLat = d.lat,
                dropoffLng = d.lng
            )
        }
    }

    fun setPickup(landmark: LandmarkLocation) {
        _uiState.value = _uiState.value.copy(
            pickupName = landmark.nameEn,
            pickupLat = landmark.lat,
            pickupLng = landmark.lng
        )
    }

    fun setDropoff(landmark: LandmarkLocation) {
        _uiState.value = _uiState.value.copy(
            dropoffName = landmark.nameEn,
            dropoffLat = landmark.lat,
            dropoffLng = landmark.lng
        )
    }

    fun setPickupCustom(name: String, lat: Double, lng: Double) {
        _uiState.value = _uiState.value.copy(
            pickupName = name,
            pickupLat = lat,
            pickupLng = lng
        )
    }

    fun setDropoffCustom(name: String, lat: Double, lng: Double) {
        _uiState.value = _uiState.value.copy(
            dropoffName = name,
            dropoffLat = lat,
            dropoffLng = lng
        )
    }

    fun updateParcelInfo(info: ParcelDeliveryInfo) {
        _uiState.value = _uiState.value.copy(parcelInfo = info)
    }

    fun updateRider(profile: RiderProfile) {
        repository.updateRiderProfile(profile)
        _uiState.value = _uiState.value.copy(showRiderProfileModal = false)
    }

    fun openDriverDetail(driver: DriverProfile?) {
        _uiState.value = _uiState.value.copy(selectedDriverForDetail = driver)
    }

    fun openRiderProfile(open: Boolean) {
        _uiState.value = _uiState.value.copy(showRiderProfileModal = open)
    }

    fun setCashGiven(amount: Int) {
        _uiState.value = _uiState.value.copy(cashGivenByCustomer = amount)
    }

    // Calculations
    fun getTripDistanceKm(): Double {
        val s = _uiState.value
        val d = RideRepository.calculateDistanceKm(s.pickupLat, s.pickupLng, s.dropoffLat, s.dropoffLng)
        return if (d < 0.5) 1.2 else d
    }

    fun isPickupWithinRadius(): Boolean {
        val hub = activeHub.value
        val s = _uiState.value
        val distFromHub = RideRepository.calculateDistanceKm(hub.centerLat, hub.centerLng, s.pickupLat, s.pickupLng)
        return distFromHub <= serviceRadiusKm.value
    }

    fun isDropoffWithinRadius(): Boolean {
        val hub = activeHub.value
        val s = _uiState.value
        val distFromHub = RideRepository.calculateDistanceKm(hub.centerLat, hub.centerLng, s.dropoffLat, s.dropoffLng)
        return distFromHub <= serviceRadiusKm.value
    }

    fun calculateFareNpr(): Int {
        val vehicle = _uiState.value.selectedVehicle
        val distKm = getTripDistanceKm()
        val tariff = activeHub.value.cityTariff
        val (base, perKm) = when (vehicle) {
            VehicleType.E_RICKSHAW -> tariff.eRickshawBaseNpr to tariff.eRickshawPerKmNpr
            VehicleType.AUTO_RICKSHAW -> tariff.autoRickshawBaseNpr to tariff.autoRickshawPerKmNpr
            VehicleType.CARGO_RICKSHAW -> tariff.cargoBaseNpr to tariff.cargoPerKmNpr
        }
        val total = base + (distKm * perKm).toInt()
        return total.coerceAtLeast(base)
    }

    fun getRatesForCurrentCity(vehicle: VehicleType): Pair<Int, Int> {
        val tariff = activeHub.value.cityTariff
        return when (vehicle) {
            VehicleType.E_RICKSHAW -> tariff.eRickshawBaseNpr to tariff.eRickshawPerKmNpr
            VehicleType.AUTO_RICKSHAW -> tariff.autoRickshawBaseNpr to tariff.autoRickshawPerKmNpr
            VehicleType.CARGO_RICKSHAW -> tariff.cargoBaseNpr to tariff.cargoPerKmNpr
        }
    }

    fun startBookingFlow() {
        val state = _uiState.value
        val bestDriver = repository.findBestDriver(state.selectedVehicle)
        val orderId = UUID.randomUUID().toString()

        _uiState.value = state.copy(
            rideStatus = RideStatus.SEARCHING,
            activeOrderId = orderId,
            activeDriver = bestDriver
        )

        viewModelScope.launch {
            // Simulate searching local rickshaws nearby
            delay(1500)
            _uiState.value = _uiState.value.copy(rideStatus = RideStatus.ACCEPTED)

            delay(2000)
            _uiState.value = _uiState.value.copy(rideStatus = RideStatus.ARRIVING)

            delay(2500)
            _uiState.value = _uiState.value.copy(rideStatus = RideStatus.IN_TRANSIT)
        }
    }

    fun completeCurrentTrip() {
        val s = _uiState.value
        val driver = s.activeDriver
        val fare = calculateFareNpr()
        val dist = getTripDistanceKm()

        val order = RideOrderEntity(
            id = s.activeOrderId ?: UUID.randomUUID().toString(),
            serviceType = s.serviceType.name,
            vehicleType = s.selectedVehicle.name,
            pickupAddress = s.pickupName,
            pickupLat = s.pickupLat,
            pickupLng = s.pickupLng,
            dropoffAddress = s.dropoffName,
            dropoffLat = s.dropoffLat,
            dropoffLng = s.dropoffLng,
            distanceKm = dist,
            fareNpr = fare,
            paymentMethod = s.selectedPaymentMethod.name,
            paymentStatus = if (s.selectedPaymentMethod == PaymentMethod.CASH) "CASH_COLLECTED" else "PAID",
            transactionRef = s.paymentSuccessRef ?: "TXN-SAF-${System.currentTimeMillis() % 1000000}",
            driverId = driver?.id,
            driverName = driver?.name,
            driverPhone = driver?.phone,
            driverVehicle = driver?.vehicleModel,
            driverPlate = driver?.licensePlate,
            receiverName = s.parcelInfo.receiverName.ifBlank { null },
            receiverPhone = s.parcelInfo.receiverPhone.ifBlank { null },
            parcelNote = s.parcelInfo.instructions.ifBlank { null },
            status = RideStatus.COMPLETED.name,
            timestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            repository.saveOrder(order)
            _uiState.value = s.copy(
                rideStatus = RideStatus.COMPLETED,
                showRatingModal = true,
                orderBeingRated = order
            )
        }
    }

    fun cancelCurrentTrip() {
        _uiState.value = _uiState.value.copy(
            rideStatus = RideStatus.IDLE,
            activeDriver = null,
            activeOrderId = null,
            orderBeingRated = null
        )
    }

    fun openRatingForOrder(order: RideOrderEntity?) {
        _uiState.value = _uiState.value.copy(
            showRatingModal = order != null,
            orderBeingRated = order
        )
    }

    fun dismissRatingModal() {
        _uiState.value = _uiState.value.copy(
            showRatingModal = false,
            orderBeingRated = null,
            rideStatus = if (_uiState.value.rideStatus == RideStatus.COMPLETED) RideStatus.IDLE else _uiState.value.rideStatus,
            activeDriver = if (_uiState.value.rideStatus == RideStatus.COMPLETED) null else _uiState.value.activeDriver,
            activeOrderId = if (_uiState.value.rideStatus == RideStatus.COMPLETED) null else _uiState.value.activeOrderId,
            paymentSuccessRef = null
        )
    }

    fun submitRating(score: Float, feedback: String) {
        val s = _uiState.value
        val driverId = s.orderBeingRated?.driverId ?: s.activeDriver?.id
        val orderId = s.orderBeingRated?.id ?: s.activeOrderId

        if (driverId != null) {
            repository.addDriverReview(
                driverId = driverId,
                reviewerName = riderProfile.value.name,
                rating = score,
                comment = feedback
            )
        }

        if (orderId != null) {
            viewModelScope.launch {
                repository.updateOrderRating(orderId, score, feedback)
            }
        }

        _uiState.value = s.copy(
            showRatingModal = false,
            orderBeingRated = null,
            rideStatus = RideStatus.IDLE,
            activeDriver = null,
            activeOrderId = null,
            paymentSuccessRef = null
        )
    }

    fun ratePastTrip(orderId: String, driverId: String?, rating: Float, feedback: String) {
        if (driverId != null) {
            repository.addDriverReview(
                driverId = driverId,
                reviewerName = riderProfile.value.name,
                rating = rating,
                comment = feedback
            )
        }
        viewModelScope.launch {
            repository.updateOrderRating(orderId, rating, feedback)
        }
    }

    fun openPaymentFlow(open: Boolean) {
        _uiState.value = _uiState.value.copy(showPaymentFlow = open)
    }

    fun confirmPayment(refCode: String) {
        _uiState.value = _uiState.value.copy(
            showPaymentFlow = false,
            paymentSuccessRef = refCode
        )
        // If trip was awaiting payment, complete it
        completeCurrentTrip()
    }

    fun openVendorConsole(open: Boolean) {
        _uiState.value = _uiState.value.copy(showVendorConsoleScreen = open)
    }

    fun openGoogleSignIn(open: Boolean) {
        _uiState.value = _uiState.value.copy(showGoogleSignInDialog = open)
    }

    fun signInWithGoogle(email: String, name: String) {
        _uiState.value = _uiState.value.copy(
            showGoogleSignInDialog = false,
            userEmail = email,
            userName = name
        )
        repository.updateRiderProfile(
            riderProfile.value.copy(
                email = email,
                name = name
            )
        )
    }
}
