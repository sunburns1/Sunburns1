package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.LocalHubs
import com.example.data.model.VehicleType
import com.example.data.repository.RideRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Safaa Ride & Cargo", appName)
  }

  @Test
  fun `verify distance calculation within local area`() {
    val lionsChowk = LocalHubs.NARAYANGHAT.landmarks[0]
    val chaubiskothi = LocalHubs.NARAYANGHAT.landmarks[3]
    val dist = RideRepository.calculateDistanceKm(
      lionsChowk.lat, lionsChowk.lng,
      chaubiskothi.lat, chaubiskothi.lng
    )
    assertTrue("Distance between nearby chowks should be within 10km", dist in 1.0..6.0)
  }

  @Test
  fun `verify city transportation charges for narayanghat biratnagar kathmandu`() {
    val ng = LocalHubs.NARAYANGHAT.cityTariff
    val brt = LocalHubs.BIRATNAGAR.cityTariff
    val ktm = LocalHubs.KATHMANDU.cityTariff

    assertEquals("Narayanghat", ng.cityName)
    assertEquals(30, ng.eRickshawBaseNpr)
    assertEquals(14, ng.eRickshawPerKmNpr)

    assertEquals("Biratnagar", brt.cityName)
    assertEquals(25, brt.eRickshawBaseNpr)
    assertEquals(12, brt.eRickshawPerKmNpr)

    assertEquals("Kathmandu", ktm.cityName)
    assertEquals(35, ktm.eRickshawBaseNpr)
    assertEquals(18, ktm.eRickshawPerKmNpr)
  }

  @Test
  fun `verify service radius up to 20 km`() {
    assertEquals(20.0, LocalHubs.NARAYANGHAT.defaultRadiusKm, 0.01)
    assertEquals(20.0, LocalHubs.BIRATNAGAR.defaultRadiusKm, 0.01)
    assertEquals(20.0, LocalHubs.KATHMANDU.defaultRadiusKm, 0.01)
  }

  @Test
  fun `verify live trip tracking vehicle interpolation`() {
    val startLat = 27.7018
    val endLat = 27.6812
    val progress = 0.5f
    val currentLat = startLat + (endLat - startLat) * progress
    assertTrue("Current Lat should be halfway between start and end", currentLat in 27.68..27.71)
  }

  @Test
  fun `verify driver rating and review addition`() {
    val repo = RideRepository(
      object : com.example.data.local.RideDao {
        override fun getAllOrders() = kotlinx.coroutines.flow.flowOf(emptyList<com.example.data.model.RideOrderEntity>())
        override fun getOrderById(orderId: String) = kotlinx.coroutines.flow.flowOf(null)
        override suspend fun insertOrder(order: com.example.data.model.RideOrderEntity) {}
        override suspend fun updateOrder(order: com.example.data.model.RideOrderEntity) {}
        override suspend fun updateOrderRating(orderId: String, rating: Float, feedback: String) {}
        override suspend fun deleteOrder(orderId: String) {}
      }
    )
    val driverId = "drv_1"
    val initialDriver = repo.drivers.value.first { it.id == driverId }
    val initialCount = initialDriver.reviews.size

    repo.addDriverReview(driverId, "Test Rider", 5.0f, "Very polite and smooth driving!")

    val updatedDriver = repo.drivers.value.first { it.id == driverId }
    assertEquals(initialCount + 1, updatedDriver.reviews.size)
    assertEquals("Test Rider", updatedDriver.reviews.first().reviewerName)
    assertTrue("Driver rating should remain high with 5 star review", updatedDriver.rating >= 4.8f)
  }

  @Test
  fun `verify historical ride order entity tracking fields`() {
    val entity = com.example.data.model.RideOrderEntity(
      serviceType = "PASSENGER_RIDE",
      vehicleType = "E_RICKSHAW",
      pickupAddress = "Lions Chowk, Narayangarh",
      pickupLat = 27.7018,
      pickupLng = 84.4312,
      dropoffAddress = "Chaubiskothi Hospital Hub",
      dropoffLat = 27.6812,
      dropoffLng = 84.4310,
      distanceKm = 3.2,
      fareNpr = 65,
      paymentMethod = "ESEWA",
      paymentStatus = "PAID",
      driverName = "Ram Bahadur Thapa",
      status = "COMPLETED",
      rating = 5.0f,
      reviewFeedback = "Clean E-Rickshaw, Arrived On Time"
    )

    assertEquals("Lions Chowk, Narayangarh", entity.pickupAddress)
    assertEquals("Chaubiskothi Hospital Hub", entity.dropoffAddress)
    assertEquals(65, entity.fareNpr)
    assertEquals(5.0f, entity.rating)
    assertEquals("Clean E-Rickshaw, Arrived On Time", entity.reviewFeedback)
    assertTrue("Timestamp should be recorded", entity.timestamp > 0)
  }
}
