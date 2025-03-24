package me.francis.services_handson34

import android.content.Context
import android.content.pm.PackageManager

// Implementação concreta do serviço de aplicativos instalados que estende IInstalledAppsService.Stub()
// Esta classe é responsável por obter a lista de aplicativos instalados no dispositivo
class InstalledAppsService(val context: Context) : IInstalledAppsService.Stub() {

    // Método sobrescrito da interface IInstalledAppsService
    // Retorna uma lista de nomes de aplicativos instalados
    override fun getInstalledApps(): List<String?>? {
        // Obtém o PackageManager do contexto, que fornece informações sobre pacotes instalados
        val packageManager = context.packageManager

        // Obtém a lista de todos os aplicativos instalados
        // GET_META_DATA flag para incluir metadados adicionais se necessário
        val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        // Mapeia cada aplicativo para seu nome (label) e retorna a lista de nomes
        return installedApps.map {
            // Converte o ApplicationInfo em um nome legível usando getApplicationLabel
            packageManager.getApplicationLabel(it).toString()
        }
    }
}
