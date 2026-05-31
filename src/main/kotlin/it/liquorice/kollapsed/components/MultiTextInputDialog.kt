package it.liquorice.kollapsed.components

import javafx.geometry.Insets
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane

class MultiTextInputDialog(vararg labelAndField: Pair<Label, TextField>) : Dialog<MutableList<String>>() {

    init {
        // 使用 GridPane 布局，每行一个 Label 和对应的 TextField
        val grid = GridPane().apply {
            hgap = 10.0
            vgap = 5.0
            padding = Insets(10.0)
        }
        labelAndField.forEachIndexed { index, (label, field) ->
            grid.add(label, 0, index)
            grid.add(field, 1, index)
        }
        dialogPane.content = grid

        // 添加 OK 和 CANCEL 按钮
        dialogPane.buttonTypes.addAll(ButtonType.OK, ButtonType.CANCEL)

        // OK 时收集所有文本，否则返回 null
        setResultConverter { buttonType ->
            if (buttonType == ButtonType.OK) {
                labelAndField.map { it.second.text }.toMutableList()
            } else {
                null
            }
        }
    }
}