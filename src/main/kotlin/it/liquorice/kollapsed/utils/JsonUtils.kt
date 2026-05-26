package it.liquorice.kollapsed.utils

import kotlinx.serialization.json.Json
import java.io.File

object JsonUtils {
    inline fun <reified T : Any> writeToJsonFile(obj: T, file: File) {
        val json = Json.encodeToString(obj)
        file.writeText(json, Charsets.UTF_8)
    }

    inline fun <reified T : Any> readFromJsonFile(file: File): T {
        require(file.exists()) { "File does not exist: ${file.absolutePath}." }
        val obj = Json.decodeFromString<T>(file.readText())
        return obj
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