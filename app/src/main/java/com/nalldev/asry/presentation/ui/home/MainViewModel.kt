package com.nalldev.asry.presentation.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.nalldev.asry.R
import com.nalldev.asry.domain.models.StoryModel
import com.nalldev.asry.domain.usecases.main.FetchAllStoriesUseCase
import com.nalldev.asry.domain.usecases.user_session.UserSessionUseCases
import com.nalldev.asry.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userSessionUseCases: UserSessionUseCases,
    fetchAllStoriesUseCase: FetchAllStoriesUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val storiesObservable = fetchAllStoriesUseCase().cachedIn(viewModelScope).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    private val _stories : MutableLiveData<PagingData<StoryModel>> = MutableLiveData()
    val stories : LiveData<PagingData<StoryModel>> = _stories

    private val _navigateState = SingleLiveEvent<NavigateState>()
    val navigateState: LiveData<NavigateState> = _navigateState

    private val _reloadStories = SingleLiveEvent<Nothing>()
    val reloadStories: LiveData<Nothing> = _reloadStories

    private val _toastEvent = SingleLiveEvent<String>()
    val toastEvent: LiveData<String> = _toastEvent

    private val disposables = CompositeDisposable()

    init {
        disposables.add(
            storiesObservable.subscribe{ pagingData ->
                _stories.postValue(pagingData)
            }
        )
    }

    fun doLogOut() {
        val disposable = userSessionUseCases.removeUserSession()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _navigateState.postValue(NavigateState.AUTH)
            }, {
                _toastEvent.postValue(context.getString(R.string.put_session_error_msg))
            })

        disposables.add(disposable)
    }

    fun reloadStories() {
        _reloadStories.call()
    }

    enum class NavigateState {
        AUTH, DETAIL
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}