package com.nsiva.odin.dev.tipsy

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView

private const val INITIAL_SPLIT_NUMBER = 1
private const val INITIAL_TIP_AMOUNT = 0.0
private const val INITIAL_TOTAL_AMOUNT = 0.0

class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText
    private lateinit var etTipPercentage: EditText
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var tvShareAmount: TextView
    private lateinit var tvSplit: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBaseAmount = findViewById(R.id.etBaseAmount)
        etTipPercentage = findViewById(R.id.etTipPercentage)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        seekBar = findViewById(R.id.seekBar)
        tvShareAmount = findViewById(R.id.tvShareAmount)
        tvSplit = findViewById(R.id.tvSplit)
        var split = 1

        tvSplit.text = "Split by $INITIAL_SPLIT_NUMBER"
        tvTipAmount.text = "$INITIAL_TIP_AMOUNT"
        tvTotalAmount.text = "$INITIAL_TOTAL_AMOUNT"
        tvShareAmount.text = "$INITIAL_TOTAL_AMOUNT"

        etBaseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "Crashed")
                calculateTotalAmount(split)
            }
        })
        etTipPercentage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                calculateTotalAmount(split)
            }
        })

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChanged $p1")
                if (p1 <= 1) {
                    split = 1
                }
                split = p1
                shareAmount(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
    }

    private fun shareAmount(splitNumber: Int) {
        var split = splitNumber
        if (split < 1) split = 1
        tvSplit.text = "Split by $split"
        calculateTotalAmount(split)
        if (etBaseAmount.text.isEmpty() || etTipPercentage.text.isEmpty()) {
            tvShareAmount.text = ""
            tvTotalAmount.text = "$INITIAL_TOTAL_AMOUNT"
            tvTipAmount.text = "$INITIAL_TIP_AMOUNT"
            tvShareAmount.text = "$INITIAL_TOTAL_AMOUNT"
            tvShareAmount.text = "$INITIAL_TOTAL_AMOUNT"
            return
        }
        val shareAmountPerPerson =
            "%.2f".format(tvTotalAmount.text.toString().toDouble() / split)
        tvShareAmount.text = "$shareAmountPerPerson"
    }

    private fun calculateTotalAmount(split: Int) {
        if (etBaseAmount.text.isEmpty() || etTipPercentage.text.isEmpty()) {
            tvShareAmount.text = ""
            tvTotalAmount.text = ""
            tvTipAmount.text = ""
            tvShareAmount.text = ""
            return
        }
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipAmount = etTipPercentage.text.toString().toDouble() * baseAmount / 100
        val totalAmount = baseAmount + tipAmount
        tvTotalAmount.text = "%.2f".format(totalAmount)
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvShareAmount.text = "%.2f".format(totalAmount / split)
    }
}