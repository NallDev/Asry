package com.nalldev.asry.presentation.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.nalldev.asry.R
import com.nalldev.asry.domain.usecases.main.FetchAllStoriesUseCase
import com.nalldev.asry.domain.usecases.user_session.UserSessionUseCases
import com.nalldev.asry.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userSessionUseCases: UserSessionUseCases,
    fetchAllStoriesUseCase: FetchAllStoriesUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val stories = fetchAllStoriesUseCase().cachedIn(viewModelScope)

    private val _navigateState = SingleLiveEvent<NavigateState>()
    val navigateState: LiveData<NavigateState> = _navigateState

    private val _reloadStories = SingleLiveEvent<Nothing>()
    val reloadStories: LiveData<Nothing> = _reloadStories

    private val _toastEvent = SingleLiveEvent<String>()
    val toastEvent: LiveData<String> = _toastEvent

    var page : Int = 0
        private set

    private val disposables = CompositeDisposable()

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

    fun addPage() {
        page = page.plus(1)
    }

    fun reloadStories() {
        page = 0
        _reloadStories.call()
    }

    enum class NavigateState {
        AUTH
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}