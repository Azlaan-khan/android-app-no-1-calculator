package com.example.app1_cally;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplay;
    private double firstValue = Double.NaN;
    private String currentOperator = "";
    private boolean isNewOperation = true;
    private DecimalFormat decimalFormat = new DecimalFormat("#.##########");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);

        // Number Buttons
        int[] numberIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};

        for (int id : numberIds) {
            findViewById(id).setOnClickListener(v -> onNumberClick(((MaterialButton) v).getText().toString()));
        }

        // Dot Button
        findViewById(R.id.btnDot).setOnClickListener(v -> onDotClick());

        // Operator Buttons
        findViewById(R.id.btnAdd).setOnClickListener(v -> onOperatorClick("+"));
        findViewById(R.id.btnSubtract).setOnClickListener(v -> onOperatorClick("−"));
        findViewById(R.id.btnMultiply).setOnClickListener(v -> onOperatorClick("×"));
        findViewById(R.id.btnDivide).setOnClickListener(v -> onOperatorClick("÷"));
        findViewById(R.id.btnPercent).setOnClickListener(v -> onPercentClick());

        // Special Buttons
        findViewById(R.id.btnEquals).setOnClickListener(v -> calculateResult());
        findViewById(R.id.btnClear).setOnClickListener(v -> clearCalculator());
        findViewById(R.id.btnDelete).setOnClickListener(v -> deleteLastCharacter());
    }

    private void onNumberClick(String number) {
        if (isNewOperation || tvDisplay.getText().toString().equals("0") || tvDisplay.getText().toString().equals("Error")) {
            tvDisplay.setText(number);
            isNewOperation = false;
        } else {
            tvDisplay.append(number);
        }
    }

    private void onDotClick() {
        if (isNewOperation) {
            tvDisplay.setText("0.");
            isNewOperation = false;
        } else if (!tvDisplay.getText().toString().contains(".")) {
            tvDisplay.append(".");
        }
    }

    private void onOperatorClick(String operator) {
        try {
            String currentText = tvDisplay.getText().toString();
            if (currentText.equals("Error")) return;

            firstValue = Double.parseDouble(currentText);
            currentOperator = operator;
            isNewOperation = true;
        } catch (NumberFormatException e) {
            tvDisplay.setText("Error");
        }
    }

    private void onPercentClick() {
        try {
            String currentText = tvDisplay.getText().toString();
            if (currentText.equals("Error") || currentText.equals("0")) return;
            
            double value = Double.parseDouble(currentText) / 100;
            tvDisplay.setText(decimalFormat.format(value));
            isNewOperation = true;
        } catch (NumberFormatException e) {
            tvDisplay.setText("Error");
        }
    }

    private void calculateResult() {
        if (Double.isNaN(firstValue) || currentOperator.isEmpty()) return;

        try {
            double secondValue = Double.parseDouble(tvDisplay.getText().toString());
            double result = 0;

            switch (currentOperator) {
                case "+": result = firstValue + secondValue; break;
                case "−": result = firstValue - secondValue; break;
                case "×": result = firstValue * secondValue; break;
                case "÷":
                    if (secondValue != 0) {
                        result = firstValue / secondValue;
                    } else {
                        tvDisplay.setText("Error");
                        firstValue = Double.NaN;
                        currentOperator = "";
                        isNewOperation = true;
                        return;
                    }
                    break;
            }

            tvDisplay.setText(decimalFormat.format(result));
            firstValue = Double.NaN;
            currentOperator = "";
            isNewOperation = true;
        } catch (NumberFormatException e) {
            tvDisplay.setText("Error");
        }
    }

    private void deleteLastCharacter() {
        String currentText = tvDisplay.getText().toString();
        if (currentText.length() > 0 && !currentText.equals("0") && !currentText.equals("Error")) {
            if (currentText.length() == 1) {
                tvDisplay.setText("0");
            } else {
                tvDisplay.setText(currentText.substring(0, currentText.length() - 1));
            }
        }
    }

    private void clearCalculator() {
        tvDisplay.setText("0");
        firstValue = Double.NaN;
        currentOperator = "";
        isNewOperation = true;
    }
}