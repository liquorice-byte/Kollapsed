package it.liquorice.kollapsed.config

import it.liquorice.kollapsed.utils.Consts
import it.liquorice.kollapsed.utils.JsonUtils
import java.io.File

object ConfigManager {
    fun initializeConfig() {
        val configFile = File("${Consts.STORAGE_DIR}/${Consts.CONFIG_RELATIVE}")
        configFile.createNewFile()
        configFile.writeText("[\n\n]")
        writeStringConfig("language", "en_us")
    }

    fun readConfig(key: String): Config? {
        val list = JsonUtils.readUnderStorageDir<MutableList<Config>>(Consts.CONFIG_RELATIVE)
        val result = list.find { it.key == key }
        return result
    }

    inline fun <reified T> readConfigValue(key: String): T? {
        val config = readConfig(key) ?: return null
        return config.value.rawValue as? T
    }

    inline fun <reified T> readConfigValue(key: String, defaultValue: T): T =
        readConfigValue<T>(key) ?: defaultValue

    fun readConfigValueAsString(key: String): String? = readConfigValue(key)

    fun readConfigValueAsInt(key: String): Int? = readConfigValue(key)

    fun readConfigValueAsBoolean(key: String): Boolean? = readConfigValue(key)

    fun writeConfig(config: Config) {
        val list = JsonUtils.readUnderStorageDir<MutableList<Config>>(Consts.CONFIG_RELATIVE)
        val index = list.indexOfFirst { it.key == config.key }
        if (index != -1) {
            list[index] = config
        } else {
            list.add(config)
        }
        JsonUtils.writeUnderStorageDir(list, Consts.CONFIG_RELATIVE)
    }

    fun writeConfig(key: String, value: ConfigValue) {
        writeConfig(Config(key, value))
    }

    fun writeStringConfig(key: String, value: String) =
        writeConfig(key, ConfigValue.StringValue(value))

    fun writeIntConfig(key: String, value: Int) =
        writeConfig(key, ConfigValue.IntValue(value))

    fun writeBooleanConfig(key: String, value: Boolean) =
        writeConfig(key, ConfigValue.BooleanValue(value))
}