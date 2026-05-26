package it.liquorice.kollapsed

import it.liquorice.kollapsed.config.ConfigManager
import it.liquorice.kollapsed.utils.Consts
import javafx.application.Application
import org.slf4j.LoggerFactory
import java.io.File

class Launcher {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val logger = LoggerFactory.getLogger(Launcher::class.java)
            val dir = File(Consts.STORAGE_DIR)
            if (!dir.exists()) {
                logger.info("First run, creating directory and files...")
                if (dir.mkdirs()) {
                    logger.info("Created directory: {}", dir.absolutePath)
                }
            }
            val configFile = File(dir, Consts.CONFIG_RELATIVE)
            if (!configFile.exists() || configFile.readText() == "") {
                logger.info("No config file found. Initializing: {}", Consts.CONFIG_ABSOLUTE)
                ConfigManager.initializeConfig()
            }

            logger.info("Launching application...")
            Application.launch(KollapsedApplication::class.java)


        }
    }
}
