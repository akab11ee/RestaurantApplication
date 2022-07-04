package com.restaurant.restaurantlist.data.local

import android.content.res.AssetManager
import com.restaurant.core.core.di.qualifier.IoDispatcher
import com.restaurant.restaurantlist.data.local.model.RestaurantsDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject

class RestaurantsProvider @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val json: Json,
    private val assetManager: AssetManager
) {

    @Suppress("BlockingMethodInNonBlockingContext")
    @OptIn(ExperimentalSerializationApi::class)
    suspend fun fetchRestaurants(): RestaurantsDto = withContext(ioDispatcher) {
        json.decodeFromStream(
            stream = assetManager.open("restaurantList.json")
        )
    }
}