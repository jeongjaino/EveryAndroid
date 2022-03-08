package com.example.dictionary.dictionary.feature_dictionary.data.local.entity
import androidx.room.Entity
import com.example.dictionary.dictionary.feature_dictionary.domain.model.Meaning
import com.example.dictionary.dictionary.feature_dictionary.domain.model.WordInfo

@Entity
data class WordInfoEntity(
    val word: String,
    val phonetic: String,
    val license: String,
    val meaning: List<Meaning>
){
    fun toWordInfo(): WordInfo {
        return WordInfo(
            meanings = meaning,
            word= word,
            phonetic = phonetic,
            license = license
            )
    }
}