package com.example.matran.Utils;

import android.content.Context;

import com.example.matran.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Lớp tiện ích để chuyển đổi mã phép toán thành tên hiển thị tiếng Việt
 */
public class OperationUtils {
    
    // Ánh xạ từ mã phép toán sang resource ID chuỗi
    private static final Map<String, Integer> operationStringMap = new HashMap<>();
    
    static {
        operationStringMap.put("ADD", R.string.operation_add);
        operationStringMap.put("SUBTRACT", R.string.operation_subtract);
        operationStringMap.put("MULTIPLY", R.string.operation_multiply);
        operationStringMap.put("TRANSPOSE", R.string.operation_transpose);
        operationStringMap.put("DETERMINANT", R.string.operation_determinant);
        operationStringMap.put("INVERSE", R.string.operation_inverse);
        operationStringMap.put("CONVOLUTION", R.string.operation_convolution);
        operationStringMap.put("EIGENVALUES", R.string.operation_eigenvalues);
        operationStringMap.put("SVD", R.string.operation_svd);
        operationStringMap.put("RANK", R.string.operation_rank);
        operationStringMap.put("LINEAR_SYSTEM", R.string.operation_linear_system);
        
        // Các tên hiển thị tiếng Anh
        operationStringMap.put("Add Matrices", R.string.operation_add);
        operationStringMap.put("Subtract Matrices", R.string.operation_subtract);
        operationStringMap.put("Multiply Matrices", R.string.operation_multiply);
        operationStringMap.put("Transpose", R.string.operation_transpose);
        operationStringMap.put("Determinant", R.string.operation_determinant);
        operationStringMap.put("Inverse Matrix", R.string.operation_inverse);
        operationStringMap.put("Convolution", R.string.operation_convolution);
        operationStringMap.put("Eigenvalues", R.string.operation_eigenvalues);
        operationStringMap.put("SVD Decomposition", R.string.operation_svd);
        operationStringMap.put("Matrix Rank", R.string.operation_rank);
        operationStringMap.put("Linear System", R.string.operation_linear_system);
    }
    
    /**
     * Chuyển đổi mã phép toán thành tên hiển thị tiếng Việt
     * 
     * @param context Context để truy cập resources
     * @param operationType Mã phép toán (ADD, SUBTRACT, v.v.)
     * @return Tên phép toán tiếng Việt
     */
    public static String getOperationDisplayName(Context context, String operationType) {
        Integer stringResId = operationStringMap.get(operationType);
        if (stringResId != null) {
            return context.getString(stringResId);
        } else {
            return operationType; // Trả về mã nếu không tìm thấy ánh xạ
        }
    }
}