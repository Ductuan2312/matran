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
import com.example.matran.Utils.OperationUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Locale;

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

        // Set toolbar với tên phép toán đã việt hóa
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String operationTitle = OperationUtils.getOperationDisplayName(this, calculationRecord.getOperationType()) + " - " + getString(R.string.result);
        getSupportActionBar().setTitle(operationTitle);

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

        // Sử dụng tên tiếng Việt cho phép toán
        operationTitleText.setText(OperationUtils.getOperationDisplayName(this, calculationRecord.getOperationType()));

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

        // Tạo lời giải thích dựa trên loại phép toán
        switch (operationType) {
            case "Add Matrices":
                explanation.append("Các bước thực hiện phép Cộng Ma trận:\n\n");
                explanation.append("1. Đối với mỗi vị trí (i,j) trong ma trận:\n");
                explanation.append("   C[i,j] = A[i,j] + B[i,j]\n\n");
                explanation.append("Ví dụ cho vị trí (0,0):\n");
                explanation.append("C[0,0] = A[0,0] + B[0,0]\n");
                explanation.append(String.format("C[0,0] = %.2f + %.2f = %.2f",
                        matrixA.getValue(0, 0), matrixB.getValue(0, 0), result.getValue(0, 0)));
                break;

            case "Subtract Matrices":
                explanation.append("Các bước thực hiện phép Trừ Ma trận:\n\n");
                explanation.append("1. Đối với mỗi vị trí (i,j) trong ma trận:\n");
                explanation.append("   C[i,j] = A[i,j] - B[i,j]\n\n");
                explanation.append("Ví dụ cho vị trí (0,0):\n");
                explanation.append("C[0,0] = A[0,0] - B[0,0]\n");
                explanation.append(String.format("C[0,0] = %.2f - %.2f = %.2f",
                        matrixA.getValue(0, 0), matrixB.getValue(0, 0), result.getValue(0, 0)));
                break;

            case "Multiply Matrices":
                explanation.append("Các bước thực hiện phép Nhân Ma trận:\n\n");
                explanation.append("1. Đối với mỗi vị trí (i,j) trong ma trận kết quả:\n");
                explanation.append("   C[i,j] = Σ(A[i,k] * B[k,j]) với k = 0 đến n-1\n\n");

                // Ví dụ tính toán cho vị trí (0,0)
                explanation.append("Ví dụ cho vị trí (0,0):\n");
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
                explanation.append("Các bước thực hiện phép Chuyển vị Ma trận:\n\n");
                explanation.append("1. Đối với mỗi vị trí (i,j) trong ma trận ban đầu:\n");
                explanation.append("   B[j,i] = A[i,j]\n\n");
                explanation.append("Ví dụ cho vị trí (0,1):\n");
                explanation.append("B[1,0] = A[0,1]\n");
                explanation.append(String.format("B[1,0] = %.2f", matrixA.getValue(0, 1)));
                break;

            case "Determinant":
                explanation.append("Các bước tính Định thức:\n\n");
                if (matrixA.getRows() == 2) {
                    explanation.append("Đối với ma trận 2x2:\n");
                    explanation.append("det(A) = A[0,0] * A[1,1] - A[0,1] * A[1,0]\n");
                    explanation.append(String.format("det(A) = %.2f * %.2f - %.2f * %.2f = %.2f",
                            matrixA.getValue(0, 0), matrixA.getValue(1, 1),
                            matrixA.getValue(0, 1), matrixA.getValue(1, 0), scalarResult));
                } else {
                    explanation.append("Đối với ma trận NxN, chúng ta sử dụng khai triển theo cofactor dọc theo hàng đầu tiên:\n");
                    explanation.append("det(A) = Σ((-1)^j * A[0,j] * det(M_j)) với j = 0 đến n-1\n\n");
                    explanation.append("trong đó M_j là ma trận con được tạo bằng cách loại bỏ hàng 0 và cột j.\n\n");
                    explanation.append("Việc tính định thức liên quan đến nhiều bước đệ quy phức tạp nên không thể hiển thị chi tiết ở đây.");
                }
                break;

            case "Inverse Matrix":
                explanation.append("Các bước tìm Ma trận Nghịch đảo:\n\n");
                explanation.append("1. Tính định thức của ma trận.\n");
                explanation.append("2. Nếu định thức bằng không, ma trận không khả nghịch.\n");
                explanation.append("3. Đối với mỗi phần tử (i,j):\n");
                explanation.append("   - Tính cofactor C_ij\n");
                explanation.append("   - Tính ma trận phụ hợp: adj(A)[j,i] = C_ij\n");
                explanation.append("4. Chia mỗi phần tử của ma trận phụ hợp cho định thức:\n");
                explanation.append("   A^-1[i,j] = adj(A)[i,j] / det(A)\n\n");
                explanation.append("Quá trình nghịch đảo liên quan đến nhiều bước phức tạp nên không thể hiển thị chi tiết ở đây.");
                break;

            case "Phân Tích SVD":
                explanation.append("Các bước thực hiện Phân tích SVD (Singular Value Decomposition):\n\n");
                explanation.append("1. Tính A^T * A (hoặc A * A^T cho ma trận rộng).\n");
                explanation.append("2. Tìm trị riêng và vector riêng của ma trận này.\n");
                explanation.append("   - Các trị riêng là bình phương của các giá trị kỳ dị.\n");
                explanation.append("   - Các vector riêng tạo thành các cột của ma trận V.\n");
                explanation.append("3. Tính các giá trị kỳ dị bằng cách lấy căn bậc hai của các trị riêng.\n");
                explanation.append("4. Tạo ma trận đường chéo Σ với các giá trị kỳ dị.\n");
                explanation.append("5. Tính ma trận U sử dụng công thức: U = A * V * Σ^-1\n\n");
                explanation.append("Phân tích cuối cùng là: A = U * Σ * V^T\n");
                explanation.append("trong đó:\n");
                explanation.append("- U là ma trận trực giao\n");
                explanation.append("- Σ là ma trận đường chéo chứa các giá trị kỳ dị\n");
                explanation.append("- V^T là chuyển vị của một ma trận trực giao\n");
                break;

            // Thêm các lời giải thích cho các phép toán khác khi cần

            default:
                explanation.append("Chi tiết các bước thực hiện cho phép toán này chưa được cập nhật.");
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
        String operationName = OperationUtils.getOperationDisplayName(this, calculationRecord.getOperationType());
        shareContent.append("Tính toán ma trận: ").append(operationName).append("\n\n");


        // Add Matrix A
        shareContent.append("Ma trận A:\n");
        appendMatrixToString(calculationRecord.getInputMatrixA(), shareContent);

        // Add Matrix B if it exists
        if (calculationRecord.hasTwoInputs()) {
            shareContent.append("\nMa trận B:\n");
            appendMatrixToString(calculationRecord.getInputMatrixB(), shareContent);
        }

        // Add result
        shareContent.append("\nKết quả:\n");
        if (isScalarResult) {
            shareContent.append(scalarResult);
        } else {
            appendMatrixToString(calculationRecord.getResultMatrix(), shareContent);
        }

        // Add SVD results if applicable
        if (calculationRecord.isSVDResult()) {
            shareContent.append("\nPhân tích SVD: A = U × Σ × V^T\n\n");

            shareContent.append("U (ma trận trực giao):\n");
            appendMatrixToString(calculationRecord.getMatrixU(), shareContent);

            shareContent.append("\nΣ (giá trị kỳ dị):\n");
            appendMatrixToString(calculationRecord.getMatrixS(), shareContent);

            shareContent.append("\nV^T (ma trận trực giao chuyển vị):\n");
            appendMatrixToString(calculationRecord.getMatrixVT(), shareContent);

            // Add singular values list
            shareContent.append("\nGiá trị kỳ dị:\n");
            MatrixModel matrixS = calculationRecord.getMatrixS();
            for (int i = 0; i < Math.min(matrixS.getRows(), matrixS.getColumns()); i++) {
                double singularValue = matrixS.getValue(i, i);
                if (Math.abs(singularValue) > 1e-10) {
                    shareContent.append(String.format(Locale.getDefault(), "σ%d = %.2f\n", i+1, singularValue));
                }
            }
        }

        // Create share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Kết quả tính toán ma trận");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent.toString());

        startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"));
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