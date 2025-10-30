package com.example.simplecalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.simplecalculator.databinding.FragmentMainBinding

class MainFragment: Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = requireNotNull(_binding)
    private val calculator = Calculator()
    private var expression = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding
            .inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val numberButtons = listOf(
            binding.zero, binding.one, binding.two, binding.three,
            binding.four, binding.five, binding.six,
            binding.seven, binding.eight, binding.nine
        )

        // обработка цифр
        numberButtons.forEach { button ->
            button.setOnClickListener {
                expression += button.text
                binding.result.text = expression
            }
        }

        // обработка операторов
        val operators = listOf(
            binding.plus, binding.minus, binding.multiply, binding.divison
        )
        operators.forEach { button ->
            button.setOnClickListener {
                // не даём вставлять подряд два оператора
                if (expression.isNotEmpty() && expression.last() !in "+-*/") {
                    expression += button.text
                    binding.result.text = expression
                }
            }
        }

        // очистка
        binding.deleteAll.setOnClickListener {
            expression = ""
            binding.result.text = "0"
        }

        // backspace
        binding.back.setOnClickListener {
            if (expression.isNotEmpty()) {
                expression = expression.dropLast(1)
                binding.result.text = if (expression.isEmpty()) "0" else expression
            }
        }

        // =
        binding.equals.setOnClickListener {
            try {
                val result = calculator.evaluate(expression)
                binding.result.text = result.toString()
                expression = result.toString()
            } catch (e: Exception) {
                binding.result.text = "Ошибка"
                expression = ""
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}