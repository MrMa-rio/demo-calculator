package com.marsn.demo_gamma

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AppBackgroundTop = Color(0xFF081B33)
private val AppBackgroundBottom = Color(0xFF153E75)
private val PanelColor = Color(0xFFF5F7FB)
private val AccentColor = Color(0xFFFF8C42)
private val AccentSecondary = Color(0xFFFFB067)
private val UtilityColor = Color(0xFFD9E2F2)
private val DigitColor = Color(0xFFFFFFFF)
private val DisplayColor = Color(0xFF10233D)
private val MutedText = Color(0xFF62748A)
private val ErrorColor = Color(0xFFB3261E)

@Composable
@Preview
fun App() {
    val platform = remember { getPlatform() }
    val calculator = remember { CalculatorStateHolder(platform) }
    val uiState = calculator.uiState

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(AppBackgroundTop, AppBackgroundBottom),
                        )
                    )
                    .safeDrawingPadding()
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.Center,
            ) {
                Card(
                    modifier = Modifier
                        .sizeIn(maxWidth = 440.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = PanelColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Text(
                            text = "Calculator KMP",
                            style = MaterialTheme.typography.headlineMedium,
                            color = DisplayColor,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "Shared UI running on ${uiState.platformName}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MutedText,
                        )

                        Card(
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 18.dp),
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Text(
                                    text = uiState.expression,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (uiState.isError) ErrorColor else MutedText,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                                Text(
                                    text = uiState.display,
                                    style = MaterialTheme.typography.displaySmall,
                                    color = if (uiState.isError) ErrorColor else DisplayColor,
                                    fontSize = 42.sp,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }

                        CalculatorButtonRow(
                            buttons = listOf(
                                CalculatorButtonSpec("AC", UtilityColor) { calculator.onAction(CalculatorAction.Clear) },
                                CalculatorButtonSpec("+/-", UtilityColor) { calculator.onAction(CalculatorAction.ToggleSign) },
                                CalculatorButtonSpec("%", UtilityColor) { calculator.onAction(CalculatorAction.Percent) },
                                CalculatorButtonSpec("/", AccentSecondary) {
                                    calculator.onAction(CalculatorAction.Operation(CalculatorOperation.Divide))
                                },
                            ),
                        )
                        CalculatorButtonRow(
                            buttons = listOf(
                                CalculatorButtonSpec("7", DigitColor) { calculator.onAction(CalculatorAction.Digit(7)) },
                                CalculatorButtonSpec("8", DigitColor) { calculator.onAction(CalculatorAction.Digit(8)) },
                                CalculatorButtonSpec("9", DigitColor) { calculator.onAction(CalculatorAction.Digit(9)) },
                                CalculatorButtonSpec("x", AccentSecondary) {
                                    calculator.onAction(CalculatorAction.Operation(CalculatorOperation.Multiply))
                                },
                            ),
                        )
                        CalculatorButtonRow(
                            buttons = listOf(
                                CalculatorButtonSpec("4", DigitColor) { calculator.onAction(CalculatorAction.Digit(4)) },
                                CalculatorButtonSpec("5", DigitColor) { calculator.onAction(CalculatorAction.Digit(5)) },
                                CalculatorButtonSpec("6", DigitColor) { calculator.onAction(CalculatorAction.Digit(6)) },
                                CalculatorButtonSpec("-", AccentSecondary) {
                                    calculator.onAction(CalculatorAction.Operation(CalculatorOperation.Subtract))
                                },
                            ),
                        )
                        CalculatorButtonRow(
                            buttons = listOf(
                                CalculatorButtonSpec("1", DigitColor) { calculator.onAction(CalculatorAction.Digit(1)) },
                                CalculatorButtonSpec("2", DigitColor) { calculator.onAction(CalculatorAction.Digit(2)) },
                                CalculatorButtonSpec("3", DigitColor) { calculator.onAction(CalculatorAction.Digit(3)) },
                                CalculatorButtonSpec("+", AccentSecondary) {
                                    calculator.onAction(CalculatorAction.Operation(CalculatorOperation.Add))
                                },
                            ),
                        )
                        CalculatorButtonRow(
                            buttons = listOf(
                                CalculatorButtonSpec("0", DigitColor, weight = 2f) { calculator.onAction(CalculatorAction.Digit(0)) },
                                CalculatorButtonSpec(".", DigitColor) { calculator.onAction(CalculatorAction.Decimal) },
                                CalculatorButtonSpec("=", AccentColor) { calculator.onAction(CalculatorAction.Equals) },
                            ),
                        )
                    }
                }
            }
        }
    }
}

private data class CalculatorButtonSpec(
    val label: String,
    val containerColor: Color,
    val weight: Float = 1f,
    val onClick: () -> Unit,
)

@Composable
private fun CalculatorButtonRow(
    buttons: List<CalculatorButtonSpec>,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        buttons.forEach { button ->
            CalculatorButton(
                label = button.label,
                containerColor = button.containerColor,
                modifier = Modifier.weight(button.weight),
                onClick = button.onClick,
            )
        }
    }
}

@Composable
private fun CalculatorButton(
    label: String,
    containerColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .clip(RoundedCornerShape(28.dp)),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = if (containerColor == DigitColor || containerColor == UtilityColor) {
                DisplayColor
            } else {
                Color.White
            },
        ),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 22.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
