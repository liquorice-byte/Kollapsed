package it.liquorice.kollapsed.singletons

import it.liquorice.kollapsed.serializables.Todo

object Todos {
    val todos: MutableList<Todo> = mutableListOf()

    fun add(content: String) {
        todos.add(Todo(content))
    }

    fun getAll(): MutableList<Todo> {
        return todos
    }

    fun get(index: Int): Todo {
        return todos.getOrElse(index) { throw IndexOutOfBoundsException() }
    }
}