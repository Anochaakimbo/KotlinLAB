package com.example.lab10.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.lab10.api.StudentAPI
import com.example.lab10.data.LoginClass
import com.example.lab10.data.Screen
import com.example.lab10.sp.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginScreen(navController: NavHostController) {
    var studentID by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isButtonEnabled by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val createClient = StudentAPI.create()

    val contextForToast = LocalContext.current.applicationContext
    var studentItems by remember { mutableStateOf<LoginClass?>(null) } // เปลี่ยนเป็น nullable และใช้ by remember

    lateinit var sharedPreferences: SharedPreferencesManager
    sharedPreferences = SharedPreferencesManager(contextForToast)

    // Lifecycle State
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> { // ใช้ RESUMED state เท่านั้น
                if (sharedPreferences.isLoggedIn) {
                    navController.navigate(Screen.Profile.route)
                } else if (!sharedPreferences.userId.isNullOrEmpty()) {
                    studentID = sharedPreferences.userId!!
                }
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Log in", fontSize = 25.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = studentID,
            onValueChange = {
                studentID = it
                isButtonEnabled = studentID.isNotEmpty() && password.isNotEmpty()
            },
            label = {Text("Student ID")},
            keyboardOptions =   KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                isButtonEnabled = studentID.isNotEmpty() && password.isNotEmpty()
            },
            label = {Text("Password")},
            keyboardOptions =   KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                keyboardController?.hide()
                focusManager.clearFocus()

                createClient.loginStudent(studentID, password).enqueue(object :
                    Callback<LoginClass> {
                    override fun onResponse(call: Call<LoginClass>, response: Response<LoginClass>) {
                        response.body()?.let { loginResponse ->
                            if (loginResponse.success == 1) {
                                sharedPreferences.isLoggedIn = true
                                sharedPreferences.userId = loginResponse.std_id

                                Toast.makeText(contextForToast, "Login successful", Toast.LENGTH_LONG).show()
                                navController.navigate(Screen.Profile.route)
                            } else if (loginResponse.success == 0) {
                                Toast.makeText(contextForToast, "Student ID or password is incorrect.", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(contextForToast, "Student ID Not Found", Toast.LENGTH_LONG).show()
                            }
                        } ?: run {
                            Toast.makeText(contextForToast, "Login failed. Please try again.", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginClass>, t: Throwable) {
                        Toast.makeText(contextForToast, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            },
            enabled = isButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
        ) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            Modifier.fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                text = "Don't have an account?",
            )
            TextButton(
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    navController.navigate(Screen.Register.route)
                }
            ){
                Text("Register")
            }
        }
    }
}