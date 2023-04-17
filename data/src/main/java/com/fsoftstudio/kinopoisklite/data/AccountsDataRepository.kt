package com.fsoftstudio.kinopoisklite.data

import com.fsoftstudio.kinopoisklite.data.accounts.entities.RoomUserDataEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface AccountsDataRepository {
    fun getLoggedUserInfo() : Observable<RoomUserDataEntity>
    fun getUserInfo(login: String) : Observable<RoomUserDataEntity>
    fun saveUserInfo(roomUserDataEntity: RoomUserDataEntity) : Completable
}