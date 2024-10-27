package com.nalldev.asry.presentation.ui.home

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.nalldev.asry.domain.models.StoryModel
import com.nalldev.asry.domain.usecases.main.FetchAllStoriesUseCase
import com.nalldev.asry.domain.usecases.user_session.UserSessionUseCases
import com.nalldev.asry.presentation.adapter.StoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var userSessionUseCases: UserSessionUseCases

    @Mock
    private lateinit var fetchAllStoriesUseCase: FetchAllStoriesUseCase

    @Mock
    private lateinit var context: Context

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<StoryModel> = PagingData.from(dummyStory)
        val expectedStory = MutableLiveData<PagingData<StoryModel>>()
        expectedStory.value = data
        Mockito.`when`(fetchAllStoriesUseCase.invoke()).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(userSessionUseCases, fetchAllStoriesUseCase, context)
        val actualStory: PagingData<StoryModel> = mainViewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.Companion,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory.first(), differ.snapshot().first())
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<StoryModel> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<StoryModel>>()
        expectedStory.value = data
        Mockito.`when`(fetchAllStoriesUseCase.invoke()).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(userSessionUseCases, fetchAllStoriesUseCase, context)
        val actualStory: PagingData<StoryModel> = mainViewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.Companion,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertEquals(0, differ.snapshot().size)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}