package it.liquorice.kollapsed

import it.liquorice.kollapsed.config.ConfigManager
import it.liquorice.kollapsed.config.ConfigValue
import it.liquorice.kollapsed.utils.Consts
import it.liquorice.kollapsed.utils.SavingManager
import javafx.application.Application
import org.slf4j.LoggerFactory
import java.io.File

class Launcher {
    companion object {
        private val logger = LoggerFactory.getLogger(Launcher::class.java)
        @JvmStatic
        fun main(args: Array<String>) {
            // val logger = LoggerFactory.getLogger(Launcher::class.java)

            checkAndCompleteFiles()
            logger.info("Language: {}", ConfigManager.readConfigValue<String>("language"))
            logger.info("Launching application...")
            Application.launch(KollapsedApplication::class.java)
        }

        private fun checkAndCompleteFiles() {
            val dir = File(Consts.STORAGE_DIR)
            if (!dir.exists()) {
                logger.info("No storage directory found. Creating...")
                if (dir.mkdirs()) {
                    logger.info("Created directory: {}", dir.absolutePath)
                }
            }
            val configFile = File(dir, Consts.CONFIG_RELATIVE)
            if (!configFile.exists() || configFile.readText() == "") {
                logger.info("No config file found. Initializing: {}", Consts.CONFIG_ABSOLUTE)
                ConfigManager.initializeConfig()
            }
            val savingFile = File(dir, Consts.TODO_RELATIVE)
            if (!savingFile.exists()) {
                logger.info("No \"todo\" archive" +
                        " file found. Initializing: {}", Consts.TODO_ABSOLUTE)
                SavingManager.initializeSaving()
            }
        }
    }
}
