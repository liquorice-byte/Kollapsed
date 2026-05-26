package it.liquorice.kollapsed.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ConfigValue {
    abstract val rawValue: Any?

    @Serializable
    @SerialName("string")
    data class StringValue(val data: String) : ConfigValue() {
        override val rawValue get() = data
    }
    @Serializable
    @SerialName("int")
    data class IntValue(val data: Int) : ConfigValue() {
        override val rawValue get() = data
    }
    @Serializable
    @SerialName("boolean")
    data class BooleanValue(val data: Boolean) : ConfigValue() {
        override val rawValue get() = data
    }
}

@Serializable
data class Config(
    var key: String,
    var value: ConfigValue
)