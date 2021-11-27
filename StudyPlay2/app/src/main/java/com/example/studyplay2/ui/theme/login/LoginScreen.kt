package com.example.studyplay2.ui.theme.login

import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.Paint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyplay2.R
import com.example.studyplay2.ui.theme.CustomFont
import com.example.studyplay2.ui.theme.StudyPlay2Theme
import com.example.studyplay2.ui.theme.register.RegisterScreen

@Composable
fun LoginScreen(onLoginButtonClicked: () -> Unit){

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,)
    {
        Spacer(modifier = Modifier.height(50.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.main_cloud),
            contentDescription = "login_main_cloud",
            modifier = Modifier.size(100.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Study Play", color = MaterialTheme.colors.primary,
            fontSize = 30.sp,
            fontFamily = CustomFont)
        }
        Spacer(Modifier.height(30.dp))
        var name by remember { mutableStateOf("")}
        var password by remember { mutableStateOf("")}
        TextField(
            value = name,
            onValueChange = {name = it},
            maxLines = 1,
            label = {Text("User Name")},
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.primaryVariant ,
                unfocusedLabelColor = MaterialTheme.colors.primary
            )
        )
        Spacer(Modifier.height(30.dp))
        TextField(
            value = password,
            onValueChange ={password = it},
            maxLines = 1,
            label = {Text("Password")},
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.primaryVariant,
                unfocusedLabelColor = MaterialTheme.colors.primary
            )
        )
        Spacer(Modifier.height(30.dp))
        OutlinedButton(onClick = onLoginButtonClicked,
            border = BorderStroke(1.dp, MaterialTheme.colors.primary)
        ){
            Text("로그인")
        }
        Spacer(Modifier.height(50.dp))
        Row(verticalAlignment = Alignment.CenterVertically){
            val signupText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colors.primary,
                fontFamily = CustomFont)){
                    append("회원가입")
                }
            }
            ClickableText(text = signupText, onClick = {})
            Spacer(Modifier.width(50.dp))
            Text("계정 찾기")
        }
    }
}
@Preview
@Composable
fun LoginScreenPreview(
){
    StudyPlay2Theme {
        LoginScreen({})
    }
}