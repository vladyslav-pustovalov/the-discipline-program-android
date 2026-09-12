package my.vladpustovalov.tdp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import my.vladpustovalov.tdp.ui.navigation.AppNavGraph
import my.vladpustovalov.tdp.ui.theme.TdpTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TdpTheme {
                AppNavGraph()
            }
        }
    }
}
