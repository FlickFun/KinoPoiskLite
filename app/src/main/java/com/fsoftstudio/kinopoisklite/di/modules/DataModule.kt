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
package com.fsoftstudio.kinopoisklite.di.modules

import com.fsoftstudio.kinopoisklite.data.base.TMDbRoomDatabase
import com.fsoftstudio.kinopoisklite.data.request.local.AppLocalDataSource
import com.fsoftstudio.kinopoisklite.data.request.remote.retrofit.RetrofitTMDbDataSource
import com.fsoftstudio.kinopoisklite.data.request.remote.TMDbRemoteDataSource
import com.fsoftstudio.kinopoisklite.data.request.remote.retrofit.TMDbApi
import com.fsoftstudio.kinopoisklite.data.request.local.TMDbLocalDataSource
import com.fsoftstudio.kinopoisklite.data.request.local.room.RoomTMDbDataSource
import com.fsoftstudio.kinopoisklite.data.request.local.sharedprefs.SharedPrefsDataSource
import com.fsoftstudio.kinopoisklite.data.request.local.sharedprefs.SharedPrefsRequest
import com.fsoftstudio.kinopoisklite.data.request.remote.AppRemoteDataSource
import com.fsoftstudio.kinopoisklite.data.request.remote.firebase.FireBaseApi
import com.fsoftstudio.kinopoisklite.data.request.remote.firebase.FireBaseDataSource
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideLocalDataSource(
        tMDbRoomDatabase: TMDbRoomDatabase
    ): TMDbLocalDataSource =
        RoomTMDbDataSource(tMDbRoomDatabase.tMDbDao(), tMDbRoomDatabase.tMDbFtsDao())

    @Provides
    @Singleton
    fun provideAppLocalDataSource(
        sharedPrefsRequest: SharedPrefsRequest
    ): AppLocalDataSource =
        SharedPrefsDataSource(sharedPrefsRequest)

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        tMDbApi: TMDbApi
    ): TMDbRemoteDataSource =
        RetrofitTMDbDataSource(tMDbApi)

    @Provides
    @Singleton
    fun provideAppRemoteDataSource(
        fireBaseApi: FireBaseApi
    ): AppRemoteDataSource =
        FireBaseDataSource(fireBaseApi)

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }
}