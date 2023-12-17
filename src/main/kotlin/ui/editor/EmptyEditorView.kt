package ui.editor

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.ThemeApp

@Composable
fun EmptyEditorView(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Image(
            painterResource("images/ic_app.svg"),
            contentDescription = "App icon",
            modifier = Modifier.size(300.dp)
        )

        Text(
            "DeepCode Studio",
            color = ThemeApp.colors.secondColor,
            fontFamily = ThemeApp.text.fontFamily,
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold
        )

    }
}