package com.juvcarl.goalsapp.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.juvcarl.goalsapp.R

@Composable
fun GoalIcon(icon: Icon, modifier: Modifier = Modifier, tint : Color = MaterialTheme.colorScheme.primary){
    when (icon) {
        is Icon.ImageVectorIcon -> Icon(
            imageVector = icon.imageVector,
            contentDescription = stringResource(id = icon.description),
            modifier = modifier,
            tint = tint
        )
        is Icon.DrawableResourceIcon -> Icon(
            painter = painterResource(id = icon.id),
            contentDescription = stringResource(id = icon.description),
            modifier = modifier,
            tint = tint
        )
        else -> {}
    }
}

object Icons{
    val Add = Icon.DrawableResourceIcon(R.drawable.ic_icon_add, R.string.add)
    val Close = Icon.DrawableResourceIcon(R.drawable.ic_icon_close, R.string.close)
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector, val description: Int) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int, val description: Int) : Icon()
}
