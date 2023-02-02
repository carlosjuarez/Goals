/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.juvcarl.goalsapp.data

import com.juvcarl.goalsapp.data.local.database.Frequency
import com.juvcarl.goalsapp.data.local.database.FrequencyDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.juvcarl.goalsapp.data.local.database.Goal
import com.juvcarl.goalsapp.data.local.database.GoalDao
import javax.inject.Inject

interface FrequencyRepository {
    val frequencies: Flow<List<Frequency>>

    suspend fun addFrequency(frequency: Frequency)
    suspend fun addFrequencies(frequencies: List<Frequency>)
}

class FrequencyRepositoryImpl @Inject constructor(
    private val frequencyDao: FrequencyDao
) : FrequencyRepository {

    override val frequencies = frequencyDao.getFrequencies()

    override suspend fun addFrequency(frequency: Frequency){
        frequencyDao.insertFrequency(frequency)
    }

    override suspend fun addFrequencies(frequencies: List<Frequency>){
        frequencyDao.insertFrequencies(frequencies)
    }
}
