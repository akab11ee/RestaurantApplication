package com.restaurant.restaurantlist.navigation

sealed class Screen(val route: String) {
    object RestaurantsScreen : Screen("restaurant")
}