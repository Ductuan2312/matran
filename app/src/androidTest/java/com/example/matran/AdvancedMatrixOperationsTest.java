package com.example.matran;

import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.matran.Controller.MainActivity;
import com.example.matran.Model.MatrixModel;
import com.example.matran.Model.OperationModel;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * JUnit test cho các phép tính nâng cao và tương tác UI
 */
@RunWith(AndroidJUnit4.class)
public class AdvancedMatrixOperationsTest {

    // Rule để chạy MainActivity
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    // Ma trận mẫu để kiểm tra
    private MatrixModel matrixA; // 3x3 ma trận đặc biệt
    private MatrixModel kernelMatrix; // Ma trận kernel 2x2 cho tích chập
    private MatrixModel linearSystemCoeff; // Ma trận hệ số cho hệ phương trình
    private MatrixModel linearSystemConst; // Ma trận hằng số cho hệ phương trình

    @Before
    public void setUp() {
        // Ma trận A 3x3 đặc biệt có trị riêng dễ tính: [4, 1, 1; 1, 2, 1; 1, 1, 2]
        matrixA = new MatrixModel(3, 3);
        matrixA.setValue(0, 0, 4);
        matrixA.setValue(0, 1, 1);
        matrixA.setValue(0, 2, 1);
        matrixA.setValue(1, 0, 1);
        matrixA.setValue(1, 1, 2);
        matrixA.setValue(1, 2, 1);
        matrixA.setValue(2, 0, 1);
        matrixA.setValue(2, 1, 1);
        matrixA.setValue(2, 2, 2);

        // Ma trận kernel 2x2 cho tích chập: [1, 0; 2, -1]
        kernelMatrix = new MatrixModel(2, 2);
        kernelMatrix.setValue(0, 0, 1);
        kernelMatrix.setValue(0, 1, 0);
        kernelMatrix.setValue(1, 0, 2);
        kernelMatrix.setValue(1, 1, -1);

        // Ma trận hệ số cho hệ phương trình: [2, 1, -1; -3, -1, 2; -2, 1, 2]
        linearSystemCoeff = new MatrixModel(3, 3);
        linearSystemCoeff.setValue(0, 0, 2);
        linearSystemCoeff.setValue(0, 1, 1);
        linearSystemCoeff.setValue(0, 2, -1);
        linearSystemCoeff.setValue(1, 0, -3);
        linearSystemCoeff.setValue(1, 1, -1);
        linearSystemCoeff.setValue(1, 2, 2);
        linearSystemCoeff.setValue(2, 0, -2);
        linearSystemCoeff.setValue(2, 1, 1);
        linearSystemCoeff.setValue(2, 2, 2);

        // Ma trận hằng số cho hệ phương trình: [8; -11; -3]
        linearSystemConst = new MatrixModel(3, 1);
        linearSystemConst.setValue(0, 0, 8);
        linearSystemConst.setValue(1, 0, -11);
        linearSystemConst.setValue(2, 0, -3);
    }

    // PHẦN 1: TEST LOGIC TÍNH TOÁN NÂNG CAO

    @Test
    public void testConvolution() {
        // Tính tích chập của matrixA và kernelMatrix
        MatrixModel result = OperationModel.convolution(matrixA, kernelMatrix);

        // Kết quả nên là ma trận 2x2
        assertEquals(2, result.getRows());
        assertEquals(2, result.getColumns());

        // Kiểm tra kết quả tích chập
        // Phép tích chập giữa [4 1 1; 1 2 1; 1 1 2] và [1 0; 2 -1]
        // = [4*1 + 1*0 + 1*2 + 2*(-1), 1*1 + 1*0 + 2*2 + 1*(-1);
        //    1*1 + 1*0 + 1*2 + 1*(-1), 2*1 + 1*0 + 1*2 + 2*(-1)]
        // = [4 + 0 + 2 - 2, 1 + 0 + 4 - 1; 1 + 0 + 2 - 1, 2 + 0 + 2 - 2]
        // = [4, 4; 2, 2]
        assertEquals(4.0, result.getValue(0, 0), 0.001);
        assertEquals(4.0, result.getValue(0, 1), 0.001);
        assertEquals(2.0, result.getValue(1, 0), 0.001);
        assertEquals(2.0, result.getValue(1, 1), 0.001); // Sửa giá trị kỳ vọng từ 1.0 thành 2.0
    }

    // Các test case khác giữ nguyên
    @Test
    public void testRank() {
        // Ma trận A có hạng 3 (full rank)
        int rank = OperationModel.rank(matrixA);
        assertEquals(3, rank);

        // Tạo ma trận không full rank (hạng 2)
        MatrixModel notFullRank = new MatrixModel(3, 3);
        notFullRank.setValue(0, 0, 1);
        notFullRank.setValue(0, 1, 2);
        notFullRank.setValue(0, 2, 3);
        notFullRank.setValue(1, 0, 4);
        notFullRank.setValue(1, 1, 5);
        notFullRank.setValue(1, 2, 6);
        notFullRank.setValue(2, 0, 7);
        notFullRank.setValue(2, 1, 8);
        notFullRank.setValue(2, 2, 9);

        // Kiểm tra kết quả hạng của ma trận
        int rankNotFull = OperationModel.rank(notFullRank);
        assertEquals(2, rankNotFull);
    }

