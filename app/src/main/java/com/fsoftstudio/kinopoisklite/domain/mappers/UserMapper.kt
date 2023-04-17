package com.fsoftstudio.kinopoisklite.domain.mappers

import com.fsoftstudio.kinopoisklite.data.accounts.entities.RoomUserDataEntity
import com.fsoftstudio.kinopoisklite.domain.models.User
import javax.inject.Inject

class UserMapper @Inject constructor(){

    fun fromRoomUserDataEntity(roomUserDataEntity: RoomUserDataEntity): User =
        User(
            id = roomUserDataEntity.id,
            login = roomUserDataEntity.login,
            password = roomUserDataEntity.password,
            logged = roomUserDataEntity.logged
        )
}