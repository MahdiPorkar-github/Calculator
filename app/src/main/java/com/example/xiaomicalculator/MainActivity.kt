package com.example.xiaomicalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.Toast
import com.example.xiaomicalculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onNumberClicked()
        onOperatorClicked()


    }


    private fun onNumberClicked() {

        binding.btn0.setOnClickListener {

            if (binding.txtExpression.text.isNotEmpty()) {
                appendText("0")
            }
        }
        binding.btn1.setOnClickListener {
            appendText("1")
        }
        binding.btn2.setOnClickListener {
            appendText("2")
        }
        binding.btn3.setOnClickListener {
            appendText("3")
        }
        binding.btn4.setOnClickListener {
            appendText("4")
        }
        binding.btn5.setOnClickListener {
            appendText("5")
        }
        binding.btn6.setOnClickListener {
            appendText("6")
        }
        binding.btn7.setOnClickListener {
            appendText("7")
        }
        binding.btn8.setOnClickListener {
            appendText("8")
        }
        binding.btn9.setOnClickListener {
            appendText("9")
        }
        binding.btnDot.setOnClickListener {


            var appendingText = ""
            if (binding.txtExpression.text.isNotEmpty()) {
                val charBeforeDot = binding.txtExpression.text.toString().last().toString()

                appendingText = try {
                    charBeforeDot.toInt()
                    "."
                } catch (e: Exception) {
                    "0."
                }
            } else {
                appendingText = "0."
            }
            appendText(appendingText)


//            if (binding.txtExpression.text.isEmpty() || binding.txtAnswer.text.isNotEmpty()) {
//                appendingText = "0."
//            } else if (!binding.txtExpression.text.toString().contains(".")) {
//                appendingText = "."
//            }
//
//            appendText(appendingText)
        }
    }


    private fun checkOperator(operator: String) {


        if (binding.txtExpression.text.isNotEmpty() && binding.txtAnswer.text.isEmpty()) {

            if (isOperationValid(binding.txtExpression.text.toString())) {
                appendText(operator)
            } else {
                Toast.makeText(
                    this,
                    "can not use two operators in a row or at the start of a Expression ",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }


    private fun onOperatorClicked() {

        binding.btnAdd.setOnClickListener {

            checkOperator("+")
        }
        binding.btnMines.setOnClickListener {

            checkOperator("-")
        }
        binding.btnMultiply.setOnClickListener {
            checkOperator("*")
        }
        binding.btnDivide.setOnClickListener {
            checkOperator("/")
        }
        binding.btnSign.setOnClickListener {
            appendParentheses(binding.txtExpression.text.toString())
            appendText("-")
        }
        binding.btnParenthesis.setOnClickListener {

            appendParentheses(binding.txtExpression.text.toString())
        }

        binding.btnAC.setOnClickListener {

            binding.txtAnswer.text = ""
            binding.txtExpression.text = ""
        }
        binding.btnDelete.setOnClickListener {

            val currentText = binding.txtExpression.text.toString()
            if (currentText.isNotEmpty()) {

                binding.txtExpression.text = currentText.substring(0, currentText.length - 1)
            }
        }

        binding.btnEquals.setOnClickListener {

            if (binding.txtExpression.text.isNotEmpty()) {


                if (isOperationValid(binding.txtExpression.text.toString())) {

                    while (checkParentheses(binding.txtExpression.text.toString()) > 0) {
                        binding.txtExpression.append(" )")
                    }

                    val expression =
                        ExpressionBuilder(binding.txtExpression.text.toString()).build()
                    val result = expression.evaluate()

                    val longResult = result.toLong()

                    if (result == longResult.toDouble()) {
                        binding.txtAnswer.text = longResult.toString()
                    } else {
                        binding.txtAnswer.text = result.toString()
                    }

                } else {
                    Toast.makeText(this, "operator needs second operand ", Toast.LENGTH_SHORT).show()
                }
            }

        }


    }

    private fun appendText(inputText: String) {

        try {

            if (binding.txtAnswer.text.isNotEmpty()) {
                binding.txtExpression.text = ""
            }
            binding.txtAnswer.text = ""
            binding.txtExpression.append(inputText)


            val viewTree: ViewTreeObserver =
                binding.txtExpressionHorizontalScrollView.viewTreeObserver
            viewTree.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding.txtExpressionHorizontalScrollView.viewTreeObserver.removeOnGlobalLayoutListener(
                        this
                    )
                    binding.txtExpressionHorizontalScrollView.scrollTo(
                        binding.txtExpression.width,
                        0
                    )

                }
            })

        } catch (e: Exception) {
            Toast.makeText(this, "There was an error,Check the operation ", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun appendParentheses(inputValue: String) {

        var count = checkParentheses(binding.txtExpression.text.toString())

        when {
            binding.txtExpression.text.isEmpty() -> {
                binding.txtExpression.append("( ")
            }
            binding.txtExpression.text.toString().last() == ' ' -> {
                binding.txtExpression.append("( ")
            }
            count > 0 -> {
                binding.txtExpression.append(" )")

            }
            count <= 0 -> {
                binding.txtExpression.append("( ")
            }
        }
    }

    private fun checkParentheses(inputValue: String): Int {

        var countOpen = 0
        var countClose = 0
        for (element in inputValue) {
            if (element == '(') {
                countOpen++
            } else if (element == ')') {
                countClose++
            }
        }

        return countOpen - countClose
    }


    private fun isOperationValid(inputText: String): Boolean {
        val lastChar = binding.txtExpression.text.last()
        if (lastChar != '+' && lastChar != '-' && lastChar != '*' && lastChar != '/' && lastChar != ' ') {
            return true
        }
        return false
    }

}