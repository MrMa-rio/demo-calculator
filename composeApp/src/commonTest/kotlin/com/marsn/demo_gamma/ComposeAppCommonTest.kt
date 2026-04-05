package com.marsn.demo_gamma

import kotlin.test.Test
import kotlin.test.assertEquals

class ComposeAppCommonTest {

    @Test
    fun addsValuesAndFormatsResult() {
        val calculator = CalculatorStateHolder(platform = object : Platform {
            override val name: String = "Test"
        })

        calculator.onAction(CalculatorAction.Digit(1))
        calculator.onAction(CalculatorAction.Operation(CalculatorOperation.Add))
        calculator.onAction(CalculatorAction.Digit(2))
        calculator.onAction(CalculatorAction.Equals)

        assertEquals("3", calculator.uiState.display)
        assertEquals("Result", calculator.uiState.expression)
    }

    @Test
    fun handlesDecimalPercentAndSign() {
        val calculator = CalculatorStateHolder(platform = object : Platform {
            override val name: String = "Test"
        })

        calculator.onAction(CalculatorAction.Digit(5))
        calculator.onAction(CalculatorAction.Decimal)
        calculator.onAction(CalculatorAction.Digit(5))
        calculator.onAction(CalculatorAction.Percent)
        calculator.onAction(CalculatorAction.ToggleSign)

        assertEquals("-0.055", calculator.uiState.display)
    }

    @Test
    fun showsErrorForDivisionByZero() {
        val calculator = CalculatorStateHolder(platform = object : Platform {
            override val name: String = "Test"
        })

        calculator.onAction(CalculatorAction.Digit(8))
        calculator.onAction(CalculatorAction.Operation(CalculatorOperation.Divide))
        calculator.onAction(CalculatorAction.Digit(0))
        calculator.onAction(CalculatorAction.Equals)

        assertEquals("Error", calculator.uiState.display)
        assertEquals("Cannot divide by zero", calculator.uiState.expression)
    }
}
