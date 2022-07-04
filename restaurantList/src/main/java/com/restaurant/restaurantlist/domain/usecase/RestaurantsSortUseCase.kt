package com.restaurant.restaurantlist.domain.usecase


import com.restaurant.core.core.di.qualifier.IoDispatcher
import com.restaurant.core.core.model.Result
import com.restaurant.core.core.model.data
import com.restaurant.restaurantlist.domain.model.Restaurants
import com.restaurant.restaurantlist.domain.model.SearchSortFilter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RestaurantsSortUseCase @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    private var restaurantsResult: Result<Restaurants>? = null
    private var restaurantsData: Restaurants? = null

    private val sortFilterFlow = MutableStateFlow(SearchSortFilter())

    fun getSortFilterFlow(): Flow<SearchSortFilter> = sortFilterFlow

    fun setRestaurantsResult(result: Result<Restaurants>) {
        if (restaurantsData == null &&
            result.data?.restaurants?.isNotEmpty() == true
        ) {
            restaurantsData = result.data
        }
        restaurantsResult = result
    }

    fun setQuery(value: String) {
        sortFilterFlow.value = sortFilterFlow.value.copy(filterQuery = value)
    }

    fun setSorting(sorting: SearchSortFilter.Sorting) {
        sortFilterFlow.value = sortFilterFlow.value.copy(sorting = sorting)
    }

    fun refresh() {
        sortFilterFlow.value = SearchSortFilter()
    }

    fun sortRestaurants(): Flow<Result<Restaurants>> = flowOf(
        restaurantsData?.let {
            Result.Success(Restaurants(it.restaurants.filter { restaurant ->
                restaurant.name.lowercase()
                    .contains(sortFilterFlow.value.filterQuery.lowercase())
            }.sortedWith(
                compareBy<Restaurants.Restaurant> { restaurant ->
                    if (sortFilterFlow.value.sorting.isSortByStatus) restaurant.status.priority
                    else 1
                }.thenByDescending { restaurant ->
                    when {
                        sortFilterFlow.value.sorting.isSortByBestMatch ->
                            restaurant.sortingValues.bestMatch
                        sortFilterFlow.value.sorting.isSortByNewest ->
                            restaurant.sortingValues.newest
                        sortFilterFlow.value.sorting.isSortByRating ->
                            restaurant.sortingValues.ratingAverage
                        sortFilterFlow.value.sorting.isSortByDistance ->
                            restaurant.sortingValues.distance
                        sortFilterFlow.value.sorting.isSortByPopularity ->
                            restaurant.sortingValues.popularity
                        sortFilterFlow.value.sorting.isSortByAveragePrice ->
                            restaurant.sortingValues.averageProductPrice
                        sortFilterFlow.value.sorting.isSortByDeliveryCost ->
                            restaurant.sortingValues.deliveryCosts
                        sortFilterFlow.value.sorting.isSortByMinimumCost ->
                            restaurant.sortingValues.minCost
                        else -> -restaurant.id
                    }
                }
            )))
        } ?: restaurantsResult ?: Result.Loading)
        .flowOn(ioDispatcher)
}