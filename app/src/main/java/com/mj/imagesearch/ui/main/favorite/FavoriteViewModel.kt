package com.mj.imagesearch.ui.main.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.mj.domain.model.ThumbnailData
import com.mj.domain.usecase.GetLocalImageUseCase
import com.mj.imagesearch.ui.main.favorite.FavoritesAdapter.FavoriteCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getLocalImageUseCase: GetLocalImageUseCase
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext = viewModelScope.coroutineContext

    val favoritesItems = getLocalImageUseCase.favoriteImages.map {
        it.map { data -> data.formalize() }
    }

    val isEmpty = favoritesItems.map { it.isEmpty() }

    val callback = object : FavoriteCallback {
        override fun toggle(data: FavoritesAdapter.Item) {
            launch(Dispatchers.IO) {
                deleteFavoriteImage(data.uid)
            }
        }
    }

    private suspend fun deleteFavoriteImage(uid: Long) {
        getLocalImageUseCase.delete(uid)
        Timber.d("delete thumbnail : $uid")
    }

    private fun ThumbnailData.formalize(): FavoritesAdapter.Item =
        FavoritesAdapter.Item(uid = uid, thumbnail = thumbnail)
}