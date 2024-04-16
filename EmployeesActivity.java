package com.example.kpitusers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kpitusers.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class EmployeesActivity extends AppCompatActivity implements EmployeeAdapter.OnEmployeeActionListener {
    private RecyclerView recyclerViewEmployees;
    private EmployeeAdapter employeeAdapter;

    private DatabaseHelper databaseHelper;
    private static final int ADD_EMPLOYEE_REQUEST = 1;
    private static final int UPDATE_EMPLOYEE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);

        // Initialize the database helper
        databaseHelper = new DatabaseHelper(this);

        // Initialize RecyclerView and its adapter
        recyclerViewEmployees = findViewById(R.id.recyclerViewEmployees);
        recyclerViewEmployees.setLayoutManager(new LinearLayoutManager(this));
        employeeAdapter = new EmployeeAdapter(this, databaseHelper.getAllEmployees(), this);
        recyclerViewEmployees.setAdapter(employeeAdapter);

        // Set up FloatingActionButton click listener to navigate to AddEmployeeActivity
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAddEmployee();
            }
        });

        addSampleDataIfNeeded();
    }

    private void addSampleDataIfNeeded() {
        List<Employee> existingEmployees = databaseHelper.getAllEmployees();
        if (existingEmployees.isEmpty()) {
            addSampleData();
        } else {
            // If there are existing employees, update the RecyclerView to display them
            employeeAdapter.updateEmployeeList(existingEmployees);
        }
    }

    // Method to add sample data to the database
    private void addSampleData() {
        Employee employee1 = new Employee("John Doe", "1234567890",null);
        Employee employee2 = new Employee("Jane Smith", "0987654321",null);
        Employee employee3 = new Employee("Alice Johnson", "9876543210",null);

        databaseHelper.addEmployee(employee1,null);
        databaseHelper.addEmployee(employee2,null);
        databaseHelper.addEmployee(employee3,null);

        // Refresh the RecyclerView after adding sample data
        employeeAdapter.updateEmployeeList(databaseHelper.getAllEmployees());
    }

    // Method to navigate to AddEmployeeActivity
    private void navigateToAddEmployee() {
        Intent intent = new Intent(this, AddEmployeeActivity.class);
        startActivityForResult(intent, ADD_EMPLOYEE_REQUEST);
    }

    // Method to navigate to UpdateEmployeeActivity
    private void navigateToUpdateEmployee(Employee employee) {
        Intent intent = new Intent(this, UpdateEmployeeActivity.class);
        // Assuming employeeId is of type int
        intent.putExtra("employee_id", (int) employee.getId());
        intent.putExtra("employee_name", employee.getName());
        intent.putExtra("employee_phone", employee.getPhoneNumber());
        startActivityForResult(intent, UPDATE_EMPLOYEE_REQUEST);
    }


    // Handle the result from AddEmployeeActivity and UpdateEmployeeActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EMPLOYEE_REQUEST && resultCode == RESULT_OK) {
            // Refresh the list of employees after adding a new employee
            employeeAdapter.updateEmployeeList(databaseHelper.getAllEmployees());
            Toast.makeText(this, "Employee added successfully", Toast.LENGTH_SHORT).show();
        } else if (requestCode == UPDATE_EMPLOYEE_REQUEST && resultCode == RESULT_OK) {
            // Refresh the list of employees after updating an employee
            employeeAdapter.updateEmployeeList(databaseHelper.getAllEmployees());
            Toast.makeText(this, "Employee updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdateEmployee(Employee employee) {
        navigateToUpdateEmployee(employee);
    }

    @Override
    public void onDeleteEmployee(Employee employee) {
        // Handle delete operation
        // Remove the employee from the database
        boolean isDeleted = databaseHelper.deleteEmployee(employee.getId());
        if (isDeleted) {
            // If deletion is successful, show a toast and update the RecyclerView
            Toast.makeText(this, "Employee deleted successfully", Toast.LENGTH_SHORT).show();
            employeeAdapter.updateEmployeeList(databaseHelper.getAllEmployees());
        } else {
            // If deletion fails, show an error toast
            Toast.makeText(this, "Failed to delete employee", Toast.LENGTH_SHORT).show();
        }
    }
}
