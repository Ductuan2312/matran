//matran - A matrix manipulation application
//        Copyright (c) 2025 Đức Tuân
//
//        Licensed under the MIT License. See LICENSE file in the project root for full license information.
package com.example.matran.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model class to represent a single calculation record for history
 */
public class CalculationRecord implements Serializable {
    private String operationType;
    private MatrixModel inputMatrixA;
    private MatrixModel inputMatrixB;  // Nullable for operations with single input
    private MatrixModel resultMatrix;
    private Date timestamp;
    private MatrixModel matrixU;
    private MatrixModel matrixS;
    private MatrixModel matrixVT;
    private boolean isSVDResult;

    // Mảng lưu các bước tính toán
    private List<CalculationStep> calculationSteps;

    /**
     * Constructor for operations with one input matrix
     * @param operationType type of operation performed
     * @param inputMatrix input matrix
     * @param resultMatrix result matrix
     */
    public CalculationRecord(String operationType, MatrixModel inputMatrix, MatrixModel resultMatrix) {
        this.operationType = operationType;
        this.inputMatrixA = inputMatrix;
        this.inputMatrixB = null;
        this.resultMatrix = resultMatrix;
        this.timestamp = new Date();
        this.calculationSteps = new ArrayList<>();
    }

    // Constructor cho kết quả SVD
    public CalculationRecord(String operationType, MatrixModel inputMatrixA, MatrixModel matrixU, MatrixModel matrixS, MatrixModel matrixVT) {
        this.operationType = operationType;
        this.inputMatrixA = inputMatrixA;
        this.matrixU = matrixU;
        this.matrixS = matrixS;
        this.matrixVT = matrixVT;
        this.resultMatrix = matrixS; // Sử dụng ma trận S làm resultMatrix chính
        this.isSVDResult = true;
        this.timestamp = new Date();
        this.calculationSteps = new ArrayList<>();
    }

    /**
     * Constructor for operations with two input matrices
     * @param operationType type of operation performed
     * @param inputMatrixA first input matrix
     * @param inputMatrixB second input matrix
     * @param resultMatrix result matrix
     */
    public CalculationRecord(String operationType, MatrixModel inputMatrixA,
                             MatrixModel inputMatrixB, MatrixModel resultMatrix) {
        this.operationType = operationType;
        this.inputMatrixA = inputMatrixA;
        this.inputMatrixB = inputMatrixB;
        this.resultMatrix = resultMatrix;
        this.timestamp = new Date();
        this.calculationSteps = new ArrayList<>();
    }

    /**
     * Thêm một bước tính toán
     * @param description mô tả bước tính toán
     * @param intermediateResult kết quả trung gian (có thể null)
     */
    public void addStep(String description, MatrixModel intermediateResult) {
        if (calculationSteps == null) {
            calculationSteps = new ArrayList<>();
        }
        calculationSteps.add(new CalculationStep(description, intermediateResult));
    }

    /**
     * Thêm một bước tính toán chỉ có mô tả
     * @param description mô tả bước tính toán
     */
    public void addStep(String description) {
        addStep(description, null);
    }

    /**
     * Lấy danh sách các bước tính toán
     * @return danh sách các bước tính toán
     */
    public List<CalculationStep> getCalculationSteps() {
        return calculationSteps;
    }

    /**
     * Kiểm tra xem có các bước tính toán chi tiết không
     * @return true nếu có các bước chi tiết
     */
    public boolean hasCalculationSteps() {
        return calculationSteps != null && !calculationSteps.isEmpty();
    }

    // Các getters mới
    public MatrixModel getMatrixU() {
        return matrixU;
    }

    public MatrixModel getMatrixS() {
        return matrixS;
    }

    public MatrixModel getMatrixVT() {
        return matrixVT;
    }

    public boolean isSVDResult() {
        return isSVDResult;
    }

    /**
     * Get operation type
     * @return operation type as string
     */
    public String getOperationType() {
        return operationType;
    }

    /**
     * Get first input matrix
     * @return input matrix A
     */
    public MatrixModel getInputMatrixA() {
        return inputMatrixA;
    }

    /**
     * Get second input matrix if available
     * @return input matrix B or null
     */
    public MatrixModel getInputMatrixB() {
        return inputMatrixB;
    }

    /**
     * Get result matrix
     * @return result matrix
     */
    public MatrixModel getResultMatrix() {
        return resultMatrix;
    }

    /**
     * Get timestamp of calculation
     * @return timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Check if operation had two input matrices
     * @return true if operation had two input matrices
     */
    public boolean hasTwoInputs() {
        return inputMatrixB != null;
    }

    /**
     * Get string representation for display in history
     * @return formatted string describing the calculation
     */
    @Override
    public String toString() {
        return operationType + " (" +
                inputMatrixA.getRows() + "x" + inputMatrixA.getColumns() + ")" +
                (hasTwoInputs() ? " with (" + inputMatrixB.getRows() + "x" +
                        inputMatrixB.getColumns() + ")" : "") +
                " → (" + resultMatrix.getRows() + "x" + resultMatrix.getColumns() + ")";
    }

    /**
     * Inner class to represent a single calculation step
     */
    public static class CalculationStep implements Serializable {
        private String description;
        private MatrixModel intermediateResult;

        public CalculationStep(String description, MatrixModel intermediateResult) {
            this.description = description;
            this.intermediateResult = intermediateResult;
        }

        public String getDescription() {
            return description;
        }

        public MatrixModel getIntermediateResult() {
            return intermediateResult;
        }

        public boolean hasIntermediateResult() {
            return intermediateResult != null;
        }
    }
}