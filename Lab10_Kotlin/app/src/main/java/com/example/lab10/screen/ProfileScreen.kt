package com.example.lab10.screen

import android.inputmethodservice.Keyboard
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ModalNavigationDrawer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.lab10.api.StudentAPI
import com.example.lab10.data.ProfileClass
import com.example.lab10.data.Screen
import com.example.lab10.sp.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ProfileScreen(navController: NavHostController) {
    lateinit var sharedPreferences: SharedPreferencesManager
    val contextForToast = LocalContext.current.applicationContext
    sharedPreferences = SharedPreferencesManager(contextForToast)
    val userId = sharedPreferences.userId ?: "" // userId จะเป็นค่าว่างถ้าไม่มีใน SharedPreferences
    val createClient = StudentAPI.create()
    val initialStudent = ProfileClass(std_id = "", std_name = "", std_gender = "", role = "") // ข้อมูลเริ่มต้น
    var studentItems by remember { mutableStateOf(initialStudent) } // ใช้ by remember กับ mutableStateOf
    var logoutDialog by remember { mutableStateOf(false) }
    var checkedState by remember { mutableStateOf(false) }
    // Lifecycle State
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> { // ดึงข้อมูลเมื่อหน้าจอ Resume
                createClient.searchStudent(userId).enqueue(object : Callback<ProfileClass> {
                    override fun onResponse(call: Call<ProfileClass>, response: Response<ProfileClass>) {
                        if (response.isSuccessful) {
                            studentItems = ProfileClass( // กำหนดค่าให้ studentItems
                                response.body()!!.std_id,
                                response.body()!!.std_name,
                                response.body()!!.std_gender,
                                response.body()!!.role
                            )
                        } else {
                            Toast.makeText(contextForToast, "Student ID Not Found", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<ProfileClass>, t: Throwable) {
                        Toast.makeText(contextForToast, "Error onFailure + t.message", Toast.LENGTH_LONG).show()
                    }
                })
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Profile",
            fontSize = 25.sp
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            lineHeight = 30.sp,
            text = "Student ID: $userId\n\nName: ${studentItems.std_name}\n\n"
                +"Gender : ${studentItems.std_gender}\n\nRole: ${studentItems.role}",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        val isInvisible = studentItems.role == "admin"
        Box(
            content={
                if(isInvisible){
                    Button(
                        modifier = Modifier.fillMaxWidth()
                            .height(50.dp),
                        onClick = {navController.navigate(Screen.AllStudent.route)}
                    ) {
                        Text("Show all Student")
                    }
                }
            })

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                logoutDialog = true
            },
            modifier = Modifier.fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Logout")
        }
    }
    if(logoutDialog){
        AlertDialog(
            onDismissRequest = {logoutDialog = false},
            confirmButton = {
                TextButton(
                    onClick = {
                        logoutDialog = false
                        if(checkedState) {
                            sharedPreferences.clearUserLogin()
                            Toast.makeText(contextForToast,"Clear User Login",
                                Toast.LENGTH_SHORT).show()
                        }else{
                            sharedPreferences.clearUserAll()
                            Toast.makeText(contextForToast,"Clear User Login and Email",
                                Toast.LENGTH_SHORT).show()
                        }
                        navController.navigate(Screen.Login.route)
                        }
                ) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        logoutDialog = false
                        Toast.makeText(contextForToast,"Click on No",
                            Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text(text ="No")
                }
            },
            title = {Text(text = "Logout")},
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Text(text = "Are you sure you want to logout?")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Checkbox(
                            checked = checkedState,
                            onCheckedChange = { isChecked ->
                                checkedState = isChecked
                            }
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text("Remember my email")
                    }
                }
            }
        )
    }
}




