package com.restaurant.restaurantlist.data.local

import com.restaurant.restaurantlist.data.interfaces.RestaurantsDataSource
import com.restaurant.restaurantlist.data.local.model.RestaurantsDto
import kotlinx.coroutines.delay
import timber.log.Timber
import javax.inject.Inject

class RestaurantLocalDataSource @Inject constructor(private val restaurantsProvider: RestaurantsProvider) :
    RestaurantsDataSource.Local {
    override suspend fun fetchRestaurants(): RestaurantsDto {
        val restaurants = restaurantsProvider.fetchRestaurants()
        delay(750)
        Timber.d("restaurant list is fetched")
        return restaurants
    }
}