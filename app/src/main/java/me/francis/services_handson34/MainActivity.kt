package me.francis.services_handson34

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.francis.services_handson34.ui.theme.Services_HandsOn34Theme
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    private var installedAppsService: IInstalledAppsService? = null
    private var isServiceConnected = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            installedAppsService = IInstalledAppsService.Stub.asInterface(service)
            isServiceConnected = true
            Toast.makeText(applicationContext, "Service connected", Toast.LENGTH_LONG).show()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            installedAppsService = null
            isServiceConnected = false
        }
    }

    // *** Ciclos de vida *** //
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Creating Activity")
        enableEdgeToEdge()
        setContent {
            // *** Conteúdo da tela *** //
            Services_HandsOn34Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var appList = remember { mutableStateListOf<String>() }

                    Box {
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            AppList(
                                appNames = appList,
                                modifier = Modifier
                                    .padding(innerPadding)
                                    .weight(1f)
                            )

                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    if (isServiceConnected) {
                                        getInstalledApps().forEach { appList.add(it) }
                                    } else {
                                        Toast.makeText(
                                            applicationContext,
                                            "Service not connected",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            ) {
                                Text(text = "Buscar Apps Instalados")
                            }
                        }

                        RandomButtonPosition(applicationContext)
                    }
                }
            }
            // *** Fim do conteúdo da tela *** //
        }
    }

    override fun onStart() {
        super.onStart()
        println("Starting Activity")
        val intent = Intent(this, InstalledAppsServiceImpl::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }

    override fun onResume() {
        super.onResume()
        println("Resuming Activity")
    }

    override fun onPause() {
        super.onPause()
        println("Pausing Activity")
    }

    override fun onStop() {
        super.onStop()
        println("Stopping Activity")
        if (isServiceConnected) {
            unbindService(serviceConnection)
            isServiceConnected = false
        }
    }

    override fun onDestroy() {
        println("Destroying Activity")
        super.onDestroy()
    }
    // *** Fim dos ciclos de vida *** //

    private fun getInstalledApps(): List<String> {
        return if (isServiceConnected) {
            installedAppsService?.installedApps ?: emptyList()
        } else {
            Toast.makeText(applicationContext, "Service not connected", Toast.LENGTH_LONG).show()
            emptyList()
        }
    }
}

@Composable
private fun AppList(
    appNames: List<String>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        if (appNames.isEmpty()) {
            item {
                Text(text = "Nenhum aplicativo listado")
            }
        } else {
            items(appNames) { appName ->
                Text(text = appName)
                HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun RandomButtonPosition(context: Context) {
    // Obter o WindowManager para acessar as dimensões da tela
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayMetrics = windowManager.currentWindowMetrics.bounds

    // Obter largura e altura da tela
    val screenWidth = displayMetrics.width()
    val screenHeight = displayMetrics.height()

    // Estado para armazenar a posição aleatória do botão
    var position by remember { mutableStateOf(Pair(0f, 0f)) }

    // Função para gerar uma nova posição aleatória
    fun generateRandomPosition() {
        // Definir uma margem para garantir que o botão não saia da tela
        val buttonWidth = 300f // Estima o tamanho do botão, em pixels
        val buttonHeight = 80f // Estima a altura do botão, em pixels

        // Gerar uma posição X e Y aleatórias dentro da tela, respeitando as margens
        val randomX =
            (screenWidth - buttonWidth) * Random.nextFloat() // Posição X aleatória dentro da tela
        val randomY =
            (screenHeight - buttonHeight) * Random.nextFloat() // Posição Y aleatória dentro da tela

        position = Pair(randomX, randomY)
    }

    // Botão que muda de posição a cada clique
    Button(
        onClick = {
            generateRandomPosition() // Gera uma nova posição a cada clique
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
        modifier = Modifier
            .offset(
                x = (position.first / context.resources.displayMetrics.density).dp, // Converte de px para dp
                y = (position.second / context.resources.displayMetrics.density).dp
            ) // Converte de px para dp
            .padding(8.dp) // Dá um espaço para o botão não ficar colado nas bordas
    ) {
        Text("Mudar de lugar")
    }
}
