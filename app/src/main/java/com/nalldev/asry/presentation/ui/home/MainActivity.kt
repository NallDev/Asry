package com.nalldev.asry.presentation.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.util.Pair
import com.nalldev.asry.R
import com.nalldev.asry.databinding.ActivityMainBinding
import com.nalldev.asry.databinding.ViewStoryBinding
import com.nalldev.asry.domain.models.StoryModel
import com.nalldev.asry.presentation.adapter.LoadingStateAdapter
import com.nalldev.asry.presentation.adapter.StoryAdapter
import com.nalldev.asry.presentation.ui.auth.AuthActivity
import com.nalldev.asry.presentation.ui.detail.DetailStoryActivity
import com.nalldev.asry.presentation.ui.story_maps.MapsStoryActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var storyAdapter : StoryAdapter

    private lateinit var loadingAdapter : LoadingStateAdapter

    private val storyAdapterListener = object : StoryAdapter.Listener{
        override fun onItemClicked(storyData: StoryModel, view: ViewStoryBinding) {
            val intent = Intent(this@MainActivity, DetailStoryActivity::class.java)
            intent.putExtra(DetailStoryActivity.EXTRAS, storyData)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@MainActivity,
                Pair(view.ivProfile, view.ivProfile.transitionName),
                Pair(view.tvItemName, view.tvItemName.transitionName),
                Pair(view.ivItemPhoto, view.ivItemPhoto.transitionName)
            )

            startActivity(intent, options.toBundle())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
        initObserver()
        initListener()
    }

    private fun initView() = with(binding) {
        storyAdapter = StoryAdapter(storyAdapterListener)
        loadingAdapter = LoadingStateAdapter {
            storyAdapter.retry()
        }
        storyAdapter.withLoadStateFooter(footer = loadingAdapter)

        rvStory.adapter = storyAdapter
    }

    private fun initObserver() = with(viewModel) {
        stories.observe(this@MainActivity) { stories ->
            binding.swipeRefresh.isRefreshing = false
            storyAdapter.submitData(lifecycle, stories)
        }

        reloadStories.observe(this@MainActivity) {
            binding.rvStory.post {
                binding.rvStory.scrollToPosition(0)
            }
            binding.swipeRefresh.isRefreshing = true
            storyAdapter.refresh()
        }

        navigateState.observe(this@MainActivity) { navigateState ->
            if (navigateState == MainViewModel.NavigateState.AUTH) {
                navigateToAuth()
            }
        }

        toastEvent.observe(this@MainActivity) { message ->
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initListener() = with(binding) {
        actionMap.setOnClickListener {
            navigateToMap()
        }

        actionLogout.setOnClickListener {
            viewModel.doLogOut()
        }

        actionLanguage.setOnClickListener {
            navigateToLocaleSetting()
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.reloadStories()
        }
    }

    private fun navigateToAuth() {
        val intent = Intent(this@MainActivity, AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun navigateToMap() {
        val intent = Intent(this@MainActivity, MapsStoryActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToLocaleSetting() {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }
}