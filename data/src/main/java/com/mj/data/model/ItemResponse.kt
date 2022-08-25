package com.mj.data.model

import com.google.gson.annotations.SerializedName

data class ItemResponse(
    val title: String,
    val link: String,
    val thumbnail: String,
    @SerializedName("sizeheight") val sizeHeight: Int,
    @SerializedName("sizewidth") val sizeWidth: Int
)