package com.example.calculadoraimc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "inicio"
            ) {

                composable("inicio") {
                    PantallaInicio(navController)
                }

                composable(
                    route = "resultado/{nombre}/{imc}",
                    arguments = listOf(
                        navArgument("nombre") {
                            type = NavType.StringType
                        },
                        navArgument("imc") {
                            type = NavType.FloatType
                        }
                    )
                ) { backStackEntry ->

                    val nombre =
                        backStackEntry.arguments?.getString("nombre") ?: ""

                    val imc =
                        backStackEntry.arguments?.getFloat("imc") ?: 0f

                    PantallaResultado(
                        nombre = nombre,
                        imc = imc,
                        navController = navController
                    )
                }
            }
        }
    }
@Composable
fun PantallaInicio(navController: NavHostController
) {

    var nombre by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }

    var error by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Calculadora IMC",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = peso,
            onValueChange = { peso = it },
            label = { Text("Peso (kg)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = altura,
            onValueChange = { altura = it },
            label = { Text("Altura (m)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            )
        )

        Spacer(modifier = Modifier.height(20.dp))
        if (error) {
            Text(
                text = "Por favor, ingresa valores válidos",
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
        Button(
            onClick = {

                val pesoDouble = peso.toDoubleOrNull()
                val alturaDouble = altura.toDoubleOrNull()

                if (
                    pesoDouble == null ||
                    alturaDouble == null ||
                    pesoDouble <= 0 ||
                    alturaDouble <= 0
                ) {
                    error = true
                } else {
                    error = false

                    val imc =
                        pesoDouble / (alturaDouble * alturaDouble)

                    navController.navigate(
                        "resultado/$nombre/${imc.toFloat()}")
                }
            }
        ) {
            Text("Calcular")
        }
    }
}
        @Composable
        fun PantallaResultado(
            nombre: String,
            imc: Float,
            navController: NavHostController
        ) {

            val categoria: String
            val colorCategoria: Color

            when {

                imc < 18.5 -> {
                    categoria = "Bajo peso"
                    colorCategoria = Color.Red
                }

                imc < 25 -> {
                    categoria = "Peso normal"
                    colorCategoria = Color.Green
                }

                imc < 30 -> {
                    categoria = "Sobrepeso"
                    colorCategoria = Color(0xFFFF9800)
                }

                else -> {
                    categoria = "Obesidad"
                    colorCategoria = Color.Red
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Hola $nombre, tu resultado es:"
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = String.format("IMC: %.1f", imc),
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = categoria,
                    color = colorCategoria,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Text("Volver")
                }
            }
        }
}