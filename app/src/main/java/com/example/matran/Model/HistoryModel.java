//matran - A matrix manipulation application
//        Copyright (c) 2025 Đức Tuân
//
//        Licensed under the MIT License. See LICENSE file in the project root for full license information.
package com.example.matran.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model class to manage calculation history
 */
public class HistoryModel {
    private static final String PREF_NAME = "matrix_solver_history";
    private static final String KEY_HISTORY = "calculation_history";
    
    private List<CalculationRecord> historyList;
    private SharedPreferences preferences;
    private Gson gson;

    /**
     * Constructor
     * @param context application context
     */
    public HistoryModel(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        loadHistory();
    }

    /**
     * Load history from SharedPreferences
     */
    private void loadHistory() {
        String json = preferences.getString(KEY_HISTORY, null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<CalculationRecord>>(){}.getType();
            historyList = gson.fromJson(json, type);
        } else {
            historyList = new ArrayList<>();
        }
    }

    /**
     * Save history to SharedPreferences
     */
    private void saveHistory() {
        String json = gson.toJson(historyList);
        preferences.edit().putString(KEY_HISTORY, json).apply();
    }

    /**
     * Add a new calculation record to history
     * @param record calculation record to add
     */
    public void addRecord(CalculationRecord record) {
        // Add to beginning of list (most recent first)
        historyList.add(0, record);
        
        // Limit history size to 20 entries
        if (historyList.size() > 20) {
            historyList.remove(historyList.size() - 1);
        }
        
        saveHistory();
    }

    /**
     * Get all history records
     * @return list of calculation records
     */
    public List<CalculationRecord> getHistory() {
        return Collections.unmodifiableList(historyList);
    }

    /**
     * Get most recent calculations
     * @param count number of records to get
     * @return list of most recent calculation records
     */
    public List<CalculationRecord> getRecentHistory(int count) {
        int size = Math.min(count, historyList.size());
        return Collections.unmodifiableList(historyList.subList(0, size));
    }

    /**
     * Clear all history
     */
    public void clearHistory() {
        historyList.clear();
        saveHistory();
    }
}