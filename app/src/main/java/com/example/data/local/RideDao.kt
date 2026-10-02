package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.RideOrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RideDao {
    @Query("SELECT * FROM ride_orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<RideOrderEntity>>

    @Query("SELECT * FROM ride_orders WHERE id = :orderId LIMIT 1")
    fun getOrderById(orderId: String): Flow<RideOrderEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: RideOrderEntity)

    @Update
    suspend fun updateOrder(order: RideOrderEntity)

    @Query("UPDATE ride_orders SET rating = :rating, reviewFeedback = :feedback WHERE id = :orderId")
    suspend fun updateOrderRating(orderId: String, rating: Float, feedback: String)

    @Query("DELETE FROM ride_orders WHERE id = :orderId")
    suspend fun deleteOrder(orderId: String)
}
