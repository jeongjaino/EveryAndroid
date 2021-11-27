package com.example.studyplay2.ui.theme.register

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyplay2.R
import com.example.studyplay2.ui.theme.CustomFont
import com.example.studyplay2.ui.theme.StudyPlay2Theme
import com.example.studyplay2.ui.theme.login.LoginScreen

@Composable
fun RegisterScreen(onRegisterButtonClicked: ()-> Unit){
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically){
            Image(
                painter = painterResource(id = R.drawable.main_cloud),
                contentDescription = "register_main_cloud",
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(30.dp))
            Text(
                text = "Study Play",
                fontFamily = CustomFont,
                fontSize = 20.sp,
                color = MaterialTheme.colors.primary
            )
        }
        Spacer(modifier = Modifier.height(50.dp))
        var registerName by remember { mutableStateOf("")}
        TextField(
            value = registerName,
            onValueChange = {registerName = it},
            label = {Text("User Name")},
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.primaryVariant,
                unfocusedLabelColor = MaterialTheme.colors.primary
            )
        )
        Spacer(modifier = Modifier.height(30.dp))
        var registerPassword by remember { mutableStateOf("")}
        TextField(
            value = registerPassword,
            onValueChange = {registerPassword = it},
            label = {Text("Password")},
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.primaryVariant,
                unfocusedLabelColor = MaterialTheme.colors.primary
            )
        )
        Spacer(modifier = Modifier.height(30.dp))
        var confirmPassword by remember { mutableStateOf("")}
        TextField(
            value = confirmPassword,
            onValueChange = {confirmPassword = it},
            label = {Text("User Name")},
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.primaryVariant,
                unfocusedLabelColor = MaterialTheme.colors.primary
            )
        )
        Spacer(modifier = Modifier.height(30.dp))
        OutlinedButton(onClick = onRegisterButtonClicked,
            border = BorderStroke(1.dp, MaterialTheme.colors.primary)
        ){
            Text("회원 가입")
        }
    }
}
@Preview
@Composable
fun PreviewRegisterScreen(){
    StudyPlay2Theme {
        RegisterScreen({})
    }
}