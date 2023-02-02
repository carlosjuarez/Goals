package com.juvcarl.goalsapp.data.local.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity
data class Frequency(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String?,
    val category: Int
)

@Dao
interface FrequencyDao {

    @Query("SELECT * FROM frequency ORDER BY name DESC")
    fun getFrequencies(): Flow<List<Frequency>>

    @Insert
    fun insertFrequency(frequency: Frequency)

    @Insert
    fun insertFrequencies(frequencies: List<Frequency>)

}

