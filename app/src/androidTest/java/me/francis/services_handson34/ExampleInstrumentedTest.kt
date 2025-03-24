package me.francis.services_handson34

import androidx.compose.ui.test.junit4.createComposeRule
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

    @Test
    fun test() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        composeTestRule.setContent {
            Services_HandsOn34Theme {
                MainContent(appContext, getInstalledApps = ::getAppInstalledFake)
            }
        }

        composeTestRule.onNodeWithText("Nenhum aplicativo listado").assertExists()
        composeTestRule.onNodeWithText("Buscar Apps Instalados").assertExists()
        composeTestRule.onNodeWithText("Buscar Apps Instalados").performClick()

        composeTestRule.onNodeWithTag("app_list").assertExists()
    }

    fun getAppInstalledFake(): List<String> {
        return listOf("Whatsapp, Facebook, Instagram")
    }

}