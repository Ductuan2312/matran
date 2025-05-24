package com.example.matran.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.matran.Model.MatrixModel;
import com.example.matran.R;
import com.example.matran.Model.CalculationRecord;
import com.example.matran.Model.HistoryModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView historyListView;
    private TextView emptyHistoryText;
    private FloatingActionButton clearHistoryFab;
    
    private HistoryModel historyModel;
    private List<CalculationRecord> historyRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        
        // Initialize UI components
        toolbar = findViewById(R.id.toolbar);
        historyListView = findViewById(R.id.history_list_view);
        emptyHistoryText = findViewById(R.id.empty_history_text);
        clearHistoryFab = findViewById(R.id.clear_history_fab);
        
        // Set toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.history);
        
        // Initialize history model
        historyModel = new HistoryModel(this);
        
        // Set up clear history button
        clearHistoryFab.setOnClickListener(this::onClearHistoryClick);
        
        // Load and display history
        loadHistory();
    }
    
    /**
     * Load history from model and display
     */
    private void loadHistory() {
        historyRecords = historyModel.getHistory();
        
        if (historyRecords.isEmpty()) {
            historyListView.setVisibility(View.GONE);
            emptyHistoryText.setVisibility(View.VISIBLE);
        } else {
            historyListView.setVisibility(View.VISIBLE);
            emptyHistoryText.setVisibility(View.GONE);
            
            // Create adapter for history list
            HistoryAdapter adapter = new HistoryAdapter(
                    this, 
                    android.R.layout.simple_list_item_2, 
                    historyRecords);
            historyListView.setAdapter(adapter);
            
            // Set click listener for history items
            historyListView.setOnItemClickListener(this::onHistoryItemClick);
        }
    }
    
    /**
     * Handle history item click
     */
    private void onHistoryItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Get the calculation record for the clicked position
        CalculationRecord record = historyRecords.get(position);
        
        // Open result activity to show details
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("calculation_record", record);
        startActivity(intent);
    }
    
    /**
     * Handle clear history button click
     */
    private void onClearHistoryClick(View view) {
        // Show confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Xóa lịch sử")
                .setMessage("Bạn có chắc chắn muốn xóa toàn bộ lịch sử không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    historyModel.clearHistory();
                    loadHistory();
                })
                .setNegativeButton("Thoát", null)
                .show();
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
    
    /**
     * Custom adapter for history list
     */
    private class HistoryAdapter extends ArrayAdapter<CalculationRecord> {
        
        public HistoryAdapter(Context context, int resource, List<CalculationRecord> objects) {
            super(context, resource, objects);
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                view = inflater.inflate(R.layout.item_history, parent, false);
            }
            
            CalculationRecord record = getItem(position);
            
            TextView operationText = view.findViewById(R.id.operation_text);
            TextView dimensionsText = view.findViewById(R.id.dimensions_text);
            TextView timestampText = view.findViewById(R.id.timestamp_text);
            
            // Set texts
            operationText.setText(record.getOperationType());
            
            // Format dimensions
            String dimensions = formatDimensions(record);
            dimensionsText.setText(dimensions);
            
            // Format timestamp
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            timestampText.setText(sdf.format(record.getTimestamp()));
            
            return view;
        }

        private String formatDimensions(CalculationRecord record) {
            StringBuilder sb = new StringBuilder();

            MatrixModel matrixA = record.getInputMatrixA();
            sb.append("(").append(matrixA.getRows()).append("×").append(matrixA.getColumns()).append(")");

            if (record.hasTwoInputs()) {
                MatrixModel matrixB = record.getInputMatrixB();
                sb.append(" → (").append(matrixB.getRows()).append("×").append(matrixB.getColumns()).append(")");
            }

            MatrixModel resultMatrix = record.getResultMatrix();
            // Kiểm tra null trước khi truy cập
            if (resultMatrix != null) {
                sb.append(" = (").append(resultMatrix.getRows()).append("×").append(resultMatrix.getColumns()).append(")");
            } else if (record.isSVDResult()) {
                // Nếu là kết quả SVD, sử dụng ma trận S làm kết quả
                MatrixModel matrixS = record.getMatrixS();
                sb.append(" = (").append(matrixS.getRows()).append("×").append(matrixS.getColumns()).append(")");
            } else {
                sb.append(" = (unknown)");
            }

            return sb.toString();
        }
    }
}