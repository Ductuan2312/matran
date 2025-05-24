package com.example.matran.Controller;

import static java.security.AccessController.getContext;

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
        
        // Initialize UI components
        toolbar = findViewById(R.id.toolbar);
        bottomNav = findViewById(R.id.bottom_navigation);
        fab = findViewById(R.id.fab);
        basicOperationsGrid = findViewById(R.id.basic_operations_grid);
        advancedOperationsGrid = findViewById(R.id.advanced_operations_grid);
        recentCalculationsList = findViewById(R.id.recent_calculations_list);
        
        // Set toolbar
        setSupportActionBar(toolbar);
        
        // Initialize models
        historyModel = new HistoryModel(this);
        
        // Set up bottom navigation
        bottomNav.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        
        // Set up floating action button
        fab.setOnClickListener(this::onFabClick);
        
        // Populate operations grids
        setupBasicOperations();
        setupAdvancedOperations();
        
        // Show recent calculations
        updateRecentCalculations();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Update recent calculations list when returning to activity
        updateRecentCalculations();
    }
    
    /**
     * Handle navigation item selection
     */
    private boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.nav_calculator) {
            // Already in calculator screen
            return true;
        } else if (id == R.id.nav_theory) {
            // Open theory screen
            Intent intent = new Intent(this, TheoryActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_history) {
            // Open history screen
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
        // Quick action: Open matrix input screen for basic matrix operations
        Intent intent = new Intent(this, MatrixInputActivity.class);
        intent.putExtra("operation_type", "MATRIX_INPUT");
        startActivity(intent);
    }
    
    /**
     * Set up basic operations grid
     */
    private void setupBasicOperations() {
        // Define basic operations
        List<OperationItem> basicOperations = new ArrayList<>();
        basicOperations.add(new OperationItem("Add Matrices", R.drawable.ic_matrix_add, "ADD"));
        basicOperations.add(new OperationItem("Subtract Matrices", R.drawable.ic_matrix_subtract, "SUBTRACT"));
        basicOperations.add(new OperationItem("Multiply Matrices", R.drawable.ic_matrix_multiply, "MULTIPLY"));
        basicOperations.add(new OperationItem("Transpose", R.drawable.ic_matrix_transpose, "TRANSPOSE"));
        basicOperations.add(new OperationItem("Determinant", R.drawable.ic_matrix_determinant, "DETERMINANT"));
        basicOperations.add(new OperationItem("Inverse Matrix", R.drawable.ic_matrix_inverse, "INVERSE"));
        
        // Create card for each operation
        for (OperationItem op : basicOperations) {
            MaterialCardView card = createOperationCard(op);
            basicOperationsGrid.addView(card);
        }
    }
    
    /**
     * Set up advanced operations grid
     */
    private void setupAdvancedOperations() {
        // Define advanced operations
        List<OperationItem> advancedOperations = new ArrayList<>();
        advancedOperations.add(new OperationItem("Convolution", R.drawable.ic_matrix_convolution, "CONVOLUTION"));
        advancedOperations.add(new OperationItem("Eigen Values", R.drawable.ic_matrix_eigenvalues, "EIGENVALUES"));
        advancedOperations.add(new OperationItem("SVD", R.drawable.ic_matrix_svd, "SVD"));
        advancedOperations.add(new OperationItem("Matrix Rank", R.drawable.ic_matrix_rank, "RANK"));
        advancedOperations.add(new OperationItem("Linear System", R.drawable.ic_linear_system, "LINEAR_SYSTEM"));
        
        // Create card for each operation
        for (OperationItem op : advancedOperations) {
            MaterialCardView card = createOperationCard(op);
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
        
        // Set click listener to open matrix input screen
        card.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MatrixInputActivity.class);
            intent.putExtra("operation_type", operation.operationType);
            intent.putExtra("operation_title", operation.title);
            startActivity(intent);
        });
        
        return card;
    }
    
    /**
     * Update recent calculations list
     */
    private void updateRecentCalculations() {
        List<CalculationRecord> recentHistory = historyModel.getRecentHistory(5);
        // Set adapter for recent calculations list (you would need to implement this adapter)
        RecentCalculationsAdapter adapter = new RecentCalculationsAdapter(this, recentHistory);
        recentCalculationsList.setAdapter(adapter);
        
        // Show empty message if no history
        View emptyView = findViewById(R.id.empty_history_text);
        if (emptyView != null) {
            emptyView.setVisibility(recentHistory.isEmpty() ? View.VISIBLE : View.GONE);
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
     * This is a simplified implementation, you might want to create a separate class
     */
    private class RecentCalculationsAdapter extends ArrayAdapter<CalculationRecord> {
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
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("calculation_record", record);
                startActivity(intent);
            });
            
            return convertView;
        }
    }
}