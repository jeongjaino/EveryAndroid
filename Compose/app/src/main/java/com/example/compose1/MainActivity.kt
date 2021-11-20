package com.example.compose1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose1.ui.theme.Compose1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Compose1Theme {
                // A surface container using the 'background' color from the theme
                Inverter()
            }
        }
    }
}
@Composable
fun FirstScreen(onCountClicked: () -> Unit){
    Surface(color = MaterialTheme.colors.secondary){

        Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("next page")
            OutlinedButton(onClick = onCountClicked) {
                Text("Let's Go")
            }
        }
    }
}
@Composable
fun Inverter(){
    var showFirst by remember { mutableStateOf(true)}

    if(showFirst) {
        FirstScreen(onCountClicked = { showFirst = false })
    } else{
        showList()
    }
}
@Composable
fun Greetings(name: String){
    Card(
        //카드 형태로
    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)){
        Greeting(name) //카드에 들어갈 Compose
    }
}
@Composable
fun showList(names : List<String> = List(1000){ "$it"}){
    LazyColumn{ // List 선언
        items(items = names){
            name -> Greetings(name)
        }
    }
}
@Composable
fun Greeting(name: String) {

    var expanded by remember {mutableStateOf( false)}

    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier
            .padding(24.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )) {
            Image(
                painter = painterResource(id = R.drawable.cat),
                contentDescription = "android",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape),

            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("compose ", color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Jaino")
                if(expanded){
                    Text (
                        text = ("So What's the Trouble" +
                                "Yo I'm in Trouble " +
                                "우린 무에서 유" +
                                "유에서 Full 소유").repeat(4),
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(onClick = { expanded = !expanded },
               modifier  = Modifier.align(Alignment.CenterVertically)) {
                Text(name)
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Compose1Theme {
        showList()
    }
}