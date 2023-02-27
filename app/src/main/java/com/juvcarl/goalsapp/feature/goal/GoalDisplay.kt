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
import android.widget.ImageButton
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.material3.DrawerDefaults.scrimColor
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.juvcarl.goalsapp.ui.theme.ApplicationTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.juvcarl.goalsapp.R
import com.juvcarl.goalsapp.data.local.database.Goal
import com.juvcarl.goalsapp.data.local.repository.fakes.fakeGoals
import com.juvcarl.goalsapp.ui.components.DefaultButton
import com.juvcarl.goalsapp.ui.components.GoalIcon
import com.juvcarl.goalsapp.ui.components.Icons
import com.juvcarl.goalsapp.ui.components.PageTitle
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GoalDisplay(modifier: Modifier = Modifier, viewModel: GoalViewModel = hiltViewModel(), navigateToSteps : (Int) -> Unit) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    GoalScreen(modifier = modifier, state, viewModel::addGoal,navigateToSteps)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalScreen(
    modifier: Modifier = Modifier,
    state: GoalUiState,
    addGoal: (Goal) -> Unit,
    navigateToSteps: (Int) -> Unit
){
    var showAddGoalDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
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
                        modifier = modifier,
                        navigateToSteps
                    )
                }
                is GoalUiState.Error -> CircularProgressIndicator()
                GoalUiState.Loading -> CircularProgressIndicator()
            }
        }
        if(showAddGoalDialog){
            AddGoalScreen(onSave = addGoal) {
                showAddGoalDialog = false
            }
        }
    }
}


@Composable
fun GoalFAB(onClick: () -> Unit) {
  FloatingActionButton(onClick = { onClick() } ) {
    GoalIcon(icon = Icons.Add)
  }
}

@Composable
internal fun GoalList(
    goals: List<Goal>,
    modifier: Modifier = Modifier,
    navigateToSteps: (Int) -> Unit
) {
    val lazyState = rememberLazyListState()
    LazyColumn(
        modifier.fillMaxWidth()
        , state = lazyState ) {
        item{
            PageTitle(id = R.string.goal_title)
        }
        items(goals){goal ->
            GoalCard(goal = goal,navigateToSteps)
        }
    }
}

@Composable
fun GoalCard(goal: Goal, navigateToSteps: (Int) -> Unit){

    val colorStops = arrayOf(
        goal.progress / 100 to MaterialTheme.colorScheme.primaryContainer,
        goal.progress % 100 to MaterialTheme.colorScheme.background
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Brush.horizontalGradient(colorStops = colorStops))
            .padding(vertical = 20.dp)
            .clickable { navigateToSteps(goal.uid) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = goal.name, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(2f))
        Text(text = "${goal.progress / 100} %",textAlign = TextAlign.Right, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun Scrim(
    color: Color,
    onDismissRequest: () -> Unit,
    visible: Boolean
) {
    if (color.isSpecified) {
        val alpha by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = TweenSpec()
        )
        val dismissSheet = if (visible) {
            Modifier
                .pointerInput(onDismissRequest) {
                    detectTapGestures {
                        onDismissRequest()
                    }
                }
                .semantics(mergeDescendants = true) {
                    contentDescription = "sheetDescription"
                    onClick { onDismissRequest(); true }
                }
        } else {
            Modifier
        }
        Canvas(
            Modifier
                .fillMaxSize()
                .then(dismissSheet)
        ) {
            drawRect(color = color, alpha = alpha)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddGoalScreen(modifier: Modifier = Modifier, onSave: (Goal) -> Unit, onDismiss: () -> Unit){

    var goalName by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val saveGoal = {
        onDismiss()
        onSave(Goal(name = goalName, progress = 0.0f))
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        Scrim(
            color = scrimColor,
            onDismissRequest = {
                onDismiss()
            },
            visible = true
        )
        Surface(
            modifier = Modifier.align(Alignment.BottomCenter)
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            )
            {
                PageTitle(id = R.string.add_goal)

                Row(modifier = Modifier.fillMaxWidth()){
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = stringResource(id = R.string.goal_name), style = MaterialTheme.typography.labelLarge)
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = goalName,
                            onValueChange = { goalName = it },
                            textStyle = MaterialTheme.typography.bodyMedium,
                            singleLine = true,
                            keyboardActions = KeyboardActions(onDone = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                                saveGoal()
                            })
                        )
                        Text(text = stringResource(id = R.string.title_instructions), style = MaterialTheme.typography.labelMedium)
                    }
                }

                DefaultButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = saveGoal
                ) {
                    Text(text = stringResource(id = R.string.save))
                }

            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalScreenModalBottomSheet(onSave: (Goal) -> Unit, onDismiss: () -> Unit){

    var goalName by remember { mutableStateOf("") }
    
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    var skipHalfExpanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberSheetState(skipHalfExpanded = skipHalfExpanded)

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        )
        {
            PageTitle(id = R.string.add_goal)

            Row(modifier = Modifier.fillMaxWidth()){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = stringResource(id = R.string.goal_name), style = MaterialTheme.typography.labelLarge)
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = goalName,
                        onValueChange = { goalName = it },
                        textStyle = MaterialTheme.typography.bodyMedium)
                    Text(text = stringResource(id = R.string.title_instructions), style = MaterialTheme.typography.labelMedium)
                }
            }

            DefaultButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { onSave(Goal(name = goalName, progress = 0.0f)) }
            ) {
                Text(text = stringResource(id = R.string.save))
            }

        }
    }
}


// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    ApplicationTheme {
        val state = GoalUiState.Success(fakeGoals)
        GoalScreen(state = state, addGoal = {}, navigateToSteps = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun AddGoalBottomSheetPreview() {
    ApplicationTheme {
        AddGoalScreen(onSave = {}, onDismiss = {})
    }
}

