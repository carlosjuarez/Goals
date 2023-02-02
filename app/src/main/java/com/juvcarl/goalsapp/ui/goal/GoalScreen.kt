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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.repeatOnLifecycle
import com.juvcarl.goalsapp.ui.theme.MyApplicationTheme
import androidx.compose.runtime.*
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.juvcarl.goalsapp.data.local.database.Goal
import com.juvcarl.goalsapp.data.local.repository.fakes.fakeGoals

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun GoalScreen(modifier: Modifier = Modifier, viewModel: GoalViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when(state){
        is GoalUiState.Success ->{
            GoalScreen(
                items = (state as GoalUiState.Success).data,
                onSave = viewModel::addGoal,
                modifier = modifier
            )
        }
        is GoalUiState.Error -> CircularProgressIndicator()
        GoalUiState.Loading -> CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GoalScreen(
    items: List<Goal>,
    onSave: (name: Goal) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        var nameGoal by remember { mutableStateOf("Goal") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = nameGoal,
                onValueChange = { nameGoal = it }
            )

            Button(modifier = Modifier.width(96.dp), onClick = {
                val goal = Goal(name = nameGoal, progress = 0f)
                onSave(goal)
            }) {
                Text("Save")
            }
        }
        items.forEach {
            Text("Saved item: ${it.name} : ${it.progress}")
        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        GoalScreen(fakeGoals, onSave = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        GoalScreen(fakeGoals, onSave = {})
    }
}
