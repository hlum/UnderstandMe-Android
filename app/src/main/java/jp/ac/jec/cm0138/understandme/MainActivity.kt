package jp.ac.jec.cm0138.understandme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import jp.ac.jec.cm0138.understandme.Navigation.AppNavigation
import jp.ac.jec.cm0138.understandme.customTheme.MyAppTheme
import jp.ac.jec.cm0138.understandme.ui.theme.UnderstandMeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(innerPadding)
                }
            }
        }
    }
}