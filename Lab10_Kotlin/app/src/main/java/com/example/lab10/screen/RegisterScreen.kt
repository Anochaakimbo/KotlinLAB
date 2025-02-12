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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lab10.api.StudentAPI
import com.example.lab10.data.LoginClass
import com.example.lab10.data.Screen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun RegisterScreen(navController: NavHostController) {
    val contextForToast = LocalContext.current // ใช้ context ใน Composable ได้เลย
    val createClient = StudentAPI.create()  // ใช้ remember เพื่อสร้าง client ครั้งเดียว
    var selectedGender by remember { mutableStateOf("") }
    var studentName by remember { mutableStateOf("") }
    var studentID by remember { mutableStateOf("") } // ใช้ by remember กับ mutableStateOf
    var password by remember { mutableStateOf("") } // ใช้ by remember กับ mutableStateOf
    var isButtonEnabled by remember { mutableStateOf(false) } // ใช้ by remember กับ mutableStateOf

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Register",
            fontSize = 25.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = studentID,
            onValueChange = {
                studentID = it
                isButtonEnabled = validateInput(studentID,password,studentName)
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

        OutlinedTextField(
            value = studentName,
            onValueChange = {
                studentName = it
                isButtonEnabled = validateInput(studentID,password,studentName)
            },
            label = {Text("Student Name")},
            keyboardOptions =   KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = "Gender")
        Row {
            RadioButton(
                selected = selectedGender == "Male",
                onClick = { selectedGender = "Male" }
            )
            Text("Male")
            Spacer(modifier = Modifier.width(8.dp))
            RadioButton(
                selected = selectedGender == "Female",
                onClick = { selectedGender = "Female" }
            )
            Text("Female")
            Spacer(modifier = Modifier.width(8.dp))
            RadioButton(
                selected = selectedGender == "Other",
                onClick = { selectedGender = "Other" }
            )
            Text("Other")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                isButtonEnabled = validateInput(studentID,password,studentName)
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
                keyboardController?.hide() // ซ่อนคีย์บอร์ด
                focusManager.clearFocus() // เคลียร์ focus

                createClient.registerStudent(
                    studentID,
                    studentName, // ค่า std_name ยังไม่ได้กำหนด
                    password,
                    selectedGender// กำหนด std_gender เป็น "Other"
                ).enqueue(object : Callback<LoginClass> {
                    override fun onResponse(
                        call: Call<LoginClass>,
                        response: Response<LoginClass>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(contextForToast, "Successfully Inserted", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.Login.route) // เปลี่ยนหน้าไปยัง LoginScreen
                        } else {
                            Toast.makeText(contextForToast, "Inserted Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call:Call<LoginClass>,t:Throwable){
                        Toast.makeText(contextForToast, "Error onFailure" + t.message , Toast.LENGTH_LONG).show()
                    }
                })
            },
            enabled = isButtonEnabled,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Register")
        }

    }
}

fun validateInput(studentID:String,password:String,studentName:String):Boolean{
    return studentID.isNotEmpty() && password.isNotEmpty()
}