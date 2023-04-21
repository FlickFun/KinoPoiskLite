package com.fsoftstudio.kinopoisklite.domain.mappers

import com.fsoftstudio.kinopoisklite.data.cinema.entities.*
import com.fsoftstudio.kinopoisklite.domain.models.CinemaInfo
import com.fsoftstudio.kinopoisklite.common.entity.Const.MOVIE
import com.fsoftstudio.kinopoisklite.common.entity.Const.NO_DATA
import com.fsoftstudio.kinopoisklite.common.entity.Const.NO_TITLE
import com.fsoftstudio.kinopoisklite.common.entity.Const.TV_SERIES
import javax.inject.Inject

class CinemaInfoMapper @Inject constructor(){

    fun fromRoomCinemaInfoDataEntity(entity: RoomCinemaInfoDataEntity): CinemaInfo =
        CinemaInfo(
            title = entity.title,
            releaseDate = entity.releaseDate ?: NO_DATA,
            runtime = entity.runtime ?: NO_DATA,
            genres = entity.genresStr ?: NO_DATA,
            actors = entity.actorsStr ?: NO_DATA,
            oveview = entity.overview ?: NO_DATA
        )
    fun fromRetrofitCinemaInfoDataEntity(entity: RetrofitCinemaInfoDataEntity, retrofitCinemaActorDataEntitiesList: RetrofitCinemaActorDataEntitiesList?): CinemaInfo =
        CinemaInfo(
            title = setNoTitleIfNullOrEmpty(entity.titleMovie ?: entity.titleTvSeries),

            releaseDate = setNoDataIfNullOrEmpty(entity.releaseDate),
            runtime = setNoDataIfNullOrEmpty(entity.runtime),

            genres = setNoDataIfNullOrEmpty(getGenres(entity)),
            actors = setNoDataIfNullOrEmpty(retrofitCinemaActorDataEntitiesList?.let { getActors(it) }),

            oveview = setNoDataIfNullOrEmpty(entity.overview)
        )
    fun fromRetrofitToRoomCinemaInfoDataEntity(entity: RetrofitCinemaInfoDataEntity, cinemaInfo: CinemaInfo, cinema: String): RoomCinemaInfoDataEntity =
        RoomCinemaInfoDataEntity(
            rowId = entity.id + if (cinema == TV_SERIES) 1_000_000_000 else 0,
            id = entity.id,
            title = setNoTitleIfNullOrEmpty(if (cinema == MOVIE) entity.titleMovie else entity.titleTvSeries),
            posterPath = setNullIfEmpty(entity.posterPath),
            popularityWeight = entity.popularityWeight ?: 0F,
            releaseDate = setNullIfEmpty(entity.releaseDate),
            runtime = setNullIfEmpty(entity.runtime),
            overview = setNullIfEmpty(entity.overview),

            genresStr = setNullIfNoData(cinemaInfo.genres),
            actorsStr = setNullIfNoData(cinemaInfo.actors),

            cinema = cinema
        )
}

private fun setNullIfNoData(data: String) =
    if (data == NO_DATA) null else data
private fun setNullIfEmpty(data: String?) =
    if (data.isNullOrEmpty()) null else data
private fun setNoDataIfNullOrEmpty(data: String?) =
    if (data.isNullOrEmpty()) NO_DATA else data
private fun setNoTitleIfNullOrEmpty(data: String?) =
    if (data.isNullOrEmpty()) NO_TITLE else data

fun getActors(retrofitCinemaActorDataEntitiesList: RetrofitCinemaActorDataEntitiesList): String? {

    if (retrofitCinemaActorDataEntitiesList.cast.isEmpty()) {
        return NO_DATA
    }
    var strActors = ""
    val countActors = retrofitCinemaActorDataEntitiesList.cast.size

    val first = if (countActors > 5) {
        retrofitCinemaActorDataEntitiesList.cast.take(5).toMutableList() + (retrofitCinemaActorDataEntitiesList.cast
            .drop(5).filter { e -> e.popularityWeight > 3 })
    } else {
        retrofitCinemaActorDataEntitiesList.cast
    }

    first.forEach { e -> strActors += "${e.name}, " }
    return if (strActors.isNotEmpty()) strActors.dropLast(2) else null
}

fun getGenres(retrofitCinemaInfoDataEntity: RetrofitCinemaInfoDataEntity): String? {
    var strGenres = ""
    retrofitCinemaInfoDataEntity.genres?.forEach { e -> strGenres += "${e.name}, " }
    return if (strGenres.isNotEmpty()) strGenres.dropLast(2) else null
}