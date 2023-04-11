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
package com.fsoftstudio.kinopoisklite.ui.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fsoftstudio.kinopoisklite.R
import com.fsoftstudio.kinopoisklite.databinding.CardSearchBinding
import com.fsoftstudio.kinopoisklite.domain.models.Poster
import com.fsoftstudio.kinopoisklite.domain.usecase.ListCinemaFavoriteUseCase
import com.fsoftstudio.kinopoisklite.domain.usecase.ListCinemaSearchUseCase
import com.fsoftstudio.kinopoisklite.parameters.Sys
import com.fsoftstudio.kinopoisklite.ui.screens.MainActivity
import com.fsoftstudio.kinopoisklite.utils.ImagesDownloader
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import javax.inject.Inject

class ListCinemaRcAdapter @Inject constructor(
    private val useCase: ListCinemaSearchUseCase
) :
    RecyclerView.Adapter<ListCinemaRcAdapter.SearchHolder>() {

    private val findPosters = mutableListOf<Poster>()
    private var favoriteHashSet: HashSet<Int>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val binding = CardSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchHolder(binding, useCase, favoriteHashSet)
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        holder.setData(findPosters[position])
    }

    override fun getItemCount(): Int {
        return findPosters.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSearchRcAdapter(newFindPosters: List<Poster>) {
        favoriteHashSet = ListCinemaFavoriteUseCase.favorite
        newFindPosters.forEach { e -> e.favorite = favoriteHashSet?.contains(e.id) ?: false }
        val diffResult =
            DiffUtil.calculateDiff(DiffUtilForListsChangedAnimation(findPosters, newFindPosters))
        diffResult.dispatchUpdatesTo(this)
        findPosters.clear()
        findPosters.addAll(newFindPosters)
        notifyDataSetChanged()
    }

    class SearchHolder(
        private val binding: CardSearchBinding,
        private val useCase: ListCinemaSearchUseCase,
        private val favoriteHashSet: HashSet<Int>?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
        fun setData(poster: Poster) = with(binding) {

            val coroutineExceptionHandler = CoroutineExceptionHandler { _, _ ->

            }
            CoroutineScope(Dispatchers.IO).launch(coroutineExceptionHandler) {
                if (poster.posterPath != null && poster.posterPath != Sys.NOTHING) {
                    ImagesDownloader().getImage(poster.posterPath!!, poster.id.toString(), ivSearch, pbListItem) {
                        launch(Dispatchers.Main) {
                            ivSearch.setImageURI(Uri.fromFile(File(it)))
                            ivSearch.visibility = View.VISIBLE
                            pbListItem.visibility = View.GONE
                        }
                    }
                } else {
                    ivSearch.setImageDrawable(binding.root.context.getDrawable(R.drawable.round_no_photography_24))
                    ivSearch.visibility = View.VISIBLE
                    pbListItem.visibility = View.GONE
                }
            }

            ibSwitchFavoriteInfo.setImageDrawable(binding.root.context.let {
                if (poster.favorite) it.getDrawable(R.drawable.round_star_24)
                else it.getDrawable(R.drawable.round_star_border_24)
            })
            tvMovieNameFindItem.text =
                poster.title ?: binding.root.context.getString(R.string.no_title)

            cvItem.setOnClickListener {
                MainActivity.mainActivity?.openCinemaInfo(poster)
            }

            ibSwitchFavoriteInfo.setOnClickListener {
                if (favoriteHashSet?.contains(poster.id!!) == true) {
                    poster.favorite = false
                    useCase.deleteFavoriteCinemaFromFavoritesList(poster.id!!)
                    ibSwitchFavoriteInfo.setImageDrawable(binding.root.context.getDrawable(R.drawable.round_star_border_24))
                } else {
                    poster.favorite = true
                    useCase.addFavoriteCinemaToFavoritesList(poster.id!!)
                    ibSwitchFavoriteInfo.setImageDrawable(binding.root.context.getDrawable(R.drawable.round_star_24))
                    useCase.favoriteChecked(poster.id!!, poster.cinema)
                }
            }
        }
    }
}

class DiffUtilForListsChangedAnimation(
    private val oldList: List<Poster>?,
    private val newList: List<Poster>?
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newList?.size ?: 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList?.get(oldItemPosition)!!.id == newList?.get(newItemPosition)!!.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList?.get(oldItemPosition) == newList?.get(newItemPosition)
    }
}