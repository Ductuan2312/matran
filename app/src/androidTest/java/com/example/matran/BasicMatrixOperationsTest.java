package com.example.matran;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.matran.Controller.MainActivity;
import com.example.matran.Controller.MatrixInputActivity;
import com.example.matran.Model.MatrixModel;
import com.example.matran.Model.OperationModel;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;

/**
 * JUnit test cho các phép tính cơ bản và tương tác UI
 */
@RunWith(AndroidJUnit4.class)
public class BasicMatrixOperationsTest {

    // Rule để chạy MainActivity
    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    // Ma trận mẫu để kiểm tra
    private MatrixModel matrixA;
    private MatrixModel matrixB;

    @Before
    public void setUp() {
        // Chuẩn bị dữ liệu test
        // Ma trận A (2x2): [1, 2; 3, 4]
        matrixA = new MatrixModel(2, 2);
        matrixA.setValue(0, 0, 1);
        matrixA.setValue(0, 1, 2);
        matrixA.setValue(1, 0, 3);
        matrixA.setValue(1, 1, 4);

        // Ma trận B (2x2): [5, 6; 7, 8]
        matrixB = new MatrixModel(2, 2);
        matrixB.setValue(0, 0, 5);
        matrixB.setValue(0, 1, 6);
        matrixB.setValue(1, 0, 7);
        matrixB.setValue(1, 1, 8);
    }

    // PHẦN 1: TEST LOGIC TÍNH TOÁN

    @Test
    public void testAddition() {
        // A + B = [6, 8; 10, 12]
        MatrixModel result = OperationModel.add(matrixA, matrixB);

        assertEquals(6.0, result.getValue(0, 0), 0.001);
        assertEquals(8.0, result.getValue(0, 1), 0.001);
        assertEquals(10.0, result.getValue(1, 0), 0.001);
        assertEquals(12.0, result.getValue(1, 1), 0.001);
    }

    @Test
    public void testSubtraction() {
        // A - B = [-4, -4; -4, -4]
        MatrixModel result = OperationModel.subtract(matrixA, matrixB);

        assertEquals(-4.0, result.getValue(0, 0), 0.001);
        assertEquals(-4.0, result.getValue(0, 1), 0.001);
        assertEquals(-4.0, result.getValue(1, 0), 0.001);
        assertEquals(-4.0, result.getValue(1, 1), 0.001);
    }

    @Test
    public void testMultiplication() {
        // A * B = [19, 22; 43, 50]
        MatrixModel result = OperationModel.multiply(matrixA, matrixB);

        assertEquals(19.0, result.getValue(0, 0), 0.001);
        assertEquals(22.0, result.getValue(0, 1), 0.001);
        assertEquals(43.0, result.getValue(1, 0), 0.001);
        assertEquals(50.0, result.getValue(1, 1), 0.001);
    }

    @Test
    public void testTranspose() {
        // A^T = [1, 3; 2, 4]
        MatrixModel result = OperationModel.transpose(matrixA);

        assertEquals(1.0, result.getValue(0, 0), 0.001);
        assertEquals(3.0, result.getValue(0, 1), 0.001);
        assertEquals(2.0, result.getValue(1, 0), 0.001);
        assertEquals(4.0, result.getValue(1, 1), 0.001);
    }

    @Test
    public void testDeterminant() {
        // det(A) = 1*4 - 2*3 = -2
        double det = OperationModel.determinant(matrixA);
        assertEquals(-2.0, det, 0.001);
    }

