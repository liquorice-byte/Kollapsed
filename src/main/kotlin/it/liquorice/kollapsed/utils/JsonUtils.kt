package it.liquorice.kollapsed.utils

import it.liquorice.kollapsed.config.ConfigValue
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import java.io.File

object JsonUtils {
    val json = Json {
        ignoreUnknownKeys = true
        // 必须注册 ConfigValue 的子类
        serializersModule = SerializersModule {
            polymorphic(ConfigValue::class) {
                subclass(ConfigValue.StringValue::class)
                subclass(ConfigValue.IntValue::class)
                subclass(ConfigValue.BooleanValue::class)
            }
        }
    }

    inline fun <reified T : Any> writeToJsonFile(obj: T, file: File) {
        val text = json.encodeToString(obj)
        file.writeText(text, Charsets.UTF_8)
    }

    inline fun <reified T : Any> readFromJsonFile(file: File): T {
        require(file.exists()) { "File does not exist: ${file.absolutePath}." }
        val text = file.readText(Charsets.UTF_8)
        return json.decodeFromString<T>(text)
    }

    inline fun <reified T: Any> writeToJsonFile(obj: T, path: String) = writeToJsonFile(obj, File(path))

    inline fun <reified T: Any> readFromJsonFile(path: String): T = readFromJsonFile(File(path))

    inline fun <reified T: Any> writeUnderStorageDir(obj: T, path: String) = writeToJsonFile(obj, buildString {
        append(Consts.STORAGE_DIR)
        append("/$path")
    })

    inline fun <reified T: Any> readUnderStorageDir(path: String): T = readFromJsonFile(
        buildString {
            append(Consts.STORAGE_DIR)
            append("/$path")
        }
    )

}