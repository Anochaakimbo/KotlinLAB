package com.example.lab10.screen

import android.content.Context
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.lab10.api.StudentAPI
import com.example.lab10.data.ProfileClass
import com.example.lab10.data.Screen
import com.example.lab10.sp.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun AllStudentScreen(navController: NavHostController) {
    val contextForToast = LocalContext.current.applicationContext
    lateinit var sharedPreferences: SharedPreferencesManager
    sharedPreferences = SharedPreferencesManager(contextForToast)

    var studentList = remember { mutableStateListOf<ProfileClass>() }
    val userRole = sharedPreferences.userRole ?: ""

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()




    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.DESTROYED -> {}
            Lifecycle.State.INITIALIZED -> {}
            Lifecycle.State.CREATED -> {}
            Lifecycle.State.STARTED -> {}
            Lifecycle.State.RESUMED -> {
                if (userRole == "admin") {
                    showallData(studentList, contextForToast)
                } else {
                    Toast.makeText(
                        contextForToast,
                        "You are not Admin. (Role: $userRole)",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    Column {
        Spacer(modifier = Modifier.height(height = 20.dp))
        Row(
            Modifier.fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Column(modifier = Modifier.weight(0.85f))
            {
                Text(text = "Student List:", fontSize = 25.sp)
            }
            Button(onClick = {
                navController.navigate(Screen.Profile.route)
            }) {
                Text("Back to Profile")
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            itemsIndexed(
                items = studentList,
            ) { index, item ->
                Card(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(130.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                    onClick = {
                        Toast.makeText(
                            contextForToast, "Click on ${item.std_name}.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                ) {
                    Row(
                        Modifier.fillMaxWidth().height(Dp(130f)).padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ID : ${item.std_id}\n" +
                                    "Name : ${item.std_name}\n" +
                                    "Gender : ${item.std_gender}\n" +
                                    "Role : ${item.role} ",
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}


fun showallData(studentItemsList:MutableList<ProfileClass>,context : Context){
    val createClient = StudentAPI.create()
    createClient.retrieveStudent()
        .enqueue(object : Callback<List<ProfileClass>> {
            override fun onResponse(call : Call<List<ProfileClass>>,
                                    response: Response<List<ProfileClass>>
            ){
                studentItemsList.clear()
                response.body()?.forEach{
                    studentItemsList.add(ProfileClass(it.std_id,it.std_name,it.std_gender,it.role))
                }
            }
            override fun onFailure(call : Call<List<ProfileClass>>, t : Throwable){
                Toast.makeText(context,"Error onFailure" + t.message,
                    Toast.LENGTH_LONG).show()
            }
        })
}
