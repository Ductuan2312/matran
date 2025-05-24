package com.example.matran.Model;

import android.util.Log;

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
}