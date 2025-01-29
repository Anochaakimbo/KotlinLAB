package com.example.lab8

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
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun InsertScreen(navController: NavHostController) {
    var textFieldID by remember { mutableStateOf("") }
    var textFieldName by remember { mutableStateOf("") }
    var textFieldAge by remember { mutableStateOf("") }
    val contextForToast = LocalContext.current.applicationContext
    var selectedGender by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = "Insert New Student",
            fontSize = 25.sp
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldID,
            onValueChange = { textFieldID = it },
            label = { Text(text = "Student ID") }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldName,
            onValueChange = { textFieldName = it },
            label = { Text(text = "Student name") }
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

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            value = textFieldAge,
            onValueChange = { textFieldAge = it },
            label = { Text(text = "Student age") }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier
                    .width(130.dp),
                onClick = {
                    val createClient = StudentAPI.create()
                    createClient.insertStd(textFieldID,textFieldName,selectedGender,textFieldAge)
                        .enqueue(object : Callback<Student> {
                            override fun onResponse(call: Call<Student>, response: Response<Student>){
                                if(response.isSuccessful){

                                    Toast.makeText(contextForToast,"Sucessfully insert",
                                        Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(contextForToast,"Failed insert",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onFailure(call: Call<Student>,t : Throwable){
                                Toast.makeText(contextForToast,"Failed insert" + t.message,
                                    Toast.LENGTH_SHORT).show()
                            }
                        })
                    navController.navigateUp()
                }
            ) {
                Text(text = "Save")
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                modifier = Modifier
                    .width(130.dp),
                onClick = {
                    textFieldID = ""
                    textFieldName = ""
                    textFieldAge = ""
                    selectedGender = ""
                    navController.navigate(Screen.Home.route)
                }
            ) {
                Text(text = "Cancel")
            }
        }
    }
}