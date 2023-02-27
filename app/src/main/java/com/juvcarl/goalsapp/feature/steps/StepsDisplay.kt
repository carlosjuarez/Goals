package com.juvcarl.goalsapp.feature.steps

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.juvcarl.goalsapp.ui.theme.ApplicationTheme

@Composable
fun StepsDisplay(modifier: Modifier = Modifier, viewModel: StepsViewModel = hiltViewModel()) {

    val stepsState by viewModel.uiState.collectAsStateWithLifecycle()

    StepsScreen(modifier,stepsState,viewModel::saveFrequency)

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepsScreen(modifier: Modifier, state: StepsUIState, saveStep: () -> Unit) {

    var showAddStep by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        floatingActionButton = { StepFAB(onClick = { showAddStep = true }) }
    ) {
        when(state){
            is StepsUIState.Success -> {
                StepsList(state)
            }
            is StepsUIState.Loading -> { CircularProgressIndicator() }
            is StepsUIState.Error -> { CircularProgressIndicator() }
        }

        if(showAddStep){
            AddStepScreen(onSave = saveStep, { showAddStep = false})
        }
    }

}

@Composable
fun StepsList(state: StepsUIState.Success) {

}

@Composable
fun AddStepScreen(onSave: () -> Unit, onDismiss: () -> Unit) {


}

@Composable
fun StepFAB(onClick: () -> Unit) {

}

@Preview(showBackground = true)
@Composable
fun StepsScreenPreview(){
    ApplicationTheme {
        val state = StepsUIState.Success(listOf())
        StepsList(state = state)
    }
}

@Preview(showBackground = true)
@Composable
fun AddStepScreenPreview(){
    ApplicationTheme {
        val state = StepsUIState.Success(listOf())
        StepsList(state = state)
    }
}