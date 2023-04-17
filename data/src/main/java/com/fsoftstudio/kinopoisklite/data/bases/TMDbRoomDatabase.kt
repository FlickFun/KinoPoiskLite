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
package com.fsoftstudio.kinopoisklite.data.bases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fsoftstudio.kinopoisklite.data.accounts.entities.RoomUserDataEntity
import com.fsoftstudio.kinopoisklite.data.accounts.sources.AccountsDao
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntity
import com.fsoftstudio.kinopoisklite.data.cinema.entities.RoomCinemaInfoDataEntityFTS4
import com.fsoftstudio.kinopoisklite.data.cinema.sources.CinemaTMDbDao
import com.fsoftstudio.kinopoisklite.data.favorites.entities.RoomFavoritesEntity
import com.fsoftstudio.kinopoisklite.data.favorites.sources.FavoritesTMDbDao
import com.fsoftstudio.kinopoisklite.data.movies.entities.RoomMoviePosterDataEntity
import com.fsoftstudio.kinopoisklite.data.movies.sources.MoviesTMDbDao
import com.fsoftstudio.kinopoisklite.data.movies.sources.MoviesTMDbFtsDao
import com.fsoftstudio.kinopoisklite.data.tvseries.entities.RoomTvSeriesPosterEntity
import com.fsoftstudio.kinopoisklite.data.tvseries.sources.TvSeriesTMDbDao

@Database(
    entities = [
        RoomMoviePosterDataEntity::class,
        RoomTvSeriesPosterEntity::class,
        RoomFavoritesEntity::class,
        RoomUserDataEntity::class,
        RoomCinemaInfoDataEntity::class,
        RoomCinemaInfoDataEntityFTS4::class
    ],
    version = 1,
    exportSchema = true
)
abstract class TMDbRoomDatabase : RoomDatabase() {
    abstract fun moviesTMDbDao(): MoviesTMDbDao
    abstract fun moviesTMDbFtsDao(): MoviesTMDbFtsDao
    abstract fun tvSeriesTMDbDao(): TvSeriesTMDbDao
    abstract fun accountsDao(): AccountsDao
    abstract fun cinemaTMDbDao(): CinemaTMDbDao
    abstract fun favoriteTMDbDao(): FavoritesTMDbDao

}