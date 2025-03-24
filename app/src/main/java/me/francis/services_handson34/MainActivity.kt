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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import me.francis.services_handson34.ui.theme.Services_HandsOn34Theme
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    // Variável para armazenar a referência ao serviço de aplicativos instalados
    private var installedAppsService: IInstalledAppsService? = null
    // Flag para controlar o estado da conexão com o serviço
    private var isServiceConnected = false

    // Objeto de conexão com o serviço que implementa ServiceConnection
    private val serviceConnection = object : ServiceConnection {
        // Chamado quando a conexão com o serviço é estabelecida
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            installedAppsService = IInstalledAppsService.Stub.asInterface(service)
            isServiceConnected = true
            Toast.makeText(applicationContext, "Service connected", Toast.LENGTH_LONG).show()
        }

        // Chamado quando a conexão com o serviço é perdida
        override fun onServiceDisconnected(name: ComponentName?) {
            installedAppsService = null
            isServiceConnected = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Creating Activity")
        enableEdgeToEdge() // Ativa o modo edge-to-edge (borda a borda)
        setContent {
            Services_HandsOn34Theme {
                MainContent(applicationContext, ::getInstalledApps)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        println("Starting Activity")
        // Prepara a intent para vincular ao serviço
        val intent = Intent(this, InstalledAppsServiceImpl::class.java)
        // Vincula ao serviço com a flag BIND_AUTO_CREATE
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
        // Desvincula o serviço se estiver conectado
        if (isServiceConnected) {
            unbindService(serviceConnection)
            isServiceConnected = false
        }
    }

    override fun onDestroy() {
        println("Destroying Activity")
        super.onDestroy()
    }

    // Função para obter a lista de aplicativos instalados
    private fun getInstalledApps(): List<String> {
        return if (isServiceConnected) {
            installedAppsService?.installedApps ?: emptyList()
        } else {
            Toast.makeText(applicationContext, "Service not connected", Toast.LENGTH_LONG).show()
            emptyList()
        }
    }

}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun MainContent(
    context: Context,
    getInstalledApps: () -> List<String>, // Callback para obter apps instalados
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        // Estado para armazenar a lista de aplicativos
        val appList = remember { mutableStateListOf<String>() }

        // Box permite sobrepor componentes
        Box {
            // Coluna organiza os componentes verticalmente
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Lista de aplicativos
                AppList(
                    appNames = appList,
                    modifier = Modifier
                        .padding(innerPadding)
                        .weight(1f)  // Ocupa todo o espaço disponível
                )

                // Botão para buscar aplicativos
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        // Adiciona cada app retornado à lista
                        getInstalledApps().forEach { appList.add(it) }
                    }
                ) {
                    Text(text = "Buscar Apps Instalados")
                }
            }

            RandomButtonPosition(context)
        }
    }
}

@Composable
private fun AppList(
    appNames: List<String>,
    modifier: Modifier = Modifier
) {
    // LazyColumn renderiza apenas os itens visíveis
    LazyColumn(
        modifier = modifier.testTag("app_list"),  // Tag para testes
        verticalArrangement = Arrangement.Center
    ) {
        if (appNames.isEmpty()) {
            item {
                Text(text = "Nenhum aplicativo listado")  // Mensagem para lista vazia
            }
        } else {
            items(appNames) { appName ->
                Text(
                    modifier = Modifier.testTag("app_list_item"),  // Tag para testes
                    text = appName, // Nome do aplicativo
                )
                HorizontalDivider(thickness = 1.dp, color = Color.Gray) // Divisor
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
