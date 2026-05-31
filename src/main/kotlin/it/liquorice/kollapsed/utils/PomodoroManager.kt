package it.liquorice.kollapsed.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class PomodoroState {
    FOCUS,
    SHORT_REST,
    LONG_REST,
}

data class PomodoroConfig(
    val focusMinutes: Int,
    val shortRestMinutes: Int,
    val longRestMinutes: Int,
    val cycles: Int
) {
    internal val focusSeconds = focusMinutes * 60
    internal val shortRestSeconds = shortRestMinutes * 60
    internal val longRestSeconds = longRestMinutes * 60
}

data class PomodoroStatus(
    val isActive: Boolean,
    val currentState: PomodoroState,
    val elapsedSeconds: Int,
    val currentCycle: Int,
    val config: PomodoroConfig?
) {
    val totalSecondsOfCurrentStage: Int
        get() = when {
            !isActive || config == null -> 0
            currentState == PomodoroState.FOCUS -> config.focusSeconds
            currentState == PomodoroState.SHORT_REST -> config.shortRestSeconds
            currentState == PomodoroState.LONG_REST -> config.longRestSeconds
            else -> 0
        }

    val remainingSeconds: Int
        get() = totalSecondsOfCurrentStage - elapsedSeconds
}

object PomodoroManager {
    private var activeJob: Job? = null
    private val _statusFlow = MutableStateFlow(
        PomodoroStatus(
            isActive = false,
            currentState = PomodoroState.FOCUS,
            elapsedSeconds = 0,
            currentCycle = 1,
            config = null
        )
    )
    val statusFlow: StateFlow<PomodoroStatus> = _statusFlow.asStateFlow()

    /**
     * 启动番茄钟（suspend 版本，通常从协程中调用）
     */
    suspend fun startPomodoro(
        focusMinutes: Int = 25,
        shortRestMinutes: Int = 5,
        longRestMinutes: Int = 20,
        cycles: Int = 4
    ) = synchronized(this) {
        if (_statusFlow.value.isActive) return

        val config = PomodoroConfig(focusMinutes, shortRestMinutes, longRestMinutes, cycles)
        _statusFlow.value = PomodoroStatus(
            isActive = true,
            currentState = PomodoroState.FOCUS,
            elapsedSeconds = 0,
            currentCycle = 1,
            config = config
        )

        activeJob?.cancel()
        activeJob = CoroutineScope(Dispatchers.Main).launch {
            while (_statusFlow.value.isActive) {
                val current = _statusFlow.value
                val duration = when (current.currentState) {
                    PomodoroState.FOCUS -> current.config!!.focusSeconds
                    PomodoroState.SHORT_REST -> current.config!!.shortRestSeconds
                    PomodoroState.LONG_REST -> current.config!!.longRestSeconds
                }

                while (_statusFlow.value.isActive && _statusFlow.value.elapsedSeconds < duration) {
                    delay(1000)
                    _statusFlow.update { state ->
                        val newElapsed = (state.elapsedSeconds + 1).coerceAtMost(duration)
                        state.copy(elapsedSeconds = newElapsed)
                    }
                }

                if (!_statusFlow.value.isActive) break
                transitionToNextState()
            }

            activeJob = null
            if (_statusFlow.value.isActive) {
                _statusFlow.update { it.copy(isActive = false, config = null) }
            }
        }
    }

    /**
     * 停止番茄钟（非挂起版本，可在任何线程调用，包括主线程）
     */
    fun stop() = synchronized(this) {
        if (!_statusFlow.value.isActive) return
        _statusFlow.update { it.copy(isActive = false, config = null) }
        activeJob?.cancel()
        activeJob = null
    }

    /**
     * 兼容原设计的挂起停止方法
     */
    @Suppress("unused")
    suspend fun stopPomodoro() = stop()

    private suspend fun transitionToNextState() {
        val current = _statusFlow.value
        if (!current.isActive) return
        val config = current.config ?: return

        when (current.currentState) {
            PomodoroState.FOCUS -> {
                val newCycle = current.currentCycle + 1
                val nextState = if (newCycle > config.cycles) {
                    PomodoroState.LONG_REST
                } else {
                    PomodoroState.SHORT_REST
                }
                _statusFlow.update {
                    it.copy(
                        currentState = nextState,
                        elapsedSeconds = 0,
                        currentCycle = newCycle
                    )
                }
            }
            PomodoroState.SHORT_REST -> {
                _statusFlow.update {
                    it.copy(currentState = PomodoroState.FOCUS, elapsedSeconds = 0)
                }
            }
            PomodoroState.LONG_REST -> {
                _statusFlow.update { it.copy(isActive = false, config = null) }
                activeJob?.cancel()
                activeJob = null
            }
        }
    }
}