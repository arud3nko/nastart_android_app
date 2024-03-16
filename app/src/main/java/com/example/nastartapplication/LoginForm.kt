//package com.example.nastartapplication
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.FilledTonalButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.nastartapplication.ui.theme.NastartApplicationTheme
//
//class LoginForm : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            NastartApplicationTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Login()
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Login() {
//    /*
//    * Welcome composition, here user have to enter the authentication token
//    *
//    */
//
//    var text by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally) {
//        Image(
//            painter = painterResource(id = R.drawable.nastart_logo),
//            contentDescription = "App logo",
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(50.dp),
//            contentScale = ContentScale.FillWidth)
//    }
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally) {
//
//        Text(
//            text = stringResource(id = R.string.welcome_message),
//            modifier = Modifier.padding(bottom = 50.dp),
//            fontSize = 30.sp,
//        )
//
//
//        OutlinedTextField(
//            value = text,
//            onValueChange = {text = it},
//            label = {
//                Text(
//                    text = stringResource(id = R.string.token_field_label)
//                )
//            })
//
//        FilledTonalButton(
//            onClick = { /*TODO*/ },
//            modifier = Modifier.padding(top = 50.dp),
//        ) {
//            Text(
//                text = stringResource(id = R.string.login_button_text),
//                fontSize = 20.sp
//            )
//        }
//
//    }
//}
