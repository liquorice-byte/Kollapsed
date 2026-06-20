package it.liquorice.kollapsed.utils

import atlantafx.base.layout.InputGroup
import atlantafx.base.theme.Styles
import it.liquorice.kollapsed.config.ConfigManager
import it.liquorice.kollapsed.utils.i18n.Translator
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.slf4j.LoggerFactory

object PrefUtils {
    private var logger = LoggerFactory.getLogger(javaClass)

    fun createSettingsWindow(parentStage: Stage) {
        logger.info("Creating settings window...")
        val settingsStage = Stage()
        val settingsScene: Scene
        val tabPane = TabPane()
        val borderPane = BorderPane()

        settingsStage.width = 400.0
        settingsStage.height = 300.0

        settingsStage.initOwner(parentStage)

        val generalTab = Tab(Translator.translate("SETTINGS_GENERAL"))
        val generalContent = VBox()
        val languageComboBox = ComboBox<String>();
        val group = InputGroup(
            Label(Translator.translate("SETTINGS_GENERAL_LANGUAGE")),
            languageComboBox
        )
        languageComboBox.items.addAll("English (US)")
        languageComboBox.selectionModel.select(
            when (ConfigManager.readConfigValueAsString("language")) {
                "en_us" -> "English (US)"
                else -> "English (US)"
            }
        )

        generalContent.children.add(group)
        generalContent.padding = Insets(10.0, 10.0, 10.0, 10.0)
        generalTab.content = generalContent

        tabPane.tabs.addAll(generalTab)
        tabPane.tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

        val okButton = Button(Translator.translate("OK"))
        okButton.isDefaultButton = true
        okButton.onAction = EventHandler {
            logger.info("Saving config...")
            logger.info("Saving language choice...")
            saveLanguage(
                when (languageComboBox.selectionModel.selectedItem) {
                    "English (US)" -> "en_us"
                    else -> "en_us"
                }
            )
            logger.info("Config saved. Closing window...")
            settingsStage.close()
        }

        borderPane.center = tabPane
        borderPane.bottom = okButton
        borderPane.padding = Insets(10.0, 10.0, 10.0, 10.0)
        BorderPane.setAlignment(okButton, Pos.BOTTOM_RIGHT)

        settingsScene = Scene(borderPane)

        settingsStage.scene = settingsScene
        settingsStage.show()
        settingsStage.centerOnScreen()
        logger.info("Settings window initialized!")
    }

    private fun saveLanguage(language: String) {
        ConfigManager.writeStringConfig("language", language)
    }
}