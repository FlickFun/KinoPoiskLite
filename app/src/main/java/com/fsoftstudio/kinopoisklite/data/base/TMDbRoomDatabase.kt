/**
 * Copyright (C) 2023 Anatoliy Ferin - Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fsoftstudio.kinopoisklite.data.base

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fsoftstudio.kinopoisklite.data.request.local.room.TMDbDao
import com.fsoftstudio.kinopoisklite.data.request.local.room.TMDbFtsDao
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.*
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.CinemaInfoEntity
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.FavoriteEntity
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.TvSeriesPosterEntity
import com.fsoftstudio.kinopoisklite.data.request.local.room.dao.UserEntity

@Database(
    entities = [
        MoviePosterEntity::class,
        TvSeriesPosterEntity::class,
        FavoriteEntity::class,
        UserEntity::class,
        CinemaInfoEntity::class,
        CinemaInfoEntityFTS4::class
    ],
    version = 1,
    exportSchema = true
)
abstract class TMDbRoomDatabase : RoomDatabase() {
    abstract fun tMDbDao(): TMDbDao
    abstract fun tMDbFtsDao(): TMDbFtsDao
}