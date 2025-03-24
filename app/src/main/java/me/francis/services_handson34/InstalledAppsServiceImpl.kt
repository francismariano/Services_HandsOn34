package me.francis.services_handson34

import android.app.Service
import android.content.Intent
import android.os.IBinder

// Implementação de um Service do Android que fornece acesso à lista de apps instalados
class InstalledAppsServiceImpl : Service() {

    // Serviço que contém a lógica para obter a lista de aplicativos instalados
    // 'lateinit' indica que a variável será inicializada posteriormente
    private lateinit var appListService: InstalledAppsService

    // Chamado quando o serviço é criado pela primeira vez
    override fun onCreate() {
        super.onCreate()
        appListService = InstalledAppsService(applicationContext)
    }

    // Método chamado quando um componente quer se vincular a este serviço
    // Retorna a interface de comunicação (IBinder) para o cliente
    override fun onBind(p0: Intent?): IBinder? {
        // Retorna a instância do serviço que implementa IBinder
        // Permite que os clientes se comuniquem com este serviço
        return appListService
    }
}
