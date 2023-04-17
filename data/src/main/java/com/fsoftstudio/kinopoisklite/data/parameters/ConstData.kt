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
package com.fsoftstudio.kinopoisklite.data.parameters

internal object ConstData {

    const val MOVIE_POPULAR = "movie/popular"
    const val TV_SERIES_POPULAR = "tv/popular"
    const val MOVIE_INFO = "movie/"
    const val TV_SERIES_INFO = "tv/"
    const val CREDITS_ACTORS = "/credits"
    const val SEARCH_MOVIE = "search/movie"
    const val SEARCH_TV_SERIES = "search/tv"

    const val APPLICATION_JSON = "Content-Type: application/json"
    const val API_KEY  = "api_key"
    const val LANGUAGE = "language"
    const val APPEND_TO_RESPONSE = "append_to_response"
    const val INCLUDE_IMAGE_LANGUAGE = "include_image_language"
    const val QUERY = "query"

    const val LANGUAGE_VALUE = "ru-RU"
    const val APPEND_TO_RESPONSE_VALUE = "images"
    const val INCLUDE_IMAGE_LANGUAGE_VALUE = "ru,null"

    const val NOTHING = "nothing"
    const val MOVIE = "movie"
    const val TV_SERIES = "tvSeries"

    const val NO_DATA = "Нет данных"
    const val NO_TITLE = "Без названия"

    const val ERROR_WRONG_CATEGORY = "{\"status_code\":\"100\",\"status_message\":\"Неверно указана категория для поиска.\"}"

    const val MAIN_NODE = "main"
    const val APP_DATA_NODE = "app_data"
    const val TAG_NOTIFY_CHECK_CHANGE_POSTERS_WORKER = "NOTIFY_POSTERS_WORKER"


// -----  SharedPrefs -------

    const val PREFS_NAME = "app_prefs"
    const val KEY_THEME = "prefs.theme"
    const val KEY_TELEGRAM_DEV = "prefs.telegram.dev"
    const val KEY_BOOT_AUTO_START = "prefs.boot_auto_start"

    const val THEME_SYSTEM = -1

    const val BOOT_AUTO_START_ASC_LATE = 1
}