package it.liquorice.kollapsed.components

import atlantafx.base.controls.Card
import atlantafx.base.controls.CustomTextField
import atlantafx.base.controls.Tile
import atlantafx.base.theme.Styles
import it.liquorice.kollapsed.serializables.Todo
import it.liquorice.kollapsed.utils.JsonUtils
import it.liquorice.kollapsed.utils.SavingManager
import it.liquorice.kollapsed.utils.i18n.Translator
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextInputDialog
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import javafx.stage.Window
import org.kordamp.ikonli.fluentui.FluentUiRegularAL
import org.kordamp.ikonli.fluentui.FluentUiRegularMZ
import org.kordamp.ikonli.javafx.FontIcon
import org.slf4j.LoggerFactory

class TodoCard: Card() {
    private var cardHeader: Tile
    private var searchBar: CustomTextField
    private var todosBox: VBox
    private var scrollPane: ScrollPane
    private var addButton: Button
    private val logger = LoggerFactory.getLogger(TodoCard::class.java)
    init {
        styleClass.add(Styles.INTERACTIVE)
        minWidth = 300.0
        // minHeight = scene.window.height / 4
        maxWidth = 300.0
        // maxHeight = scene.window.height / 4

        cardHeader = Tile(
            Translator.translate("TODO_CARD_TITLE"),
            Translator.translate("TODO_CARD_DESC"),
        )

        searchBar = CustomTextField()
        searchBar.promptText = Translator.translate("SEARCH_BAR_PROMPT")
        searchBar.left = FontIcon(FluentUiRegularMZ.SEARCH_20)

        todosBox = VBox(8.0)
        todosBox.padding = Insets(8.0)

        scrollPane = ScrollPane(todosBox)
        scrollPane.isFitToWidth = true
        scrollPane.isFitToHeight = true


        addButton = Button("", FontIcon(FluentUiRegularMZ.TASK_LIST_ADD_20))
        addButton.styleClass.add(Styles.BUTTON_ICON)
        addButton.onAction = EventHandler {
            logger.info("Add button clicked, showing dialog...")
            val dialog = TextInputDialog()
            dialog.title = Translator.translate("ADD_TODO_DIALOG_TITLE")
            dialog.headerText = null
            dialog.contentText = Translator.translate("ADD_TODO_DIALOG_CONTENT")
            dialog.initOwner(scene.window)
            dialog.showAndWait()
            logger.info("User input: ${dialog.result}")
            dialog.result?.let { content ->
                if (content.isNotBlank()) {
                    logger.info("Adding todo to file...")
                    SavingManager.writeTodo(dialog.result)
                    logger.info("Done. Refreshing...")
                    refresh()
                } else {
                    logger.info("User input is empty.")
                }
            }
        }

        refresh()
        header = cardHeader
        subHeader = searchBar
        body = scrollPane
        footer = addButton
        logger.info("TodoCard initialized")
    }

    private fun refresh() {
        logger.info("Refreshing...")
        var list = SavingManager.loadTodos()
        list = list.sortedBy { it.isCompleted } as MutableList<Todo>
        todosBox.children.clear()
        list.forEach { todo ->
            val hbox = HBox(8.0)
            val checkBox = CheckBox()
            val label = Label()
            val deleteButton = Button("", FontIcon(FluentUiRegularAL.DELETE_16))
            hbox.maxWidth = Double.MAX_VALUE
            hbox.alignment = Pos.CENTER_LEFT

            label.text = todo.content
            label.maxWidth = Double.MAX_VALUE
            label.isWrapText = true
            HBox.setHgrow(label, Priority.ALWAYS)

            checkBox.isSelected = todo.isCompleted

            checkBox.selectedProperty().addListener { _, oldValue, newValue ->
                if (oldValue != newValue) {
                    todo.isCompleted = newValue
                    SavingManager.writeTodo(todo)
                    refresh()
                    logger.info("Todo '${todo.content}' completion toggled to $newValue")
                }
            }

            deleteButton.styleClass.add(Styles.BUTTON_CIRCLE)
            deleteButton.alignment = Pos.CENTER_RIGHT
            deleteButton.onAction = EventHandler {
                logger.info("Deleting todo: ${todo.content}")
                val deleted = SavingManager.deleteTodo(todo)
                if (deleted) {
                    logger.info("Todo deleted successfully")
                    refresh()
                } else {
                    logger.warn("Failed to delete todo: ${todo.content}")
                }
            }

            hbox.children.addAll(checkBox, label, deleteButton)
            todosBox.children.add(hbox)
        }
    }
}