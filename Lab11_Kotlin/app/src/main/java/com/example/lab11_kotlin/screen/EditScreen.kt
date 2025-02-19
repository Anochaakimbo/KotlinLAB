package com.example.lab11_kotlin.screen

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.lab11_kotlin.database.DatabaseHelper
import com.example.lab11_kotlin.database.Student


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(navController: NavHostController) {
    var deleteDialog by remember { mutableStateOf(false) }
    val data = navController.previousBackStackEntry?.savedStateHandle?.get<Student>("data") ?:
    Student("","","",0)
    var textFieldID by remember { mutableStateOf(data.std_id) }
    var textFieldName by remember { mutableStateOf(data.std_name) }
    var textFieldAge by remember { mutableStateOf(data.std_age.toString()) }
    val contextForToast = LocalContext.current.applicationContext
    var selectedGender by remember { mutableStateOf(data.std_gender) }

    val dbHandler = DatabaseHelper.getInstance(contextForToast)
    dbHandler.writableDatabase
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
            enabled = false,
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
                modifier = Modifier.width(130.dp),
                onClick = {
                    deleteDialog = true
                }
            ) {
                Text(text = "Delete")
            }

            if (deleteDialog) {
                AlertDialog(
                    onDismissRequest = { deleteDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                deleteDialog = false
                                val result = dbHandler.deleteStudent(textFieldID)
                                if(result>-1){
                                    Toast.makeText(contextForToast,"Delete OK",
                                        Toast.LENGTH_SHORT).show()
                                }else{
                                    Toast.makeText(contextForToast,"Delete ERROR",
                                        Toast.LENGTH_SHORT).show()
                                }
                                navController.navigate(Screen.Home.route)
                            }
                        ) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { deleteDialog = false }) {
                            Text("Cancel")
                        }
                    },
                    title = { Text("Confirm Deletion") },
                    text = { Text("Are you sure you want to delete this ${data.std_name}?") }
                )
            }
            Button(
                modifier = Modifier
                    .width(130.dp),
                onClick = {
                    var result = dbHandler.updateStudent(Student(std_id = textFieldID,
                        std_name = textFieldName, std_gender = selectedGender, std_age = textFieldAge.toInt()))
                    if(result>-1){
                        Toast.makeText(contextForToast,"Update OK",
                            Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(contextForToast,"Update ERROR",
                            Toast.LENGTH_SHORT).show()
                    }
                    navController.navigateUp()
                }
            ) {
                Text(text = "Update")
            }



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