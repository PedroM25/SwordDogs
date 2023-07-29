package com.example.sworddogs.model

import com.google.gson.annotations.SerializedName

data class BreedResponse(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("temperament")
    val temperament: String? = null,

    @field:SerializedName("origin")
    val origin: String? = null,

    @field:SerializedName("breed_group")
    val breedGroup: String? = null,

    @field:SerializedName("image")
    val image: ImageResponse? = null
)

data class ImageResponse(
    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("width")
    val width: Int? = null,

    @field:SerializedName("height")
    val height: Int? = null,

    @field:SerializedName("url")
    val url: String? = null
)