package com.example.kpitusers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EmployeeAdapter.OnEmployeeActionListener {
    private RecyclerView recyclerViewEmployees;
    private EmployeeAdapter employeeAdapter;
    private DatabaseHelper databaseHelper;
    private static final int ADD_EMPLOYEE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewEmployees = findViewById(R.id.recyclerViewEmployees);
        recyclerViewEmployees.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);

        employeeAdapter = new EmployeeAdapter(this, databaseHelper.getAllEmployees(), this);
        recyclerViewEmployees.setAdapter(employeeAdapter);

        // Add sample data if database is empty
        if (databaseHelper.getAllEmployees().isEmpty()) {
            addSampleData();
        }

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAddEmployee();
            }
        });
    }

    private void addSampleData() {
        Employee employee1 = new Employee("John Doe", "1234567890", null);
        Employee employee2 = new Employee("Jane Smith", "0987654321", null);
        Employee employee3 = new Employee("Alice Johnson", "9876543210", null);

        databaseHelper.addEmployee(employee1, null);
        databaseHelper.addEmployee(employee2, null);
        databaseHelper.addEmployee(employee3, null);

        // Refresh the RecyclerView after adding sample data
        employeeAdapter.updateEmployeeList(databaseHelper.getAllEmployees());
    }

    private void navigateToAddEmployee() {
        Intent intent = new Intent(this, AddEmployeeActivity.class);
        startActivityForResult(intent, ADD_EMPLOYEE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EMPLOYEE_REQUEST && resultCode == RESULT_OK) {
            // Refresh the list of employees after adding a new employee
            employeeAdapter.updateEmployeeList(databaseHelper.getAllEmployees());
            Toast.makeText(this, "New employee added successfully", Toast.LENGTH_SHORT).show();
        }
    }

    public void onUpdateEmployee(Employee employee) {
        // Handle update operation here if needed
    }

    public void onDeleteEmployee(Employee employee) {
        // Handle delete operation here if needed
        // You can also leave this method empty if you don't need to handle delete in the MainActivity
    }
}
