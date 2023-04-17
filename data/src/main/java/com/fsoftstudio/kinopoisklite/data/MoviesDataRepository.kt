package com.fsoftstudio.kinopoisklite.data

import com.fsoftstudio.kinopoisklite.data.movies.entities.RetrofitMovieDataEntitiesList
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface MoviesDataRepository {
    suspend fun getLocalMoviePosters() : RetrofitMovieDataEntitiesList
    suspend fun getRemoteMoviePosters() : Response<RetrofitMovieDataEntitiesList>

    suspend fun getLocalListMovieSearch(searchText: String) : Flow<RetrofitMovieDataEntitiesList>
    suspend fun getRemoteListMovieSearch(searchText: String) : Response<RetrofitMovieDataEntitiesList>

}