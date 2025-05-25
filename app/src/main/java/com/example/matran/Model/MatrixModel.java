//matran - A matrix manipulation application
//        Copyright (c) 2025 Đức Tuân
//
//        Licensed under the MIT License. See LICENSE file in the project root for full license information.
package com.example.matran.Model;

import java.io.Serializable;

/**
 * Model class to represent matrices in the application
 */
public class MatrixModel implements Serializable {
    private double[][] data;
    private int rows;
    private int columns;

    /**
     * Constructor to create a matrix with specified dimensions
     * @param rows number of rows
     * @param columns number of columns
     */
    public MatrixModel(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.data = new double[rows][columns];
    }

    /**
     * Set value at specific position in matrix
     * @param row row index
     * @param column column index
     * @param value value to set
     */
    public void setValue(int row, int column, double value) {
        if (row < 0 || row >= rows || column < 0 || column >= columns) {
            throw new IndexOutOfBoundsException("Invalid matrix indices");
        }
        data[row][column] = value;
    }

    /**
     * Get value at specific position in matrix
     * @param row row index
     * @param column column index
     * @return value at specified position
     */
    public double getValue(int row, int column) {
        if (row < 0 || row >= rows || column < 0 || column >= columns) {
            throw new IndexOutOfBoundsException("Invalid matrix indices");
        }
        return data[row][column];
    }

    /**
     * Get number of rows
     * @return number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Get number of columns
     * @return number of columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Get raw data array
     * @return 2D array containing matrix data
     */
    public double[][] getData() {
        return data;
    }

    /**
     * Create a deep copy of the matrix
     * @return new MatrixModel with same values
     */
    public MatrixModel copy() {
        MatrixModel copy = new MatrixModel(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                copy.setValue(i, j, data[i][j]);
            }
        }
        return copy;
    }

    /**
     * Convert matrix to string for display
     * @return string representation of matrix
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            sb.append("[ ");
            for (int j = 0; j < columns; j++) {
                sb.append(data[i][j]);
                if (j < columns - 1) {
                    sb.append(", ");
                }
            }
            sb.append(" ]\n");
        }
        return sb.toString();
    }
}