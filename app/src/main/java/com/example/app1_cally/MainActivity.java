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
        String currentText = tvDisplay.getText().toString();
        if (isNewOperation || currentText.equals("0") || currentText.equals("Error")) {
            tvDisplay.setText(number);
            isNewOperation = false;
        } else {
            tvDisplay.append(number);
        }
    }

    private void onDotClick() {
        String currentText = tvDisplay.getText().toString();
        if (isNewOperation) {
            tvDisplay.setText("0.");
            isNewOperation = false;
        } else {
            String[] parts = currentText.split(" ");
            String lastPart = parts[parts.length - 1];
            if (!lastPart.contains(".")) {
                if (lastPart.isEmpty()) tvDisplay.append("0.");
                else tvDisplay.append(".");
            }
        }
    }

    private void onOperatorClick(String operator) {
        String currentText = tvDisplay.getText().toString();
        if (currentText.equals("Error")) return;

        try {
            String[] parts = currentText.split(" ");
            if (parts.length == 3) {
                calculateResult();
                currentText = tvDisplay.getText().toString();
            }

            firstValue = Double.parseDouble(currentText);
            currentOperator = operator;
            tvDisplay.setText(decimalFormat.format(firstValue) + " " + operator + " ");
            isNewOperation = false;
        } catch (NumberFormatException e) {
            if (currentText.contains(" ")) {
                currentOperator = operator;
                String base = currentText.substring(0, currentText.indexOf(" "));
                tvDisplay.setText(base + " " + operator + " ");
            }
        }
    }

    private void onPercentClick() {
        try {
            String currentText = tvDisplay.getText().toString();
            if (currentText.equals("Error") || currentText.equals("0")) return;

            String[] parts = currentText.split(" ");
            String lastPart = parts[parts.length - 1];
            
            if (lastPart.isEmpty()) return; // Nothing to apply percent to

            double value = Double.parseDouble(lastPart) / 100;
            String formattedValue = decimalFormat.format(value);
            
            if (parts.length == 1) {
                tvDisplay.setText(formattedValue);
                isNewOperation = true;
            } else {
                // Replace the last part with the percentage value
                String base = currentText.substring(0, currentText.lastIndexOf(" ") + 1);
                tvDisplay.setText(base + formattedValue);
            }
        } catch (NumberFormatException e) {
            tvDisplay.setText("Error");
        }
    }

    private void calculateResult() {
        if (Double.isNaN(firstValue) || currentOperator.isEmpty()) return;

        String currentText = tvDisplay.getText().toString();
        try {
            String[] parts = currentText.split(" ");
            if (parts.length < 3) return;

            double secondValue = Double.parseDouble(parts[2]);
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
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            tvDisplay.setText("Error");
        }
    }

    private void deleteLastCharacter() {
        String currentText = tvDisplay.getText().toString();
        if (currentText.isEmpty() || currentText.equals("0") || currentText.equals("Error")) {
            tvDisplay.setText("0");
            return;
        }

        if (currentText.endsWith(" ")) {
            // Remove " + " (3 characters)
            tvDisplay.setText(currentText.substring(0, currentText.length() - 3));
            currentOperator = "";
            firstValue = Double.NaN;
        } else {
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