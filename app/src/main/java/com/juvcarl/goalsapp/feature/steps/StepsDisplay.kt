package com.juvcarl.goalsapp.feature.steps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.juvcarl.goalsapp.data.local.database.Step
import com.juvcarl.goalsapp.feature.goal.GoalViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun StepsDisplay(modifier: Modifier = Modifier, viewModel: StepsViewModel = hiltViewModel()) {

    val stepsState = viewModel.uiState.collectAsStateWithLifecycle()
    StepsScreen(modifier,stepsState,viewModel::saveFrequency)

}

@Composable
fun StepsScreen(modifier: Modifier, stepsState: State<StepsUIState>, saveStep: () -> Unit) {

}
