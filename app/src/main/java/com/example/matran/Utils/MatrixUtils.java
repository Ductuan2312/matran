//matran - A matrix manipulation application
//        Copyright (c) 2025 Đức Tuân
//
//        Licensed under the MIT License. See LICENSE file in the project root for full license information.
package com.example.matran.Utils;

import com.example.matran.Model.MatrixModel;

/**
 * Utility class for matrix operations
 */
public class MatrixUtils {

    /**
     * Format matrix for display
     * @param matrix input matrix
     * @param precision number of decimal places
     * @return formatted string representation
     */
    public static String formatMatrixString(MatrixModel matrix, int precision) {
        if (matrix == null) {
            return "null";
        }
        
        StringBuilder sb = new StringBuilder();
        String format = "%." + precision + "f";
        
        for (int i = 0; i < matrix.getRows(); i++) {
            sb.append("[ ");
            for (int j = 0; j < matrix.getColumns(); j++) {
                sb.append(String.format(format, matrix.getValue(i, j)));
                if (j < matrix.getColumns() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(" ]\n");
        }
        
        return sb.toString();
    }

    /**
     * Get LaTeX representation of a matrix
     * @param matrix input matrix
     * @param precision number of decimal places
     * @return LaTeX string
     */
    public static String getMatrixLatex(MatrixModel matrix, int precision) {
        if (matrix == null) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        String format = "%." + precision + "f";
        
        sb.append("\\begin{bmatrix}\n");
        
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getColumns(); j++) {
                sb.append(String.format(format, matrix.getValue(i, j)));
                if (j < matrix.getColumns() - 1) {
                    sb.append(" & ");
                }
            }
            
            if (i < matrix.getRows() - 1) {
                sb.append(" \\\\\n");
            }
        }
        
        sb.append("\n\\end{bmatrix}");
        
        return sb.toString();
    }
    
    /**
     * Check if two matrices have same dimensions
     * @param a first matrix
     * @param b second matrix
     * @return true if dimensions match
     */
    public static boolean sameDimensions(MatrixModel a, MatrixModel b) {
        if (a == null || b == null) {
            return false;
        }
        return a.getRows() == b.getRows() && a.getColumns() == b.getColumns();
    }
    
    /**
     * Check if matrices can be multiplied
     * @param a first matrix
     * @param b second matrix
     * @return true if multiplication is possible
     */
    public static boolean canMultiply(MatrixModel a, MatrixModel b) {
        if (a == null || b == null) {
            return false;
        }
        return a.getColumns() == b.getRows();
    }
}
    