    @Test
    public void testInverse() {
        // A^(-1) = [(-2/det, 1/det), (1.5/det, -0.5/det)] với det = -2
        MatrixModel result = OperationModel.inverse(matrixA);

        assertEquals(-2.0, result.getValue(0, 0), 0.001);
        assertEquals(1.0, result.getValue(0, 1), 0.001);
        assertEquals(1.5, result.getValue(1, 0), 0.001);
        assertEquals(-0.5, result.getValue(1, 1), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAdditionWithIncompatibleDimensions() {
        // Ma trận C (3x2)
        MatrixModel matrixC = new MatrixModel(3, 2);

        // Nên ném ra ngoại lệ khi thực hiện phép cộng với ma trận không tương thích
        OperationModel.add(matrixA, matrixC);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplicationWithIncompatibleDimensions() {
        // Ma trận D (3x3)
        MatrixModel matrixD = new MatrixModel(3, 3);

        // Nên ném ra ngoại lệ khi số cột của A khác số dòng của D
        OperationModel.multiply(matrixA, matrixD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInverseOfSingularMatrix() {
        // Ma trận kỳ dị (singular matrix)
        MatrixModel singular = new MatrixModel(2, 2);
        singular.setValue(0, 0, 1);
        singular.setValue(0, 1, 2);
        singular.setValue(1, 0, 2);
        singular.setValue(1, 1, 4);

        // Nên ném ra ngoại lệ khi ma trận kỳ dị (det = 0)
        OperationModel.inverse(singular);
    }

    // PHẦN 2: TEST TƯƠNG TÁC UI

    @Test
    public void testMainActivityDisplayed() {
        // Kiểm tra các phần tử UI chính của MainActivity hiển thị đúng
        onView(withId(R.id.basic_operations_grid)).check(matches(isDisplayed()));
        onView(withId(R.id.advanced_operations_grid)).check(matches(isDisplayed()));
    }

    @Test
    public void testOperationTitleDisplayed() {
        // Kiểm tra hiển thị của các tiêu đề phép tính
        onView(withText("Cộng Ma Trận")).check(matches(isDisplayed()));
        onView(withText("Trừ Ma Trận")).check(matches(isDisplayed()));
        onView(withText("Nhân Ma Trận")).check(matches(isDisplayed()));
        onView(withText("Chuyển Vị")).check(matches(isDisplayed()));
        onView(withText("Định Thức")).check(matches(isDisplayed()));
        onView(withText("Ma Trận Nghịch Đảo")).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigateToMatrixAddition() {
        // Click vào nút "Cộng Ma Trận"
        onView(withText("Cộng Ma Trận")).perform(click());

        // Kiểm tra đã chuyển sang MatrixInputActivity và có hiển thị toolbar
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));

        // Kiểm tra tiêu đề trên toolbar
        onView(allOf(
                instanceOf(TextView.class),
                withParent(withId(R.id.toolbar))
        )).check(matches(withText(containsString("Cộng Ma Trận"))));
    }

    @Test
    public void testNavigateToMatrixMultiplication() {
        // Click vào nút "Nhân Ma Trận"
        onView(withText("Nhân Ma Trận")).perform(click());

        // Kiểm tra đã chuyển sang MatrixInputActivity
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withId(R.id.matrix_input_table)).check(matches(isDisplayed()));
        onView(withId(R.id.second_matrix_input_table)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigateToTranspose() {
        // Click vào nút "Chuyển Vị"
        onView(withText("Chuyển Vị")).perform(click());

        // Kiểm tra đã chuyển sang MatrixInputActivity và có bảng nhập ma trận
        onView(withId(R.id.matrix_input_table)).check(matches(isDisplayed()));

        // Chuyển vị chỉ cần một ma trận, ma trận thứ hai nên ẩn
        onView(withId(R.id.second_matrix_input_table)).check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }

    @Test
    public void testMatrixDimensionsUI() {
        // Click vào nút "Chuyển Vị" để vào màn hình nhập ma trận
        onView(withText("Chuyển Vị")).perform(click());

        // Kiểm tra trường nhập số dòng và số cột hiển thị đúng
        onView(withId(R.id.rows_input)).check(matches(isDisplayed()));
        onView(withId(R.id.columns_input)).check(matches(isDisplayed()));
        onView(withId(R.id.apply_dimensions_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testCalculateButtonDisplayed() {
        // Click vào nút "Định Thức" để vào màn hình nhập ma trận
        onView(withText("Định Thức")).perform(click());

        // Kiểm tra nút tính toán hiển thị
        onView(withId(R.id.calculate_button)).check(matches(isDisplayed()));
        onView(withId(R.id.calculate_button)).check(matches(withText("Tính")));
    }
}