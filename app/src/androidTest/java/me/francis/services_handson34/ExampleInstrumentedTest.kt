package me.francis.services_handson34

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import me.francis.services_handson34.ui.theme.Services_HandsOn34Theme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("me.francis.services_handson34", appContext.packageName)
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    // Teste de UI usando Jetpack Compose Testing
    @Test
    fun test() {
        // Obtém o contexto de teste do ambiente de instrumentação
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // Configura o conteúdo testável da UI usando a regra de teste do Compose
        composeTestRule.setContent {
            // Aplica o tema customizado do aplicativo
            Services_HandsOn34Theme {
                // Renderiza o componente MainContent passando:
                // - Contexto do aplicativo
                // - Função fake para simular a obtenção de apps instalados
                MainContent(appContext, getInstalledApps = ::getAppInstalledFake)
            }
        }

        // Verifica se o texto inicial "Nenhum aplicativo listado" está presente
        composeTestRule.onNodeWithText("Nenhum aplicativo listado").assertExists()
        // Verifica se o botão "Buscar Apps Instalados" está presente e realiza um clique
        composeTestRule.onNodeWithText("Buscar Apps Instalados").assertExists()
        composeTestRule.onNodeWithText("Buscar Apps Instalados").performClick()

        // Verifica se a lista de aplicativos está presente na tela
        composeTestRule.onNodeWithTag("app_list").assertExists()

        // Verificação adicional após o clique
        composeTestRule.onAllNodesWithTag("app_list_item")
            .assertCountEquals(3) // Verifica quantidade de itens
    }

    // Função fake para simular a obtenção de apps instalados
    fun getAppInstalledFake(): List<String> {
        return listOf("Whatsapp", "Facebook", "Instagram")
    }

}