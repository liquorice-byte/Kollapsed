package it.liquorice.kollapsed.utils.i18n

import it.liquorice.kollapsed.config.ConfigManager
import it.liquorice.kollapsed.config.ConfigValue
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import java.io.File

object Translator {
    fun translate(key: String, language: String): String? {
        val jsonFile: File = File(javaClass.getResource("languages/$language.json").path)
        val jsonString: String = jsonFile.readText(Charsets.UTF_8)
        return translateString(jsonString, key)
    }

    fun translate(key: String): String? {
        val jsonFile: File = File(javaClass.getResource(
            "languages/${ConfigManager.readConfigValue<ConfigValue.StringValue>("language")!!.rawValue}.json").path
        )
        val jsonString: String = jsonFile.readText(Charsets.UTF_8)
        return translateString(jsonString, key)
    }

    private fun translateString(jsonString: String, key: String): String? {
        val obj = Json.parseToJsonElement(jsonString).jsonObject
        for ((k, v) in obj) {
            if (k == key) {
                return v.toString()
            }
        }
        return null
    }
}