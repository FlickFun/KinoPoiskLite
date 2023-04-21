package com.fsoftstudio.kinopoisklite.utils

import android.net.Uri
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
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.io.File

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

fun ImageView.setPopularPosterImage(posterBinding: CardPosterBinding, poster: Poster) = with(posterBinding) {
    val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
    }
    CoroutineScope(IO).launch(coroutineExceptionHandler) {
        fun showPoster() {
            cardIvPoster.animateDefault()
            pbPoster.visibility = View.GONE
        }

        when (val posterPath = poster.posterPath) {
            null -> {
                withContext(Main) {
                    setImageResource(R.drawable.round_no_photography_24)
                    showPoster()
                }
            }
            else -> {
                ImagesDownloader().getImage(
                    posterPath,
                    poster.id.toString(),
                    ivPoster,
                    pbPoster
                ) {
                    launch(Main) {
                        setImageURI(Uri.fromFile(File(it)))
                        showPoster()
                    }
                }
            }
        }
    }
}

fun ImageView.setPosterImage(poster: Poster, pbListItem: ProgressBar) {
    when (poster.posterPath) {
        null -> {
            setImageResource(R.drawable.round_no_photography_24)
            visibility = View.VISIBLE
            pbListItem.visibility = View.GONE
        }
        else -> {
            val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->
            }
            CoroutineScope(IO).launch(coroutineExceptionHandler) {
                ImagesDownloader().getImage(
                    poster.posterPath.toString(),
                    poster.id.toString(),
                    this@setPosterImage,
                    pbListItem
                ) {
                    launch(Main) {
                        setImageURI(Uri.fromFile(File(it)))
                        visibility = View.VISIBLE
                        pbListItem.visibility = View.GONE
                    }
                }
            }
        }
    }
}

