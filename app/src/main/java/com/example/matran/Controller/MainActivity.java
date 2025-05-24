package com.example.matran.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.matran.R;
import com.example.matran.Model.CalculationRecord;
import com.example.matran.Model.HistoryModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // UI Components
    private Toolbar toolbar;
    private BottomNavigationView bottomNav;
    private FloatingActionButton fab;
    private GridLayout basicOperationsGrid;
    private GridLayout advancedOperationsGrid;
    private ListView recentCalculationsList;

    // Models
    private HistoryModel historyModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize UI components
        toolbar = findViewById(R.id.toolbar);
        bottomNav = findViewById(R.id.bottom_navigation);
        fab = findViewById(R.id.fab);

        // Set toolbar
        setSupportActionBar(toolbar);

        // Initialize models
        historyModel = new HistoryModel(this);

        // Set up bottom navigation
        bottomNav.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Set up floating action button
        fab.setOnClickListener(this::onFabClick);

        // Load calculator fragment
        if (savedInstanceState == null) {
            loadCalculatorFragment();
        }
    }

    // Tạo một fragment mới cho calculator
    private void loadCalculatorFragment() {
        CalculatorFragment calculatorFragment = new CalculatorFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, calculatorFragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật lại fragment khi cần thiết
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof CalculatorFragment) {
            ((CalculatorFragment) currentFragment).updateRecentCalculations();
        }
    }

    /**
     * Handle navigation item selection
     */
    private boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_calculator) {
            // Đã ở màn hình calculator
            return true;
        } else if (id == R.id.nav_theory) {
            // Mở màn hình theory
            Intent intent = new Intent(this, TheoryActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_history) {
            // Mở màn hình history
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }

    /**
     * Handle FAB click
     */
    private void onFabClick(View view) {
        // Mở màn hình nhập ma trận
        Intent intent = new Intent(this, MatrixInputActivity.class);
        intent.putExtra("operation_type", "MATRIX_INPUT");
        startActivity(intent);
    }

    // Inner class cho calculator fragment
    public static class CalculatorFragment extends Fragment {

        private GridLayout basicOperationsGrid;
        private GridLayout advancedOperationsGrid;
        private ListView recentCalculationsList;
        private HistoryModel historyModel;
        private View rootView; // Thêm biến này để lưu trữ view gốc


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_calculator, container, false); // Lưu view gốc

            basicOperationsGrid = rootView.findViewById(R.id.basic_operations_grid);
            advancedOperationsGrid = rootView.findViewById(R.id.advanced_operations_grid);
            recentCalculationsList = rootView.findViewById(R.id.recent_calculations_list);

            historyModel = new HistoryModel(getActivity());

            setupBasicOperations();
            setupAdvancedOperations();
            updateRecentCalculations();

            return rootView;
        }

        /**
         * Set up basic operations grid
         */
        private void setupBasicOperations() {

            // Define basic operations
            List<OperationItem> basicOperations = new ArrayList<>();
            basicOperations.add(new OperationItem("Cộng Ma Trận", R.drawable.ic_add, "ADD"));
            basicOperations.add(new OperationItem("Trừ Ma Trận", R.drawable.ic_matrix_multiply, "SUBTRACT"));
            basicOperations.add(new OperationItem("Nhân Ma Trận", R.drawable.ic_matrix_multiply, "MULTIPLY"));
            basicOperations.add(new OperationItem("Chuyển Vị", R.drawable.ic_matrix_transpose, "TRANSPOSE"));
            basicOperations.add(new OperationItem("Định Thức", R.drawable.ic_matrix_determinant, "DETERMINANT"));
            basicOperations.add(new OperationItem("Ma Trận Nghịch Đảo", R.drawable.ic_matrix_inverse, "INVERSE"));

            // Calculate columnWidth - Lấy 50% chiều rộng màn hình cho mỗi thẻ (2 cột)
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int cardWidth = (screenWidth - 48) / 2; // Trừ đi padding của LinearLayout (16dp * 2) + margin giữa (16dp)

            // Create card for each operation
            for (OperationItem op : basicOperations) {
                MaterialCardView card = createOperationCard(op);

                // Đặt LayoutParams để thẻ lấp đầy không gian
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = cardWidth;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.setMargins(4, 4, 4, 4);

                card.setLayoutParams(params);
                basicOperationsGrid.addView(card);
            }
        }

        /**
         * Set up advanced operations grid
         */
        private void setupAdvancedOperations() {
            // Define advanced operations
            List<OperationItem> advancedOperations = new ArrayList<>();
            advancedOperations.add(new OperationItem("Tích Chập", R.drawable.ic_matrix_convolution, "CONVOLUTION"));
            advancedOperations.add(new OperationItem("Trị Riêng", R.drawable.ic_matrix_add, "EIGENVALUES"));
            advancedOperations.add(new OperationItem("Phân Tích SVD", R.drawable.ic_matrix_add, "SVD"));
            advancedOperations.add(new OperationItem("Hạng Ma Trận", R.drawable.ic_matrix_add, "RANK"));
            advancedOperations.add(new OperationItem("Hệ Phương Trình Tuyến Tính", R.drawable.ic_linear_system, "LINEAR_SYSTEM"));

            // Calculate columnWidth - Lấy 50% chiều rộng màn hình cho mỗi thẻ (2 cột)
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int cardWidth = (screenWidth - 48) / 2; // Trừ đi padding của LinearLayout (16dp * 2) + margin giữa (16dp)

            // Create card for each operation
            for (OperationItem op : advancedOperations) {
                MaterialCardView card = createOperationCard(op);

                // Đặt LayoutParams để thẻ lấp đầy không gian
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = cardWidth;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.setMargins(4, 4, 4, 4);

                card.setLayoutParams(params);
                advancedOperationsGrid.addView(card);
            }
        }

        /**
         * Create operation card for grid
         */
        private MaterialCardView createOperationCard(OperationItem operation) {
            // Inflate card layout from XML
            MaterialCardView card = (MaterialCardView) getLayoutInflater()
                    .inflate(R.layout.item_operation_card, null);

            // Set card content
            card.findViewById(R.id.operation_icon).setBackgroundResource(operation.iconResId);

            // Thêm dòng sau để thiết lập tiêu đề
            TextView titleTextView = card.findViewById(R.id.operation_title);
            titleTextView.setText(operation.title);

            // Set click listener to open matrix input screen
            card.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), MatrixInputActivity.class);
                intent.putExtra("operation_type", operation.operationType);
                intent.putExtra("operation_title", operation.title);
                startActivity(intent);
            });

            return card;
        }

        /**
         * Update recent calculations list
         */
        public void updateRecentCalculations() {
            // Kiểm tra xem rootView có tồn tại không
            if (rootView == null) return;

            List<CalculationRecord> recentHistory = historyModel.getRecentHistory(5);
            // Set adapter for recent calculations list
            RecentCalculationsAdapter adapter = new RecentCalculationsAdapter(getActivity(), recentHistory);
            recentCalculationsList.setAdapter(adapter);

            // Show empty message if no history
            View emptyView = rootView.findViewById(R.id.empty_history_text); // Sử dụng rootView thay vì getView()
            if (emptyView != null) {
                emptyView.setVisibility(recentHistory.isEmpty() ? View.VISIBLE : View.GONE);
            }
        }
    }

    /**
     * Helper class to store operation information
     */
    private static class OperationItem {
        String title;
        int iconResId;
        String operationType;

        OperationItem(String title, int iconResId, String operationType) {
            this.title = title;
            this.iconResId = iconResId;
            this.operationType = operationType;
        }
    }

    /**
     * Adapter for recent calculations list
     */
    private static class RecentCalculationsAdapter extends ArrayAdapter<CalculationRecord> {
        public RecentCalculationsAdapter(Context context, List<CalculationRecord> records) {
            super(context, R.layout.item_recent_calculation, records);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            CalculationRecord record = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.item_recent_calculation, parent, false);
            }

            // Set up the view
            TextView titleTextView = convertView.findViewById(R.id.calculation_title);
            TextView timestampTextView = convertView.findViewById(R.id.calculation_timestamp);

            titleTextView.setText(record.getOperationType());
            timestampTextView.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm")
                    .format(record.getTimestamp()));

            // Set click listener to view this calculation
            convertView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), ResultActivity.class);
                intent.putExtra("calculation_record", record);
                getContext().startActivity(intent);
            });

            return convertView;
        }
    }
}