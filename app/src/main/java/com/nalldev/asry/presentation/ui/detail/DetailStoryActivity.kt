package com.nalldev.asry.presentation.ui.detail

import android.os.Build
import android.os.Bundle
import android.transition.AutoTransition
import android.view.Window
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.ImageLoader
import coil.load
import com.nalldev.asry.R
import com.nalldev.asry.databinding.ActivityDetailStoryBinding
import com.nalldev.asry.domain.models.StoryModel

class DetailStoryActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetailStoryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupTransition()
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val storyData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRAS, StoryModel::class.java)
        } else {
            intent.getParcelableExtra(EXTRAS)
        }

        storyData?.let {
            binding.ivDetailProfile.transitionName = "profileImage_${it.id}"
            binding.tvDetailName.transitionName = "profileName_${it.id}"
            binding.tvDetailDescription.transitionName = "itemDescription_${it.id}"
            binding.ivDetailPhoto.transitionName = "itemPhoto_${it.id}"

            binding.tvDetailName.text = storyData.name
            binding.tvDetailDate.text = storyData.createdAt
            binding.tvDetailDescription.text = storyData.description
            val imageLoader = ImageLoader.Builder(this).allowHardware(false).build()
            binding.ivDetailPhoto.load(storyData.photoUrl, imageLoader) {
                placeholder(R.drawable.img_placeholder)
                crossfade(true)
            }

            binding.actionBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun setupTransition() = with(window) {
        requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        sharedElementEnterTransition = AutoTransition()
        sharedElementExitTransition = AutoTransition()
        exitTransition = AutoTransition()
        enterTransition = AutoTransition()
    }

    companion object {
        const val EXTRAS = "extras"
    }
}