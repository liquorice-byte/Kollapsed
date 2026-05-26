package it.liquorice.kollapsed.components

import atlantafx.base.controls.Card
import atlantafx.base.controls.CustomTextField
import atlantafx.base.controls.Tile
import atlantafx.base.theme.Styles
import org.kordamp.ikonli.fluentui.FluentUiRegularMZ
import org.kordamp.ikonli.javafx.FontIcon

class TodoCard: Card() {
    init {
        styleClass.add(Styles.INTERACTIVE)
        minWidth = 300.0
        minHeight = 300.0

        val cardHeader = Tile(
            "To-dos",
            "Hmm...what am I going to do?"
        )

        val searchBar = CustomTextField()
        searchBar.promptText = "Search..."
        searchBar.left = FontIcon(FluentUiRegularMZ.SEARCH_20)


        header = cardHeader
        subHeader = searchBar
    }
}