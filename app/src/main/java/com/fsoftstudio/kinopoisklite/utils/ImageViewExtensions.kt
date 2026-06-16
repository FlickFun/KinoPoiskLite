package com.fsoftstudio.kinopoisklite.utils

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.fsoftstudio.kinopoisklite.R
import com.fsoftstudio.kinopoisklite.common.FavoriteIdsStorage
import com.fsoftstudio.kinopoisklite.data.Utils.ImagesDownloader
import com.fsoftstudio.kinopoisklite.databinding.CardPosterBinding
import com.fsoftstudio.kinopoisklite.domain.models.Poster
import com.fsoftstudio.kinopoisklite.domain.usecase.ListCinemaSearchUseCase
import com.fsoftstudio.kinopoisklite.presentation.animateDefault
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ImagesDownloaderEntryPoint {
    fun imagesDownloader(): ImagesDownloader
}

private fun getImagesDownloader(view: View): ImagesDownloader {
    return EntryPointAccessors.fromApplication(
        view.context.applicationContext,
        ImagesDownloaderEntryPoint::class.java
    ).imagesDownloader()
}

fun ImageView.changeFavorite(
    poster: Poster,
    listCinemaSearchUseCase: ListCinemaSearchUseCase
) {
    val icon = when (FavoriteIdsStorage.get().contains(poster.id)) {
        true -> {
            poster.favorite = false
            listCinemaSearchUseCase.deleteFavoritesCinemaFromFavoritesList(poster.id)
            R.drawable.round_star_border_24
        }
        false -> {
            poster.favorite = true
            listCinemaSearchUseCase.addFavoritesCinemaToFavoritesList(poster.id)
            listCinemaSearchUseCase.favoriteChecked(poster.id, poster.cinema)
            R.drawable.round_star_24
        }
    }
    setImageResource(icon)
}

fun ImageView.setCheckedFavorite(isChecked: Boolean) {
    val icon = when (isChecked) {
        true -> R.drawable.round_star_24
        false -> R.drawable.round_star_border_24
    }
    setImageResource(icon)
}

fun ImageView.setPopularPosterImage(posterBinding: CardPosterBinding, poster: Poster) {
    if (poster.posterPath == null) {
        setImageResource(R.drawable.round_no_photography_24)
        posterBinding.pbPoster.visibility = View.GONE
        return
    }

    getImagesDownloader(this).getImage(
        poster.posterPath,
        poster.id.toString(),
        this,
        posterBinding.pbPoster
    ) { bitmap ->
        setImageBitmap(bitmap)
        visibility = View.VISIBLE
        posterBinding.cardIvPoster.animateDefault()
    }
}

fun ImageView.setPosterImage(poster: Poster, pbListItem: ProgressBar) {
    if (poster.posterPath == null) {
        setImageResource(R.drawable.round_no_photography_24)
        visibility = View.VISIBLE
        pbListItem.visibility = View.GONE
        return
    }

    getImagesDownloader(this).getImage(
        poster.posterPath,
        poster.id.toString(),
        this,
        pbListItem
    ) { bitmap ->
        setImageBitmap(bitmap)
        visibility = View.VISIBLE
        pbListItem.visibility = View.GONE
    }
}
