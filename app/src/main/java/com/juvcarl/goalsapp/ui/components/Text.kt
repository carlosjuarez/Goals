package com.juvcarl.goalsapp.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.juvcarl.goalsapp.R

@Composable
fun PageTitle(@StringRes id: Int){
    Text(text = stringResource(id = id), style = MaterialTheme.typography.titleLarge)
}