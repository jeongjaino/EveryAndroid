package com.example.dictionary.dictionary.feature_dictionary.domain.model

import com.example.dictionary.dictionary.feature_dictionary.data.remote.dto.License
import com.example.dictionary.dictionary.feature_dictionary.data.remote.dto.MeaningDto
import com.example.dictionary.dictionary.feature_dictionary.data.remote.dto.PhoneticDto

data class WordInfo(
    val license: License,
    val meanings: List<Meaning>,
    val phonetic: String,
    val word: String
)
