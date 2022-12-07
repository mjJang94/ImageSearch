package com.mj.imagesearch.ui.main.favorite

import androidx.lifecycle.*
import com.mj.domain.model.ThumbnailData
import com.mj.domain.usecase.GetLocalImageUseCase
import com.mj.imagesearch.ui.main.favorite.FavoritesAdapter.FavoriteCallback
import com.mj.imagesearch.util.hide
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getLocalImageUseCase: GetLocalImageUseCase
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext = viewModelScope.coroutineContext

    private val _favoritesItems = MutableLiveData<List<FavoritesAdapter.Item>>()
    val favoritesItems = _favoritesItems.hide()

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty = _isEmpty.hide()

    val callback = object : FavoriteCallback {
        override fun toggle(data: FavoritesAdapter.Item) {
            launch(Dispatchers.IO) {
                deleteFavoriteImage(data.uid)
            }
        }
    }

    suspend fun emitData() {
        getLocalImageUseCase.getAll()
            .flowOn(Dispatchers.IO)
            .onStart { Timber.d("favorite flow start") }
            .onCompletion { Timber.d("favorite flow complete") }
            .catch { Timber.e("favorite flow error") }
            .collect { favorites ->
                if (favorites.isEmpty()) _isEmpty.postValue(true)
                Timber.d("favorite size : ${favorites.size}")
                val item = favorites.map { it.formalize() }
                _favoritesItems.postValue(item)
                _isEmpty.postValue(false)
            }
    }

    private suspend fun deleteFavoriteImage(uid: Long) {
        getLocalImageUseCase.delete(uid)
        Timber.d("delete thumbnail : $uid")
        emitData()
    }

    private fun ThumbnailData.formalize(): FavoritesAdapter.Item =
        FavoritesAdapter.Item(uid = uid, thumbnail = thumbnail)
}