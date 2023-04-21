package com.fsoftstudio.kinopoisklite.common

object FavoriteIdsStorage {
    private val favoriteIds by lazy { mutableSetOf<Int>() }
    fun get() = favoriteIds
    fun addId(favoriteId: Int) = favoriteIds.add(favoriteId)
    fun addAllIds(favoriteIdsSet: HashSet<Int>) {
        favoriteIds.clear()
        favoriteIds.addAll(favoriteIdsSet)
    }
    fun removeId(favoriteId: Int) = favoriteIds.remove(favoriteId)
}