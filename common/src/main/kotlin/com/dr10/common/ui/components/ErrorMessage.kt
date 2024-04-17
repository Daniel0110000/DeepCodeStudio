package com.dr10.common.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dr10.common.ui.ThemeApp

@Composable
fun ErrorMessage(
    errorTitle: String = "An error occurred",
    errorDescription: String,
    onCloseRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painterResource("images/ic_error.svg"),
                    contentDescription = "Error icon",
                    modifier = Modifier.size(30.dp)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    errorTitle,
                    fontFamily = ThemeApp.text.fontFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = ThemeApp.colors.textColor,
                )
            }
        },
        text = {
            Text(
                errorDescription,
                fontFamily = ThemeApp.text.fontFamily,
                fontSize = 12.sp,
                color = ThemeApp.colors.textColor,
            )
        },
        buttons = {
            Button(
                onClick = { onCloseRequest() },
                colors = ButtonDefaults.buttonColors(backgroundColor = ThemeApp.colors.buttonColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    "Ok",
                    fontFamily = ThemeApp.text.fontFamily,
                    fontSize = 13.sp,
                    color = ThemeApp.colors.textColor
                )
            }
        },
        backgroundColor = ThemeApp.colors.secondColor,
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.width(250.dp)
    )

}