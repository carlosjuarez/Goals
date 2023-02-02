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

import kotlinx.coroutines.flow.Flow
import com.juvcarl.goalsapp.data.local.database.Goal
import com.juvcarl.goalsapp.data.local.database.GoalDao
import javax.inject.Inject

interface GoalRepository {

    val goals: Flow<List<Goal>>

    suspend fun upsert(goal: Goal)
    suspend fun delete(goal: Goal)
}

class GoalRepositoryImpl @Inject constructor(
    private val goalDao: GoalDao
) : GoalRepository {

    override val goals: Flow<List<Goal>> = goalDao.getGoals()
    override suspend fun upsert(goal: Goal) {
        goalDao.upsertGoal(goal)
    }

    override suspend fun delete(goal: Goal){
        goalDao.deleteGoal(goal)
    }
}
