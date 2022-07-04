package com.restaurant.restaurantlist.data.interfaces

import com.restaurant.restaurantlist.data.local.model.RestaurantsDto

interface RestaurantsDataSource {

    interface Memory {
        fun cacheInMemory(dto: RestaurantsDto)

        fun fetchRestaurants(): RestaurantsDto?
    }

    interface Local {
        suspend fun fetchRestaurants(): RestaurantsDto
    }
}