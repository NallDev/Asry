package com.nalldev.asry.presentation.ui.home

import com.nalldev.asry.domain.models.StoryModel

object DataDummy {

    fun generateDummyStoryResponse(): List<StoryModel> {
        val items: MutableList<StoryModel> = arrayListOf()
        for (i in 0..100) {
            val story = StoryModel(
                i.toString(),
                "name $i",
                "description $i",
                "https://picsum.photos/200/300",
                "2024-10-27T10:54:53.518Z",
                null,
                null
            )
            items.add(story)
        }
        return items
    }
}