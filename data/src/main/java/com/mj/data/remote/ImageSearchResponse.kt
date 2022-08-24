package com.mj.data.remote

import com.mj.data.remote.model.ItemResponse

data class ImageSearchResponse(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<ItemResponse>
)