package it.liquorice.kollapsed.components

import atlantafx.base.controls.Card
import atlantafx.base.controls.CustomTextField
import atlantafx.base.controls.Tile
import atlantafx.base.theme.Styles
import it.liquorice.kollapsed.utils.i18n.Translator
import org.kordamp.ikonli.fluentui.FluentUiRegularMZ
import org.kordamp.ikonli.javafx.FontIcon

class TodoCard: Card() {
    init {
        styleClass.add(Styles.INTERACTIVE)
        minWidth = 300.0
        minHeight = 300.0

        val cardHeader = Tile(
            Translator.translate("TODO_CARD_TITLE"),
            Translator.translate("TODO_CARD_DESCRIPTION"),
        )

        val searchBar = CustomTextField()
        searchBar.promptText = Translator.translate("SEARCH_BAR_PROMPT")
        searchBar.left = FontIcon(FluentUiRegularMZ.SEARCH_20)


        header = cardHeader
        subHeader = searchBar
    }
}