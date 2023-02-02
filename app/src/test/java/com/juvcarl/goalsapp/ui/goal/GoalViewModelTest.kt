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

package com.juvcarl.goalsapp.ui.goal


import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import com.juvcarl.goalsapp.data.GoalRepository
import com.juvcarl.goalsapp.data.local.database.Goal

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class GoalViewModelTest {
    @Test
    fun uiState_initiallyLoading() = runTest {
        val viewModel = GoalViewModel(FakeGoalRepository())
        assertEquals(viewModel.uiState.first(), GoalUiState.Loading)
    }

    @Test
    fun uiState_onItemSaved_isDisplayed() = runTest {
        val viewModel = GoalViewModel(FakeGoalRepository())
        assertEquals(viewModel.uiState.first(), GoalUiState.Loading)
    }
}

private class FakeGoalRepository : GoalRepository {

    private val data = mutableListOf<Goal>()

    override val goals: Flow<List<Goal>>
        get() = flow { emit(data.toList()) }

    override suspend fun upsert(goal: Goal) {
        if(data.contains(goal)){
            data.remove(goal)
        }
        data.add(goal)
    }

    override suspend fun delete(goal: Goal) {
        data.remove(goal)
    }

}
