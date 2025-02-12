package com.example.lab10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.lab10.screen.NavGraph
import com.example.lab10.ui.theme.Lab10Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab10Theme {
                MyScreen()
                }
            }
        }
    }


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Lab10Theme {
        Greeting("Android")
    }
}

@Composable
fun MyScreen() {
    val navController = rememberNavController() // สร้าง NavHostController
    Column(
        modifier = Modifier
            .fillMaxSize(), // กำหนดขนาดเต็มหน้าจอ
        horizontalAlignment = Alignment.CenterHorizontally // จัดเรียงองค์ประกอบแนวนอนตรงกลาง
    ) {
    }
    NavGraph(navController = navController) // เรียกใช้ NavGraph
}