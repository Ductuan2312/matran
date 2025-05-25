//matran - A matrix manipulation application
//        Copyright (c) 2025 Đức Tuân
//
//        Licensed under the MIT License. See LICENSE file in the project root for full license information.
package com.example.matran.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.matran.R;
import com.example.matran.Model.CalculationRecord;
import com.example.matran.Model.HistoryModel;
import com.example.matran.Model.MatrixModel;
import com.example.matran.Model.OperationModel;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

public class MatrixInputActivity extends AppCompatActivity {

    // UI Components
    private Toolbar toolbar;
    private TextInputEditText rowsInput;
    private TextInputEditText columnsInput;
    private Button applyDimensionsButton;
    private TableLayout matrixInputTable;
    private Button calculateButton;

    // For second matrix if needed (e.g., addition, multiplication)
    private TableLayout secondMatrixInputTable;
    private TextView secondMatrixLabel;

    // For constants vector (right-hand side of linear system)
    private MaterialCardView constantsCard;
    private TableLayout constantsInputTable;

    // Operation info
    private String operationType;
    private String operationTitle;
    private boolean needsSecondMatrix = false;
    private boolean isLinearSystem = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix_input);

        // Get operation information from intent
        operationType = getIntent().getStringExtra("operation_type");
        operationTitle = getIntent().getStringExtra("operation_title");

        // Initialize UI components
        toolbar = findViewById(R.id.toolbar);
        rowsInput = findViewById(R.id.rows_input);
        columnsInput = findViewById(R.id.columns_input);
        applyDimensionsButton = findViewById(R.id.apply_dimensions_button);
        matrixInputTable = findViewById(R.id.matrix_input_table);
        calculateButton = findViewById(R.id.calculate_button);

        // Optional UI components for second matrix
        secondMatrixInputTable = findViewById(R.id.second_matrix_input_table);
        secondMatrixLabel = findViewById(R.id.second_matrix_label);

        // Components for constants vector
        constantsCard = findViewById(R.id.constants_card);
        constantsInputTable = findViewById(R.id.constants_input_table);

        // Set toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(operationTitle != null ? operationTitle : "Matrix Input");

        // Determine if we need a second matrix or constants vector
        determineOperationType();

        // Set up button listeners
        applyDimensionsButton.setOnClickListener(this::onApplyDimensionsClick);
        calculateButton.setOnClickListener(this::onCalculateClick);

        // Create initial matrix input fields
        createMatrixInputFields(3, 3);

        // Create second matrix input fields if needed
        if (needsSecondMatrix && !isLinearSystem) {
            createSecondMatrixInputFields(3, 3);
        } else {
            // Hide second matrix UI elements
            if (secondMatrixInputTable != null) {
                secondMatrixInputTable.setVisibility(View.GONE);
            }
            if (secondMatrixLabel != null) {
                secondMatrixLabel.setVisibility(View.GONE);
            }
        }

        // Create constants vector input fields if needed
        if (isLinearSystem) {
            constantsCard.setVisibility(View.VISIBLE);
            createConstantsInputFields(3);
        } else {
            constantsCard.setVisibility(View.GONE);
        }
    }

    /**
     * Determine operation type and set up UI accordingly
     */
    private void determineOperationType() {
        if (operationType == null) {
            operationType = "MATRIX_INPUT";
        }

        switch (operationType) {
            case "ADD":
            case "SUBTRACT":
            case "MULTIPLY":
            case "CONVOLUTION":
                needsSecondMatrix = true;
                isLinearSystem = false;
                break;
            case "LINEAR_SYSTEM":
                isLinearSystem = true;
                needsSecondMatrix = false; // Sẽ sử dụng bảng nhập riêng cho vế phải
                break;
            default:
                needsSecondMatrix = false;
                isLinearSystem = false;
        }
    }

    /**
     * Handle apply dimensions button click
     */
    private void onApplyDimensionsClick(View view) {
        try {
            int rows = Integer.parseInt(rowsInput.getText().toString());
            int columns = Integer.parseInt(columnsInput.getText().toString());

            // Validate dimensions
            if (rows <= 0 || columns <= 0 || rows > 10 || columns > 10) {
                Toast.makeText(this, "Dimensions must be between 1 and 10", Toast.LENGTH_SHORT).show();
                return;
            }

            // Special handling for determinant and inverse operations
            if (("DETERMINANT".equals(operationType) || "INVERSE".equals(operationType)) && rows != columns) {
                Toast.makeText(this, "Matrix must be square for this operation", Toast.LENGTH_SHORT).show();
                columnsInput.setText(String.valueOf(rows));
                columns = rows;
            }

            // Create matrix input fields
            createMatrixInputFields(rows, columns);

            // Create second matrix if needed
            if (needsSecondMatrix && !isLinearSystem) {
                int rows2 = rows;
                int cols2 = columns;

                // For multiplication, the second matrix columns can be different
                if ("MULTIPLY".equals(operationType)) {
                    cols2 = columns; // Different variable in case we need to adjust
                }

                createSecondMatrixInputFields(rows2, cols2);
            }

            // Create constants vector if needed for linear system
            if (isLinearSystem) {
                if (rows != columns) {
                    Toast.makeText(this, "For linear system, matrix must be square (number of equations = number of unknowns)", Toast.LENGTH_LONG).show();
                    columnsInput.setText(String.valueOf(rows));
                    columns = rows;
                }
                createConstantsInputFields(rows);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Create matrix input fields in the table
     */
    private void createMatrixInputFields(int rows, int columns) {
        matrixInputTable.removeAllViews();

        for (int i = 0; i < rows; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < columns; j++) {
                EditText editText = new EditText(this);
                editText.setTag("cell_" + i + "_" + j);
                editText.setHint("0");
                editText.setText("0");
                editText.setTextSize(14);
                editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER
                        | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
                        | android.text.InputType.TYPE_NUMBER_FLAG_SIGNED);

                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                params.setMargins(5, 5, 5, 5);
                editText.setLayoutParams(params);

                tableRow.addView(editText);
            }

            matrixInputTable.addView(tableRow);
        }
    }

    /**
     * Create second matrix input fields in the table
     */
    private void createSecondMatrixInputFields(int rows, int columns) {
        if (secondMatrixInputTable == null) return;

        secondMatrixInputTable.setVisibility(View.VISIBLE);
        if (secondMatrixLabel != null) {
            secondMatrixLabel.setVisibility(View.VISIBLE);
        }

        secondMatrixInputTable.removeAllViews();

        for (int i = 0; i < rows; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < columns; j++) {
                EditText editText = new EditText(this);
                editText.setTag("cell2_" + i + "_" + j);
                editText.setHint("0");
                editText.setText("0");
                editText.setTextSize(14);
                editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER
                        | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
                        | android.text.InputType.TYPE_NUMBER_FLAG_SIGNED);

                TableRow.LayoutParams params = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT);
                params.setMargins(5, 5, 5, 5);
                editText.setLayoutParams(params);

                tableRow.addView(editText);
            }

            secondMatrixInputTable.addView(tableRow);
        }
    }

    /**
     * Create constants vector input fields (RHS of linear system)
     */
    private void createConstantsInputFields(int rows) {
        if (constantsInputTable == null) return;

        constantsInputTable.removeAllViews();

        for (int i = 0; i < rows; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            // Only one column for constants vector
            EditText editText = new EditText(this);
            editText.setTag("constant_" + i);
            editText.setHint("0");
            editText.setText("0");
            editText.setTextSize(14);
            editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER
                    | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
                    | android.text.InputType.TYPE_NUMBER_FLAG_SIGNED);

            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 5, 5, 5);
            editText.setLayoutParams(params);

            tableRow.addView(editText);
            constantsInputTable.addView(tableRow);
        }
    }

    /**
     * Read constants vector from input fields
     */
    private MatrixModel readConstantsFromInputs() {
        try {
            int rows = constantsInputTable.getChildCount();
            if (rows == 0) return null;

            MatrixModel constants = new MatrixModel(rows, 1); // Vector vế phải luôn có 1 cột

            for (int i = 0; i < rows; i++) {
                TableRow row = (TableRow) constantsInputTable.getChildAt(i);
                EditText cell = (EditText) row.getChildAt(0); // Chỉ có 1 phần tử trong mỗi hàng
                String value = cell.getText().toString();
                if (TextUtils.isEmpty(value) || value.equals("-")) {
                    constants.setValue(i, 0, 0);
                } else {
                    constants.setValue(i, 0, Double.parseDouble(value));
                }
            }

            return constants;
        } catch (Exception e) {
            Toast.makeText(this, "Error reading constants: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     * Read matrix from input fields
     */
    private MatrixModel readMatrixFromInputs(boolean isSecondMatrix) {
        try {
            int rows = matrixInputTable.getChildCount();
            if (rows == 0) return null;

            int columns = ((TableRow) matrixInputTable.getChildAt(0)).getChildCount();
            MatrixModel matrix = new MatrixModel(rows, columns);

            TableLayout table = isSecondMatrix ? secondMatrixInputTable : matrixInputTable;
            String cellPrefix = isSecondMatrix ? "cell2_" : "cell_";

            for (int i = 0; i < rows; i++) {
                TableRow row = (TableRow) table.getChildAt(i);
                for (int j = 0; j < columns; j++) {
                    EditText cell = (EditText) row.getChildAt(j);
                    String value = cell.getText().toString();
                    if (TextUtils.isEmpty(value) || value.equals("-")) {
                        matrix.setValue(i, j, 0);
                    } else {
                        matrix.setValue(i, j, Double.parseDouble(value));
                    }
                }
            }

            return matrix;
        } catch (Exception e) {
            Toast.makeText(this, "Error reading matrix: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     * Handle calculate button click
     */
    /**
     * Handle calculate button click
     */
    private void onCalculateClick(View view) {
        // Read matrix from input fields
        MatrixModel matrixA = readMatrixFromInputs(false);
        MatrixModel matrixB = needsSecondMatrix ? readMatrixFromInputs(true) : null;

        // Read constants for linear system
        MatrixModel constants = isLinearSystem ? readConstantsFromInputs() : null;

        if (matrixA == null) {
            Toast.makeText(this, "Error reading matrix values", Toast.LENGTH_SHORT).show();
            return;
        }

        if (needsSecondMatrix && matrixB == null && !isLinearSystem) {
            Toast.makeText(this, "Error reading second matrix values", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isLinearSystem && constants == null) {
            Toast.makeText(this, "Error reading constants vector", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Perform the requested operation with steps
            CalculationRecord record = null;

            switch (operationType) {
                case "ADD":
                    record = OperationModel.addWithSteps(matrixA, matrixB);
                    break;
                case "SUBTRACT":
                    record = OperationModel.subtractWithSteps(matrixA, matrixB);
                    break;
                case "MULTIPLY":
                    record = OperationModel.multiplyWithSteps(matrixA, matrixB);
                    break;
                case "TRANSPOSE":
                    record = OperationModel.transposeWithSteps(matrixA);
                    break;
                case "DETERMINANT":
                    record = OperationModel.determinantWithSteps(matrixA);
                    break;
                case "EIGENVALUES":
                    record = OperationModel.eigenvaluesWithSteps(matrixA);
                    break;
                case "INVERSE":
                    record = OperationModel.inverseWithSteps(matrixA);
                    break;
                case "CONVOLUTION":
                    record = OperationModel.convolutionWithSteps(matrixA, matrixB);
                    break;
                case "SVD":
                    record = OperationModel.svdWithSteps(matrixA);
                    break;
                case "RANK":
                    record = OperationModel.rankWithSteps(matrixA);
                    break;
                case "LINEAR_SYSTEM":
                    record = OperationModel.solveLinearSystemWithSteps(matrixA, constants);
                    break;
                default:
                    // Just show the input matrix
                    record = new CalculationRecord(operationTitle, matrixA, matrixA);
                    break;
            }

            // Lưu vào lịch sử
            HistoryModel historyModel = new HistoryModel(this);
            historyModel.addRecord(record);

            // Navigate to result screen
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("calculation_record", record);

            // Xác định nếu kết quả là giá trị scalar (kết quả đơn lẻ)
            boolean isScalarResult = false;
            double scalarResult = 0;

            if (operationType.equals("DETERMINANT") || operationType.equals("RANK")) {
                isScalarResult = true;
                MatrixModel result = record.getResultMatrix();
                scalarResult = result.getValue(0, 0);
            }

            intent.putExtra("is_scalar_result", isScalarResult);
            intent.putExtra("scalar_result", scalarResult);
            intent.putExtra("is_linear_system", isLinearSystem);
            startActivity(intent);

        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Calculation error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (UnsupportedOperationException e) {
            Toast.makeText(this, "Operation not supported: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle back button on toolbar
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}