package com.example.basiccalculator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

class CalculatorViewModel : ViewModel() {

    private val _equationText = MutableLiveData("")
    val equationText: LiveData<String> = _equationText

    private val _resultText = MutableLiveData("0")
    val resultText: LiveData<String> = _resultText

    private var openParenthesesCount = 0

    fun onButtonClick(btn: String) {
        _equationText.value?.let {
            when (btn) {
                "AC" -> {
                    _equationText.value = ""
                    _resultText.value = "0"
                    openParenthesesCount = 0
                }
                "clr" -> {
                    if (it.isNotEmpty()) {
                        if (it.last() == '(') {
                            openParenthesesCount--
                        } else if (it.last() == ')') {
                            openParenthesesCount++
                        }
                        _equationText.value = it.substring(0, it.length - 1)
                    }
                }
                "=" -> {
                    try {
                        val result = calculateResult(_equationText.value.toString())
                        _resultText.value = result
                        _equationText.value = result
                    } catch (e: Exception) {
                        _resultText.value = "Error"
                    }
                }
                "( )" -> {
                    _equationText.value = it + if (shouldAddOpenParenthesis(it)) "(" else ")"
                }
                else -> {
                    _equationText.value = it + btn
                }
            }
        }
    }

    private fun shouldAddOpenParenthesis(equation: String): Boolean {
        return if (equation.isEmpty()) {
            openParenthesesCount++
            true
        } else {
            val lastChar = equation.last()
            if (lastChar.isDigit() || lastChar == ')') {
                if (openParenthesesCount > 0) {
                    openParenthesesCount--
                    false
                } else {
                    openParenthesesCount++
                    true
                }
            } else {
                openParenthesesCount++
                true
            }
        }
    }

    private fun calculateResult(equation: String): String {
        val context: Context = Context.enter()
        context.optimizationLevel = -1
        val scriptable: Scriptable = context.initStandardObjects()

        var modifiedEquation = equation.replace("÷", "/").replace("x", "*")

        // Handle percentages in the format "number%number"
        val percentageRegex = """(\d+(\.\d+)?)%(\d+(\.\d+)?)""".toRegex()
        modifiedEquation = percentageRegex.replace(modifiedEquation) { matchResult ->
            val (firstNum, _, secondNum) = matchResult.destructured
            "(${firstNum.toDouble() / 100} * $secondNum)"
        }

        var finalResult = context.evaluateString(scriptable, modifiedEquation, "Javascript", 1, null).toString()
        if (finalResult.endsWith(".0")) {
            finalResult = finalResult.replace(".0", "")
        }

        return finalResult
    }
}
