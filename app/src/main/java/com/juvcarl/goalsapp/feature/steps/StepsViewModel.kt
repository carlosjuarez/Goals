package com.juvcarl.goalsapp.feature.steps

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juvcarl.goalsapp.data.FrequencyRepository
import com.juvcarl.goalsapp.data.StepRepository
import com.juvcarl.goalsapp.data.local.database.Step
import com.juvcarl.goalsapp.feature.steps.StepsUIState.Success
import com.juvcarl.goalsapp.feature.steps.StepsUIState.Loading
import com.juvcarl.goalsapp.feature.steps.StepsUIState.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class StepsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    stepsRepository: StepRepository,
    frequencyRepository: FrequencyRepository
) : ViewModel() {

    private val goalId: Int = checkNotNull(savedStateHandle["goalId"])

    val uiState : StateFlow<StepsUIState> = stepsRepository
        .getStepsFromGoalStream(goalId)
        .map(::Success)
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),Loading)

    val frequencies = frequencyRepository.frequencies

    fun saveFrequency(){

    }

}

sealed interface StepsUIState {
    object Loading : StepsUIState
    data class Error(val throwable: Throwable) : StepsUIState
    data class Success(val data: List<Step>) : StepsUIState
}

