package me.francis.services_handson34

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.francis.services_handson34.ui.theme.Services_HandsOn34Theme

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Creating Activity")
        enableEdgeToEdge()
        setContent {
            // *** Conteúdo da tela *** //
            Services_HandsOn34Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var appList = remember { mutableStateListOf<String>() }

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
