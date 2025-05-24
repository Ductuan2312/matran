package com.example.matran.Model;

import org.junit.Test;
import static org.junit.Assert.*;

public class MatrixOperationsTest {

    @Test
    public void testMatrixCreation() {
        MatrixModel matrix = new MatrixModel(3, 4);
        assertEquals("Matrix should have 3 rows", 3, matrix.getRows());
        assertEquals("Matrix should have 4 columns", 4, matrix.getColumns());
    }

    @Test
    public void testSetAndGetValue() {
        MatrixModel matrix = new MatrixModel(2, 2);
        matrix.setValue(0, 0, 1.5);
        matrix.setValue(0, 1, 2.5);
        matrix.setValue(1, 0, 3.5);
        matrix.setValue(1, 1, 4.5);

        assertEquals("Value at (0,0) should be 1.5", 1.5, matrix.getValue(0, 0), 0.001);
        assertEquals("Value at (0,1) should be 2.5", 2.5, matrix.getValue(0, 1), 0.001);
        assertEquals("Value at (1,0) should be 3.5", 3.5, matrix.getValue(1, 0), 0.001);
        assertEquals("Value at (1,1) should be 4.5", 4.5, matrix.getValue(1, 1), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidSetValue() {
        MatrixModel matrix = new MatrixModel(2, 2);
        matrix.setValue(2, 0, 5.0); // This should throw an exception
    }

    @Test
    public void testMatrixAddition() {
        // Create first matrix
        MatrixModel matrixA = new MatrixModel(2, 2);
        matrixA.setValue(0, 0, 1.0);
        matrixA.setValue(0, 1, 2.0);
        matrixA.setValue(1, 0, 3.0);
        matrixA.setValue(1, 1, 4.0);

        // Create second matrix
        MatrixModel matrixB = new MatrixModel(2, 2);
        matrixB.setValue(0, 0, 5.0);
        matrixB.setValue(0, 1, 6.0);
        matrixB.setValue(1, 0, 7.0);
        matrixB.setValue(1, 1, 8.0);

        // Perform addition
        MatrixModel result = OperationModel.add(matrixA, matrixB);

        // Verify result
        assertEquals("Result should have 2 rows", 2, result.getRows());
        assertEquals("Result should have 2 columns", 2, result.getColumns());
        assertEquals("Value at (0,0) should be 6.0", 6.0, result.getValue(0, 0), 0.001);
        assertEquals("Value at (0,1) should be 8.0", 8.0, result.getValue(0, 1), 0.001);
        assertEquals("Value at (1,0) should be 10.0", 10.0, result.getValue(1, 0), 0.001);
        assertEquals("Value at (1,1) should be 12.0", 12.0, result.getValue(1, 1), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatrixAdditionWithDifferentDimensions() {
        MatrixModel matrixA = new MatrixModel(2, 2);
        MatrixModel matrixB = new MatrixModel(2, 3);
        OperationModel.add(matrixA, matrixB); // Should throw exception
    }

    @Test
    public void testMatrixSubtraction() {
        MatrixModel matrixA = new MatrixModel(2, 2);
        matrixA.setValue(0, 0, 5.0);
        matrixA.setValue(0, 1, 6.0);
        matrixA.setValue(1, 0, 7.0);
        matrixA.setValue(1, 1, 8.0);

        MatrixModel matrixB = new MatrixModel(2, 2);
        matrixB.setValue(0, 0, 1.0);
        matrixB.setValue(0, 1, 2.0);
        matrixB.setValue(1, 0, 3.0);
        matrixB.setValue(1, 1, 4.0);

        MatrixModel result = OperationModel.subtract(matrixA, matrixB);

        assertEquals("Value at (0,0) should be 4.0", 4.0, result.getValue(0, 0), 0.001);
        assertEquals("Value at (0,1) should be 4.0", 4.0, result.getValue(0, 1), 0.001);
        assertEquals("Value at (1,0) should be 4.0", 4.0, result.getValue(1, 0), 0.001);
        assertEquals("Value at (1,1) should be 4.0", 4.0, result.getValue(1, 1), 0.001);
    }

    @Test
    public void testMatrixMultiplication() {
        MatrixModel matrixA = new MatrixModel(2, 3);
        matrixA.setValue(0, 0, 1.0);
        matrixA.setValue(0, 1, 2.0);
        matrixA.setValue(0, 2, 3.0);
        matrixA.setValue(1, 0, 4.0);
        matrixA.setValue(1, 1, 5.0);
        matrixA.setValue(1, 2, 6.0);

        MatrixModel matrixB = new MatrixModel(3, 2);
        matrixB.setValue(0, 0, 7.0);
        matrixB.setValue(0, 1, 8.0);
        matrixB.setValue(1, 0, 9.0);
        matrixB.setValue(1, 1, 10.0);
        matrixB.setValue(2, 0, 11.0);
        matrixB.setValue(2, 1, 12.0);

        MatrixModel result = OperationModel.multiply(matrixA, matrixB);

        assertEquals("Result should have 2 rows", 2, result.getRows());
        assertEquals("Result should have 2 columns", 2, result.getColumns());
        assertEquals("Value at (0,0) should be 58.0", 58.0, result.getValue(0, 0), 0.001);
        assertEquals("Value at (0,1) should be 64.0", 64.0, result.getValue(0, 1), 0.001);
        assertEquals("Value at (1,0) should be 139.0", 139.0, result.getValue(1, 0), 0.001);
        assertEquals("Value at (1,1) should be 154.0", 154.0, result.getValue(1, 1), 0.001);
    }

    @Test
    public void testMatrixTranspose() {
        MatrixModel matrix = new MatrixModel(2, 3);
        matrix.setValue(0, 0, 1.0);
        matrix.setValue(0, 1, 2.0);
        matrix.setValue(0, 2, 3.0);
        matrix.setValue(1, 0, 4.0);
        matrix.setValue(1, 1, 5.0);
        matrix.setValue(1, 2, 6.0);

        MatrixModel transposed = OperationModel.transpose(matrix);

        assertEquals("Transposed should have 3 rows", 3, transposed.getRows());
        assertEquals("Transposed should have 2 columns", 2, transposed.getColumns());
        assertEquals("Value at (0,0) should be 1.0", 1.0, transposed.getValue(0, 0), 0.001);
        assertEquals("Value at (0,1) should be 4.0", 4.0, transposed.getValue(0, 1), 0.001);
        assertEquals("Value at (1,0) should be 2.0", 2.0, transposed.getValue(1, 0), 0.001);
        assertEquals("Value at (1,1) should be 5.0", 5.0, transposed.getValue(1, 1), 0.001);
        assertEquals("Value at (2,0) should be 3.0", 3.0, transposed.getValue(2, 0), 0.001);
        assertEquals("Value at (2,1) should be 6.0", 6.0, transposed.getValue(2, 1), 0.001);
    }

    @Test
    public void testDeterminant2x2() {
        MatrixModel matrix = new MatrixModel(2, 2);
        matrix.setValue(0, 0, 1.0);
        matrix.setValue(0, 1, 2.0);
        matrix.setValue(1, 0, 3.0);
        matrix.setValue(1, 1, 4.0);

        double det = OperationModel.determinant(matrix);
        assertEquals("Determinant should be -2.0", -2.0, det, 0.001);
    }

    @Test
    public void testCalculationRecord() {
        MatrixModel matrixA = new MatrixModel(2, 2);
        matrixA.setValue(0, 0, 1.0);
        matrixA.setValue(0, 1, 2.0);
        matrixA.setValue(1, 0, 3.0);
        matrixA.setValue(1, 1, 4.0);

        MatrixModel result = new MatrixModel(2, 2);
        result.setValue(0, 0, 1.0);
        result.setValue(0, 1, 3.0);
        result.setValue(1, 0, 2.0);
        result.setValue(1, 1, 4.0);

        CalculationRecord record = new CalculationRecord("Transpose", matrixA, result);

        assertEquals("Operation type should be 'Transpose'", "Transpose", record.getOperationType());
        assertNotNull("Timestamp should be initialized", record.getTimestamp());
        assertFalse("Record should have one input", record.hasTwoInputs());
        
        // Test record with two inputs
        MatrixModel matrixB = new MatrixModel(2, 2);
        CalculationRecord recordWithTwoInputs = new CalculationRecord("Add Matrices", matrixA, matrixB, result);
        assertTrue("Record should have two inputs", recordWithTwoInputs.hasTwoInputs());
    }
}
