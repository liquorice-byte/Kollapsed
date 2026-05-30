package it.liquorice.kollapsed.utils

import it.liquorice.kollapsed.serializables.Todo
import kotlinx.serialization.json.Json
import java.io.File

object SavingManager {
    fun initializeSaving() {
        val todosFile = File("${Consts.STORAGE_DIR}/${Consts.TODO_RELATIVE}")
        todosFile.createNewFile()
        todosFile.writeText("[\n\n]")
    }

    fun writeTodo(todo: Todo) {
        val list = JsonUtils.readUnderStorageDir<MutableList<Todo>>(Consts.TODO_RELATIVE)
        val index = list.indexOfFirst { it.content == todo.content }
        if (index != -1) {
            list[index] = todo
        } else {
            list.add(todo)
        }
        JsonUtils.writeUnderStorageDir(list, Consts.TODO_RELATIVE)
    }

    fun writeTodo(content: String, isCompleted: Boolean = false) = writeTodo(Todo(content, isCompleted = isCompleted))

    fun loadTodos(): MutableList<Todo> {
        return JsonUtils.readUnderStorageDir(Consts.TODO_RELATIVE)
    }

    fun deleteTodo(todo: Todo): Boolean {
        val list = JsonUtils.readUnderStorageDir<MutableList<Todo>>(Consts.TODO_RELATIVE)
        val result = list.remove(todo)
        JsonUtils.writeUnderStorageDir(list, Consts.TODO_RELATIVE)
        return result
    }
}