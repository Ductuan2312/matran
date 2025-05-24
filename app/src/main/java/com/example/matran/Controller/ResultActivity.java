package com.example.matran.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.matran.R;
import com.example.matran.Model.CalculationRecord;
import com.example.matran.Model.HistoryModel;
import com.example.matran.Model.MatrixModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class ResultActivity extends AppCompatActivity {

    // UI Components
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private FrameLayout tabContent;
    private FloatingActionButton shareFab;

    // Data
    private CalculationRecord calculationRecord;
    private boolean isScalarResult;
    private double scalarResult;
    private HistoryModel historyModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Get data from intent
        calculationRecord = (CalculationRecord) getIntent().getSerializableExtra("calculation_record");

        // Kiểm tra nếu có thông tin scalar từ Intent hoặc xác định từ calculationRecord
        isScalarResult = getIntent().getBooleanExtra("is_scalar_result", false);
        scalarResult = getIntent().getDoubleExtra("scalar_result", 0);

        // Nếu không có thông tin scalar trong intent, kiểm tra từ calculationRecord
        if (!getIntent().hasExtra("is_scalar_result") && calculationRecord.getResultMatrix() != null) {
            MatrixModel result = calculationRecord.getResultMatrix();
            if (result.getRows() == 1 && result.getColumns() == 1 && !calculationRecord.isSVDResult()) {
                isScalarResult = true;
                scalarResult = result.getValue(0, 0);
            }
        }

        // Initialize UI components
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        tabContent = findViewById(R.id.tab_content);
        shareFab = findViewById(R.id.share_fab);

        // Set toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(calculationRecord.getOperationType() + " Result");

        // Set up tab layout
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    showResultTab();
                } else {
                    showStepsTab();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Set up share button
        shareFab.setOnClickListener(this::onShareClick);

        // Show result tab by default
        showResultTab();
    }

    /**
     * Show the result tab content
     */
    private void showResultTab() {
        // Clear current content
        tabContent.removeAllViews();

        // Inflate result view
        View resultView = getLayoutInflater().inflate(R.layout.tab_result, tabContent, false);

        TextView operationTitleText = resultView.findViewById(R.id.operation_title);
        TextView matrixALabel = resultView.findViewById(R.id.matrix_a_label);
        TableLayout matrixATable = resultView.findViewById(R.id.matrix_a_table);
        TextView matrixBLabel = resultView.findViewById(R.id.matrix_b_label);
        TableLayout matrixBTable = resultView.findViewById(R.id.matrix_b_table);
        TextView resultLabel = resultView.findViewById(R.id.result_label);
        TableLayout resultTable = resultView.findViewById(R.id.result_table);
        TextView scalarResultText = resultView.findViewById(R.id.scalar_result_text);

        // Set operation title
        operationTitleText.setText(calculationRecord.getOperationType());

        // Display Matrix A
        displayMatrix(calculationRecord.getInputMatrixA(), matrixATable);

        // Display Matrix B if it exists
        if (calculationRecord.getInputMatrixB() != null) {
            matrixBLabel.setVisibility(View.VISIBLE);
            matrixBTable.setVisibility(View.VISIBLE);
            displayMatrix(calculationRecord.getInputMatrixB(), matrixBTable);
        } else {
            matrixBLabel.setVisibility(View.GONE);
            matrixBTable.setVisibility(View.GONE);
        }

        // Display result
        if (isScalarResult) {
            resultTable.setVisibility(View.GONE);
            scalarResultText.setVisibility(View.VISIBLE);
            scalarResultText.setText(String.valueOf(scalarResult));
        } else {
            resultTable.setVisibility(View.VISIBLE);
            scalarResultText.setVisibility(View.GONE);
            displayMatrix(calculationRecord.getResultMatrix(), resultTable);

            // Thêm phần hiển thị trị riêng phía dưới ma trận kết quả nếu là phép tính trị riêng
            if (calculationRecord.getOperationType().equals("Trị Riêng")) {
                // Tìm LinearLayout chính trong ScrollView
                LinearLayout mainContainer = (LinearLayout) ((ScrollView) resultView).getChildAt(0);

                // Tạo TextView để hiển thị tiêu đề giải thích
                TextView eigenvalueTitle = new TextView(this);
                eigenvalueTitle.setText("Các trị riêng là các giá trị nằm trên đường chéo chính của ma trận kết quả:");
                eigenvalueTitle.setTextAppearance(this, android.R.style.TextAppearance_Medium);
                eigenvalueTitle.setPadding(0, 32, 0, 16);
                mainContainer.addView(eigenvalueTitle);

                // Tạo container cho các giá trị trị riêng
                LinearLayout eigenvaluesContainer = new LinearLayout(this);
                eigenvaluesContainer.setOrientation(LinearLayout.VERTICAL);
                eigenvaluesContainer.setPadding(32, 0, 0, 16);

                // Thêm thông tin từng trị riêng
                MatrixModel result = calculationRecord.getResultMatrix();
                int n = Math.min(result.getRows(), result.getColumns());

                for (int i = 0; i < n; i++) {
                    double eigenvalue = result.getValue(i, i);
                    if (Math.abs(eigenvalue) > 1e-10) { // Chỉ hiển thị trị riêng khác 0
                        TextView eigenvalueText = new TextView(this);
                        eigenvalueText.setText(String.format("λ%d = %.2f", i+1, eigenvalue));
                        eigenvalueText.setTextAppearance(this, android.R.style.TextAppearance_Medium);
                        eigenvalueText.setPadding(0, 8, 0, 8);
                        eigenvaluesContainer.addView(eigenvalueText);
                    }
                }

                // Thêm container vào LinearLayout chính
                mainContainer.addView(eigenvaluesContainer);
            }

            // Nếu là kết quả SVD, hiển thị thêm các ma trận U, S, VT
            if (calculationRecord.isSVDResult()) {
                // Tìm LinearLayout chính trong ScrollView
                LinearLayout mainContainer = (LinearLayout) ((ScrollView) resultView).getChildAt(0);

                // Hiển thị tiêu đề SVD
                TextView svdTitle = new TextView(this);
                svdTitle.setText("Phân tích SVD: A = U × Σ × V^T");
                svdTitle.setTextSize(16);
                svdTitle.setPadding(0, 32, 0, 16);
                mainContainer.addView(svdTitle);

                // Hiển thị ma trận U
                TextView uTitle = new TextView(this);
                uTitle.setText("Ma trận U (orthogonal):");
                uTitle.setTextSize(14);
                uTitle.setPadding(0, 16, 0, 8);
                mainContainer.addView(uTitle);

                HorizontalScrollView uScrollView = new HorizontalScrollView(this);
                TableLayout uTable = new TableLayout(this);
                uTable.setBackgroundResource(R.drawable.matrix_border);
                uScrollView.addView(uTable);
                mainContainer.addView(uScrollView);
                displayMatrix(calculationRecord.getMatrixU(), uTable);

                // Hiển thị ma trận Sigma (S)
                TextView sTitle = new TextView(this);
                sTitle.setText("Ma trận Σ (singular values):");
                sTitle.setTextSize(14);
                sTitle.setPadding(0, 16, 0, 8);
                mainContainer.addView(sTitle);

                HorizontalScrollView sScrollView = new HorizontalScrollView(this);
                TableLayout sTable = new TableLayout(this);
                sTable.setBackgroundResource(R.drawable.matrix_border);
                sScrollView.addView(sTable);
                mainContainer.addView(sScrollView);
                displayMatrix(calculationRecord.getMatrixS(), sTable);

                // Hiển thị ma trận VT
                TextView vtTitle = new TextView(this);
                vtTitle.setText("Ma trận V^T (orthogonal transposed):");
                vtTitle.setTextSize(14);
                vtTitle.setPadding(0, 16, 0, 8);
                mainContainer.addView(vtTitle);

                HorizontalScrollView vtScrollView = new HorizontalScrollView(this);
                TableLayout vtTable = new TableLayout(this);
                vtTable.setBackgroundResource(R.drawable.matrix_border);
                vtScrollView.addView(vtTable);
                mainContainer.addView(vtScrollView);
                displayMatrix(calculationRecord.getMatrixVT(), vtTable);

                // Hiển thị danh sách singular values
                TextView singularValueTitle = new TextView(this);
                singularValueTitle.setText("Các singular values:");
                singularValueTitle.setTextSize(16);
                singularValueTitle.setPadding(0, 32, 0, 16);
                mainContainer.addView(singularValueTitle);

                LinearLayout svContainer = new LinearLayout(this);
                svContainer.setOrientation(LinearLayout.VERTICAL);
                svContainer.setPadding(32, 0, 0, 16);

                MatrixModel matrixS = calculationRecord.getMatrixS();
                for (int i = 0; i < Math.min(matrixS.getRows(), matrixS.getColumns()); i++) {
                    double singularValue = matrixS.getValue(i, i);
                    if (Math.abs(singularValue) > 1e-10) { // Chỉ hiển thị singular values khác 0
                        TextView svText = new TextView(this);
                        svText.setText(String.format("σ%d = %.2f", i+1, singularValue));
                        svText.setTextSize(16);
                        svText.setPadding(0, 8, 0, 8);
                        svContainer.addView(svText);
                    }
                }

                mainContainer.addView(svContainer);
            }

            // Nếu là kết quả của hệ phương trình tuyến tính
            if (getIntent().getBooleanExtra("is_linear_system", false)) {
                // Tìm LinearLayout chính trong ScrollView
                LinearLayout mainContainer = (LinearLayout) ((ScrollView) resultView).getChildAt(0);

                // Tạo TextView để hiển thị tiêu đề giải thích
                TextView linearSystemTitle = new TextView(this);
                linearSystemTitle.setText("Nghiệm của hệ phương trình tuyến tính:");
                linearSystemTitle.setTextAppearance(this, android.R.style.TextAppearance_Medium);
                linearSystemTitle.setPadding(0, 32, 0, 16);
                mainContainer.addView(linearSystemTitle);

                // Tạo container cho các giá trị nghiệm
                LinearLayout solutionContainer = new LinearLayout(this);
                solutionContainer.setOrientation(LinearLayout.VERTICAL);
                solutionContainer.setPadding(32, 0, 0, 16);

                // Thêm thông tin từng nghiệm
                MatrixModel result = calculationRecord.getResultMatrix();
                for (int i = 0; i < result.getRows(); i++) {
                    TextView solutionText = new TextView(this);
                    solutionText.setText(String.format("x%d = %.4f", i+1, result.getValue(i, 0)));
                    solutionText.setTextAppearance(this, android.R.style.TextAppearance_Medium);
                    solutionText.setPadding(0, 8, 0, 8);
                    solutionContainer.addView(solutionText);
                }

                // Thêm container vào LinearLayout chính
                mainContainer.addView(solutionContainer);
            }
        }

        tabContent.addView(resultView);
    }

    /**
     * Show the steps tab content
     */
    /**
     * Show the steps tab content
     */
    private void showStepsTab() {
        // Clear current content
        tabContent.removeAllViews();

        // Inflate steps view
        View stepsView = getLayoutInflater().inflate(R.layout.tab_steps, tabContent, false);

        // Get steps content
        String stepsContent;

        // Kiểm tra xem có các bước chi tiết không
        if (calculationRecord.hasCalculationSteps()) {
            stepsContent = buildStepsContentFromSteps(calculationRecord);
        } else {
            // Sử dụng cách cũ nếu không có bước chi tiết
            stepsContent = getStepsExplanation();
        }

        // Set steps explanation text
        TextView stepsText = stepsView.findViewById(R.id.steps_text);
        stepsText.setText(stepsContent);

        // Add view to container
        tabContent.addView(stepsView);
    }

    /**
     * Build steps content from calculation steps
     */
    private String buildStepsContentFromSteps(CalculationRecord record) {
        StringBuilder sb = new StringBuilder();
        sb.append("Các bước thực hiện ").append(record.getOperationType()).append(":\n\n");

        List<CalculationRecord.CalculationStep> steps = record.getCalculationSteps();

        for (int i = 0; i < steps.size(); i++) {
            CalculationRecord.CalculationStep step = steps.get(i);
            sb.append("Bước ").append(i + 1).append(": ").append(step.getDescription()).append("\n");

            // Nếu có ma trận kết quả trung gian, hiển thị nó
            if (step.hasIntermediateResult()) {
                MatrixModel matrix = step.getIntermediateResult();
                sb.append("\n");
                for (int r = 0; r < matrix.getRows(); r++) {
                    sb.append("[ ");
                    for (int c = 0; c < matrix.getColumns(); c++) {
                        sb.append(String.format("%.2f", matrix.getValue(r, c)));
                        if (c < matrix.getColumns() - 1) {
                            sb.append(", ");
                        }
                    }
                    sb.append(" ]\n");
                }
                sb.append("\n");
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Get explanation of steps for the current operation
     */
    private String getStepsExplanation() {
        String operationType = calculationRecord.getOperationType();
        MatrixModel matrixA = calculationRecord.getInputMatrixA();
        MatrixModel matrixB = calculationRecord.getInputMatrixB();
        MatrixModel result = calculationRecord.getResultMatrix();

        StringBuilder explanation = new StringBuilder();

        // Generate explanation based on operation type
        switch (operationType) {
            case "Add Matrices":
                explanation.append("Steps for Matrix Addition:\n\n");
                explanation.append("1. For each position (i,j) in the matrices:\n");
                explanation.append("   C[i,j] = A[i,j] + B[i,j]\n\n");
                explanation.append("Example for position (0,0):\n");
                explanation.append("C[0,0] = A[0,0] + B[0,0]\n");
                explanation.append(String.format("C[0,0] = %.2f + %.2f = %.2f",
                        matrixA.getValue(0, 0), matrixB.getValue(0, 0), result.getValue(0, 0)));
                break;

            case "Subtract Matrices":
                explanation.append("Steps for Matrix Subtraction:\n\n");
                explanation.append("1. For each position (i,j) in the matrices:\n");
                explanation.append("   C[i,j] = A[i,j] - B[i,j]\n\n");
                explanation.append("Example for position (0,0):\n");
                explanation.append("C[0,0] = A[0,0] - B[0,0]\n");
                explanation.append(String.format("C[0,0] = %.2f - %.2f = %.2f",
                        matrixA.getValue(0, 0), matrixB.getValue(0, 0), result.getValue(0, 0)));
                break;

            case "Multiply Matrices":
                explanation.append("Steps for Matrix Multiplication:\n\n");
                explanation.append("1. For each position (i,j) in the result matrix:\n");
                explanation.append("   C[i,j] = Σ(A[i,k] * B[k,j]) for k = 0 to n-1\n\n");

                // Example calculation for position (0,0)
                explanation.append("Example for position (0,0):\n");
                explanation.append("C[0,0] = ");

                double sum = 0;
                for (int k = 0; k < matrixA.getColumns(); k++) {
                    explanation.append(String.format("A[0,%d] * B[%d,0]", k, k));
                    if (k < matrixA.getColumns() - 1) {
                        explanation.append(" + ");
                    }
                    sum += matrixA.getValue(0, k) * matrixB.getValue(k, 0);
                }

                explanation.append("\nC[0,0] = ");
                for (int k = 0; k < matrixA.getColumns(); k++) {
                    explanation.append(String.format("%.2f * %.2f",
                            matrixA.getValue(0, k), matrixB.getValue(k, 0)));
                    if (k < matrixA.getColumns() - 1) {
                        explanation.append(" + ");
                    }
                }

                explanation.append(String.format(" = %.2f", sum));
                break;

            case "Transpose":
                explanation.append("Steps for Matrix Transposition:\n\n");
                explanation.append("1. For each position (i,j) in the original matrix:\n");
                explanation.append("   B[j,i] = A[i,j]\n\n");
                explanation.append("Example for position (0,1):\n");
                explanation.append("B[1,0] = A[0,1]\n");
                explanation.append(String.format("B[1,0] = %.2f", matrixA.getValue(0, 1)));
                break;

            case "Determinant":
                explanation.append("Steps for Determinant Calculation:\n\n");
                if (matrixA.getRows() == 2) {
                    explanation.append("For a 2x2 matrix:\n");
                    explanation.append("det(A) = A[0,0] * A[1,1] - A[0,1] * A[1,0]\n");
                    explanation.append(String.format("det(A) = %.2f * %.2f - %.2f * %.2f = %.2f",
                            matrixA.getValue(0, 0), matrixA.getValue(1, 1),
                            matrixA.getValue(0, 1), matrixA.getValue(1, 0), scalarResult));
                } else {
                    explanation.append("For an NxN matrix, we use cofactor expansion along the first row:\n");
                    explanation.append("det(A) = Σ((-1)^j * A[0,j] * det(M_j)) for j = 0 to n-1\n\n");
                    explanation.append("where M_j is the submatrix formed by removing row 0 and column j.\n\n");
                    explanation.append("The determinant calculation involves multiple recursive steps that are too detailed to display here.");
                }
                break;

            case "Inverse Matrix":
                explanation.append("Steps for Matrix Inversion:\n\n");
                explanation.append("1. Calculate the determinant of the matrix.\n");
                explanation.append("2. If determinant is zero, the matrix is not invertible.\n");
                explanation.append("3. For each element (i,j):\n");
                explanation.append("   - Calculate the cofactor C_ij\n");
                explanation.append("   - Calculate the adjugate matrix: adj(A)[j,i] = C_ij\n");
                explanation.append("4. Divide each element of the adjugate by the determinant:\n");
                explanation.append("   A^-1[i,j] = adj(A)[i,j] / det(A)\n\n");
                explanation.append("The inversion process involves multiple steps that are too detailed to display here.");
                break;

            case "Phân Tích SVD":
                explanation.append("Steps for Singular Value Decomposition (SVD):\n\n");
                explanation.append("1. Calculate A^T * A (or A * A^T for wide matrices).\n");
                explanation.append("2. Find the eigenvalues and eigenvectors of this matrix.\n");
                explanation.append("   - The eigenvalues are the squares of the singular values.\n");
                explanation.append("   - The eigenvectors form the columns of V.\n");
                explanation.append("3. Calculate the singular values by taking the square root of the eigenvalues.\n");
                explanation.append("4. Form the diagonal matrix Σ with singular values.\n");
                explanation.append("5. Calculate U using the formula: U = A * V * Σ^-1\n\n");
                explanation.append("The final decomposition is: A = U * Σ * V^T\n");
                explanation.append("where:\n");
                explanation.append("- U is an orthogonal matrix\n");
                explanation.append("- Σ is a diagonal matrix with singular values\n");
                explanation.append("- V^T is the transpose of an orthogonal matrix\n");
                break;

            // Add explanations for other operations as needed

            default:
                explanation.append("Detailed steps for this operation are not available.");
                break;
        }

        return explanation.toString();
    }

    /**
     * Display a matrix in a table layout
     */
    private void displayMatrix(MatrixModel matrix, TableLayout tableLayout) {
        tableLayout.removeAllViews();

        for (int i = 0; i < matrix.getRows(); i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < matrix.getColumns(); j++) {
                TextView textView = new TextView(this);
                textView.setText(String.format("%.2f", matrix.getValue(i, j)));
                textView.setPadding(16, 8, 16, 8);
                tableRow.addView(textView);
            }

            tableLayout.addView(tableRow);
        }
    }

    /**
     * Handle share button click
     */
    private void onShareClick(View view) {
        // Create share content
        StringBuilder shareContent = new StringBuilder();
        shareContent.append("Matrix Calculation: ").append(calculationRecord.getOperationType()).append("\n\n");

        // Add Matrix A
        shareContent.append("Matrix A:\n");
        appendMatrixToString(calculationRecord.getInputMatrixA(), shareContent);

        // Add Matrix B if it exists
        if (calculationRecord.hasTwoInputs()) {
            shareContent.append("\nMatrix B:\n");
            appendMatrixToString(calculationRecord.getInputMatrixB(), shareContent);
        }

        // Add result
        shareContent.append("\nResult:\n");
        if (isScalarResult) {
            shareContent.append(scalarResult);
        } else {
            appendMatrixToString(calculationRecord.getResultMatrix(), shareContent);
        }

        // Add SVD results if applicable
        if (calculationRecord.isSVDResult()) {
            shareContent.append("\nSVD Decomposition: A = U × Σ × V^T\n\n");

            shareContent.append("U (orthogonal):\n");
            appendMatrixToString(calculationRecord.getMatrixU(), shareContent);

            shareContent.append("\nΣ (singular values):\n");
            appendMatrixToString(calculationRecord.getMatrixS(), shareContent);

            shareContent.append("\nV^T (orthogonal transposed):\n");
            appendMatrixToString(calculationRecord.getMatrixVT(), shareContent);

            // Add singular values list
            shareContent.append("\nSingular values:\n");
            MatrixModel matrixS = calculationRecord.getMatrixS();
            for (int i = 0; i < Math.min(matrixS.getRows(), matrixS.getColumns()); i++) {
                double singularValue = matrixS.getValue(i, i);
                if (Math.abs(singularValue) > 1e-10) {
                    shareContent.append(String.format("σ%d = %.2f\n", i+1, singularValue));
                }
            }
        }

        // Create share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Matrix Calculation Result");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent.toString());

        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    /**
     * Append matrix to string builder
     */
    private void appendMatrixToString(MatrixModel matrix, StringBuilder sb) {
        for (int i = 0; i < matrix.getRows(); i++) {
            sb.append("[ ");
            for (int j = 0; j < matrix.getColumns(); j++) {
                sb.append(String.format("%.2f", matrix.getValue(i, j)));
                if (j < matrix.getColumns() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(" ]\n");
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