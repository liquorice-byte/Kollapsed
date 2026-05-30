package it.liquorice.kollapsed.components

import atlantafx.base.controls.Card
import atlantafx.base.controls.RingProgressIndicator
import atlantafx.base.controls.Tile
import atlantafx.base.theme.Styles
import it.liquorice.kollapsed.serializables.Todo
import it.liquorice.kollapsed.utils.SavingManager
import it.liquorice.kollapsed.utils.i18n.Translator
import javafx.application.Platform
import javafx.scene.control.ProgressIndicator
import javafx.scene.text.Text
import org.slf4j.LoggerFactory
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.timer

class ProgressCard: Card() {
    private var indicator: RingProgressIndicator

    private var autoTimer: Timer? = null

    private val logger = LoggerFactory.getLogger(ProgressCard::class.java)
    init {
        styleClass.add(Styles.INTERACTIVE)

        indicator = RingProgressIndicator(0.0, false)
        indicator.setMinSize(300.0, 300.0)

        // autoTimer = Timer()
        refresh()
        startAutoRefresh()
        header = Tile(
            Translator.translate("PROGRESS_CARD_TITLE"),
            Translator.translate("PROGRESS_CARD_DESC"),
        )
        body = indicator
        logger.info("ProgressCard initialized")
    }

    private fun refresh() {
        logger.info("Refreshing...")
        val list = SavingManager.loadTodos()
        val filteredList = list.filter { it.isCompleted } as MutableList<Todo>
        indicator.progress = filteredList.size / list.size.toDouble()
        logger.info("Todos count: {}, completed todos count: {}", list.size, filteredList.size)
    }

    private fun startAutoRefresh() {
        if (autoTimer != null) return;
        logger.info("Starting auto refresh...")
        autoTimer = timer(name = "AutoRefresh", daemon = true, period = 1000L) {
            Platform.runLater {
                refresh()
            }
        }
    }

    fun stopAutoRefresh() {
        logger.info("Stopping auto refresh...")
        autoTimer?.cancel()
        autoTimer = null
        logger.info("Auto refresh stopped")
    }
}