package com.fsoftstudio.kinopoisklite.data

import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface FavoritesDataRepository {

    fun loadAllFavoritesEntitiesIdsByLogin(login: String): Single<List<Int>>
    fun addEntityIdToFavoritesEntities(id: Int, login: String): Completable
    fun deleteEntityIdFromFavoritesEntities(id: Int, login: String): Completable

    fun getFavoritesMoviePostersListByLogin(login: String) : Observable<List<RoomCinemaInfoDataEntity>>
    fun getFavoritesTvSeriesPostersListByLogin(login: String) : Observable<List<RoomCinemaInfoDataEntity>>
}