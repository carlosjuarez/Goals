package com.juvcarl.goalsapp.data.local.repository.fakes

import com.juvcarl.goalsapp.data.local.database.Frequency
import com.juvcarl.goalsapp.data.local.repository.GoalRepository
import com.juvcarl.goalsapp.data.local.database.Goal
import com.juvcarl.goalsapp.data.local.repository.FrequencyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeFrequencyRepository @Inject constructor() : FrequencyRepository {

    override suspend fun getFrequencies(): List<Frequency> {
        return fakeFrequencies
    }

    override suspend fun addFrequency(frequency: Frequency) {
        fakeFrequencies.add(frequency)
    }

    override suspend fun addFrequencies(frequencies: List<Frequency>) {
        fakeFrequencies.addAll(frequencies)
    }

}

val fakeFrequencies = mutableListOf(
    Frequency(1,"unique","unique value",1),
    Frequency(1,"unique","unique value",1),
    Frequency(1,"unique","unique value",1)
)
