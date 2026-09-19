package com.example.data.repository

import com.example.data.api.RickAndMortyApiService
import com.example.data.model.CharacterResponse

class CharacterRepository(
    private val apiService: RickAndMortyApiService = RickAndMortyApiService.create()
) {
    suspend fun getCharacters(
        page: Int = 1,
        name: String? = null,
        status: String? = null
    ): Result<CharacterResponse> {
        return try {
            val cleanName = if (name.isNullOrBlank()) null else name.trim()
            val cleanStatus = if (status.isNullOrBlank() || status.equals("all", ignoreCase = true)) {
                null
            } else {
                status.lowercase().trim()
            }
            val response = apiService.getCharacters(page = page, name = cleanName, status = cleanStatus)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
