package it.liquorice.kollapsed

import atlantafx.base.controls.Card
import atlantafx.base.theme.CupertinoLight
import it.liquorice.kollapsed.components.TodoCard
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.FlowPane
import javafx.stage.Stage
import org.slf4j.LoggerFactory

class KollapsedApplication : Application() {
    private val logger = LoggerFactory.getLogger(KollapsedApplication::class.java)
    override fun start(stage: Stage) {
        logger.info("Starting application...")
        setUserAgentStylesheet(CupertinoLight().userAgentStylesheet)
        logger.info("Current Stylesheet: {}", getUserAgentStylesheet())

        // layout
        val flowPane = FlowPane()

        // 设置间隔
        flowPane.hgap = 10.0
        flowPane.vgap = 10.0
        logger.info("hgap: ${flowPane.hgap}, vgap: ${flowPane.vgap}")

        // 创建卡片
        val todoCard = TodoCard()
        val progressCard = Card()
        val pomodoroCard = Card()
        val memoCard = Card()

        // 添加到flowPane
        flowPane.children.add(todoCard)
        flowPane.children.add(progressCard)
        flowPane.children.add(pomodoroCard)
        flowPane.children.add(memoCard)
        logger.info("flowPane children: {}", flowPane.children)

        // prefWrapLength
        // flowPane.prefWrapLength = 170.0

        // 创建scene, 设置stage
        val scene = Scene(flowPane, 1000.0, 800.0)
        stage.title = "Kollapsed"
        stage.scene = scene
        stage.show()
        logger.info("Application started, title: ${stage.title}, scene root: ${stage.scene.root}, width: ${stage.width}, height: ${stage.height}")
    }
}
