package com.juvcarl.goalsapp.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    onClick : () -> Unit,
    content : @Composable RowScope.() -> Unit){
    Button(
        modifier = modifier,
        onClick = { onClick() },
        shape = ShapeDefaults.Small, content = content
    )
}
