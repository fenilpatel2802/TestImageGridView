package com.wipro.testimagescrollapp.domain.model

import com.google.gson.annotations.SerializedName

data class ImageListResponse(

    @field:SerializedName("urls")
    val urls: Urls? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("likes")
    val likes: Int? = null
)


