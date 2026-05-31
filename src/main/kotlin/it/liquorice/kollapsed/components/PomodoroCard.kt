package it.liquorice.kollapsed.components

import atlantafx.base.controls.Card
import atlantafx.base.controls.RingProgressIndicator
import atlantafx.base.controls.Tile
import atlantafx.base.theme.Styles
import it.liquorice.kollapsed.utils.PomodoroManager
import it.liquorice.kollapsed.utils.i18n.Translator
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import java.util.Timer
import kotlin.concurrent.timer

class PomodoroCard: Card() {
    private val vBox: VBox = VBox()
    private val indicator: RingProgressIndicator
    private val button: Button
    private val stateText: Text
    private val timeText: Text
    private val cycleText: Text

    private var autoTimer: Timer? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    private val logger = LoggerFactory.getLogger(PomodoroCard::class.java)

    init {
        styleClass.add(Styles.INTERACTIVE)
        vBox.alignment = Pos.CENTER
        vBox.spacing = 15.0

        indicator = RingProgressIndicator(0.0, false)
        indicator.setMinSize(250.0, 250.0)

        button = Button()
        button.onAction = EventHandler {
            onButtonClick()
        }
        stateText = Text()
        timeText = Text()
        cycleText = Text()

        vBox.children.add(indicator)
        vBox.children.add(button)
        vBox.children.add(stateText)
        vBox.children.add(timeText)
        vBox.children.add(cycleText)

        startAutoRefresh()
        header = Tile(
            Translator.translate("POMODORO_CARD_TITLE"),
            Translator.translate("POMODORO_CARD_DESC")
        )
        body = vBox
        logger.info("PomodoroCard initialized")
    }

    private fun onButtonClick() {
        val status = PomodoroManager.statusFlow.value
        if (status.isActive) {
            logger.info("Stopping Pomodoro...")
            PomodoroManager.stop()
        } else {
            logger.info("Showing Pomodoro config dialog...")
            showConfigDialog()
        }
    }

    private fun refresh() {
        logger.info("Refreshing...")
        val status = PomodoroManager.statusFlow.value
        if (status.isActive) {
            button.text = Translator.translate("POMODORO_STOP_BUTTON_TEXT")
            val totalSeconds = status.totalSecondsOfCurrentStage
            indicator.progress = if (totalSeconds > 0) status.elapsedSeconds / status.totalSecondsOfCurrentStage.toDouble() else 0.0
            logger.info("Current progress: ${indicator.progress}")
            logger.info("Elapsed: ${status.elapsedSeconds}, totalSeconds: $totalSeconds")
            stateText.text = Translator.translate(status.currentState.name)
            timeText.text = "${Translator.translate("ELAPSED")}: ${String.format("%.2f", status.elapsedSeconds / 60.toDouble())} (${status.elapsedSeconds} sec), ${Translator.translate("TOTAL")}: ${totalSeconds / 60.toDouble()} (min)"
            cycleText.text = buildString {
                append(Translator.translate("POMODORO_CYCLE"))
                append(": ")
                append(status.currentCycle)
            }
            stateText.isVisible = true
            timeText.isVisible = true
            cycleText.isVisible = true
        } else {
            button.text = Translator.translate("POMODORO_START_BUTTON_TEXT")
            indicator.progress = 0.0
            stateText.isVisible = false
            timeText.isVisible = false
            cycleText.isVisible = false
        }
    }

    private fun startAutoRefresh() {
        if (autoTimer != null) return
        logger.info("Starting auto refresh...")
        autoTimer = timer(name = "AutoRefresh", daemon = true, period = 1000L)  {
            Platform.runLater { refresh() }
        }
    }

    fun stopAutoRefresh() {
        logger.info("Stopping auto refresh...")
        autoTimer?.cancel()
        autoTimer = null
        coroutineScope.coroutineContext[Job]?.cancelChildren()
        logger.info("Auto refresh stopped...")
    }

    private fun showConfigDialog() {
        val focusMinutesLabel = Label(Translator.translate("POMODORO_DIALOG_FOCUS"))
        val focusMinutesField = TextField()
        val shortRestMinutesLabel = Label(Translator.translate("POMODORO_DIALOG_SHORT_REST"))
        val shortRestMinutesField = TextField()
        val longRestMinutesLabel = Label(Translator.translate("POMODORO_DIALOG_LONG_REST"))
        val longRestMinutesField = TextField()
        val cyclesLabel = Label(Translator.translate("POMODORO_DIALOG_CYCLES"))
        val cyclesField = TextField()

        val dialog = MultiTextInputDialog(
            focusMinutesLabel to focusMinutesField,
            shortRestMinutesLabel to shortRestMinutesField,
            longRestMinutesLabel to longRestMinutesField,
            cyclesLabel to cyclesField,
        ).apply {
            title = Translator.translate("POMODORO_DIALOG_TITLE")
            headerText = null
        }

        val result = dialog.showAndWait()

        result.ifPresent { values ->
            val focusStr = focusMinutesField.text
            val shortRestStr = shortRestMinutesField.text
            val longRestStr = longRestMinutesField.text
            val cyclesStr = cyclesField.text

            if (validateInputs(focusStr, shortRestStr, longRestStr, cyclesStr)) {
                val focus = focusStr.toInt()
                val shortRest = shortRestStr.toInt()
                val longRest = longRestStr.toInt()
                val cycles = cyclesStr.toInt()
                startPomodoro(focus, shortRest, longRest, cycles)
            } else {
                showAlert(Translator.translate("POMODORO_INVALID_INPUT"))
            }
        }
    }

    private fun validateInputs(
        focus: String,
        shortRest: String,
        longRest: String,
        cycles: String
    ): Boolean {
        val focusInt = focus.toIntOrNull()
        val shortRestInt = shortRest.toIntOrNull()
        val longRestInt = longRest.toIntOrNull()
        val cyclesInt = cycles.toIntOrNull()

        if (focusInt == null || shortRestInt == null || longRestInt == null || cyclesInt == null) {
            return false
        }

        return focusInt in 1..60 &&
                shortRestInt in 1..30 &&
                longRestInt in 1..120 &&
                cyclesInt in 1..10
    }

    private fun showAlert(message: String?) {
        val alert = Alert(Alert.AlertType.ERROR)
        alert.title = Translator.translate("ERROR")
        alert.headerText = null
        alert.contentText = message
        alert.showAndWait()
    }

    private fun startPomodoro(
        focusMinutes: Int,
        shortRestMinutes: Int,
        longRestMinutes: Int,
        cycles: Int
    ) {
        coroutineScope.launch {
            PomodoroManager.startPomodoro(
                focusMinutes = focusMinutes,
                shortRestMinutes = shortRestMinutes,
                longRestMinutes = longRestMinutes,
                cycles = cycles
            )
        }
    }
}