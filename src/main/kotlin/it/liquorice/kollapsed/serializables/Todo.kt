package it.liquorice.kollapsed.serializables

import kotlinx.serialization.Serializable

@Serializable
data class Todo(val content: String, val isCompleted: Boolean = false) {}