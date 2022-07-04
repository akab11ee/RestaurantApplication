package com.restaurant.restaurantlist.domain.mapper

import androidx.annotation.VisibleForTesting
import com.restaurant.restaurantlist.domain.model.Restaurants
import com.restaurant.restaurantlist.domain.model.Restaurants.Restaurant
import com.restaurant.restaurantlist.domain.model.Restaurants.Restaurant.SortingValues
import com.restaurant.restaurantlist.domain.model.Status
import com.restaurant.restaurantlist.data.local.model.RestaurantsDto
import javax.inject.Inject

class RestaurantsMapper @Inject constructor() {

    fun mapToRestaurants(dto: RestaurantsDto) = Restaurants(
        restaurants = dto.restaurants?.map(::mapToRestaurant).orEmpty()
    )

    @VisibleForTesting
    fun mapToRestaurant(dto: RestaurantsDto.RestaurantDto): Restaurant = Restaurant(
        id = dto.id.toDouble(),
        name = dto.name,
        status = mapToStatus(dto.status),
        sortingValues = mapToSortingValue(dto.sortingValues)
    )

    @VisibleForTesting
    fun mapToStatus(status: String?) =
        Status.values().find {
            it.toString().lowercase().replace("_", " ") == status
        } ?: Status.CLOSED

    @VisibleForTesting
    fun mapToSortingValue(dto: RestaurantsDto.RestaurantDto.SortingValuesDto): SortingValues =
        SortingValues(
            bestMatch = dto.bestMatch,
            deliveryCosts = dto.deliveryCosts.toDouble(),
            distance = dto.distance.toDouble(),
            minCost = dto.minCost.toDouble(),
            newest = dto.newest,
            popularity = dto.popularity,
            ratingAverage = dto.ratingAverage,
            averageProductPrice = dto.averageProductPrice.toDouble()
        )
}
