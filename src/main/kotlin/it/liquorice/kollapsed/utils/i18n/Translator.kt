package it.liquorice.kollapsed.utils.i18n

import it.liquorice.kollapsed.config.ConfigManager
import it.liquorice.kollapsed.config.ConfigValue
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object Translator {
    fun translate(key: String, language: String): String? {
        val resource = javaClass.getResource("/it/liquorice/kollapsed/languages/$language.json")
            ?: return "[$key]"   // 文件不存在时返回 key 或 null
        val jsonString = resource.readText(Charsets.UTF_8)
        return translateString(jsonString, key)
    }

    fun translate(key: String): String? {
        val language = ConfigManager.readConfigValue<String>("language") ?: return null
        return translate(key, language)
    }

    private fun translateString(jsonString: String, key: String): String? {
        val obj = Json.parseToJsonElement(jsonString).jsonObject
        return obj[key]?.jsonPrimitive?.content
    }
}