package com.marsn.demo_gamma

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.math.abs
import kotlin.math.roundToLong

data class CalculatorUiState(
    val display: String = "0",
    val expression: String = "Ready",
    val platformName: String,
    val isError: Boolean = false,
)

enum class CalculatorOperation(val symbol: String) {
    Divide("/"),
    Multiply("x"),
    Subtract("-"),
    Add("+"),
}

sealed interface CalculatorAction {
    data class Digit(val value: Int) : CalculatorAction
    data object Decimal : CalculatorAction
    data object Clear : CalculatorAction
    data object Equals : CalculatorAction
    data object Percent : CalculatorAction
    data object ToggleSign : CalculatorAction
    data class Operation(val value: CalculatorOperation) : CalculatorAction
}

class CalculatorStateHolder(
    platform: Platform,
) {
    private var currentInput = "0"
    private var storedValue: Double? = null
    private var pendingOperation: CalculatorOperation? = null
    private var resetInput = false
    private var justEvaluated = false
    private var errorMessage: String? = null

    var uiState by mutableStateOf(CalculatorUiState(platformName = platform.name))
        private set

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Digit -> appendDigit(action.value)
            CalculatorAction.Decimal -> appendDecimal()
            CalculatorAction.Clear -> clear()
            CalculatorAction.Equals -> evaluate()
            CalculatorAction.Percent -> applyPercent()
            CalculatorAction.ToggleSign -> toggleSign()
            is CalculatorAction.Operation -> setOperation(action.value)
        }
        syncState()
    }

    private fun appendDigit(value: Int) {
        if (errorMessage != null) {
            clear()
        }

        if (resetInput || justEvaluated) {
            currentInput = value.toString()
            resetInput = false
            justEvaluated = false
            return
        }

        currentInput = if (currentInput == "0") {
            value.toString()
        } else {
            currentInput + value
        }
    }

    private fun appendDecimal() {
        if (errorMessage != null) {
            clear()
        }

        if (resetInput || justEvaluated) {
            currentInput = "0."
            resetInput = false
            justEvaluated = false
            return
        }

        if (!currentInput.contains('.')) {
            currentInput += "."
        }
    }

    private fun clear() {
        currentInput = "0"
        storedValue = null
        pendingOperation = null
        resetInput = false
        justEvaluated = false
        errorMessage = null
        syncState()
    }

    private fun toggleSign() {
        if (errorMessage != null) {
            clear()
            return
        }

        currentInput = when {
            currentInput == "0" -> currentInput
            currentInput.startsWith("-") -> currentInput.drop(1)
            else -> "-$currentInput"
        }
    }

    private fun applyPercent() {
        if (errorMessage != null) {
            clear()
            return
        }

        val value = currentInput.toDoubleOrNull() ?: return
        currentInput = formatNumber(value / 100.0)
        justEvaluated = false
    }

    private fun setOperation(operation: CalculatorOperation) {
        if (errorMessage != null) {
            clear()
        }

        val currentValue = currentInput.toDoubleOrNull() ?: return
        if (pendingOperation != null && !resetInput) {
            val result = calculate(storedValue ?: currentValue, currentValue, pendingOperation!!)
            if (result == null) {
                setError("Cannot divide by zero")
                return
            }
            storedValue = result
            currentInput = formatNumber(result)
        } else {
            storedValue = currentValue
        }

        pendingOperation = operation
        resetInput = true
        justEvaluated = false
    }

    private fun evaluate() {
        if (errorMessage != null) {
            clear()
            return
        }

        val operation = pendingOperation ?: return
        val left = storedValue ?: currentInput.toDoubleOrNull() ?: return
        val right = currentInput.toDoubleOrNull() ?: return
        val result = calculate(left, right, operation)
        if (result == null) {
            setError("Cannot divide by zero")
            return
        }

        currentInput = formatNumber(result)
        storedValue = null
        pendingOperation = null
        resetInput = true
        justEvaluated = true
    }

    private fun calculate(left: Double, right: Double, operation: CalculatorOperation): Double? =
        when (operation) {
            CalculatorOperation.Add -> left + right
            CalculatorOperation.Subtract -> left - right
            CalculatorOperation.Multiply -> left * right
            CalculatorOperation.Divide -> if (right == 0.0) null else left / right
        }

    private fun setError(message: String) {
        currentInput = "Error"
        storedValue = null
        pendingOperation = null
        resetInput = true
        justEvaluated = false
        errorMessage = message
        syncState()
    }

    private fun syncState() {
        uiState = CalculatorUiState(
            display = currentInput,
            expression = errorMessage ?: buildExpression(),
            platformName = uiState.platformName,
            isError = errorMessage != null,
        )
    }

    private fun buildExpression(): String {
        val operation = pendingOperation ?: return if (justEvaluated) "Result" else "Ready"
        val left = storedValue?.let(::formatNumber) ?: "0"
        return "$left ${operation.symbol}"
    }
}

internal fun formatNumber(value: Double): String {
    if (!value.isFinite()) return "Error"
    val rounded = value.roundToLong().toDouble()
    if (abs(value - rounded) < 1e-10) {
        return rounded.roundToLong().toString()
    }

    val raw = value.toString()
    return raw.removeSuffix(".0")
}
