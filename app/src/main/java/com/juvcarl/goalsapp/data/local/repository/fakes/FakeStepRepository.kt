package com.juvcarl.goalsapp.data.local.repository.fakes

import com.juvcarl.goalsapp.data.local.database.Step
import com.juvcarl.goalsapp.data.local.database.StepFrequency
import com.juvcarl.goalsapp.data.local.database.StepWithFrequencies
import com.juvcarl.goalsapp.data.local.repository.StepRepository
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import javax.inject.Inject

class FakeStepRepository @Inject constructor() : StepRepository {

    override fun getStepsFromGoalStream(goalId: Int) = flowOf(fakeSteps)

    override fun getStepsWithFrequencyStream(goalId: Int) = flowOf(fakeStepFrequencies)

    override suspend fun upsertStep(step: Step) {
        if(fakeSteps.contains(step)){
            fakeSteps.remove(step)
        }
        fakeSteps.add(step)
    }

    override suspend fun deleteStep(step: Step) {
        if(fakeSteps.contains(step)){
            fakeSteps.remove(step)
        }
    }

    override suspend fun upsertStepFrequency(frequencies: List<StepFrequency>) {
        frequencies.forEach { freq ->
            val stepWithFrequency = fakeStepFrequencies.firstOrNull { it.step == fakeSteps[fakeSteps.indexOfFirst { it.uid == freq.stepId }] }
            stepWithFrequency?.let {
                val newFrequencies = stepWithFrequency.frequencies.toMutableList()
                if(newFrequencies.contains(freq)){
                    newFrequencies.remove(freq)
                }
                newFrequencies.add(freq)
                val stepWithFrequencies = it.copy(frequencies = newFrequencies)
                fakeStepFrequencies.removeIf {
                    it.step == stepWithFrequencies.step
                }
                fakeStepFrequencies.add(stepWithFrequencies)
            }
        }
    }
}

val fakeSteps = mutableListOf(
    Step(1,"gather information",1),
    Step(2,"create account",1),
    Step(3,"save purpose",1),
)

val fakeStepFrequencies = mutableListOf(
    StepWithFrequencies(fakeSteps[0], mutableListOf(
        StepFrequency(1,1,1, LocalDate.now(),LocalTime.now(),Date(),Date(),2),
        StepFrequency(2,1,2, LocalDate.now(),LocalTime.now(),Date(),Date(),2),
        StepFrequency(3,1,3, LocalDate.now(),LocalTime.now(),Date(),Date(),2)
    )),
        StepWithFrequencies(fakeSteps[1], mutableListOf(
        StepFrequency(4,2,1, LocalDate.now(),LocalTime.now(),Date(),Date(),2),
        StepFrequency(5,3,2, LocalDate.now(),LocalTime.now(),Date(),Date(),2),
        StepFrequency(6,2,3, LocalDate.now(),LocalTime.now(),Date(),Date(),2)
    ))
)