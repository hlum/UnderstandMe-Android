package jp.ac.jec.cm0138.understandme.Presentation.Screens.Components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ClassItemView(
    classID: String,
    className: String,
    teacherName: String,
    showIcon: Boolean = true,
    height: Dp =90.dp,
    modifier: Modifier = Modifier
) {
    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        Color(0xFF6C5CE7),
        Color.Blue,
        Color(0xFFA52A2A),
        Color(0xFFFF9800),
        Color.Cyan,
        Color.Gray,
        Color.Green,
        Color(0xFF3F51B5),
        Color(0xFF458645)
    )

    val backgroundColor = remember { colors.random() }
    val firstChar = className.firstOrNull()?.uppercase() ?: ""

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {

        if (showIcon) {
            // Icon Box
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = firstChar,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.width(20.dp))

        // Text Content
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = className,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = teacherName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ClassCellPreview() {
    Scaffold { innerPadding ->
        ClassItemView(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
            ,
            classID = "",
            className = "セキュアーコーディング",
            teacherName = "先生名",
            showIcon = true
        )
    }
}