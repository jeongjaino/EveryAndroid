package com.example.dictionary.dictionary.feature_dictionary.data.remote

import com.example.dictionary.dictionary.feature_dictionary.data.remote.dto.WordInfoDto
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {

    @GET("/api/v2/entries/en//{word}")
    suspend fun getWordInfo(
        @Path("Word") word: String
    ): List<WordInfoDto>
}