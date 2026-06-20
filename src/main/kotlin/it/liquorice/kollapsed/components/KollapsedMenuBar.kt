package it.liquorice.kollapsed.components

import it.liquorice.kollapsed.utils.PrefUtils
import it.liquorice.kollapsed.utils.i18n.Translator
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import javafx.stage.Stage
import org.kordamp.ikonli.Ikon
import org.kordamp.ikonli.fluentui.FluentUiRegularMZ
import org.kordamp.ikonli.javafx.FontIcon

class KollapsedMenuBar(private var parentStage: Stage): MenuBar() {

    init {
        menus.addAll(
            fileMenu()
        )
    }

    private fun fileMenu(): Menu{
        val menu = Menu("File")
        menu.isMnemonicParsing = true
        val settingsKeyCode = when {
            System.getProperty("os.name").startsWith("Mac") -> KeyCodeCombination(KeyCode.COMMA, KeyCombination.SHORTCUT_DOWN)
            else -> null
        }
        val settingsMenu = createItem(
            Translator.translate("SETTINGS"),
            FluentUiRegularMZ.SETTINGS_24,
            settingsKeyCode,
            EventHandler {
                PrefUtils.createSettingsWindow(parentStage)
            }
        )
        settingsMenu.isMnemonicParsing = true
        menu.items.add(settingsMenu)
        return menu
    }

    private fun createItem(text: String?, graphic: Ikon?, accelerator: KeyCombination?, action: EventHandler<ActionEvent>?): MenuItem {
        val item = MenuItem(text)

        if (graphic != null) {
            item.graphic = FontIcon(graphic)
        }

        if (accelerator != null) {
            item.accelerator = accelerator
        }

        if (action != null) {
            item.onAction = action
        }

        return item
    }


}