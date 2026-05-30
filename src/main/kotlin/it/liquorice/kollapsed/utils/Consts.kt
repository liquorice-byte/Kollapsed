package it.liquorice.kollapsed.utils

class Consts {
    companion object {
        val STORAGE_DIR: String = buildString {
            append(System.getProperty("user.home"))
            append("/Kollapsed")
        }

        const val CONFIG_RELATIVE: String = "config.json"

        val CONFIG_ABSOLUTE: String = buildString {
            append(STORAGE_DIR)
            append("/")
            append(CONFIG_RELATIVE)
        }

        const val TODO_RELATIVE: String = "todos.json"

        val TODO_ABSOLUTE: String = buildString {
            append(STORAGE_DIR)
            append("/")
            append(TODO_RELATIVE)
        }
    }
}