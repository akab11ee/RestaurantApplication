package com.restaurant.restaurantlist.data

import com.restaurant.core.core.di.qualifier.IoDispatcher
import com.restaurant.restaurantlist.data.interfaces.RestaurantsDataSource
import com.restaurant.restaurantlist.data.interfaces.RestaurantsRepository
import com.restaurant.restaurantlist.data.local.model.RestaurantsDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RestaurantsRepoImpl @Inject constructor(
    private val restaurantMemoryDataSource: RestaurantsDataSource.Memory,
    private val restaurantLocalDataSource: RestaurantsDataSource.Local,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RestaurantsRepository {
    override suspend fun fetchRestaurants(): Flow<RestaurantsDto> = channelFlow {
        restaurantMemoryDataSource
            .fetchRestaurants()
            ?.let {
                send(it)
                close()
            }

        withContext(ioDispatcher) {
            launch {
                trySend(
                    restaurantLocalDataSource
                        .fetchRestaurants()
                        .also(restaurantMemoryDataSource::cacheInMemory)
                )
            }
        }
    }
}