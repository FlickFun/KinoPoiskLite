package com.fsoftstudio.kinopoisklite.data.settings.entity

object ApiKey {
    private const val DEFAULT_KEY = "XXX"
    var getKey: String = DEFAULT_KEY
        private set
        
    fun setApiKey(key: String) {
        getKey = key.ifEmpty { DEFAULT_KEY }
    }
}