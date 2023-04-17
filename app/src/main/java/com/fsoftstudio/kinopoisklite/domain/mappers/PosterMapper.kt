package com.fsoftstudio.kinopoisklite.domain.mappers

import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity
import com.fsoftstudio.kinopoisklite.data.movies.entities.RetrofitMoviePosterDataEntity
import com.fsoftstudio.kinopoisklite.data.tvseries.entities.RetrofitTvSeriesPosterDataEntity
import com.fsoftstudio.kinopoisklite.domain.models.Poster
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.MOVIE
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.NO_DATA
import com.fsoftstudio.kinopoisklite.parameters.ConstApp.TV_SERIES
import javax.inject.Inject

class PosterMapper @Inject constructor() {

    fun fromRoomCinemaInfoDataEntityListForFavorites(roomCinemaInfoDataEntityList: List<RoomCinemaInfoDataEntity>): List<Poster> =
        roomCinemaInfoDataEntityList.map {
            Poster(
                id = it.id,
                title = it.title,
                posterPath = it.posterPath,
                cinema = it.cinema,
                favorite = true
            )
        }

    fun fromRetrofitMoviePosterDataEntityList(
        retrofitMoviePosterDataEntity: List<RetrofitMoviePosterDataEntity>
    ): List<Poster> =
        retrofitMoviePosterDataEntity.map {
            Poster(
                id = it.id,
                title = it.title ?: NO_DATA,
                posterPath = it.posterPath,
                cinema = MOVIE,
                favorite = false
            )
        }

    fun fromRetrofitTvSeriesPosterDataEntityList(
        retrofitTvSeriesPosterDataEntity: List<RetrofitTvSeriesPosterDataEntity>,
    ): List<Poster> =
        retrofitTvSeriesPosterDataEntity.map {
            Poster(
                id = it.id,
                title = it.title ?: NO_DATA,
                posterPath = it.posterPath,
                cinema = TV_SERIES,
                favorite = false
            )
        }
}