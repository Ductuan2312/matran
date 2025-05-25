//matran - A matrix manipulation application
//        Copyright (c) 2025 Đức Tuân
//
//        Licensed under the MIT License. See LICENSE file in the project root for full license information.
package com.example.matran.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class for matrix operations
 */
public class OperationModel {
    private static final String TAG = "OperationModel";

    /**
     * Add two matrices
     * @param a first matrix
     * @param b second matrix
     * @return resulting matrix after addition
     * @throws IllegalArgumentException if dimensions don't match
     */
    public static MatrixModel add(MatrixModel a, MatrixModel b) {
        if (a.getRows() != b.getRows() || a.getColumns() != b.getColumns()) {
            throw new IllegalArgumentException("Matrices must have same dimensions for addition");
        }

        MatrixModel result = new MatrixModel(a.getRows(), a.getColumns());
        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < a.getColumns(); j++) {
                result.setValue(i, j, a.getValue(i, j) + b.getValue(i, j));
            }
        }
        return result;
    }

    /**
     * Add two matrices with steps
     * @param a first matrix
     * @param b second matrix
     * @return calculation record with steps
     */
    public static CalculationRecord addWithSteps(MatrixModel a, MatrixModel b) {
        if (a.getRows() != b.getRows() || a.getColumns() != b.getColumns()) {
            throw new IllegalArgumentException("Matrices must have same dimensions for addition");
        }

        MatrixModel result = new MatrixModel(a.getRows(), a.getColumns());
        CalculationRecord record = new CalculationRecord("Add Matrices", a, b, result);

        record.addStep("Cộng hai ma trận cùng kích thước bằng cách cộng từng phần tử tương ứng.");

        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < a.getColumns(); j++) {
                double sum = a.getValue(i, j) + b.getValue(i, j);
                result.setValue(i, j, sum);

                // Chỉ lưu một vài bước ví dụ để không quá nhiều
                if (i == 0 && j == 0) {
                    record.addStep(String.format("C[%d,%d] = A[%d,%d] + B[%d,%d] = %.2f + %.2f = %.2f",
                            i, j, i, j, i, j, a.getValue(i, j), b.getValue(i, j), sum));
                }
                else if (i == a.getRows()-1 && j == a.getColumns()-1) {
                    record.addStep(String.format("C[%d,%d] = A[%d,%d] + B[%d,%d] = %.2f + %.2f = %.2f",
                            i, j, i, j, i, j, a.getValue(i, j), b.getValue(i, j), sum));
                }
            }
        }

        record.addStep("Kết quả cuối cùng:", result);
        return record;
    }

    /**
     * Subtract two matrices
     * @param a first matrix
     * @param b second matrix
     * @return resulting matrix after subtraction
     * @throws IllegalArgumentException if dimensions don't match
     */
    public static MatrixModel subtract(MatrixModel a, MatrixModel b) {
        if (a.getRows() != b.getRows() || a.getColumns() != b.getColumns()) {
            throw new IllegalArgumentException("Matrices must have same dimensions for subtraction");
        }

        MatrixModel result = new MatrixModel(a.getRows(), a.getColumns());
        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < a.getColumns(); j++) {
                result.setValue(i, j, a.getValue(i, j) - b.getValue(i, j));
            }
        }
        return result;
    }

    /**
     * Subtract two matrices with steps
     * @param a first matrix
     * @param b second matrix
     * @return calculation record with steps
     */
    public static CalculationRecord subtractWithSteps(MatrixModel a, MatrixModel b) {
        if (a.getRows() != b.getRows() || a.getColumns() != b.getColumns()) {
            throw new IllegalArgumentException("Matrices must have same dimensions for subtraction");
        }

        MatrixModel result = new MatrixModel(a.getRows(), a.getColumns());
        CalculationRecord record = new CalculationRecord("Subtract Matrices", a, b, result);

        record.addStep("Trừ hai ma trận cùng kích thước bằng cách trừ từng phần tử tương ứng.");

        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < a.getColumns(); j++) {
                double diff = a.getValue(i, j) - b.getValue(i, j);
                result.setValue(i, j, diff);

                // Chỉ lưu một vài bước ví dụ để không quá nhiều
                if (i == 0 && j == 0) {
                    record.addStep(String.format("C[%d,%d] = A[%d,%d] - B[%d,%d] = %.2f - %.2f = %.2f",
                            i, j, i, j, i, j, a.getValue(i, j), b.getValue(i, j), diff));
                }
                else if (i == a.getRows()-1 && j == a.getColumns()-1) {
                    record.addStep(String.format("C[%d,%d] = A[%d,%d] - B[%d,%d] = %.2f - %.2f = %.2f",
                            i, j, i, j, i, j, a.getValue(i, j), b.getValue(i, j), diff));
                }
            }
        }

        record.addStep("Kết quả cuối cùng:", result);
        return record;
    }

    /**
     * Multiply two matrices
     * @param a first matrix
     * @param b second matrix
     * @return resulting matrix after multiplication
     * @throws IllegalArgumentException if dimensions are incompatible
     */
    public static MatrixModel multiply(MatrixModel a, MatrixModel b) {
        if (a.getColumns() != b.getRows()) {
            throw new IllegalArgumentException("Number of columns in first matrix must equal number of rows in second matrix");
        }

        MatrixModel result = new MatrixModel(a.getRows(), b.getColumns());
        for (int i = 0; i < a.getRows(); i++) {
            for (int j = 0; j < b.getColumns(); j++) {
                double sum = 0;
                for (int k = 0; k < a.getColumns(); k++) {
                    sum += a.getValue(i, k) * b.getValue(k, j);
                }
                result.setValue(i, j, sum);
            }
        }
        return result;
    }

    /**
     * Multiply two matrices with steps
     * @param a first matrix
     * @param b second matrix
     * @return calculation record with steps
     */
    public static CalculationRecord multiplyWithSteps(MatrixModel a, MatrixModel b) {
        if (a.getColumns() != b.getRows()) {
            throw new IllegalArgumentException("Number of columns in first matrix must equal number of rows in second matrix");
        }

        MatrixModel result = new MatrixModel(a.getRows(), b.getColumns());
        CalculationRecord record = new CalculationRecord("Multiply Matrices", a, b, result);

        record.addStep("Ma trận kết quả có kích thước (" + a.getRows() + "×" + b.getColumns() + ")");
        record.addStep("Để nhân hai ma trận, ta tính từng phần tử của ma trận kết quả bằng tích vô hướng của hàng thứ i của ma trận A với cột thứ j của ma trận B.");

        // Chỉ hiển thị các bước cho một vài phần tử đại diện
        int numStepsToShow = Math.min(3, a.getRows() * b.getColumns());
        int stepsShown = 0;

        for (int i = 0; i < a.getRows() && stepsShown < numStepsToShow; i++) {
            for (int j = 0; j < b.getColumns() && stepsShown < numStepsToShow; j++) {
                StringBuilder stepDesc = new StringBuilder();
                stepDesc.append(String.format("Tính C[%d,%d] = ", i, j));

                double sum = 0;
                for (int k = 0; k < a.getColumns(); k++) {
                    double product = a.getValue(i, k) * b.getValue(k, j);
                    sum += product;

                    stepDesc.append(String.format("A[%d,%d] × B[%d,%d]", i, k, k, j));
                    if (k < a.getColumns() - 1) {
                        stepDesc.append(" + ");
                    }
                }

                stepDesc.append("\n= ");

                for (int k = 0; k < a.getColumns(); k++) {
                    double product = a.getValue(i, k) * b.getValue(k, j);
                    stepDesc.append(String.format("%.2f × %.2f", a.getValue(i, k), b.getValue(k, j)));

                    if (k < a.getColumns() - 1) {
                        stepDesc.append(" + ");
                    }
                }

                stepDesc.append(String.format(" = %.2f", sum));
                record.addStep(stepDesc.toString());

                result.setValue(i, j, sum);
                stepsShown++;
            }
        }

        if (stepsShown < a.getRows() * b.getColumns()) {
            record.addStep("... và tương tự cho các phần tử khác.");
        }

        record.addStep("Kết quả cuối cùng:", result);
        return record;
    }

    /**
     * Transpose a matrix
     * @param matrix input matrix
     * @return transposed matrix
     */
    public static MatrixModel transpose(MatrixModel matrix) {
        MatrixModel result = new MatrixModel(matrix.getColumns(), matrix.getRows());
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getColumns(); j++) {
                result.setValue(j, i, matrix.getValue(i, j));
            }
        }
        return result;
    }

    /**
     * Transpose a matrix with steps
     * @param matrix input matrix
     * @return calculation record with steps
     */
    public static CalculationRecord transposeWithSteps(MatrixModel matrix) {
        MatrixModel result = new MatrixModel(matrix.getColumns(), matrix.getRows());
        CalculationRecord record = new CalculationRecord("Transpose", matrix, result);

        record.addStep("Chuyển vị ma trận bằng cách đổi chỗ hàng và cột.");
        record.addStep("Ma trận kết quả có kích thước (" + matrix.getColumns() + "×" + matrix.getRows() + ")");

        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getColumns(); j++) {
                result.setValue(j, i, matrix.getValue(i, j));

                // Chỉ ghi lại một vài bước để minh họa
                if ((i == 0 && j == 0) || (i == matrix.getRows()-1 && j == matrix.getColumns()-1)) {
                    record.addStep(String.format("B[%d,%d] = A[%d,%d] = %.2f",
                            j, i, i, j, matrix.getValue(i, j)));
                }
            }
        }

        record.addStep("Kết quả cuối cùng:", result);
        return record;
    }

    /**
     * Calculate determinant of a square matrix
     * @param matrix input matrix
     * @return determinant value
     * @throws IllegalArgumentException if matrix is not square
     */
    public static double determinant(MatrixModel matrix) {
        if (matrix.getRows() != matrix.getColumns()) {
            throw new IllegalArgumentException("Determinant can only be calculated for square matrices");
        }

        int n = matrix.getRows();
        if (n == 1) {
            return matrix.getValue(0, 0);
        }
        if (n == 2) {
            return matrix.getValue(0, 0) * matrix.getValue(1, 1) - matrix.getValue(0, 1) * matrix.getValue(1, 0);
        }

        double det = 0;
        for (int j = 0; j < n; j++) {
            det += Math.pow(-1, j) * matrix.getValue(0, j) * determinant(getSubMatrix(matrix, 0, j));
        }
        return det;
    }

    /**
     * Calculate determinant of a square matrix with steps
     * @param matrix input matrix
     * @return calculation record with steps
     */
    public static CalculationRecord determinantWithSteps(MatrixModel matrix) {
        List<String> steps = new ArrayList<>();
        double det = determinantWithSteps(matrix, steps, "");

        MatrixModel resultMatrix = new MatrixModel(1, 1);
        resultMatrix.setValue(0, 0, det);

        CalculationRecord record = new CalculationRecord("Determinant", matrix, resultMatrix);

        // Add all collected steps
        for (String step : steps) {
            record.addStep(step);
        }

        record.addStep(String.format("Kết quả cuối cùng: det(A) = %.4f", det));
        return record;
    }

    /**
     * Helper method to calculate determinant with steps
     * @param matrix input matrix
     * @param steps list to collect steps
     * @param prefix prefix for indentation
     * @return determinant value
     */
    private static double determinantWithSteps(MatrixModel matrix, List<String> steps, String prefix) {
        int n = matrix.getRows();

        if (n == 1) {
            steps.add(prefix + "det([" + matrix.getValue(0, 0) + "]) = " + matrix.getValue(0, 0));
            return matrix.getValue(0, 0);
        }

        if (n == 2) {
            double a = matrix.getValue(0, 0);
            double b = matrix.getValue(0, 1);
            double c = matrix.getValue(1, 0);
            double d = matrix.getValue(1, 1);
            double det = a * d - b * c;

            steps.add(prefix + String.format("det([[%.2f, %.2f], [%.2f, %.2f]]) = %.2f × %.2f - %.2f × %.2f = %.2f",
                    a, b, c, d, a, d, b, c, det));
            return det;
        }

        steps.add(prefix + "Using Laplace expansion along the first row:");

        double det = 0;
        for (int j = 0; j < n; j++) {
            double cofactor = Math.pow(-1, j) * matrix.getValue(0, j);
            steps.add(prefix + String.format("Term %d: (-1)^%d × %.2f × det(submatrix)", j+1, j, matrix.getValue(0, j)));

            MatrixModel subMatrix = getSubMatrix(matrix, 0, j);
            steps.add(prefix + "  Calculating determinant of submatrix...");
            double subDet = determinantWithSteps(subMatrix, steps, prefix + "  ");

            det += cofactor * subDet;
            steps.add(prefix + String.format("Term %d = %.2f × %.2f = %.2f", j+1, cofactor, subDet, cofactor * subDet));
        }

        steps.add(prefix + String.format("Adding all terms: det = %.4f", det));
        return det;
    }

    /**
     * Calculate inverse of a square matrix
     * @param matrix input matrix
     * @return inverse matrix
     * @throws IllegalArgumentException if matrix is not square or has zero determinant
     */
    public static MatrixModel inverse(MatrixModel matrix) {
        if (matrix.getRows() != matrix.getColumns()) {
            throw new IllegalArgumentException("Inverse can only be calculated for square matrices");
        }

        double det = determinant(matrix);
        if (Math.abs(det) < 1e-10) {
            throw new IllegalArgumentException("Matrix is singular, inverse does not exist");
        }

        int n = matrix.getRows();
        MatrixModel result = new MatrixModel(n, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double cofactor = Math.pow(-1, i + j) * determinant(getSubMatrix(matrix, i, j));
                result.setValue(j, i, cofactor / det); // Note: j,i for transpose
            }
        }

        return result;
    }

    /**
     * Calculate inverse of a square matrix with steps
     * @param matrix input matrix
     * @return calculation record with steps
     */
    public static CalculationRecord inverseWithSteps(MatrixModel matrix) {
        if (matrix.getRows() != matrix.getColumns()) {
            throw new IllegalArgumentException("Inverse can only be calculated for square matrices");
        }

        CalculationRecord record = new CalculationRecord("Inverse Matrix", matrix, null);

        record.addStep("Bước 1: Tính định thức của ma trận");
        double det = determinant(matrix);
        record.addStep(String.format("Định thức det(A) = %.4f", det));

        if (Math.abs(det) < 1e-10) {
            throw new IllegalArgumentException("Ma trận suy biến, không tồn tại ma trận nghịch đảo");
        }

        record.addStep("Bước 2: Tính ma trận phụ hợp (adjugate)");

        int n = matrix.getRows();
        MatrixModel cofactorMatrix = new MatrixModel(n, n);

        // Nếu ma trận nhỏ (2x2), hiển thị chi tiết
        if (n == 2) {
            double a = matrix.getValue(0, 0);
            double b = matrix.getValue(0, 1);
            double c = matrix.getValue(1, 0);
            double d = matrix.getValue(1, 1);

            record.addStep(String.format("Ma trận phụ hợp = [[%f, %f], [%f, %f]]", d, -b, -c, a));

            cofactorMatrix.setValue(0, 0, d);
            cofactorMatrix.setValue(0, 1, -b);
            cofactorMatrix.setValue(1, 0, -c);
            cofactorMatrix.setValue(1, 1, a);
        } else {
            // Nếu ma trận lớn hơn, chỉ hiển thị một phần
            record.addStep("Tính từng phần tử của ma trận phụ hợp...");
            record.addStep("(Quá trình này gồm nhiều bước con, chỉ hiển thị một vài ví dụ)");

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    MatrixModel subMatrix = getSubMatrix(matrix, i, j);
                    double minorDet = determinant(subMatrix);
                    double cofactor = Math.pow(-1, i + j) * minorDet;
                    cofactorMatrix.setValue(i, j, cofactor);

                    // Chỉ hiển thị một vài ví dụ
                    if ((i == 0 && j == 0) || (i == n-1 && j == n-1)) {
                        record.addStep(String.format("Phần tử phụ hợp tại (%d,%d): (-1)^(%d+%d) × %.4f = %.4f",
                                i, j, i, j, minorDet, cofactor));
                    }
                }
            }
            record.addStep("Ma trận phụ hợp:", cofactorMatrix);
        }

        record.addStep("Bước 3: Chuyển vị ma trận phụ hợp");
        MatrixModel adjugate = new MatrixModel(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjugate.setValue(j, i, cofactorMatrix.getValue(i, j));
            }
        }
        record.addStep("Ma trận phụ hợp chuyển vị:", adjugate);

        record.addStep("Bước 4: Chia ma trận phụ hợp chuyển vị cho định thức");
        MatrixModel result = new MatrixModel(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result.setValue(i, j, adjugate.getValue(i, j) / det);
            }
        }

        record.addStep("Ma trận nghịch đảo = ma trận phụ hợp chuyển vị / định thức:", result);
        record.addStep("Kết quả cuối cùng:", result);

        // Set final result
        record = new CalculationRecord("Inverse Matrix", matrix, result);
        return record;
    }

    /**
     * Calculate convolution of matrix with kernel
     * @param matrix input matrix
     * @param kernel convolution kernel
     * @return result after convolution
     */
    public static MatrixModel convolution(MatrixModel matrix, MatrixModel kernel) {
        int resultRows = matrix.getRows() - kernel.getRows() + 1;
        int resultCols = matrix.getColumns() - kernel.getColumns() + 1;

        if (resultRows <= 0 || resultCols <= 0) {
            throw new IllegalArgumentException("Kernel size must be smaller than matrix size");
        }

        MatrixModel result = new MatrixModel(resultRows, resultCols);

        for (int i = 0; i < resultRows; i++) {
            for (int j = 0; j < resultCols; j++) {
                double sum = 0;
                for (int ki = 0; ki < kernel.getRows(); ki++) {
                    for (int kj = 0; kj < kernel.getColumns(); kj++) {
                        // Lật kernel 180 độ bằng cách đảo ngược chỉ số
                        int flippedRowIndex = kernel.getRows() - 1 - ki;
                        int flippedColIndex = kernel.getColumns() - 1 - kj;

                        sum += matrix.getValue(i + ki, j + kj) * kernel.getValue(flippedRowIndex, flippedColIndex);
                    }
                }
                result.setValue(i, j, sum);
            }
        }

        return result;
    }

    public static CalculationRecord convolutionWithSteps(MatrixModel matrix, MatrixModel kernel) {
        int resultRows = matrix.getRows() - kernel.getRows() + 1;
        int resultCols = matrix.getColumns() - kernel.getColumns() + 1;

        if (resultRows <= 0 || resultCols <= 0) {
            throw new IllegalArgumentException("Kernel size must be smaller than matrix size");
        }

        MatrixModel result = new MatrixModel(resultRows, resultCols);
        CalculationRecord record = new CalculationRecord("Convolution", matrix, kernel, result);

        record.addStep("Bước 1: Chuẩn bị tích chập với kernel kích thước " +
                kernel.getRows() + "x" + kernel.getColumns());
        record.addStep("Kết quả sẽ có kích thước " +
                resultRows + "x" + resultCols);

        // Chi tiết cho một vài ví dụ tại các vị trí cụ thể
        for (int i = 0; i < resultRows && i < 2; i++) {
            for (int j = 0; j < resultCols && j < 2; j++) {
                StringBuilder stepDetail = new StringBuilder();
                stepDetail.append(String.format("Tính kết quả tại vị trí (%d,%d):\n", i, j));

                double sum = 0;
                for (int ki = 0; ki < kernel.getRows(); ki++) {
                    for (int kj = 0; kj < kernel.getColumns(); kj++) {
                        int flippedRowIndex = kernel.getRows() - 1 - ki;
                        int flippedColIndex = kernel.getColumns() - 1 - kj;

                        double matValue = matrix.getValue(i + ki, j + kj);
                        double kernValue = kernel.getValue(flippedRowIndex, flippedColIndex);
                        sum += matValue * kernValue;

                        stepDetail.append(String.format("  Matrix[%d,%d] * Kernel[%d,%d] = %.2f * %.2f = %.2f\n",
                                i + ki, j + kj, flippedRowIndex, flippedColIndex, matValue, kernValue, matValue * kernValue));
                    }
                }

                stepDetail.append(String.format("  Tổng = %.2f", sum));
                record.addStep(stepDetail.toString());
                result.setValue(i, j, sum);
            }
        }

        if (resultRows > 2 || resultCols > 2) {
            record.addStep("... Tương tự cho các phần tử còn lại.");
        }

        // Hoàn thành ma trận kết quả (không hiển thị tất cả các bước)
        for (int i = 0; i < resultRows; i++) {
            for (int j = 0; j < resultCols; j++) {
                if (!(i < 2 && j < 2)) {  // Đã hiển thị chi tiết cho các vị trí này rồi
                    double sum = 0;
                    for (int ki = 0; ki < kernel.getRows(); ki++) {
                        for (int kj = 0; kj < kernel.getColumns(); kj++) {
                            int flippedRowIndex = kernel.getRows() - 1 - ki;
                            int flippedColIndex = kernel.getColumns() - 1 - kj;
                            sum += matrix.getValue(i + ki, j + kj) * kernel.getValue(flippedRowIndex, flippedColIndex);
                        }
                    }
                    result.setValue(i, j, sum);
                }
            }
        }

        record.addStep("Kết quả cuối cùng:", result);
        return record;
    }
    /**
     * Calculate rank of a matrix
     * @param matrix input matrix
     * @return rank value
     */
    public static int rank(MatrixModel matrix) {
        // Implementing Gaussian elimination to find rank
        MatrixModel rref = getRREF(matrix.copy());
        int rank = 0;

        for (int i = 0; i < rref.getRows(); i++) {
            boolean nonZeroRow = false;
            for (int j = 0; j < rref.getColumns(); j++) {
                if (Math.abs(rref.getValue(i, j)) > 1e-10) {
                    nonZeroRow = true;
                    break;
                }
            }
            if (nonZeroRow) {
                rank++;
            }
        }

        return rank;
    }

    /**
     * Calculate rank of a matrix with steps
     * @param matrix input matrix
     * @return calculation record with steps
     */
    public static CalculationRecord rankWithSteps(MatrixModel matrix) {
        CalculationRecord record = new CalculationRecord("Rank", matrix, null);

        record.addStep("Bước 1: Chuyển ma trận về dạng bậc thang (RREF)");

        List<MatrixModel> steps = new ArrayList<>();
        MatrixModel rref = getRREFWithSteps(matrix.copy(), steps);

        // Hiển thị các bước biến đổi
        for (int i = 0; i < steps.size(); i++) {
            record.addStep("Ma trận sau biến đổi " + (i+1) + ":", steps.get(i));
        }

        record.addStep("Ma trận RREF cuối cùng:", rref);

        // Đếm số hàng khác không
        int rank = 0;
        for (int i = 0; i < rref.getRows(); i++) {
            boolean nonZeroRow = false;
            for (int j = 0; j < rref.getColumns(); j++) {
                if (Math.abs(rref.getValue(i, j)) > 1e-10) {
                    nonZeroRow = true;
                    break;
                }
            }
            if (nonZeroRow) {
                rank++;
            }
        }

        record.addStep(String.format("Bước 2: Đếm số hàng khác 0 trong ma trận bậc thang"));
        record.addStep(String.format("Rank = %d", rank));

        MatrixModel resultMatrix = new MatrixModel(1, 1);
        resultMatrix.setValue(0, 0, rank);

        // Tạo record mới với kết quả cuối cùng
        record = new CalculationRecord("Rank", matrix, resultMatrix);
        return record;
    }

    /**
     * Solve system of linear equations Ax = b
     * @param coefficients coefficient matrix A
     * @param constants constant vector b
     * @return solution vector x
     */
    public static MatrixModel solveLinearSystem(MatrixModel coefficients, MatrixModel constants) {
        if (coefficients.getRows() != constants.getRows() || constants.getColumns() != 1) {
            throw new IllegalArgumentException("Dimensions mismatch for linear system");
        }

        try {
            // Using inverse method: x = A^-1 * b
            MatrixModel inverse = inverse(coefficients);
            return multiply(inverse, constants);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("System is singular or underdetermined");
        }
    }

    /**
     * Solve system of linear equations Ax = b with steps
     * @param coefficients coefficient matrix A
     * @param constants constant vector b
     * @return calculation record with steps
     */
    public static CalculationRecord solveLinearSystemWithSteps(MatrixModel coefficients, MatrixModel constants) {
        if (coefficients.getRows() != constants.getRows() || constants.getColumns() != 1) {
            throw new IllegalArgumentException("Dimensions mismatch for linear system");
        }

        CalculationRecord record = new CalculationRecord("Giải Hệ Phương Trình Tuyến Tính", coefficients, constants, null);

        record.addStep("Giải hệ phương trình tuyến tính Ax = b");
        record.addStep("A là ma trận hệ số " + coefficients.getRows() + "×" + coefficients.getColumns());
        record.addStep("b là vector vế phải:");

        // Hiển thị hệ phương trình
        StringBuilder equations = new StringBuilder();
        for (int i = 0; i < coefficients.getRows(); i++) {
            equations.append("Phương trình ").append(i+1).append(": ");

            for (int j = 0; j < coefficients.getColumns(); j++) {
                double coef = coefficients.getValue(i, j);
                if (j > 0) {
                    if (coef >= 0) {
                        equations.append(" + ");
                    } else {
                        equations.append(" - ");
                        coef = -coef;
                    }
                } else if (coef < 0) {
                    equations.append("-");
                    coef = -coef;
                }

                equations.append(String.format("%.2f×x%d", coef, j+1));
            }

            equations.append(" = ").append(String.format("%.2f", constants.getValue(i, 0))).append("\n");
        }
        record.addStep("Các phương trình:\n" + equations.toString());

        record.addStep("Phương pháp 1: Sử dụng ma trận nghịch đảo");
        record.addStep("x = A^(-1) × b");

        record.addStep("Bước 1: Tính định thức của ma trận hệ số A");
        double det = determinant(coefficients);
        record.addStep(String.format("det(A) = %.4f", det));

        if (Math.abs(det) < 1e-10) {
            throw new IllegalArgumentException("Hệ phương trình vô nghiệm hoặc vô số nghiệm (ma trận suy biến)");
        }

        record.addStep("Bước 2: Tính ma trận nghịch đảo của A");
        MatrixModel inverse = inverse(coefficients);
        record.addStep("Ma trận nghịch đảo A^(-1):", inverse);

        record.addStep("Bước 3: Tính x = A^(-1) × b");
        MatrixModel result = multiply(inverse, constants);

        // Hiển thị chi tiết nhân ma trận
        StringBuilder multiplySteps = new StringBuilder();
        for (int i = 0; i < result.getRows(); i++) {
            multiplySteps.append(String.format("x%d = ", i+1));

            for (int k = 0; k < coefficients.getRows(); k++) {
                multiplySteps.append(String.format("%.4f × %.4f", inverse.getValue(i, k), constants.getValue(k, 0)));

                if (k < coefficients.getRows() - 1) {
                    multiplySteps.append(" + ");
                }
            }

            multiplySteps.append(String.format(" = %.4f\n", result.getValue(i, 0)));
        }

        record.addStep("Chi tiết tính x = A^(-1) × b:\n" + multiplySteps.toString());
        record.addStep("Kết quả cuối cùng:", result);

        // Kiểm tra kết quả
        record.addStep("Kiểm tra kết quả:");
        MatrixModel check = multiply(coefficients, result);

        StringBuilder verification = new StringBuilder();
        for (int i = 0; i < check.getRows(); i++) {
            verification.append(String.format("Phương trình %d: %.4f ≈ %.4f %s\n",
                    i+1,
                    check.getValue(i, 0),
                    constants.getValue(i, 0),
                    Math.abs(check.getValue(i, 0) - constants.getValue(i, 0)) < 1e-10 ? "✓" : "✗"));
        }
        record.addStep("Kiểm tra A × x = b?\n" + verification.toString());

        // Tạo record mới với kết quả cuối cùng
        record = new CalculationRecord("Giải Hệ Phương Trình Tuyến Tính", coefficients, constants, result);
        return record;
    }

    /**
     * Get submatrix by removing specified row and column
     * @param matrix original matrix
     * @param rowToRemove row to remove
     * @param colToRemove column to remove
     * @return resulting submatrix
     */
    private static MatrixModel getSubMatrix(MatrixModel matrix, int rowToRemove, int colToRemove) {
        int n = matrix.getRows();
        MatrixModel subMatrix = new MatrixModel(n - 1, n - 1);

        int r = 0;
        for (int i = 0; i < n; i++) {
            if (i == rowToRemove) continue;

            int c = 0;
            for (int j = 0; j < n; j++) {
                if (j == colToRemove) continue;
                subMatrix.setValue(r, c, matrix.getValue(i, j));
                c++;
            }
            r++;
        }

        return subMatrix;
    }

    /**
     * Calculate Singular Value Decomposition of a matrix
     * @param matrix input matrix
     * @return array of three matrices [U, S, V^T] representing the SVD
     * @throws IllegalArgumentException if SVD computation fails
     */
    public static MatrixModel[] svd(MatrixModel matrix) {
        int m = matrix.getRows();
        int n = matrix.getColumns();

        // Đối với trường hợp ma trận 2x2 đơn giản, tính SVD trực tiếp
        if (m == 2 && n == 2) {
            // Bước 1: Tính A^T * A (ma trận V sẽ chứa eigenvectors của A^T * A)
            MatrixModel ata = multiply(transpose(matrix), matrix);

            // Bước 2: Tính eigenvalues và eigenvectors của A^T * A
            double a = ata.getValue(0, 0);
            double b = ata.getValue(0, 1);
            double c = ata.getValue(1, 0);
            double d = ata.getValue(1, 1);

            double trace = a + d;
            double det = a * d - b * c;

            double discriminant = trace * trace - 4 * det;

            if (discriminant < 0) {
                throw new IllegalArgumentException("Complex eigenvalues are not supported in SVD");
            }

            // Tính singular values (căn bậc 2 của eigenvalues)
            double lambda1 = (trace + Math.sqrt(discriminant)) / 2.0;
            double lambda2 = (trace - Math.sqrt(discriminant)) / 2.0;

            double sigma1 = Math.sqrt(Math.max(lambda1, 0));
            double sigma2 = Math.sqrt(Math.max(lambda2, 0));

            // Tạo ma trận Σ (singular values)
            MatrixModel S = new MatrixModel(m, n);
            S.setValue(0, 0, sigma1);
            if (m > 1 && n > 1) {
                S.setValue(1, 1, sigma2);
            }

            // Tính eigenvectors cho V
            MatrixModel V = new MatrixModel(n, n);

            if (Math.abs(b) < 1e-10 && Math.abs(c) < 1e-10) {
                // Nếu ma trận đã là đường chéo, V là ma trận đơn vị
                V.setValue(0, 0, 1);
                V.setValue(0, 1, 0);
                V.setValue(1, 0, 0);
                V.setValue(1, 1, 1);
            } else {
                double v1x, v1y, v2x, v2y;

                // Eigenvector đầu tiên
                if (Math.abs(b) > 1e-10) {
                    v1x = lambda1 - d;
                    v1y = c;
                } else {
                    v1x = b;
                    v1y = lambda1 - a;
                }

                // Eigenvector thứ hai
                if (Math.abs(b) > 1e-10) {
                    v2x = lambda2 - d;
                    v2y = c;
                } else {
                    v2x = b;
                    v2y = lambda2 - a;
                }

                // Chuẩn hóa eigenvectors
                double norm1 = Math.sqrt(v1x * v1x + v1y * v1y);
                if (norm1 > 1e-10) {
                    v1x /= norm1;
                    v1y /= norm1;
                }

                double norm2 = Math.sqrt(v2x * v2x + v2y * v2y);
                if (norm2 > 1e-10) {
                    v2x /= norm2;
                    v2y /= norm2;
                }

                V.setValue(0, 0, v1x);
                V.setValue(1, 0, v1y);
                V.setValue(0, 1, v2x);
                V.setValue(1, 1, v2y);
            }

            // Tính U = A * V * S^-1
            MatrixModel Vt = transpose(V);

            // Tạo ma trận S^-1 (nghịch đảo của ma trận đường chéo S)
            MatrixModel SInv = new MatrixModel(n, m);
            if (Math.abs(sigma1) > 1e-10) {
                SInv.setValue(0, 0, 1.0 / sigma1);
            }
            if (m > 1 && n > 1 && Math.abs(sigma2) > 1e-10) {
                SInv.setValue(1, 1, 1.0 / sigma2);
            }

            // U = A * V * S^-1
            MatrixModel U = multiply(multiply(matrix, V), SInv);

            // Trả về kết quả [U, S, V^T]
            return new MatrixModel[] { U, S, Vt };
        }

        // Cho ma trận lớn hơn hoặc trường hợp tổng quát, cần thuật toán phức tạp hơn
        throw new UnsupportedOperationException("SVD for matrices larger than 2x2 is not implemented");
    }

    public static CalculationRecord svdWithSteps(MatrixModel matrix) {
        MatrixModel[] svdMatrices = svd(matrix); // Sử dụng phương thức hiện có

        CalculationRecord record = new CalculationRecord(
                "Phân Tích SVD",
                matrix,
                svdMatrices[0], // Ma trận U
                svdMatrices[1], // Ma trận S
                svdMatrices[2]  // Ma trận V^T
        );

        record.addStep("Phương pháp phân tích SVD (Singular Value Decomposition):");
        record.addStep("Mục tiêu: Phân tích ma trận A thành tích U × Σ × V^T, trong đó:");
        record.addStep("- U là ma trận trực giao có các cột là vector riêng của AA^T");
        record.addStep("- Σ là ma trận đường chéo chứa các giá trị kỳ dị (singular values)");
        record.addStep("- V^T là ma trận trực giao chuyển vị có các hàng là vector riêng của A^T·A");

        record.addStep("Tính ma trận A^T·A");
        MatrixModel atA = OperationModel.multiply(OperationModel.transpose(matrix), matrix);
        record.addStep("A^T·A =", atA);

        record.addStep("Tính trị riêng và vector riêng của A^T·A");
        record.addStep("Các giá trị kỳ dị σᵢ là căn bậc hai của trị riêng λᵢ của A^T·A");

        // Hiển thị các giá trị kỳ dị
        MatrixModel S = svdMatrices[1];
        StringBuilder singularValues = new StringBuilder("Giá trị kỳ dị σᵢ:\n");
        for (int i = 0; i < Math.min(S.getRows(), S.getColumns()); i++) {
            if (Math.abs(S.getValue(i, i)) > 1e-10) {
                singularValues.append(String.format("σ%d = %.4f\n", i+1, S.getValue(i, i)));
            }
        }
        record.addStep(singularValues.toString());

        record.addStep("Tạo ma trận V từ các vector riêng của A^T·A");
        record.addStep("V^T =", svdMatrices[2]);

        record.addStep("Tính U = A·V·Σ^(-1)");
        record.addStep("U =", svdMatrices[0]);

        record.addStep("Kết quả cuối cùng:");
        record.addStep("A = U × Σ × V^T");
        record.addStep("phép toán khá dài và phức tạp, bạn chịu khó để hiểu nhé!!");

        return record;
    }
    /**
     * Calculate eigenvalues of a square matrix
     * @param matrix input square matrix
     * @return matrix containing eigenvalues on the diagonal
     * @throws IllegalArgumentException if matrix is not square
     */
    public static MatrixModel eigenvalues(MatrixModel matrix) {
        if (matrix.getRows() != matrix.getColumns()) {
            throw new IllegalArgumentException("Eigenvalues can only be calculated for square matrices");
        }

        int n = matrix.getRows();

        // Đối với ma trận 1x1
        if (n == 1) {
            MatrixModel result = new MatrixModel(1, 1);
            result.setValue(0, 0, matrix.getValue(0, 0));
            return result;
        }

        // Đối với ma trận 2x2
        if (n == 2) {
            double a = matrix.getValue(0, 0);
            double b = matrix.getValue(0, 1);
            double c = matrix.getValue(1, 0);
            double d = matrix.getValue(1, 1);

            double trace = a + d;
            double det = a * d - b * c;

            // Tính nghiệm của phương trình bậc hai: λ² - trace*λ + det = 0
            double discriminant = trace * trace - 4 * det;

            if (discriminant < 0) {
                // Trị riêng phức (không hỗ trợ trong phiên bản này)
                throw new IllegalArgumentException("Complex eigenvalues are not supported");
            }

            double lambda1 = (trace + Math.sqrt(discriminant)) / 2.0;
            double lambda2 = (trace - Math.sqrt(discriminant)) / 2.0;

            // Trả về ma trận đường chéo chứa các trị riêng
            MatrixModel result = new MatrixModel(n, n);
            result.setValue(0, 0, lambda1);
            result.setValue(1, 1, lambda2);
            return result;
        }

        // Đối với ma trận 3x3 (sử dụng phương pháp đơn giản hóa)
        if (n == 3) {
            // Triển khai phương pháp tính trị riêng cho ma trận 3x3
            // Đây là một phương pháp tương đối phức tạp, có thể sử dụng thư viện bên ngoài
            // Trong phiên bản hiện tại, ta sẽ chưa hỗ trợ
            throw new UnsupportedOperationException("Eigenvalue calculation for 3x3 matrices is not implemented yet");
        }

        // Đối với ma trận lớn hơn
        throw new UnsupportedOperationException("Eigenvalue calculation for matrices larger than 2x2 is not implemented");
    }

    public static CalculationRecord eigenvaluesWithSteps(MatrixModel matrix) {
        if (matrix.getRows() != matrix.getColumns()) {
            throw new IllegalArgumentException("Eigenvalues can only be calculated for square matrices");
        }

        MatrixModel result = eigenvalues(matrix); // Sử dụng phương thức hiện có
        CalculationRecord record = new CalculationRecord("Trị Riêng", matrix, result);

        // Thêm các bước chi tiết
        record.addStep("Bước 1: Thiết lập phương trình đặc trưng |A - λI| = 0");

        int n = matrix.getRows();
        if (n == 2) {
            double a = matrix.getValue(0, 0);
            double b = matrix.getValue(0, 1);
            double c = matrix.getValue(1, 0);
            double d = matrix.getValue(1, 1);

            record.addStep(String.format("Ma trận A = [[%.2f, %.2f], [%.2f, %.2f]]", a, b, c, d));
            record.addStep("Bước 2: Tính phương trình đặc trưng");
            record.addStep(String.format("det([[%.2f - λ, %.2f], [%.2f, %.2f - λ]]) = 0", a, b, c, d));
            record.addStep(String.format("(%.2f - λ)(%.2f - λ) - %.2f·%.2f = 0", a, d, b, c));

            double trace = a + d;
            double det = a * d - b * c;

            record.addStep(String.format("λ² - %.2fλ + %.2f = 0", trace, det));

            // Giải phương trình bậc 2
            record.addStep("Bước 3: Giải phương trình bậc hai để tìm các trị riêng");

            double discriminant = trace * trace - 4 * det;
            double lambda1 = (trace + Math.sqrt(discriminant)) / 2.0;
            double lambda2 = (trace - Math.sqrt(discriminant)) / 2.0;

            record.addStep(String.format("Δ = %.2f² - 4·%.2f = %.4f", trace, det, discriminant));
            record.addStep(String.format("λ₁ = (%.2f + √%.4f)/2 = %.4f", trace, discriminant, lambda1));
            record.addStep(String.format("λ₂ = (%.2f - √%.4f)/2 = %.4f", trace, discriminant, lambda2));
        } else {
            record.addStep("Đối với ma trận " + n + "x" + n + ", phương trình đặc trưng trở nên phức tạp.");
            record.addStep("Các bước cơ bản bao gồm:");
            record.addStep("1. Thiết lập phương trình |A - λI| = 0");
            record.addStep("2. Khai triển định thức để có đa thức bậc " + n + " theo λ");
            record.addStep("3. Giải phương trình đa thức để tìm các nghiệm λ₁, λ₂, ...");
        }

        record.addStep("Kết quả: Ma trận đường chéo chứa các trị riêng:", result);
        return record;
    }
    /**
     * Calculate Reduced Row Echelon Form (RREF) of matrix
     * @param matrix input matrix
     * @return matrix in RREF
     */
    private static MatrixModel getRREF(MatrixModel matrix) {
        int lead = 0;
        int rowCount = matrix.getRows();
        int colCount = matrix.getColumns();

        for (int r = 0; r < rowCount; r++) {
            if (lead >= colCount) {
                break;
            }

            int i = r;
            while (Math.abs(matrix.getValue(i, lead)) < 1e-10) {
                i++;
                if (i == rowCount) {
                    i = r;
                    lead++;
                    if (lead == colCount) {
                        break;
                    }
                }
            }

            if (lead < colCount) {
                // Swap rows
                for (int j = 0; j < colCount; j++) {
                    double temp = matrix.getValue(r, j);
                    matrix.setValue(r, j, matrix.getValue(i, j));
                    matrix.setValue(i, j, temp);
                }

                // Scale row
                double div = matrix.getValue(r, lead);
                if (Math.abs(div) > 1e-10) {
                    for (int j = 0; j < colCount; j++) {
                        matrix.setValue(r, j, matrix.getValue(r, j) / div);
                    }
                }

                // Eliminate other rows
                for (int j = 0; j < rowCount; j++) {
                    if (j != r) {
                        double sub = matrix.getValue(j, lead);
                        for (int k = 0; k < colCount; k++) {
                            matrix.setValue(j, k, matrix.getValue(j, k) - sub * matrix.getValue(r, k));
                        }
                    }
                }

                lead++;
            }
        }

        return matrix;
    }

    /**
     * Calculate Reduced Row Echelon Form (RREF) of matrix with steps
     * @param matrix input matrix
     * @param steps list to store intermediate steps
     * @return matrix in RREF
     */
    private static MatrixModel getRREFWithSteps(MatrixModel matrix, List<MatrixModel> steps) {
        int lead = 0;
        int rowCount = matrix.getRows();
        int colCount = matrix.getColumns();

        for (int r = 0; r < rowCount; r++) {
            if (lead >= colCount) {
                break;
            }

            int i = r;
            while (Math.abs(matrix.getValue(i, lead)) < 1e-10) {
                i++;
                if (i == rowCount) {
                    i = r;
                    lead++;
                    if (lead == colCount) {
                        break;
                    }
                }
            }

            if (lead < colCount) {
                // Swap rows if needed
                if (i != r) {
                    for (int j = 0; j < colCount; j++) {
                        double temp = matrix.getValue(r, j);
                        matrix.setValue(r, j, matrix.getValue(i, j));
                        matrix.setValue(i, j, temp);
                    }
                    steps.add(matrix.copy());
                }

                // Scale row
                double div = matrix.getValue(r, lead);
                if (Math.abs(div) > 1e-10) {
                    for (int j = 0; j < colCount; j++) {
                        matrix.setValue(r, j, matrix.getValue(r, j) / div);
                    }
                    steps.add(matrix.copy());
                }

                // Eliminate other rows
                for (int j = 0; j < rowCount; j++) {
                    if (j != r) {
                        double sub = matrix.getValue(j, lead);
                        if (Math.abs(sub) > 1e-10) {
                            for (int k = 0; k < colCount; k++) {
                                matrix.setValue(j, k, matrix.getValue(j, k) - sub * matrix.getValue(r, k));
                            }
                            steps.add(matrix.copy());
                        }
                    }
                }

                lead++;
            }
        }

        return matrix;
    }
}