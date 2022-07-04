package com.restaurant.restaurantlist.data.interfaces

import com.restaurant.restaurantlist.data.local.model.RestaurantsDto
import kotlinx.coroutines.flow.Flow

interface RestaurantsRepository {
    suspend fun fetchRestaurants(): Flow<RestaurantsDto>
}