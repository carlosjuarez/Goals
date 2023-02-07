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

package com.juvcarl.goalsapp.feature.goal

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.juvcarl.goalsapp.ui.theme.ApplicationTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.juvcarl.goalsapp.R
import com.juvcarl.goalsapp.data.local.database.Goal
import com.juvcarl.goalsapp.data.local.repository.fakes.fakeGoals
import com.juvcarl.goalsapp.ui.components.GoalIcon
import com.juvcarl.goalsapp.ui.components.Icons
import com.juvcarl.goalsapp.ui.components.PageTitle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun GoalDisplay(modifier: Modifier = Modifier, viewModel: GoalViewModel = hiltViewModel(), navigateToSteps : (Int) -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    GoalScreen(modifier = modifier, state, viewModel::addGoal)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalScreen(modifier: Modifier = Modifier, state: GoalUiState, addGoal: (Goal) -> Unit, ){
    var showAddGoalDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        floatingActionButton = { GoalFAB(onClick = { showAddGoalDialog = true }) }
    ) {
        when(state){
            is GoalUiState.Success ->{
                GoalList(
                    goals = state.data,
                    modifier = modifier
                )
            }
            is GoalUiState.Error -> CircularProgressIndicator()
            GoalUiState.Loading -> CircularProgressIndicator()
        }
        if(showAddGoalDialog){
            AddGoalScreen(onSave = addGoal, { showAddGoalDialog = false})
        }
    }
}



@Composable
fun GoalFAB(onClick: () -> Unit) {
  FloatingActionButton(onClick = { onClick() } ) {
    GoalIcon(icon = Icons.Add)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GoalList(
    goals: List<Goal>,
    modifier: Modifier = Modifier
) {
    val lazyState = rememberLazyListState()
    LazyColumn(modifier.fillMaxWidth(), state = lazyState ,) {
        item{
            PageTitle(id = R.string.goal_title)
        }
        items(goals){goal ->

            GoalCard(goal = goal)
        }
    }
}

@Composable
fun GoalCard(goal: Goal){

    val colorStops = arrayOf(
        goal.progress / 100 to MaterialTheme.colorScheme.primaryContainer,
        goal.progress % 100 to MaterialTheme.colorScheme.background
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Brush.horizontalGradient(colorStops = colorStops))
            .padding(vertical = 20.dp).clickable { },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = goal.name, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(2f))
        Text(text = "${goal.progress / 100} %",textAlign = TextAlign.Right, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalScreen(onSave: (Goal) -> Unit, onDismiss: () -> Unit){
    var goalName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            val newGoal = Goal(name = goalName, progress = 0f)
               TextButton( onClick = {
                   onSave(newGoal)
                   onDismiss()
               } ) {
                   Text(text = stringResource(id = R.string.save))
               }
        },dismissButton = {
            TextButton( onClick = { onDismiss() } ) { Text(text = stringResource(id = R.string.cancel)) }
        },title = {
            PageTitle(id = R.string.add_goal)
        }, text = {
            Column {
                Text(text = stringResource(id = R.string.goal_name), style = MaterialTheme.typography.labelLarge)
                OutlinedTextField(value = goalName, onValueChange = { goalName = it }, textStyle = MaterialTheme.typography.bodyMedium)
                Text(text = stringResource(id = R.string.title_instructions), style = MaterialTheme.typography.labelMedium)
            }
        },
        properties = DialogProperties()
    )
}


// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    ApplicationTheme {
        val state = GoalUiState.Success(fakeGoals)
        GoalScreen(state = state, addGoal = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun AddGoalBottomSheetPreview() {
    ApplicationTheme {
        AddGoalScreen(onSave = {}, onDismiss = {})
    }
}

