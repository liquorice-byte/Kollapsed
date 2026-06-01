package it.liquorice.kollapsed

import atlantafx.base.controls.Card
import atlantafx.base.theme.PrimerLight
import it.liquorice.kollapsed.components.MemoCard
import it.liquorice.kollapsed.components.PomodoroCard
import it.liquorice.kollapsed.components.ProgressCard
import it.liquorice.kollapsed.components.TodoCard
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.FlowPane
import javafx.stage.Stage
import org.slf4j.LoggerFactory

class KollapsedApplication : Application() {
    private lateinit var scene: Scene
    private val flowPane = FlowPane()
    private val todoCard = TodoCard()
    private val progressCard = ProgressCard()
    private val pomodoroCard = PomodoroCard()
    private val memoCard = MemoCard()
    private val logger = LoggerFactory.getLogger(KollapsedApplication::class.java)
    override fun start(stage: Stage) {
        logger.info("Starting application...")
        // windows
        stage.icons.add(Image(javaClass.getResourceAsStream("/it/liquorice/kollapsed/img/icon.png")))
        setUserAgentStylesheet(PrimerLight().userAgentStylesheet)
        logger.info("Current Stylesheet: {}", getUserAgentStylesheet())

        // 设置间隔
        flowPane.hgap = 10.0
        flowPane.vgap = 10.0
        logger.info("hgap: ${flowPane.hgap}, vgap: ${flowPane.vgap}")

        // 创建scene, 设置stage
        scene = Scene(flowPane, 1200.0, 1000.0)

        memoCard.minWidth = scene.width

        // 添加到flowPane
        flowPane.children.add(todoCard)
        flowPane.children.add(progressCard)
        flowPane.children.add(pomodoroCard)
        flowPane.children.add(memoCard)
        logger.info("flowPane children: {}", flowPane.children)

        stage.title = "Kollapsed"
        stage.scene = scene
        stage.show()
        logger.info("Application started, title: ${stage.title}, scene root: ${stage.scene.root}, width: ${stage.width}, height: ${stage.height}")
    }

    override fun stop() {
        logger.info("Stopping application...")
        logger.info("Stopping ProgressCard auto refresh...")
        progressCard.stopAutoRefresh()
        logger.info("ProgressCard auto refresh finished.")
        logger.info("Stopping PomodoroCard auto refresh...")
        pomodoroCard.stopAutoRefresh()
        logger.info("PomodoroCard auto refresh finished.")
        logger.info("Application stopped.")
    }
}
