package com.restaurant.restaurantlist.data.memory

import com.restaurant.restaurantlist.data.interfaces.RestaurantsDataSource
import com.restaurant.restaurantlist.data.local.model.RestaurantsDto
import timber.log.Timber

class RestaurantMemoryDataSource : RestaurantsDataSource.Memory {
    private var restaurantsDto: RestaurantsDto? = null

    override fun cacheInMemory(dto: RestaurantsDto) {
        restaurantsDto = dto
    }

    override fun fetchRestaurants(): RestaurantsDto? {
        if (restaurantsDto != null) Timber.d("got result from cache")
        return restaurantsDto
    }
}