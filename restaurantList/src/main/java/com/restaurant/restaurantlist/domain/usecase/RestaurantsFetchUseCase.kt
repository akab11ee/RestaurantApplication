package com.restaurant.restaurantlist.domain.usecase

import com.restaurant.core.core.di.qualifier.IoDispatcher
import com.restaurant.core.core.model.Result
import com.restaurant.restaurantlist.data.interfaces.RestaurantsRepository
import com.restaurant.restaurantlist.domain.mapper.RestaurantsMapper
import com.restaurant.restaurantlist.domain.model.Restaurants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class RestaurantsFetchUseCase @Inject constructor(
    private val restaurantsRepository: RestaurantsRepository,
    private val restaurantsMapper: RestaurantsMapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun fetchRestaurants(): Flow<Result<Restaurants>> = flow {
        emit(Result.Loading)

        emitAll(restaurantsRepository.fetchRestaurants().map {
            Result.Success(restaurantsMapper.mapToRestaurants(it))
        })
    }.catch {
        emit(Result.Error(it))
    }.onEach {
        Timber.d(it.toString())
    }.flowOn(ioDispatcher)
}