package jp.ac.jec.cm0138.understandme.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ac.jec.cm0138.understandme.R
import jp.ac.jec.cm0138.understandme.customTheme.MyAppTheme
import jp.ac.jec.cm0138.understandme.customTheme.customPrimaryButtonColors

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Understand Me",
            modifier = Modifier,
            style = TextStyle(
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            ),
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Google SignIn Button
        Button(
            onClick = {
            },
            colors = ButtonDefaults.customPrimaryButtonColors(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = "Google Sign in",
                    modifier = Modifier
                        .width(20.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Googleでサインイン",
                    color = MyAppTheme.colors.background,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Scaffold { innerpadding ->
        LoginScreen(modifier = Modifier.padding(innerpadding))
    }
}