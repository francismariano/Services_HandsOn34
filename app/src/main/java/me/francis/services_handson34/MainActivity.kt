
package me.francis.services_handson34
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.francis.services_handson34.ui.theme.Services_HandsOn34Theme

class MainActivity : ComponentActivity() {

    private var foregroundAppsService: IForegroundAppsService? = null
    private var isServiceConnected = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            foregroundAppsService = IForegroundAppsService.Stub.asInterface(service)
            isServiceConnected = true
            getInstalledApps()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            foregroundAppsService = null
            isServiceConnected = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Services_HandsOn34Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val intent = Intent(this, ForegroundAppServiceImpl::class.java)
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }

    override fun onStop() {
        if (isServiceConnected) {
            unbindService(serviceConnection)
            isServiceConnected = false
        }
        super.onStop()
    }

    private fun getInstalledApps() {
        if (isServiceConnected) {
            try {
                val apps = foregroundAppsService?.foregroundApps
                apps?.forEach { println(it) }
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Services_HandsOn34Theme {
        Greeting("Android")
    }
}