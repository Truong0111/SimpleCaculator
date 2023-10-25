package com.example.simplecaculator

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.simplecaculator.databinding.ActivityMainBinding
import java.util.Timer
import java.util.TimerTask


@SuppressLint("StaticFieldLeak")
private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var canAddOperation = false
    private var canAddDecimal = true
    private var change = false

    private var finum: Int = 0
    private var op: String = ""
    private var senum: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                calculateFunction(view)
            }
        }, 0, 100)

    }


    fun numberAction(view: View) {
        if (view is Button && canAddDecimal) {
            if (finum == 0) {
                binding.upTV.text = view.text
            } else
                binding.upTV.append(view.text)
            if (change) {
                senum = senum * 10 + Integer.parseInt(view.text.toString())
            } else {
                finum = finum * 10 + Integer.parseInt(view.text.toString())
            }
            canAddOperation = true
        }
    }

    fun operationAction(view: View) {
        if (view is Button && canAddOperation) {
            binding.upTV.append(" " + view.text.toString() + " ")
            change = true
            canAddOperation = false
            op = view.text.toString()
        }
    }

    fun calculateFunction(view: View){
        val upText = binding.upTV.text.toString()
        val numbersAndOperators = upText.split(" ")

        var result = 0
        var operator = ""

        var stack = mutableListOf<Int>()

        for (item in numbersAndOperators) {
            if (item.matches("-?\\d+(\\.\\d+)?".toRegex())) {
                val number = item.toInt()
                if (operator.isNullOrBlank()) {
                    stack.add(number)
                } else {
                    when (operator) {
                        "x" -> stack[stack.size - 1] = stack.last() * number
                        "/" -> stack[stack.size - 1] = stack.last() / number
                        "+" -> stack.add(number)
                        "-" -> stack.add(-number)
                    }
                    operator = ""
                }
            } else if (item in listOf("x", "/", "+", "-")) {
                operator = item
            }
        }

        result = stack.sum()

        stack.clear()

        binding.downTV.text = result.toString()
        canAddDecimal = true
    }

    fun equalsAction(view: View) {
        if(view is Button){
            calculateFunction(view)
            val downText = binding.downTV.text.toString()
            binding.upTV.text = downText
        }
    }

    fun clearEntryAction(view: View) {
        if (change) {
            senum = 0
            binding.upTV.text = finum.toString() + op
        } else {
            finum = 0
            binding.upTV.text = "0"
        }
    }

    fun clearAction(view: View) {
        finum = 0
        op = ""
        senum = 0
        canAddDecimal = true
        canAddOperation = false
        change = false
        binding.downTV.text = ""
        binding.upTV.text = "0"
    }

    fun backSpaceAction(view: View) {
        if (change) {
            if (senum > 0) {
                senum = (senum - senum % 10) / 10
                binding.upTV.text = finum.toString() + op
            }
        } else {
            var check =
                binding.upTV.text.subSequence(binding.upTV.length() - 1, binding.upTV.length())
            if (check != "+" && check != "-" && check != "x" && check != "/") {
                finum = (finum - finum % 10) / 10
                binding.upTV.text = finum.toString()
            }
        }
    }

    fun changeDAction(view: View) {
        if (change) {
            if (senum > 0) {
                senum = 0 - senum
                binding.upTV.text = finum.toString() + op + senum.toString()
            }
        } else {
            if (finum > 0) {
                finum = 0 - finum
                binding.upTV.text = finum.toString()
            }
        }
    }
}