    @Test
    public void testLinearSystemSolve() {
        // Giải hệ phương trình Ax = b với A là linearSystemCoeff và b là linearSystemConst
        // Hệ có nghiệm x = [2, 3, -1]
        MatrixModel solution = OperationModel.solveLinearSystem(linearSystemCoeff, linearSystemConst);

        // Kiểm tra kết quả
        assertEquals(3, solution.getRows());
        assertEquals(1, solution.getColumns());
        assertEquals(2.0, solution.getValue(0, 0), 0.001);
        assertEquals(3.0, solution.getValue(1, 0), 0.001);
        assertEquals(-1.0, solution.getValue(2, 0), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingularLinearSystem() {
        // Tạo ma trận hệ số kỳ dị
        MatrixModel singularCoeff = new MatrixModel(3, 3);
        singularCoeff.setValue(0, 0, 1);
        singularCoeff.setValue(0, 1, 2);
        singularCoeff.setValue(0, 2, 3);
        singularCoeff.setValue(1, 0, 2);
        singularCoeff.setValue(1, 1, 4);
        singularCoeff.setValue(1, 2, 6);
        singularCoeff.setValue(2, 0, 3);
        singularCoeff.setValue(2, 1, 6);
        singularCoeff.setValue(2, 2, 9);

        // Giải hệ phương trình với ma trận hệ số kỳ dị
        // Phải ném ra ngoại lệ
        OperationModel.solveLinearSystem(singularCoeff, linearSystemConst);
    }

    @Test
    public void testConvolutionValidation() {
        try {
            // Tạo ma trận nhỏ hơn kernel
            MatrixModel smallMatrix = new MatrixModel(1, 1);
            smallMatrix.setValue(0, 0, 5);

            // Phải ném ra ngoại lệ
            OperationModel.convolution(smallMatrix, kernelMatrix);
            assertTrue("Phải ném ra ngoại lệ khi ma trận nhỏ hơn kernel", false);
        } catch (IllegalArgumentException e) {
            // Test pass nếu ném ra ngoại lệ
            assertTrue(true);
        }
    }

    // PHẦN 2: TEST TƯƠNG TÁC UI (giữ nguyên như cũ)
    @Test
    public void testAdvancedOperationsDisplayed() {
        // Kiểm tra hiển thị của phần Phép toán nâng cao
        onView(withId(R.id.advanced_operations_grid)).check(matches(isDisplayed()));
    }

    @Test
    public void testAdvancedOperationTitlesDisplayed() {
        // Kiểm tra hiển thị của các tiêu đề phép tính nâng cao
        onView(withText("Tích Chập")).check(matches(isDisplayed()));
        onView(withText("Trị Riêng")).check(matches(isDisplayed()));
        onView(withText("Phân Tích SVD")).check(matches(isDisplayed()));
        onView(withText("Hạng Ma Trận")).check(matches(isDisplayed()));
        onView(withText("Hệ Phương Trình Tuyến Tính")).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigateToConvolution() {
        // Click vào nút "Tích Chập"
        onView(withText("Tích Chập")).perform(click());

        // Kiểm tra đã chuyển sang MatrixInputActivity, Toolbar hiển thị và tiêu đề chính xác
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(allOf(
                instanceOf(TextView.class),
                withParent(withId(R.id.toolbar))
        )).check(matches(withText(containsString("Tích Chập"))));

        // Kiểm tra hiển thị hai bảng ma trận (vì tích chập cần hai ma trận)
        onView(withId(R.id.matrix_input_table)).check(matches(isDisplayed()));
        onView(withId(R.id.second_matrix_input_table)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigateToMatrixRank() {
        // Click vào nút "Hạng Ma Trận"
        onView(withText("Hạng Ma Trận")).perform(click());

        // Kiểm tra đã chuyển sang MatrixInputActivity và hiển thị đúng
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.matrix_input_table)).check(matches(isDisplayed()));

        // Hạng ma trận chỉ cần một ma trận đầu vào
        onView(allOf(
                instanceOf(TextView.class),
                withParent(withId(R.id.toolbar))
        )).check(matches(withText(containsString("Hạng Ma Trận"))));
    }

    @Test
    public void testMatrixInputFlow() {
        // Click vào nút "Hạng Ma Trận" để vào màn hình nhập ma trận
        onView(withText("Hạng Ma Trận")).perform(click());

        // Đặt kích thước ma trận là 2x2
        onView(withId(R.id.rows_input)).perform(replaceText("2"));
        onView(withId(R.id.columns_input)).perform(replaceText("2"));
        onView(withId(R.id.apply_dimensions_button)).perform(click());

        // Kiểm tra bảng nhập ma trận đã được tạo
        onView(withId(R.id.matrix_input_table)).check(matches(isDisplayed()));

        // Kiểm tra nút "Tính" hiển thị để tiến hành tính toán
        onView(withId(R.id.calculate_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigateToLinearSystem() {
        // Click vào nút "Hệ Phương Trình Tuyến Tính"
        onView(withText("Hệ Phương Trình Tuyến Tính")).perform(click());

        // Kiểm tra đã chuyển sang MatrixInputActivity
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));

        // Hệ phương trình tuyến tính cần hai ma trận (hệ số và hằng số)
        onView(withId(R.id.matrix_input_table)).check(matches(isDisplayed()));
        onView(withId(R.id.second_matrix_input_table)).check(matches(isDisplayed()));
        onView(withId(R.id.second_matrix_label)).check(matches(isDisplayed()));
    }
}