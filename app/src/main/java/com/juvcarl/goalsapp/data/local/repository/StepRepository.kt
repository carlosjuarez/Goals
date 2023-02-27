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

package com.juvcarl.goalsapp.data.local.repository

import com.juvcarl.goalsapp.data.local.database.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface StepRepository {
    fun getStepsFromGoalStream(goalId: Int): Flow<List<Step>>
    fun getStepsWithFrequencyStream(goalId: Int): Flow<List<StepWithFrequencies>>

    suspend fun upsertStep(step: Step)
    suspend fun deleteStep(step: Step)
    suspend fun upsertStepFrequency(frequencies: List<StepFrequency>)
}

class StepRepositoryImpl @Inject constructor(
    private val stepDao: StepDao
) : StepRepository {

    override fun getStepsFromGoalStream(goalId: Int) = stepDao.getStepsFromGoal(goalId)

    override fun getStepsWithFrequencyStream(goalId: Int) = stepDao.getStepsWithFrequencies(goalId)

    override suspend fun deleteStep(step: Step){
        stepDao.deleteStep(step)
    }

    override suspend fun upsertStep(step: Step){
        stepDao.upsertStep(step)
    }

    override suspend fun upsertStepFrequency(frequencies: List<StepFrequency>){
        stepDao.upsertStepFrequency(frequencies)
    }


}
