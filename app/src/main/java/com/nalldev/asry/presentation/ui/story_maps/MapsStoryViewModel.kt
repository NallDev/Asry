package com.nalldev.asry.presentation.ui.story_maps

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nalldev.asry.domain.models.StoryModel
import com.nalldev.asry.domain.usecases.maps.FetchStoryWithCoordinateUseCase
import com.nalldev.asry.util.CommonHelper
import com.nalldev.asry.util.SingleLiveEvent
import com.nalldev.asry.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MapsStoryViewModel @Inject constructor(
    private val fetchStoryWithCoordinateUseCase: FetchStoryWithCoordinateUseCase,
    @ApplicationContext private val context : Context
) : ViewModel() {
    private val _stories : MutableLiveData<UIState<List<StoryModel>>> = MutableLiveData()
    val stories : LiveData<UIState<List<StoryModel>>> = _stories

    private val _toastEvent = SingleLiveEvent<String>()
    val toastEvent: LiveData<String> = _toastEvent

    private val disposables = CompositeDisposable()

    fun fetchStoryWithCoordinate() {
        _stories.postValue(UIState.Loading)

        val disposable = fetchStoryWithCoordinateUseCase()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ stories ->
                _stories.postValue(UIState.Success(stories))
            },{ throwable ->
                val message = CommonHelper.getErrorMessage(throwable, context)
                _toastEvent.postValue(message)
                _stories.postValue(UIState.Error)
            })
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}