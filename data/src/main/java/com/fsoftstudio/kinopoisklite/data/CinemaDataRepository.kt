package com.fsoftstudio.kinopoisklite.data

import com.fsoftstudio.kinopoisklite.data.cinema.entities.RetrofitCinemaActorDataEntitiesList
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RetrofitCinemaInfoDataEntity
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity
import retrofit2.Response

interface CinemaDataRepository {

    suspend fun getLocalCinemaInfo(id: Int, cinema: String) : RoomCinemaInfoDataEntity

    suspend fun saveLocalCinemaInfo(roomCinemaInfoDataEntity: RoomCinemaInfoDataEntity)

    suspend fun getRemoteCinemaInfo(id: Int, cinema: String) : Response<RetrofitCinemaInfoDataEntity>

    suspend fun getRemoteCinemaActorsList(id: Int, cinema: String) : Response<RetrofitCinemaActorDataEntitiesList>
}