package it.liquorice.kollapsed.components

import atlantafx.base.controls.Card
import atlantafx.base.controls.Tile
import atlantafx.base.theme.Styles
import it.liquorice.kollapsed.utils.i18n.Translator
import javafx.scene.web.HTMLEditor

class MemoCard: Card() {
    private var editor: HTMLEditor

    init {
        styleClass.add(Styles.INTERACTIVE)

        editor = HTMLEditor()
        editor.maxHeight = 400.0

        header = Tile(
            Translator.translate("MEMO_CARD_TITLE"),
            Translator.translate("MEMO_CARD_DESC"),
        )
        body = editor
    }
}