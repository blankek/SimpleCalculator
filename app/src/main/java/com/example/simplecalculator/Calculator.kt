package com.example.simplecalculator

class Calculator {

    fun evaluate(expression: String): Int {
        val tokens = tokenize(expression)
        val postfix = infixToPostfix(tokens)
        return evaluatePostfix(postfix)
    }

    // Разделяем строку на числа и операторы
    private fun tokenize(expr: String): List<String> {
        val tokens = mutableListOf<String>()
        var number = ""
        for (ch in expr) {
            if (ch.isDigit()) {
                number += ch
            } else if (ch in "+-*/") {
                if (number.isNotEmpty()) {
                    tokens.add(number)
                    number = ""
                }
                tokens.add(ch.toString())
            }
        }
        if (number.isNotEmpty()) tokens.add(number)
        return tokens
    }

    // Преобразование инфиксного выражения (4+5*2) → постфиксное (4 5 2 * +)
    private fun infixToPostfix(tokens: List<String>): List<String> {
        val output = mutableListOf<String>()
        val operators = ArrayDeque<String>()

        val precedence = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2)

        for (token in tokens) {
            when {
                token.all { it.isDigit() } -> output.add(token)
                token in precedence.keys -> {
                    while (operators.isNotEmpty() &&
                        precedence[operators.last()]!! >= precedence[token]!!) {
                        output.add(operators.removeLast())
                    }
                    operators.addLast(token)
                }
            }
        }
        while (operators.isNotEmpty()) output.add(operators.removeLast())
        return output
    }

    // Вычисляем постфиксное выражение
    private fun evaluatePostfix(tokens: List<String>): Int {
        val stack = ArrayDeque<Int>()
        for (token in tokens) {
            if (token.all { it.isDigit() }) {
                stack.addLast(token.toInt())
            } else {
                val b = stack.removeLast()
                val a = stack.removeLast()
                val res = when (token) {
                    "+" -> a + b
                    "-" -> a - b
                    "*" -> a * b
                    "/" -> if (b != 0) a / b else throw ArithmeticException("Divide by zero")
                    else -> 0
                }
                stack.addLast(res)
            }
        }
        return stack.last()
    }
}