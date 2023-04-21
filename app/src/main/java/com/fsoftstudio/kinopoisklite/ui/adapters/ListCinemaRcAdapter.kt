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
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fsoftstudio.kinopoisklite.common.FavoriteIdsStorage
import com.fsoftstudio.kinopoisklite.databinding.CardSearchBinding
import com.fsoftstudio.kinopoisklite.domain.models.Poster
import com.fsoftstudio.kinopoisklite.domain.usecase.ListCinemaSearchUseCase
import com.fsoftstudio.kinopoisklite.common.entity.Const.FRAGMENT_DEFAULT
import com.fsoftstudio.kinopoisklite.common.entity.Const.FRAGMENT_FAVORITE
import com.fsoftstudio.kinopoisklite.ui.screens.MainActivity
import com.fsoftstudio.kinopoisklite.utils.changeFavorite
import com.fsoftstudio.kinopoisklite.utils.setCheckedFavorite
import com.fsoftstudio.kinopoisklite.utils.setPosterImage
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import java.util.*
import javax.inject.Inject

class ListCinemaRcAdapter @Inject constructor(
    private val listCinemaSearchUseCase: ListCinemaSearchUseCase
) :
    RecyclerView.Adapter<ListCinemaRcAdapter.SearchHolder>() {

    @Volatile
    private var posters = mutableListOf<Poster>()
    private var fromFragment: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {
        val binding = CardSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchHolder(binding, listCinemaSearchUseCase, this, fromFragment)
    }

    override fun onBindViewHolder(holder: SearchHolder, position: Int) {
        holder.onBind(posters[position], position)
    }

    override fun getItemCount(): Int {
        return posters.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRcAdapter(newPosters: List<Poster> = posters, fromFragment: Int = FRAGMENT_DEFAULT) {
        this.fromFragment = fromFragment
        newPosters.forEach { e ->
            e.favorite = FavoriteIdsStorage.get().contains(e.id)
        }

        val diffResult =
            DiffUtil.calculateDiff(DiffUtilForListsChangedAnimation(posters, newPosters))
        diffResult.dispatchUpdatesTo(this)
        posters.clear()
        posters.addAll(newPosters)
    }

    fun checkNotifyItemChanged(position: Int) {
        CoroutineScope(Main).launch {
            delay(300)
            posters[position].apply {
                favorite = FavoriteIdsStorage.get().contains(id)
            }
            notifyItemChanged(position)
        }
    }

    class SearchHolder(
        private val binding: CardSearchBinding,
        private val listCinemaSearchUseCase: ListCinemaSearchUseCase,
        private val listCinemaRcAdapter: ListCinemaRcAdapter,
        private val fromFragment: Int
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
        fun onBind(poster: Poster, position: Int) = with(binding) {
            ivSearch.setPosterImage(poster, pbListItem)
            ibSwitchFavoriteInfo.setCheckedFavorite(poster.favorite)
            tvMovieNameFindItem.text = poster.title
            cvItem.setOnClickListener {
                MainActivity.mainActivity?.openCinemaInfo(poster)
                if (fromFragment != FRAGMENT_FAVORITE) {
                    cinema = poster.cinema
                    positionItem = position
                }
            }
            ibSwitchFavoriteInfo.setOnClickListener {
                ibSwitchFavoriteInfo.changeFavorite(poster, listCinemaSearchUseCase)
                if (fromFragment == FRAGMENT_FAVORITE) {
                    listCinemaRcAdapter.updateRcAdapter(newPosters = mutableListOf<Poster>().apply {
                        addAll(listCinemaRcAdapter.posters)
                        if (!remove(poster.copy(favorite = false))) {
                            remove(poster.copy(favorite = true))
                        }
                    }, fromFragment = FRAGMENT_FAVORITE)
                }
            }
        }
    }

    companion object {

        @JvmStatic
        var cinema: String? = null

        @JvmStatic
        var positionItem: Int? = null
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
        return oldList?.get(oldItemPosition)?.id == newList?.get(newItemPosition)?.id
                && oldList?.get(oldItemPosition)?.favorite == newList?.get(newItemPosition)?.favorite
    }
}