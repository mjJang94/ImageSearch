package com.mj.data.model

import com.mj.data.model.ItemResponse

data class ImageSearchResponse(
    val lastBuildDate: String,
    val total: Int,
    val start: Int,
    val display: Int,
    val items: List<ItemResponse>
)