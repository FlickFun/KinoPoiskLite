package com.fsoftstudio.kinopoisklite.data.movies

import com.fsoftstudio.kinopoisklite.data.MoviesDataRepository
import com.fsoftstudio.kinopoisklite.data.cinema.entities.mapToRetrofitMovieDataEntity
import com.fsoftstudio.kinopoisklite.data.movies.entities.RetrofitMovieDataEntitiesList
import com.fsoftstudio.kinopoisklite.data.movies.entities.mapToRemote
import com.fsoftstudio.kinopoisklite.data.movies.sources.MoviesTMDbRemoteDataSource
import com.fsoftstudio.kinopoisklite.data.movies.sources.MoviesTMDbLocalDataSource
import com.fsoftstudio.kinopoisklite.common.entity.Const.MOVIE
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class MoviesDataRepositoryImpl @Inject constructor(
    private val moviesTMDbLocalDataSource: MoviesTMDbLocalDataSource,
    private val moviesTMDbRemoteDataSource: MoviesTMDbRemoteDataSource,
) : MoviesDataRepository {

    override suspend fun getLocalMoviePosters(): RetrofitMovieDataEntitiesList =
        RetrofitMovieDataEntitiesList(
            results = moviesTMDbLocalDataSource.loadMoviePopularEntities().
            take(9).map { it.mapToRemote() }
        )

    override suspend fun getRemoteMoviePosters(): Response<RetrofitMovieDataEntitiesList> =
        moviesTMDbRemoteDataSource.getResponseMoviesList().also {
            moviesTMDbLocalDataSource.saveListMoviesPosters(it.body()?.results?.take(9))
        }

    @OptIn(FlowPreview::class)
    override suspend fun getLocalListMovieSearch(searchText: String): Flow<RetrofitMovieDataEntitiesList> {
        return moviesTMDbLocalDataSource.searchMovieEntitiesByText(MOVIE, searchText).flatMapConcat {
            flow {
                emit(
                    RetrofitMovieDataEntitiesList(
                        results = it.map { it.mapToRetrofitMovieDataEntity() }
                    ))
            }
        }
    }

    override suspend fun getRemoteListMovieSearch(searchText: String): Response<RetrofitMovieDataEntitiesList> =
        moviesTMDbRemoteDataSource.getResponseListMoviesSearchByText(searchText)
}