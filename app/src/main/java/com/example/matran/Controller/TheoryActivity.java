package com.example.matran.Controller;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.matran.R;
import com.example.matran.Utils.TheoryExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheoryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ExpandableListView expandableListView;
    private TheoryExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private Map<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory);

        // Initialize UI components
        toolbar = findViewById(R.id.toolbar);
        expandableListView = findViewById(R.id.expandable_list_view);

        // Set toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.theory);

        // Prepare list data
        prepareListData();

        // Set up expandable list view
        listAdapter = new TheoryExpandableListAdapter(this, listDataHeader, listDataChild);
        expandableListView.setAdapter(listAdapter);
    }

    /**
     * Prepare theory content data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding header data
        listDataHeader.add("Ma trận là gì");
        listDataHeader.add("Phép cộng ma trận");
        listDataHeader.add("Phép trừ ma trận");
        listDataHeader.add("Phép nhân ma trận");
        listDataHeader.add("Chuyển vị ma trận");
        listDataHeader.add("Định thức ma trận");
        listDataHeader.add("Ma trận nghịch đảo");
        listDataHeader.add("Tích chập (Convolution)");
        listDataHeader.add("Phân tích giá trị riêng");

        // Adding child data
        List<String> whatIsMatrix = new ArrayList<>();
        whatIsMatrix.add("Ma trận là một mảng hai chiều của các số, được sắp xếp thành các hàng và cột.\n\n" +
                "Ma trận kích thước m x n có m hàng và n cột.\n\n" +
                "Ví dụ ma trận 2x3:\n" +
                "A = |\t1\t2\t3\t|\n" +
                "    |\t4\t5\t6\t|");

        List<String> matrixAddition = new ArrayList<>();
        matrixAddition.add("Phép cộng ma trận được thực hiện bằng cách cộng từng phần tử tương ứng.\n\n" +
                "Hai ma trận phải có cùng kích thước để có thể cộng được.\n\n" +
                "Nếu A = [aᵢⱼ] và B = [bᵢⱼ], thì:\n" +
                "C = A + B = [aᵢⱼ + bᵢⱼ]");

        List<String> matrixSubtraction = new ArrayList<>();
        matrixSubtraction.add("Phép trừ ma trận được thực hiện bằng cách trừ từng phần tử tương ứng.\n\n" +
                "Hai ma trận phải có cùng kích thước để có thể trừ được.\n\n" +
                "Nếu A = [aᵢⱼ] và B = [bᵢⱼ], thì:\n" +
                "C = A - B = [aᵢⱼ - bᵢⱼ]");

        List<String> matrixMultiplication = new ArrayList<>();
        matrixMultiplication.add("Phép nhân ma trận A kích thước m x n và B kích thước n x p:\n\n" +
                "C = A × B có kích thước m x p, với:\n" +
                "cᵢⱼ = Σ(aᵢₖ × bₖⱼ) với k từ 1 đến n\n\n" +
                "Số cột của ma trận thứ nhất phải bằng số hàng của ma trận thứ hai.");

        List<String> matrixTranspose = new ArrayList<>();
        matrixTranspose.add("Chuyển vị ma trận là phép biến đổi ma trận bằng cách đổi các hàng thành các cột và ngược lại.\n\n" +
                "Nếu A là ma trận m x n, thì Aᵀ là ma trận n x m với:\n" +
                "(Aᵀ)ᵢⱼ = Aⱼᵢ");

        List<String> determinant = new ArrayList<>();
        determinant.add("Định thức là một số vô hướng được tính từ một ma trận vuông.\n\n" +
                "Cho ma trận vuông A kích thước n x n, định thức của A ký hiệu là det(A) hoặc |A|.\n\n" +
                "Ma trận 2x2:\n" +
                "det(A) = a₁₁a₂₂ - a₁₂a₂₁\n\n" +
                "Ma trận 3x3 và lớn hơn được tính bằng phương pháp khai triển theo một hàng hoặc một cột.");

        List<String> inverseMatrix = new ArrayList<>();
        inverseMatrix.add("Ma trận nghịch đảo A⁻¹ của ma trận vuông A thỏa mãn:\n" +
                "A × A⁻¹ = A⁻¹ × A = I (ma trận đơn vị)\n\n" +
                "Ma trận A có nghịch đảo khi và chỉ khi det(A) ≠ 0.\n\n" +
                "Công thức tính ma trận nghịch đảo:\n" +
                "A⁻¹ = adj(A) / det(A)\n" +
                "Trong đó adj(A) là ma trận kề (adjugate) của A.");

        List<String> convolution = new ArrayList<>();
        convolution.add("Tích chập là phép toán quan trọng trong xử lý hình ảnh và mạng nơ-ron tích chập (CNN).\n\n" +
                "Tích chập của ma trận A với kernel K tạo ra ma trận B với:\n" +
                "B[i,j] = Σ Σ A[i+m,j+n] × K[m,n]\n\n" +
                "Tích chập được sử dụng để trích xuất đặc trưng trong hình ảnh như phát hiện cạnh, làm mờ, làm sắc nét...");

        List<String> eigenvalues = new ArrayList<>();
        eigenvalues.add("Giá trị riêng λ và vector riêng v của ma trận vuông A thỏa mãn:\n" +
                "A × v = λ × v\n\n" +
                "Giá trị riêng được tính bằng cách giải phương trình:\n" +
                "det(A - λI) = 0\n\n" +
                "Phân tích giá trị riêng có ứng dụng trong nhiều lĩnh vực: phân tích thành phần chính (PCA), cơ học lượng tử, v.v.");

        // Adding data to HashMap
        listDataChild.put(listDataHeader.get(0), whatIsMatrix);
        listDataChild.put(listDataHeader.get(1), matrixAddition);
        listDataChild.put(listDataHeader.get(2), matrixSubtraction);
        listDataChild.put(listDataHeader.get(3), matrixMultiplication);
        listDataChild.put(listDataHeader.get(4), matrixTranspose);
        listDataChild.put(listDataHeader.get(5), determinant);
        listDataChild.put(listDataHeader.get(6), inverseMatrix);
        listDataChild.put(listDataHeader.get(7), convolution);
        listDataChild.put(listDataHeader.get(8), eigenvalues);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle back button on toolbar
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}