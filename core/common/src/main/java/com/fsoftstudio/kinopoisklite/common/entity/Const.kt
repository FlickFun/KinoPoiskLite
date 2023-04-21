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
package com.fsoftstudio.kinopoisklite.common.entity

object Const {

    const val TAG_MOVIE_BASE = "MOVIE_BASE"
    const val LOCAL_POSTERS_FILES_PATH = "data/data/com.fsoftstudio.kinopoisklite/files/posters/"
    const val URI_PATH = "https://image.tmdb.org/t/p/w185"
    const val JPG = ".jpg"

    const val LOGIN_GUEST = "Guest"
    const val ID_INT = "id"
    const val TITLE = "title"
    const val STAR_BOOLEAN = "star"
    const val NOTHING = "nothing"
    const val CINEMA = "cinema"
    const val MOVIE = "movie"
    const val TV_SERIES = "tvSeries"

    const val MIN = " мин."

// -----  Notice and errors -------
    const val NOTICE_CACHE_CLEARED = "\tКэш очищен"
    const val ERROR_CANT_CLEAN_CACHE = "\tНе удалось очистить кэш"

    const val NO_DATA = "Нет данных"
    const val NO_TITLE = "Без названия"
    const val OK = "ok"
    const val ERROR_EMPTY_FIELD = "Заполните поля."
    const val ERROR_WRONG_LOGIN_ORE_PASSWORD = "Неправильный логин или пароль."
    const val ERROR_LOGIN_ALREADY_EXIST = "Этот логин уже используется."

    const val ERROR_WRONG_CATEGORY = "{\"status_code\":\"100\",\"status_message\":\"Неверно указана категория для поиска.\"}"
    const val PLEASE_INFORM_DEV = "\nСообщите, пожалуйста, разработчику."
    const val ERROR_INVALID_API_KEY_PLEASE_INFORM_DEV = "\tНедействительный Api-ключ.$PLEASE_INFORM_DEV"
    const val ERROR_UNKNOWN_PLEASE_INFORM_DEV = "\tНеизвестная ошибка.$PLEASE_INFORM_DEV"
    const val ERROR_RESOURCE_NOT_FOUND_PLEASE_INFORM_DEV = "\tЗапрошенный ресурс не найден.$PLEASE_INFORM_DEV"
    const val ERROR_NO_INTERNET = "\tНе подключен интернет либо не работает VPN."
    const val ERROR_NO_API_KEY_PLEASE_INFORM_DEV= "\tНет Api-ключа.$PLEASE_INFORM_DEV"

// -----  Notification -------
    const val NOTIFICATION_ID = "kino_poisk_lite_notification_id"
    const val NOTIFICATION_NAME = "kino_poisk_lite"
    const val NOTIFICATION_CHANNEL = "kino_poisk_lite_channel_01"
    const val NOTIFICATION_WORK = "kino_poisk_lite_notification_work"
    const val TAG_OUTPUT = "NotifyCheckChangePostersWorker"

// -----  SharedPrefs -------
    const val PREFS_NAME = "app_prefs"
    const val KEY_THEME = "prefs.theme"
    const val KEY_TELEGRAM_DEV = "prefs.telegram.dev"
    const val KEY_BOOT_AUTO_START = "prefs.boot_auto_start"

    const val THEME_SYSTEM = -1
    const val THEME_LIGHT = 1
    const val THEME_DARK = 2
    const val THEME_BATTERY = 3

    const val BOOT_AUTO_START_DO_NOT_ASC = 0
    const val BOOT_AUTO_START_ASC_LATE = 1
    const val BOOT_AUTO_START_OPEN_SETTINGS = 2

    const val FRAGMENT_DEFAULT = 0
    const val FRAGMENT_FAVORITE = 1

// -----  Retrofit -------
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

// -----  Firebase -------
    const val MAIN_NODE = "main"
    const val APP_DATA_NODE = "app_data"
}