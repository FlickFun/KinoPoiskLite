package com.fsoftstudio.kinopoisklite.common

fun List<Int>.saveToFavorite() {
    FavoriteIdsStorage.addAllIds(this.toHashSet())
}

fun Int.addToFavorite() {
    FavoriteIdsStorage.addId(this)
}

fun Int.removeFromFavorite() {
    FavoriteIdsStorage.removeId(this)
}