package com.juvcarl.goalsapp.data.local.repository.fakes

import com.juvcarl.goalsapp.data.GoalRepository
import com.juvcarl.goalsapp.data.local.database.Goal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeGoalRepository @Inject constructor() : GoalRepository {

    override val goals: Flow<List<Goal>> = flowOf(fakeGoals)

    override suspend fun upsert(goal: Goal) {
        if(fakeGoals.contains(goal)){
            fakeGoals.remove(goal)
        }
        fakeGoals.add(goal)
    }

    override suspend fun delete(goal: Goal) {
        fakeGoals.remove(goal)
    }
}

val fakeGoals = mutableListOf(
    Goal(name = "one", progress = 0f),
    Goal(name = "two", progress = 40f),
    Goal(name = "three", progress = 100f),
    Goal(name = "four", progress = 70f),
    Goal(name = "five", progress = 20f))
