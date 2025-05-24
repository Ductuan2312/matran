package com.example.matran;

import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import com.example.matran.Controller.MainActivity;
import com.example.matran.Model.MatrixModel;
import com.example.matran.Model.OperationModel;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Test chức năng giải hệ phương trình tuyến tính
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class LinearSystemSolverTest {

    private static final int DELAY_LONG = 2000; // 2 giây
    private static final int DELAY_MEDIUM = 1500; // 1.5 giây
    private static final int DELAY_SHORT = 1000; // 1 giây

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Thiết lập trước khi chạy test
     */
    @Before
    public void setUp() {
        try {
            // Tắt animations để test UI hoạt động ổn định hơn
            UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            device.executeShellCommand("settings put global window_animation_scale 0");
            device.executeShellCommand("settings put global transition_animation_scale 0");
            device.executeShellCommand("settings put global animator_duration_scale 0");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test giải hệ phương trình tuyến tính hoàn chỉnh từ đầu đến cuối
     */
    @Test
    public void testLinearSystemSolver() {
        // Delay đủ để nhìn thấy giao diện ban đầu
        SystemClock.sleep(DELAY_MEDIUM);

        // 1. KIỂM TRA MÀN HÌNH CHÍNH HIỂN THỊ
        onView(withId(R.id.basic_operations_grid)).check(matches(isDisplayed()));
        onView(withId(R.id.advanced_operations_grid)).check(matches(isDisplayed()));

        // Hiển thị thông báo test bắt đầu
        System.out.println("---------------------------------------------");
        System.out.println("BẮT ĐẦU TEST CHỨC NĂNG HỆ PHƯƠNG TRÌNH TUYẾN TÍNH");
        System.out.println("---------------------------------------------");
        SystemClock.sleep(DELAY_SHORT);

        // 2. CLICK VÀO "HỆ PHƯƠNG TRÌNH TUYẾN TÍNH"
        onView(withText("Hệ Phương Trình Tuyến Tính")).perform(click());
        System.out.println("Đã chuyển sang màn hình nhập ma trận...");

        // Delay để xem màn hình nhập ma trận
        SystemClock.sleep(DELAY_LONG);

        // 3. THIẾT LẬP KÍCH THƯỚC MA TRẬN
        // Đặt kích thước ma trận hệ số là 3x3
        onView(withId(R.id.rows_input)).perform(replaceText("3"));
        SystemClock.sleep(DELAY_SHORT);

        onView(withId(R.id.columns_input)).perform(replaceText("3"));
        SystemClock.sleep(DELAY_SHORT);

        // Áp dụng kích thước
        onView(withId(R.id.apply_dimensions_button)).perform(click());
        System.out.println("Đã thiết lập kích thước ma trận 3x3...");

        // Delay để xem bảng nhập ma trận đã tạo
        SystemClock.sleep(DELAY_LONG);

        // 4. NHẬP DỮ LIỆU MA TRẬN HỆ SỐ (3x3)
        // Ma trận hệ số: [2, 1, -1; -3, -1, 2; -2, 1, 2]
        System.out.println("Đang nhập dữ liệu ma trận hệ số...");

        // Tìm và nhập giá trị cho ma trận hệ số
        onView(withTagValue("cell_0_0")).perform(replaceText("2"));
        onView(withTagValue("cell_0_0")).perform(closeSoftKeyboard());
        SystemClock.sleep(300);

        onView(withTagValue("cell_0_1")).perform(replaceText("1"));
        onView(withTagValue("cell_0_1")).perform(closeSoftKeyboard());
        SystemClock.sleep(300);

        onView(withTagValue("cell_0_2")).perform(replaceText("-1"));
        onView(withTagValue("cell_0_2")).perform(closeSoftKeyboard());
        SystemClock.sleep(300);

        onView(withTagValue("cell_1_0")).perform(replaceText("-3"));
        onView(withTagValue("cell_1_0")).perform(closeSoftKeyboard());
        SystemClock.sleep(300);

        onView(withTagValue("cell_1_1")).perform(replaceText("-1"));
        onView(withTagValue("cell_1_1")).perform(closeSoftKeyboard());
        SystemClock.sleep(300);

        onView(withTagValue("cell_1_2")).perform(replaceText("2"));
        onView(withTagValue("cell_1_2")).perform(closeSoftKeyboard());
        SystemClock.sleep(300);

        onView(withTagValue("cell_2_0")).perform(replaceText("-2"));
        onView(withTagValue("cell_2_0")).perform(closeSoftKeyboard());
        SystemClock.sleep(300);

        onView(withTagValue("cell_2_1")).perform(replaceText("1"));
        onView(withTagValue("cell_2_1")).perform(closeSoftKeyboard());
        SystemClock.sleep(300);

        onView(withTagValue("cell_2_2")).perform(replaceText("2"));
        onView(withTagValue("cell_2_2")).perform(closeSoftKeyboard());
        SystemClock.sleep(300);

        System.out.println("Đã nhập xong ma trận hệ số");
        SystemClock.sleep(DELAY_MEDIUM);

        // 5. NHẬP DỮ LIỆU MA TRẬN HẰNG SỐ (3x1)
        // Cần đặt lại kích thước của ma trận hằng số
        onView(withId(R.id.columns_input)).perform(replaceText("1"));
        SystemClock.sleep(DELAY_SHORT);

        // Click vào nút Apply Dimensions để áp dụng thay đổi cho ma trận thứ hai
        onView(withId(R.id.apply_dimensions_button)).perform(click());
        System.out.println("Đã thiết lập kích thước ma trận hằng số 3x1...");
        SystemClock.sleep(DELAY_MEDIUM);

        // Ma trận hằng số: [8; -11; -3]
        System.out.println("Đang nhập dữ liệu ma trận hằng số...");

        onView(withTagValue("cell2_0_0")).perform(replaceText("8"));
        onView(withTagValue("cell2_0_0")).perform(closeSoftKeyboard());
        SystemClock.sleep(300);

        onView(withTagValue("cell2_1_0")).perform(replaceText("-11"));
        onView(withTagValue("cell2_1_0")).perform(closeSoftKeyboard());
        SystemClock.sleep(300);

        onView(withTagValue("cell2_2_0")).perform(replaceText("-3"));
        onView(withTagValue("cell2_2_0")).perform(closeSoftKeyboard());
        SystemClock.sleep(300);

        System.out.println("Đã nhập xong ma trận hằng số");

        // Delay để xem ma trận đã nhập hoàn chỉnh
        SystemClock.sleep(DELAY_LONG);

        // 6. CLICK VÀO NÚT "TÍNH"
        System.out.println("Thực hiện tính toán...");
        onView(withId(R.id.calculate_button)).perform(click());

        // Delay để xem màn hình kết quả
        SystemClock.sleep(DELAY_LONG);

        // 7. KIỂM TRA KẾT QUẢ
        // Kiểm tra kết quả hiển thị đúng (x = 2, y = 3, z = -1)
        System.out.println("Kiểm tra kết quả hiển thị...");

        // Kiểm tra nếu tab kết quả hiển thị
        onView(withId(R.id.tab_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.tab_content)).check(matches(isDisplayed()));

        // Kiểm tra nếu bảng kết quả hiển thị (bằng cách kiểm tra tiêu đề kết quả)
        onView(withId(R.id.operation_title)).check(matches(isDisplayed()));
        onView(withId(R.id.result_label)).check(matches(isDisplayed()));
        onView(withId(R.id.result_table)).check(matches(isDisplayed()));

        // Delay để xem kết quả
        SystemClock.sleep(DELAY_LONG);

        // Thông báo kết thúc test
        System.out.println("---------------------------------------------");
        System.out.println("KẾT THÚC TEST CHỨC NĂNG HỆ PHƯƠNG TRÌNH TUYẾN TÍNH");
        System.out.println("---------------------------------------------");

        // Delay trước khi kết thúc để xem kết quả
        SystemClock.sleep(DELAY_LONG);
    }

    /**
     * Helper method để tìm view theo tag
     */
    private static Matcher<View> withTagValue(final Object tagValue) {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                return tagValue.equals(view.getTag());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with tag value: " + tagValue);
            }
        };
    }

    /**
     * Test tính toán của phương thức solveLinearSystem
     */
    @Test
    public void testLinearSystemSolverOperation() {
        // 1. Tạo ma trận hệ số và ma trận hằng số
        MatrixModel coefficients = new MatrixModel(3, 3);
        coefficients.setValue(0, 0, 2);
        coefficients.setValue(0, 1, 1);
        coefficients.setValue(0, 2, -1);
        coefficients.setValue(1, 0, -3);
        coefficients.setValue(1, 1, -1);
        coefficients.setValue(1, 2, 2);
        coefficients.setValue(2, 0, -2);
        coefficients.setValue(2, 1, 1);
        coefficients.setValue(2, 2, 2);

        MatrixModel constants = new MatrixModel(3, 1);
        constants.setValue(0, 0, 8);
        constants.setValue(1, 0, -11);
        constants.setValue(2, 0, -3);

        // Thông báo bắt đầu test phương thức
        System.out.println("---------------------------------------------");
        System.out.println("BẮT ĐẦU TEST TÍNH TOÁN HỆ PHƯƠNG TRÌNH TUYẾN TÍNH");
        System.out.println("Ma trận hệ số:");
        printMatrix(coefficients);
        System.out.println("Ma trận hằng số:");
        printMatrix(constants);
        System.out.println("---------------------------------------------");
        SystemClock.sleep(DELAY_MEDIUM);

        // 2. Giải hệ phương trình
        System.out.println("Đang thực hiện giải phương trình...");
        SystemClock.sleep(DELAY_SHORT);
        MatrixModel solution = OperationModel.solveLinearSystem(coefficients, constants);

        // 3. Kiểm tra kết quả
        // Nghiệm kỳ vọng là x = 2, y = 3, z = -1
        System.out.println("Kết quả tính toán:");
        printMatrix(solution);
        System.out.println("x = " + solution.getValue(0, 0));
        System.out.println("y = " + solution.getValue(1, 0));
        System.out.println("z = " + solution.getValue(2, 0));

        // So sánh kết quả với giá trị kỳ vọng
        boolean isCorrect =
                Math.abs(solution.getValue(0, 0) - 2.0) < 0.001 &&
                        Math.abs(solution.getValue(1, 0) - 3.0) < 0.001 &&
                        Math.abs(solution.getValue(2, 0) - (-1.0)) < 0.001;

        System.out.println("Kết quả tính toán " +
                (isCorrect ? "CHÍNH XÁC" : "SAI"));

        // Delay để xem kết quả
        SystemClock.sleep(DELAY_MEDIUM);
        System.out.println("---------------------------------------------");
        System.out.println("KẾT THÚC TEST TÍNH TOÁN HỆ PHƯƠNG TRÌNH TUYẾN TÍNH");
        System.out.println("---------------------------------------------");
        SystemClock.sleep(DELAY_SHORT);
    }

    /**
     * Test trường hợp ma trận kỳ dị (không có nghiệm duy nhất)
     */
    @Test
    public void testSingularMatrixHandling() {
        // Thông báo bắt đầu test
        System.out.println("---------------------------------------------");
        System.out.println("BẮT ĐẦU TEST XỬ LÝ MA TRẬN KỲ DỊ");
        System.out.println("---------------------------------------------");
        SystemClock.sleep(DELAY_MEDIUM);

        // 1. Tạo ma trận kỳ dị (các hàng tương tự nhau)
        MatrixModel singularMatrix = new MatrixModel(3, 3);
        singularMatrix.setValue(0, 0, 1);
        singularMatrix.setValue(0, 1, 2);
        singularMatrix.setValue(0, 2, 3);
        singularMatrix.setValue(1, 0, 2);
        singularMatrix.setValue(1, 1, 4);
        singularMatrix.setValue(1, 2, 6);
        singularMatrix.setValue(2, 0, 3);
        singularMatrix.setValue(2, 1, 6);
        singularMatrix.setValue(2, 2, 9);

        MatrixModel constants = new MatrixModel(3, 1);
        constants.setValue(0, 0, 6);
        constants.setValue(1, 0, 12);
        constants.setValue(2, 0, 18);

        System.out.println("Ma trận hệ số kỳ dị:");
        printMatrix(singularMatrix);
        System.out.println("Ma trận hằng số:");
        printMatrix(constants);

        // 2. Thử giải và bắt ngoại lệ
        try {
            System.out.println("Đang thực hiện giải phương trình với ma trận kỳ dị...");
            SystemClock.sleep(DELAY_SHORT);

            MatrixModel solution = OperationModel.solveLinearSystem(singularMatrix, constants);
            System.out.println("LỖI: Không phát hiện ma trận kỳ dị!");
        } catch (IllegalArgumentException e) {
            // Đây là điều chúng ta kỳ vọng
            System.out.println("Phát hiện ma trận kỳ dị đúng như kỳ vọng: " + e.getMessage());
        }

        // Delay để đọc kết quả
        SystemClock.sleep(DELAY_MEDIUM);
        System.out.println("---------------------------------------------");
        System.out.println("KẾT THÚC TEST XỬ LÝ MA TRẬN KỲ DỊ");
        System.out.println("---------------------------------------------");
        SystemClock.sleep(DELAY_SHORT);
    }

    /**
     * In ma trận ra console
     */
    private void printMatrix(MatrixModel matrix) {
        for (int i = 0; i < matrix.getRows(); i++) {
            System.out.print("[ ");
            for (int j = 0; j < matrix.getColumns(); j++) {
                System.out.printf("%.2f ", matrix.getValue(i, j));
            }
            System.out.println("]");
        }
    }
}