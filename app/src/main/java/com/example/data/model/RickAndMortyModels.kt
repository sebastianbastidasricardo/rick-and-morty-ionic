package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CharacterResponse(
    @Json(name = "info") val info: PageInfo = PageInfo(),
    @Json(name = "results") val results: List<CharacterDto> = emptyList()
)

@JsonClass(generateAdapter = true)
data class PageInfo(
    @Json(name = "count") val count: Int = 0,
    @Json(name = "pages") val pages: Int = 0,
    @Json(name = "next") val next: String? = null,
    @Json(name = "prev") val prev: String? = null
)

@JsonClass(generateAdapter = true)
data class CharacterDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "status") val status: String,
    @Json(name = "species") val species: String,
    @Json(name = "type") val type: String = "",
    @Json(name = "gender") val gender: String = "",
    @Json(name = "origin") val origin: CharacterLocation = CharacterLocation(),
    @Json(name = "location") val location: CharacterLocation = CharacterLocation(),
    @Json(name = "image") val image: String,
    @Json(name = "episode") val episode: List<String> = emptyList(),
    @Json(name = "url") val url: String = "",
    @Json(name = "created") val created: String = ""
)

@JsonClass(generateAdapter = true)
data class CharacterLocation(
    @Json(name = "name") val name: String = "",
    @Json(name = "url") val url: String = ""
)
