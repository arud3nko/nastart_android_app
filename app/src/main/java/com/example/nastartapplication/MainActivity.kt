package com.example.nastartapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.nastartapplication.ui.theme.NastartApplicationTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.dataStore by preferencesDataStore("settings")


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authKey = booleanPreferencesKey("auth")

        val exampleProduct = Product(
            name="Тестовый товар",
            quantity = 1,
            price = 128)

        val exampleCustomer = Customer(
            name = "Егор",
            phone = "+7 (995) 080 20 45")

        val exampleInvoice = Invoice(
            id = 1,
            products = listOf( exampleProduct, exampleProduct, exampleProduct, exampleProduct),
            price = 356,
            createdTime = "31.12.23 в 19:21"
        )

        val exampleOrder = Order(
            customer = exampleCustomer,
            invoice = exampleInvoice)

        setContent {
            val authFlow: Flow<Boolean?> = dataStore.data.map { preferences ->
                preferences[authKey]
            }

            val coroutineScope = rememberCoroutineScope()

            val authState by authFlow.collectAsState(initial = false)

            NastartApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val isAuthorized = authState ?: false
                    Column {
                        Header()

                        if (!isAuthorized) {
                            LoginForm { token ->
                                checkAuthToken(token) { isValid ->
                                    if (isValid) {
                                        coroutineScope.launch {
                                            storeAuthKey(true)
                                        }
                                    }
                                }
                            }
                        } else {
                            ActiveCompletedSwitcher()
                            OrderView(exampleOrder)
                        }
                    }

                }
            }
        }
    }

    private suspend fun storeAuthKey(isAuth: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey("auth")] = isAuth
        }
    }

    private fun checkAuthToken(token: String, onResult: (Boolean) -> Unit) {
        onResult(token == "token")
    }

}

@Composable
fun MainView(order: Order) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var i = 10

        while (i >= 0) {
            i--
            BuildOrderButton(order)
        }

        
    }
}

@Composable
fun OrderView(order: Order) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(360.dp)
                .height(360.dp)
                .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))
                .border(2.dp, Color.Black.copy(alpha = 0.3f), RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp))

        ) {
            Text(
                text = order.invoice.createdTime,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 4.dp)
            )

            val nameColumn = .60f // 60%
            val quantityColumn = .15f // 15%
            val priceColumn = .25f // 25%
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 34.dp,
                        start = 12.dp,
                        end = 12.dp
                    ),
                userScrollEnabled = true) {
                item {
                    Row(
                        Modifier
                            .background(colorResource(id = R.color.secondary))) {
                        TableCell(text = "Наименование", weight = nameColumn)
                        TableCell(text = "x", weight = quantityColumn)
                        TableCell(text = "Сумма", weight = priceColumn)
                    }
                }

                for (product in order.invoice.products) {
                    item {
                        Row {
                            Row(Modifier.fillMaxWidth()) {
                                TableCell(text = product.name, weight = nameColumn)
                                TableCell(text = product.quantity.toString(), weight = quantityColumn)
                                TableCell(text = (product.price * product.quantity).toString(), weight = priceColumn)
                            }
                        }
                    }
                }

                item {
                    Row(
                        Modifier
                            .background(colorResource(id = R.color.secondary))) {
                        TableCell(text = "Итого", weight = 0.6f)
                        TableCell(text = order.invoice.price.toString() + " руб.", weight = 0.4f)
                    }
                    Row(Modifier.padding(bottom = 34.dp)) {}
                }
            }
        }
        Button(
            onClick = {},
            modifier = Modifier
                .width(360.dp)
                .height(80.dp)
                .shadow(
                    elevation = 4.dp,
                    spotColor = Color.Black,
                    shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.primary),
                contentColor = colorResource(id = R.color.main_black)
            ),
            shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp),
        ) {
            Text(
                text = "Отправить в доставку"
            )
        }
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black.copy(alpha = 0.1f))
            .weight(weight)
            .padding(4.dp)
    )
}

@Composable
fun BuildOrderButton(order: Order) {

    Button(
        onClick = {},
        modifier = Modifier
            .padding(
                top = 12.dp,
                bottom = 12.dp
            )
            .width(360.dp)
            .height(160.dp)
            .border(1.dp, Color.Black.copy(alpha = 0.1f), RoundedCornerShape(15.dp))
            .shadow(
                elevation = 8.dp,
                spotColor = Color.Black,
                shape = RoundedCornerShape(15.dp)
            ),
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = colorResource(id = R.color.main_black)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = order.invoice.createdTime,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            Row {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(top = 25.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.avataaars),
                            contentDescription = "Client avatar",
                            modifier = Modifier
                                .width(70.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = order.customer.name,
                            fontSize = 20.sp,
                        )
                    }
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    for (product in order.invoice.products) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = product.name + " x " + product.quantity)
                        }
                    }
                }
            }
            Text(
                text = "Итог: " + order.invoice.price + " руб.",
                modifier = Modifier
                    .align(Alignment.BottomCenter))

        }
    }
}

@Composable
fun Header() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.nastart_logo),
            contentDescription = "App logo",
            modifier = Modifier
                .width(340.dp)
                .padding(start = 50.dp, end = 50.dp, top = 10.dp),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Composable
fun ActiveCompletedSwitcher() {
    var activeWindowToggled by remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Button(
                onClick = {if (!activeWindowToggled) activeWindowToggled = true },
                modifier = Modifier.width(180.dp),
                shape = RoundedCornerShape(topStart = 15.dp, bottomStart = 15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (activeWindowToggled) colorResource(id = R.color.primary) else colorResource(id = R.color.secondary),
                    contentColor = colorResource(id = R.color.main_black)
                )
            ) {
                Text(
                    text = stringResource(id = R.string.active_window_label),
                    fontSize = 18.sp
                )
            }
            Button(
                onClick = {if (activeWindowToggled) activeWindowToggled = false},
                modifier = Modifier.width(180.dp),
                shape = RoundedCornerShape(topEnd = 15.dp, bottomEnd = 15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!activeWindowToggled) colorResource(id = R.color.primary) else colorResource(id = R.color.secondary),
                    contentColor = colorResource(id = R.color.main_black)
                )
            ) {
                Text(
                    text = stringResource(id = R.string.completed_window_label),
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun LoginForm(onLoginClick: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.welcome_message),
            modifier = Modifier.padding(bottom = 50.dp),
            fontSize = 30.sp,
        )

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = {
                Text(
                    text = stringResource(id = R.string.token_field_label)
                )
            }
        )

        FilledTonalButton(
            onClick = { onLoginClick(text) },
            modifier = Modifier.padding(top = 50.dp),
        ) {
            Text(
                text = stringResource(id = R.string.login_button_text),
                fontSize = 20.sp
            )
        }
    }
}
