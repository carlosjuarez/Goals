package com.juvcarl.goalsapp.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.juvcarl.goalsapp.R

@Composable
fun PageTitle(modifier : Modifier = Modifier, @StringRes id: Int){
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(id = id), style = MaterialTheme.typography.titleLarge)
        Divider(thickness = 2.dp)
    }

}